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
 * Controlador REST para operações de monitoramento e alertas
 */
@RestController
@RequestMapping("/monitoring")
@Tag(name = "Monitoramento", description = "Operações de monitoramento e alertas")
public class MonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringController.class);

    private final DashboardService dashboardService;

    public MonitoringController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * POST /monitoring/alerts - Registrar alertas críticos
     */
    @PostMapping("/alerts")
    @Operation(summary = "Registrar alertas críticos", description = "Verifica e registra alertas críticos para todas as plantas")
    public ResponseEntity<OperationResponseDto> registerAllAlerts() {
        logger.info("Solicitação recebida: registrar alertas críticos para todas as plantas");

        try {
            String result = dashboardService.registerAllCriticalAlerts();

            OperationResponseDto response = OperationResponseDto.success(
                    "Alertas processados com sucesso", result);
            response.setOperationType("CRITICAL_ALERTS");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erro ao registrar alertas críticos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OperationResponseDto.error("Erro ao processar alertas"));
        }
    }

    /**
     * POST /monitoring/alerts/{plantId} - Registrar alertas para planta específica
     */
    @PostMapping("/alerts/{plantId}")
    @Operation(summary = "Registrar alertas para planta específica", description = "Verifica e registra alertas críticos para uma planta")
    public ResponseEntity<OperationResponseDto> registerPlantAlerts(
            @Parameter(description = "ID da planta", required = true) @PathVariable @NotBlank String plantId) {

        logger.info("Solicitação recebida: registrar alertas para planta {}", plantId);

        try {
            String result = dashboardService.registerCriticalAlerts(plantId);

            OperationResponseDto response = OperationResponseDto.success(
                    "Alertas da planta processados com sucesso", result);
            response.setOperationType("PLANT_ALERTS");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Parâmetro inválido para planta: {}", plantId, e);
            return ResponseEntity.badRequest()
                    .body(OperationResponseDto.error("ID da planta inválido"));

        } catch (Exception e) {
            logger.error("Erro ao registrar alertas da planta: {}", plantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OperationResponseDto.error("Erro ao processar alertas da planta"));
        }
    }

    /**
     * POST /monitoring/process/{type} - Executar processamento automático
     */
    @PostMapping("/process/{type}")
    @Operation(summary = "Executar processamento automático", description = "Executa rotinas automáticas de backend (COMPLETO, ALERTAS, LIMPEZA, STATS)")
    public ResponseEntity<OperationResponseDto> executeProcessing(
            @Parameter(description = "Tipo de processamento", required = true) @PathVariable @NotBlank String type) {

        logger.info("Solicitação recebida: processamento automático tipo {}", type);

        try {
            String result = dashboardService.executeAutomaticProcessing(type.toUpperCase());

            OperationResponseDto response = OperationResponseDto.success(
                    "Processamento executado com sucesso", result);
            response.setOperationType("AUTOMATIC_PROCESSING");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Tipo de processamento inválido: {}", type, e);
            return ResponseEntity.badRequest()
                    .body(OperationResponseDto.error("Tipo de processamento inválido"));

        } catch (Exception e) {
            logger.error("Erro no processamento automático: {}", type, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OperationResponseDto.error("Erro no processamento"));
        }
    }

    /**
     * POST /monitoring/process/{type}/async - Processamento assíncrono
     */
    @PostMapping("/process/{type}/async")
    @Operation(summary = "Processamento automático assíncrono", description = "Executa processamento de forma assíncrona e retorna imediatamente")
    public ResponseEntity<OperationResponseDto> executeProcessingAsync(
            @Parameter(description = "Tipo de processamento", required = true) @PathVariable @NotBlank String type) {

        logger.info("Solicitação recebida: processamento assíncrono tipo {}", type);

        try {
            // Iniciar processamento assíncrono
            CompletableFuture<String> futureResult = dashboardService
                    .executeAutomaticProcessingAsync(type.toUpperCase());

            OperationResponseDto response = OperationResponseDto.success(
                    "Processamento assíncrono iniciado com sucesso");
            response.setOperationType("ASYNC_PROCESSING");

            return ResponseEntity.accepted().body(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Tipo de processamento inválido: {}", type, e);
            return ResponseEntity.badRequest()
                    .body(OperationResponseDto.error("Tipo de processamento inválido"));

        } catch (Exception e) {
            logger.error("Erro ao iniciar processamento assíncrono: {}", type, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(OperationResponseDto.error("Erro ao iniciar processamento"));
        }
    }
}