package com.metamorfose.repository;

import com.metamorfose.dto.PlantDashboardDto;
import com.metamorfose.exception.DatabaseException;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Repositório responsável pela integração com as procedures PL/SQL
 */
@Repository
public class MetamorfoseRepository {

    private static final Logger logger = LoggerFactory.getLogger(MetamorfoseRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public MetamorfoseRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    /**
     * Chama a procedure PRC_API_DASHBOARD_PLANTAS
     */
    public List<PlantDashboardDto> getDashboardPlants(String userId) {
        logger.debug("Chamando PRC_API_DASHBOARD_PLANTAS para userId: {}", userId);

        List<PlantDashboardDto> plants = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            // Preparar chamada da procedure
            String sql = "{call PRC_API_DASHBOARD_PLANTAS(?, ?)}";

            try (CallableStatement stmt = connection.prepareCall(sql)) {
                // Definir parâmetros
                stmt.setString(1, userId);
                stmt.registerOutParameter(2, OracleTypes.CURSOR);

                // Executar
                stmt.execute();

                // Processar resultado
                try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                    while (rs.next()) {
                        PlantDashboardDto plant = mapResultSetToPlantDto(rs);
                        plants.add(plant);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Erro ao executar PRC_API_DASHBOARD_PLANTAS", e);
            throw new DatabaseException("Erro ao buscar dados do dashboard", e);
        }

        logger.debug("Retornando {} plantas do dashboard", plants.size());
        return plants;
    }

    /**
     * Chama a procedure PRC_BACKEND_PROCESSAMENTO_AUTO
     */
    public String executeBackendProcessing(String processType) {
        logger.debug("Executando processamento backend tipo: {}", processType);

        try (Connection connection = dataSource.getConnection()) {
            String sql = "{call PRC_BACKEND_PROCESSAMENTO_AUTO(?, ?)}";

            try (CallableStatement stmt = connection.prepareCall(sql)) {
                stmt.setString(1, processType);
                stmt.registerOutParameter(2, OracleTypes.CLOB);

                stmt.execute();

                Clob resultClob = stmt.getClob(2);
                String result = resultClob.getSubString(1, (int) resultClob.length());

                logger.debug("Processamento backend concluído");
                return result;
            }

        } catch (SQLException e) {
            logger.error("Erro ao executar processamento backend", e);
            throw new DatabaseException("Erro no processamento automático", e);
        }
    }

    /**
     * Chama a procedure PRC_REGISTRAR_ALERTAS_CRITICOS
     */
    public String registerCriticalAlerts(String plantId) {
        logger.debug("Registrando alertas críticos para planta: {}", plantId);

        try (Connection connection = dataSource.getConnection()) {
            String sql = "{call PRC_REGISTRAR_ALERTAS_CRITICOS(?, ?)}";

            try (CallableStatement stmt = connection.prepareCall(sql)) {
                stmt.setString(1, plantId);
                stmt.registerOutParameter(2, OracleTypes.VARCHAR);

                stmt.execute();

                String result = stmt.getString(2);
                logger.debug("Alertas registrados: {}", result);
                return result;
            }

        } catch (SQLException e) {
            logger.error("Erro ao registrar alertas críticos", e);
            throw new DatabaseException("Erro ao registrar alertas", e);
        }
    }

    /**
     * Chama a function FN_CALCULAR_INDICE_SAUDE_PLANTA
     */
    public Double calculatePlantHealthIndex(String plantId) {
        logger.debug("Calculando índice de saúde para planta: {}", plantId);

        try {
            String sql = "SELECT FN_CALCULAR_INDICE_SAUDE_PLANTA(?) FROM DUAL";
            Double healthIndex = jdbcTemplate.queryForObject(sql, Double.class, plantId);

            logger.debug("Índice de saúde calculado: {}", healthIndex);
            return healthIndex;

        } catch (Exception e) {
            logger.error("Erro ao calcular índice de saúde", e);
            throw new DatabaseException("Erro ao calcular índice de saúde", e);
        }
    }

    /**
     * Chama a function FN_FORMATAR_STATUS_PLANTA
     */
    public String formatPlantStatus(String plantId) {
        logger.debug("Formatando status da planta: {}", plantId);

        try {
            String sql = "SELECT FN_FORMATAR_STATUS_PLANTA(?) FROM DUAL";
            String status = jdbcTemplate.queryForObject(sql, String.class, plantId);

            logger.debug("Status formatado com sucesso");
            return status;

        } catch (Exception e) {
            logger.error("Erro ao formatar status da planta", e);
            throw new DatabaseException("Erro ao formatar status", e);
        }
    }

    /**
     * Mapeia ResultSet para PlantDashboardDto
     */
    private PlantDashboardDto mapResultSetToPlantDto(ResultSet rs) throws SQLException {
        PlantDashboardDto plant = new PlantDashboardDto();

        plant.setPlantId(rs.getString("plant_id"));
        plant.setPlantName(rs.getString("plant_name"));
        plant.setSpecies(rs.getString("species"));
        plant.setPotColor(rs.getString("pot_color"));

        // Conversão de datas
        Timestamp startDate = rs.getTimestamp("start_date");
        if (startDate != null) {
            plant.setStartDate(startDate.toLocalDateTime());
        }

        plant.setUserId(rs.getString("user_id"));
        plant.setUserName(rs.getString("user_name"));
        plant.setEmail(rs.getString("email"));
        plant.setHealthIndex(rs.getDouble("health_index"));

        // Status category
        String statusStr = rs.getString("status_category");
        if (statusStr != null) {
            try {
                plant.setStatusCategory(PlantDashboardDto.StatusCategory.valueOf(statusStr));
            } catch (IllegalArgumentException e) {
                plant.setStatusCategory(PlantDashboardDto.StatusCategory.ERROR);
            }
        }

        plant.setDaysMonitored(rs.getInt("days_monitored"));
        plant.setActiveSensors(rs.getInt("active_sensors"));
        plant.setReadingsLast24h(rs.getInt("readings_last_24h"));
        plant.setMainPhotoUrl(rs.getString("main_photo_url"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            plant.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp queryTime = rs.getTimestamp("query_timestamp");
        if (queryTime != null) {
            plant.setQueryTimestamp(queryTime.toLocalDateTime());
        }

        return plant;
    }
}