package com.healthapp.itemhealth.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.flyway.enabled=true",
      "spring.mail.host=localhost",
      "spring.mail.port=1025",
      "management.health.mail.enabled=false"
    })
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class EmployeeIntegrationTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

  @Autowired private WebApplicationContext context;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(
                org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
                    .springSecurity())
            .build();
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("Integration: Create Employee and Verify Persistence")
  void testCreateAndGetEmployee() throws Exception {

    Boss testBoss = new Boss();
    testBoss.setBossId(1L);

    Employee employee =
        Employee.builder()
            .name("test10")
            .title("test")
            .bossRole(true)
            .hasBoss(false)
            .username("test10")
            .password("password123")
            .bossUserId(1L)
            .email("integrationtest@gmail.com")
            .build();

    MvcResult result =
        mockMvc
            .perform(
                post("/api/employee")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee)))
            .andExpect(status().isCreated())
            .andReturn();

    String jsonResponse = result.getResponse().getContentAsString();
    Integer createdId = com.jayway.jsonpath.JsonPath.read(jsonResponse, "$.employeeId");

    mockMvc
        .perform(get("/api/employee/" + createdId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("test10"));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("Integration: Delete Employee and Confirm Removal")
  void testDeleteEmployee() throws Exception {
    // Employee 2 is a dummy employee inserted in
    // db migration that is deletable
    mockMvc.perform(delete("/api/employee/2").with(csrf())).andExpect(status().isNoContent());

    mockMvc
        .perform(get("/api/employee/2"))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
  }
}
