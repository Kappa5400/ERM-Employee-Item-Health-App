package com.healthapp.itemhealth.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.model.Laptop;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.flyway.enabled=true"
    })
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LaptopIntegrationTest {

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
  @DisplayName("Integration: Create Laptop and Assign to Employee")
  void testCreateLaptopAndVerify() throws Exception {
    // 1. Arrange: まず持ち主となる社員を作成
    Employee owner =
        Employee.builder()
            .name("Laptop User")
            .title("Engineer")
            .username("laptop_user_")
            .password("secure123")
            .build();

    String empJson =
        mockMvc
            .perform(
                post("/api/employee")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(owner)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long employeeId =
        ((Number) com.jayway.jsonpath.JsonPath.read(empJson, "$.employeeId")).longValue();

    // 2. Act: その社員に紐付いたLaptopを作成
    Laptop laptop = Laptop.builder().employeeId(employeeId).osVersion(1).build();

    String laptopJson =
        mockMvc
            .perform(
                post("/api/laptop")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(laptop)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long laptopId =
        ((Number) com.jayway.jsonpath.JsonPath.read(laptopJson, "$.laptopId")).longValue();

    // 3. Assert: 正常に取得できるか確認
    mockMvc
        .perform(get("/api/laptop/" + laptopId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.employeeId").value(employeeId));
  }
}
