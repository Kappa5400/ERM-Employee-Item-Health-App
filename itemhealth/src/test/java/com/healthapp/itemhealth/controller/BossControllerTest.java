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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.service.BossService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BossController.class)
public class BossControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private BossService bossService;

  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

  // --- READ TESTS ---

  @Test
  @WithMockUser(roles = "BOSS")
  void getById_Positive_ReturnsBoss() throws Exception {
    Boss boss = new Boss();
    boss.setBossId(1L);
    when(bossService.findById(1L)).thenReturn(boss);

    mockMvc
        .perform(get("/api/boss/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bossId").value(1));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  void findSubordinateIds_ReturnsList() throws Exception {
    when(bossService.findSubordinateIds(1L)).thenReturn(List.of(101L, 102L));

    mockMvc
        .perform(get("/api/boss/sub/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0]").value(101L));
  }

  // --- WRITE TESTS (POST/PATCH) ---

  @Test
  @WithMockUser(roles = "BOSS")
  void insert_Positive_Returns201() throws Exception {
    Boss boss = new Boss();
    boss.setName("Big Boss"); // Assume @NotBlank on bossName
    boss.setBossId(1L);
    boss.setEmployeeId(2L);

    mockMvc
        .perform(
            post("/api/boss")
                .with(csrf()) // Required for POST/PATCH/DELETE
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boss)))
        .andExpect(status().isCreated());

    verify(bossService, times(1)).insert(any(Boss.class));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  void update_Positive_Returns200() throws Exception {
    Boss boss = new Boss();
    boss.setBossId(1L);
    boss.setName("Big Boss");
    boss.setEmployeeId(2L);

    mockMvc
        .perform(
            patch("/api/boss")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boss)))
        .andExpect(status().isOk());

    verify(bossService).update(any(Boss.class));
  }

  // --- DELETE TESTS ---

  @Test
  @WithMockUser(roles = "BOSS")
  void delete_Positive_Returns204() throws Exception {
    mockMvc.perform(delete("/api/boss/1").with(csrf())).andExpect(status().isNoContent());

    verify(bossService).delete(1L);
  }

  // --- VALIDATION TEST ---

  @Test
  @WithMockUser(roles = "BOSS")
  void insert_Negative_InvalidData_Returns400() throws Exception {
    Boss invalidBoss = new Boss();
    // Leave fields blank to trigger @Valid

    mockMvc
        .perform(
            post("/api/boss")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBoss)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "BOSS")
  void insertSubordinate_Positive_Returns201() throws Exception {
    Long bossId = 1L;
    Long subId = 99L;

    mockMvc
        .perform(post("/api/boss/sub/{bossId}/{subordinateId}", bossId, subId).with(csrf()))
        .andExpect(status().isCreated());

    // Verify the service was called with the exact IDs from the URL
    verify(bossService, times(1)).insertSubordinate(bossId, subId);
  }

  @Test
  @WithMockUser(roles = "BOSS")
  void deleteSubordinate_Positive_Returns204() throws Exception {
    Long bossId = 1L;
    Long subId = 99L;

    mockMvc
        .perform(delete("/api/boss/{bossId}/sub/{subordinateId}", bossId, subId).with(csrf()))
        .andExpect(status().isNoContent());

    verify(bossService, times(1)).deleteSubordinate(bossId, subId);
  }

  @Test
  @WithMockUser(roles = "BOSS")
  void deleteAllSubordinates_Positive_Returns204() throws Exception {
    Long bossId = 1L;

    mockMvc
        .perform(delete("/api/boss/{bossId}/sub", bossId).with(csrf()))
        .andExpect(status().isNoContent());

    verify(bossService, times(1)).deleteAllSubordinates(bossId);
  }
}
