package com.metamorfose.controller;

import com.metamorfose.dto.PlantDashboardDto;
import com.metamorfose.dto.OperationResponseDto;
import com.metamorfose.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controlador REST para operações do dashboard
 */
@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Operações do dashboard de plantas")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /dashboard/plants - Busca todas as plantas
     */
    @GetMapping("/plants")
    @Operation(summary = "Buscar todas as plantas", description = "Retorna dados do dashboard para todas as plantas ativas")
    @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<List<PlantDashboardDto>> getAllPlants() {
        logger.info("Solicitação recebida: buscar todas as plantas");

        try {
            List<PlantDashboardDto> plants = dashboardService.getAllDashboardData();
            logger.info("Retornando {} plantas", plants.size());
            return ResponseEntity.ok(plants);

        } catch (Exception e) {
            logger.error("Erro ao buscar todas as plantas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /dashboard/plants/user/{userId} - Busca plantas de um usuário
     */
    @GetMapping("/plants/user/{userId}")
    @Operation(summary = "Buscar plantas por usuário", description = "Retorna dados do dashboard para plantas de um usuário específico")
    public ResponseEntity<List<PlantDashboardDto>> getPlantsByUser(
            @Parameter(description = "ID do usuário", required = true) @PathVariable @NotBlank String userId) {

        logger.info("Solicitação recebida: buscar plantas do usuário {}", userId);

        try {
            List<PlantDashboardDto> plants = dashboardService.getDashboardData(userId);
            logger.info("Retornando {} plantas para usuário {}", plants.size(), userId);
            return ResponseEntity.ok(plants);

        } catch (IllegalArgumentException e) {
            logger.warn("Parâmetro inválido para usuário: {}", userId, e);
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            logger.error("Erro ao buscar plantas do usuário: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /dashboard/plants/{plantId}/health - Índice de saúde de uma planta
     */
    @GetMapping("/plants/{plantId}/health")
    @Operation(summary = "Calcular índice de saúde", description = "Calcula e retorna o índice de saúde de uma planta específica")
    public ResponseEntity<OperationResponseDto> getPlantHealth(
            @Parameter(description = "ID da planta", required = true) @PathVariable @NotBlank String plantId) {

        logger.info("Solicitação recebida: calcular saúde da planta {}", plantId);

        try {
            Double healthIndex = dashboardService.getPlantHealthIndex(plantId);

            OperationResponseDto response = OperationResponseDto.success(
                    "Índice de saúde calculado com sucesso", healthIndex);
            response.setOperationType("HEALTH_CALCULATION");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Parâmetro inválido para planta: {}", plantId, e);
            return ResponseEntity.badRequest()
                    .body(OperationResponseDto.error("ID da planta inválido"));

        } catch (Exception e) {
            logger.error("Erro ao calcular saúde da planta: {}", plantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OperationResponseDto.error("Erro interno do servidor"));
        }
    }

    /**
     * GET /dashboard/plants/{plantId}/status - Status formatado de uma planta
     */
    @GetMapping("/plants/{plantId}/status")
    @Operation(summary = "Status formatado da planta", description = "Retorna o status completo e formatado de uma planta")
    public ResponseEntity<OperationResponseDto> getPlantStatus(
            @Parameter(description = "ID da planta", required = true) @PathVariable @NotBlank String plantId) {

        logger.info("Solicitação recebida: status da planta {}", plantId);

        try {
            String status = dashboardService.getFormattedPlantStatus(plantId);

            OperationResponseDto response = OperationResponseDto.success(
                    "Status obtido com sucesso", status);
            response.setOperationType("STATUS_FORMATTING");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Parâmetro inválido para planta: {}", plantId, e);
            return ResponseEntity.badRequest()
                    .body(OperationResponseDto.error("ID da planta inválido"));

        } catch (Exception e) {
            logger.error("Erro ao buscar status da planta: {}", plantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OperationResponseDto.error("Erro interno do servidor"));
        }
    }
}