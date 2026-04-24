package com.healthapp.itemhealth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.Laptop;
import com.healthapp.itemhealth.security.SecurityConfig;
import com.healthapp.itemhealth.service.LaptopService;
import java.util.Arrays;
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

@WebMvcTest(LaptopController.class)
@Import({ObjectMapper.class, SecurityConfig.class})
public class LaptopControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private LaptopService laptopService;

  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

  private Laptop sampleLaptop;
  private Laptop sampleLaptop2;

  @Autowired private WebApplicationContext context;

  @BeforeEach
  void setUp() {
    // Initialize MockMvc with Spring Security filters
    mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(
                org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
                    .springSecurity())
            .build();

    sampleLaptop = new Laptop();
    sampleLaptop.setLaptopId(1L);
    sampleLaptop.setOsVersion(11); // Valid int version
    sampleLaptop.setEmployeeId(101L);

    sampleLaptop2 = new Laptop();
    sampleLaptop2.setLaptopId(2L);
    sampleLaptop2.setOsVersion(22); // Valid int version
    sampleLaptop2.setEmployeeId(201L);
  }

  // --- 1. SUCCESS PATHS ---

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("POST /api/laptop - Success")
  void create_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/laptop")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleLaptop)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.employeeId").isNotEmpty());

    verify(laptopService, times(1)).create(any(Laptop.class));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("PATCH /api/laptop - Success")
  void update_Success() throws Exception {

    mockMvc
        .perform(
            patch("/api/laptop")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleLaptop)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.employeeId").isNotEmpty());

    verify(laptopService, times(1)).update(any(Laptop.class));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/laptop - Success")
  void getAll_Success() throws Exception {
    Laptop laptop = new Laptop();

    when(laptopService.getAll()).thenReturn(Arrays.asList(sampleLaptop, sampleLaptop2));

    mockMvc
        .perform(get("/api/laptop"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].laptopId").value(1L))
        .andExpect(jsonPath("$[1].laptopId").value(2L));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/laptop/{id} - Success")
  void getById_Success() throws Exception {
    when(laptopService.getById(1L)).thenReturn(sampleLaptop);

    mockMvc
        .perform(get("/api/laptop/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.osVersion").value(11));
  }

  // --- 2. FAIL & NULL PATHS (異常系) ---

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("POST /api/laptop - Fail (Validation: Non-Positive OS Version)")
  void create_Fail_InvalidOs() throws Exception {
    // Your model uses @Positive, so 0 or -5 should trigger a 400
    sampleLaptop.setOsVersion(0);

    mockMvc
        .perform(
            post("/api/laptop")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleLaptop)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/laptop/{id} - Not Found (Returns 200/Empty)")
  void getById_NotFound() throws Exception {
    when(laptopService.getById(999L)).thenReturn(null);

    mockMvc
        .perform(get("/api/laptop/999"))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
  }

  // --- 3. SECURITY PATHS ---

  @Test
  @DisplayName("ANY /api/laptop - Fail (Unauthenticated)")
  void access_Unauthenticated_Returns401() throws Exception {
    mockMvc
        .perform(get("/api/laptop/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/*login"));
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE") // Not BOSS
  @DisplayName("DELETE /api/laptop/{id} - Fail (Insufficient Roles)")
  void delete_WrongRole_Returns403() throws Exception {
    mockMvc.perform(delete("/api/laptop/1").with(csrf())).andExpect(status().isForbidden());
  }
}
