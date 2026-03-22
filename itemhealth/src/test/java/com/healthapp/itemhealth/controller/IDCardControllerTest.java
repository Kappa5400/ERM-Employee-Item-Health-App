package com.healthapp.itemhealth.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.IDCard;
import com.healthapp.itemhealth.security.SecurityConfig;
import com.healthapp.itemhealth.service.IDCardService;
import java.time.LocalDate;
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

@WebMvcTest(IDCardController.class)
@Import({ObjectMapper.class, SecurityConfig.class})
public class IDCardControllerTest {

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext context;

  @MockitoBean private IDCardService idCardService;

  // Critical: Register JavaTimeModule for LocalDate expirationDate
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

  private IDCard sampleCard;

  @BeforeEach
  void setUp() {
    // Initialize MockMvc with Spring Security filters to catch 401/403/400
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

    sampleCard = new IDCard();
    sampleCard.setIdCardId(1L);
    sampleCard.setEmployeeId(101L);
    sampleCard.setLastRenewedDate(LocalDate.now().minusMonths(6));
    sampleCard.setInUse(true);
  }

  // --- 1. SUCCESS PATHS (正常系) ---
  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/id-card/{id} - Success")
  void getById_Success() throws Exception {
    when(idCardService.getById(1L)).thenReturn(sampleCard);
    mockMvc
        .perform(get("/api/id-card/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.idCardId").value(1));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/id-card - Success")
  void getAll_Success() throws Exception {
    when(idCardService.getAll()).thenReturn(Arrays.asList(sampleCard));
    mockMvc
        .perform(get("/api/id-card"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].idCardId").value(1));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/id-card/employee/{id} - Success")
  void getByEmployeeId_Success() throws Exception {
    when(idCardService.getByEmployeeId(101L)).thenReturn(sampleCard);
    mockMvc.perform(get("/api/id-card/employee/101")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/id-card/in-use - Success")
  void getInUse_Success() throws Exception {
    mockMvc.perform(get("/api/id-card/in-use")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/id-card/to-renew - Success")
  void getToRenew_Success() throws Exception {
    mockMvc.perform(get("/api/id-card/to-renew")).andExpect(status().isOk());
  }

  // --- 2. POST / PATCH / DELETE ---

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("POST /api/id-card - Success")
  void create_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/id-card")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCard)))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("PATCH /api/id-card - Success")
  void update_Success() throws Exception {
    mockMvc
        .perform(
            patch("/api/id-card")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCard)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("DELETE /api/id-card/{id} - Success")
  void delete_Success() throws Exception {
    mockMvc.perform(delete("/api/id-card/1").with(csrf())).andExpect(status().isNoContent());
  }

  // --- 3. FAIL PATHS (Security & Logic) ---

  @Test
  @DisplayName("GET /api/id-card - Fail (Unauthenticated)")
  void access_Unauthenticated_Returns401() throws Exception {
    mockMvc.perform(get("/api/id-card")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  @DisplayName("DELETE /api/id-card/{id} - Fail (Forbidden for non-BOSS)")
  void delete_WrongRole_Returns403() throws Exception {
    // Ensure SecurityConfig has .requestMatchers("/api/id-card/**").hasRole("BOSS")
    mockMvc.perform(delete("/api/id-card/1").with(csrf())).andExpect(status().isForbidden());
  }
}
