package com.metamorfose.service;

import com.metamorfose.dto.PlantDashboardDto;
import com.metamorfose.exception.DatabaseException;
import com.metamorfose.repository.MetamorfoseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Serviço principal para operações do dashboard e monitoramento
 */
@Service
public class DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    private final MetamorfoseRepository repository;

    public DashboardService(MetamorfoseRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca dados do dashboard para um usuário específico
     */
    @Cacheable(value = "dashboard", key = "#userId", unless = "#result.isEmpty()")
    public List<PlantDashboardDto> getDashboardData(String userId) {
        logger.info("Buscando dados do dashboard para usuário: {}", userId);

        try {
            List<PlantDashboardDto> plants = repository.getDashboardPlants(userId);

            if (plants.isEmpty()) {
                logger.warn("Nenhuma planta encontrada para o usuário: {}", userId);
            } else {
                logger.info("Encontradas {} plantas para o usuário: {}", plants.size(), userId);
            }

            return plants;

        } catch (Exception e) {
            logger.error("Erro ao buscar dados do dashboard para usuário: {}", userId, e);
            throw new DatabaseException("Falha ao carregar dados do dashboard", e);
        }
    }

    /**
     * Busca dados do dashboard para todos os usuários
     */
    public List<PlantDashboardDto> getAllDashboardData() {
        logger.info("Buscando dados do dashboard para todos os usuários");
        return getDashboardData(null); // null = todos os usuários
    }

    /**
     * Executa processamento automático do backend
     */
    public String executeAutomaticProcessing(String processType) {
        logger.info("Iniciando processamento automático tipo: {}", processType);

        // Validar tipo de processamento
        if (!isValidProcessType(processType)) {
            throw new IllegalArgumentException("Tipo de processamento inválido: " + processType);
        }

        try {
            String result = repository.executeBackendProcessing(processType);
            logger.info("Processamento automático concluído com sucesso");
            return result;

        } catch (Exception e) {
            logger.error("Erro no processamento automático tipo: {}", processType, e);
            throw new DatabaseException("Falha no processamento automático", e);
        }
    }

    /**
     * Registra alertas críticos para uma planta específica
     */
    public String registerCriticalAlerts(String plantId) {
        logger.info("Registrando alertas críticos para planta: {}", plantId);

        if (plantId == null || plantId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da planta não pode ser nulo ou vazio");
        }

        try {
            String result = repository.registerCriticalAlerts(plantId);
            logger.info("Alertas críticos registrados com sucesso para planta: {}", plantId);
            return result;

        } catch (Exception e) {
            logger.error("Erro ao registrar alertas para planta: {}", plantId, e);
            throw new DatabaseException("Falha ao registrar alertas críticos", e);
        }
    }

    /**
     * Registra alertas para todas as plantas
     */
    public String registerAllCriticalAlerts() {
        logger.info("Registrando alertas críticos para todas as plantas");
        return registerCriticalAlerts(null); // null = todas as plantas
    }

    /**
     * Calcula índice de saúde de uma planta
     */
    public Double getPlantHealthIndex(String plantId) {
        logger.info("Calculando índice de saúde para planta: {}", plantId);

        if (plantId == null || plantId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da planta não pode ser nulo ou vazio");
        }

        try {
            Double healthIndex = repository.calculatePlantHealthIndex(plantId);
            logger.info("Índice de saúde calculado: {} para planta: {}", healthIndex, plantId);
            return healthIndex;

        } catch (Exception e) {
            logger.error("Erro ao calcular índice de saúde para planta: {}", plantId, e);
            throw new DatabaseException("Falha ao calcular índice de saúde", e);
        }
    }

    /**
     * Busca status formatado de uma planta
     */
    public String getFormattedPlantStatus(String plantId) {
        logger.info("Buscando status formatado para planta: {}", plantId);

        if (plantId == null || plantId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID da planta não pode ser nulo ou vazio");
        }

        try {
            String status = repository.formatPlantStatus(plantId);
            logger.info("Status formatado obtido com sucesso para planta: {}", plantId);
            return status;

        } catch (Exception e) {
            logger.error("Erro ao buscar status formatado para planta: {}", plantId, e);
            throw new DatabaseException("Falha ao obter status formatado", e);
        }
    }

    /**
     * Execução assíncrona do processamento automático
     */
    public CompletableFuture<String> executeAutomaticProcessingAsync(String processType) {
        logger.info("Iniciando processamento automático assíncrono tipo: {}", processType);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeAutomaticProcessing(processType);
            } catch (Exception e) {
                logger.error("Erro no processamento assíncrono", e);
                throw new RuntimeException("Falha no processamento assíncrono", e);
            }
        });
    }

    /**
     * Valida tipos de processamento permitidos
     */
    private boolean isValidProcessType(String processType) {
        if (processType == null)
            return false;

        String upperType = processType.toUpperCase();
        return upperType.equals("COMPLETO") ||
                upperType.equals("ALERTAS") ||
                upperType.equals("LIMPEZA") ||
                upperType.equals("STATS");
    }
}