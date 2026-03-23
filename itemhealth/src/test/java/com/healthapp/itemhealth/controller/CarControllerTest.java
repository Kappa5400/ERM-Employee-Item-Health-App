package com.healthapp.itemhealth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.healthapp.itemhealth.model.Car;
import com.healthapp.itemhealth.security.SecurityConfig;
import com.healthapp.itemhealth.service.CarService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CarController.class)
@Import({ObjectMapper.class, SecurityConfig.class})
public class CarControllerTest {

  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockitoBean private CarService carService;

  // Critical: JavaTimeModule handles LocalDate fields in your model
  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  private Car sampleCar;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    sampleCar = new Car();
    sampleCar.setCarId(1L);
    sampleCar.setCarYear(2022);
    sampleCar.setMilage(15000);
    sampleCar.setLastServiced(LocalDate.now().minusMonths(3));
    sampleCar.setEmployeeId(101L);
    sampleCar.setInUse(true);
  }

  // --- 1. SUCCESS PATHS ---

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/car/{id} - Success")
  void getById_Success() throws Exception {
    when(carService.getById(1L)).thenReturn(sampleCar);

    mockMvc
        .perform(get("/api/car/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.carId").value(1))
        .andExpect(jsonPath("$.milage").value(15000));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("POST /api/car - Success")
  void create_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/car")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCar)))
        .andExpect(status().isCreated());

    verify(carService, times(1)).create(any(Car.class));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("DELETE /api/car/{id} - Success")
  void delete_Success() throws Exception {
    mockMvc.perform(delete("/api/car/1").with(csrf())).andExpect(status().isNoContent());
  }

  // --- 2. FAIL PATHS ---

  @Test
  @DisplayName("GET /api/car - Fail (Unauthorized)")
  void access_Unauthenticated_Returns401() throws Exception {
    mockMvc.perform(get("/api/car/1")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  @DisplayName("DELETE /api/car/{id} - Fail (Forbidden for USER role)")
  void delete_WrongRole_Returns403() throws Exception {
    // This will only pass if SecurityConfig protects /api/car/**
    mockMvc.perform(delete("/api/car/1").with(csrf())).andExpect(status().isForbidden());
  }
}
