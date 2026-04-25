package com.healthapp.itemhealth.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.Boss;
import com.healthapp.itemhealth.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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
public class BossIntegrationTest {

  @Autowired private MockMvc mockMvc;
  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

  @Autowired private WebApplicationContext context;

  @MockitoBean private JavaMailSender mailSender;

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
  @DisplayName("Integration: Create Boss from existing Employee and Verify")
  void testCreateBossAndVerifySubordinates() throws Exception {

    // 1. Create the Employee
    Employee employee =
        Employee.builder()
            .name("Boss Candidate")
            .title("Manager")
            .username("boss_user_unique") // Ensure uniqueness
            .password("secure123")
            .bossRole(false)
            .hasBoss(false)
            .bossUserId(3L)
            .email("Test@gmail.com")
            .build();

    String empJson =
        mockMvc
            .perform(
                post("/api/employee")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Capture the ID assigned by the database
    Long generatedEmployeeId =
        ((Number) com.jayway.jsonpath.JsonPath.read(empJson, "$.employeeId")).longValue();

    // 2. Create the Boss using the GENERATED ID
    Boss boss =
        Boss.builder()
            .employeeId(generatedEmployeeId) // Use the dynamic ID here!
            .bossId(3L)
            .title("Bossy boss")
            .name("Boss Candidate")
            .build();

    String bossJson =
        mockMvc
            .perform(
                post("/api/boss")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boss)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long generatedBossId =
        ((Number) com.jayway.jsonpath.JsonPath.read(bossJson, "$.bossId")).longValue();

    // 3. Verify
    mockMvc
        .perform(get("/api/boss/" + generatedBossId))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.employeeId").value(generatedEmployeeId)) // Compare against the dynamic ID
        .andExpect(jsonPath("$.name").value("Boss Candidate"));
  }
}
