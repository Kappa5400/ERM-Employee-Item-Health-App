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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Tells Spring to use application-test.properties
@Transactional // Rolls back the database after each test so it stays clean
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
    // 1. Arrange: Create a sample employee object
    Boss testBoss = new Boss();
    testBoss.setBossId(1L);

    Employee employee =
        Employee.builder()
            .name("test10")
            .title("test")
            .bossRole(true)
            .hasBoss(false)
            .username("test10")
            .password("secure123")
            .build();

    // 2. Act: Send POST request to the real Controller -> Service -> Mapper -> H2
    MvcResult result =
        mockMvc
            .perform(
                post("/api/employee")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee)))
            .andExpect(status().isCreated())
            .andReturn();

    // 2. Extract the ID (Assuming your API returns the created Employee JSON)
    String jsonResponse = result.getResponse().getContentAsString();
    Integer createdId = com.jayway.jsonpath.JsonPath.read(jsonResponse, "$.employeeId");

    // 3. Assert: Use the REAL ID to fetch it
    mockMvc
        .perform(get("/api/employee/" + createdId))
        .andDo(print()) // This will show you exactly what the server sends back
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("test10"));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("Integration: Delete Employee and Confirm Removal")
  void testDeleteEmployee() throws Exception {
    // Assume ID 999 exists (created in previous step or via data.sql)
    mockMvc.perform(delete("/api/employee/999").with(csrf())).andExpect(status().isNoContent());

    // Verify that a GET request no longer returns the employee
    mockMvc
        .perform(get("/api/employee/999"))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
  }
}
