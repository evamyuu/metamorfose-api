package com.metamorfose.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para dados do dashboard de plantas
 */
public class PlantDashboardDto {
    
    @JsonProperty("plant_id")
    private String plantId;
    
    @JsonProperty("plant_name")
    @NotBlank(message = "Nome da planta é obrigatório")
    private String plantName;
    
    @NotBlank(message = "Espécie é obrigatória")
    private String species;
    
    @JsonProperty("pot_color")
    private String potColor;
    
    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    
    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("user_name")
    private String userName;
    
    private String email;
    
    @JsonProperty("health_index")
    @NotNull(message = "Índice de saúde é obrigatório")
    private Double healthIndex;
    
    @JsonProperty("status_category")
    private StatusCategory statusCategory;
    
    @JsonProperty("days_monitored")
    private Integer daysMonitored;
    
    @JsonProperty("active_sensors")
    private Integer activeSensors;
    
    @JsonProperty("readings_last_24h")
    private Integer readingsLast24h;
    
    @JsonProperty("main_photo_url")
    private String mainPhotoUrl;
    
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonProperty("query_timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime queryTimestamp;
    
    // Enum para categorias de status
    public enum StatusCategory {
        EXCELLENT, GOOD, WARNING, CAUTION, CRITICAL, ERROR
    }
    
    // Construtores
    public PlantDashboardDto() {}
    
    public PlantDashboardDto(String plantId, String plantName, String species) {
        this.plantId = plantId;
        this.plantName = plantName;
        this.species = species;
    }
    
    // Getters e Setters
    public String getPlantId() { return plantId; }
    public void setPlantId(String plantId) { this.plantId = plantId; }
    
    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }
    
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    
    public String getPotColor() { return potColor; }
    public void setPotColor(String potColor) { this.potColor = potColor; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Double getHealthIndex() { return healthIndex; }
    public void setHealthIndex(Double healthIndex) { this.healthIndex = healthIndex; }
    
    public StatusCategory getStatusCategory() { return statusCategory; }
    public void setStatusCategory(StatusCategory statusCategory) { this.statusCategory = statusCategory; }
    
    public Integer getDaysMonitored() { return daysMonitored; }
    public void setDaysMonitored(Integer daysMonitored) { this.daysMonitored = daysMonitored; }
    
    public Integer getActiveSensors() { return activeSensors; }
    public void setActiveSensors(Integer activeSensors) { this.activeSensors = activeSensors; }
    
    public Integer getReadingsLast24h() { return readingsLast24h; }
    public void setReadingsLast24h(Integer readingsLast24h) { this.readingsLast24h = readingsLast24h; }
    
    public String getMainPhotoUrl() { return mainPhotoUrl; }
    public void setMainPhotoUrl(String mainPhotoUrl) { this.mainPhotoUrl = mainPhotoUrl; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getQueryTimestamp() { return queryTimestamp; }
    public void setQueryTimestamp(LocalDateTime queryTimestamp) { this.queryTimestamp = queryTimestamp; }
}