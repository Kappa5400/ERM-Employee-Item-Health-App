package com.healthapp.itemhealth.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.Car;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CarIntegrationTest {

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
  @DisplayName("Integration: Create Car and Assign to Employee")
  void testCreateCarAndVerify() throws Exception {
    // 1. Arrange: 持ち主となる社員を作成
    Employee driver =
        Employee.builder()
            .name("Test Driver")
            .title("Sales")
            .username("driver_")
            .password("secure123")
            .build();

    String empJson =
        mockMvc
            .perform(
                post("/api/employee")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(driver)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long employeeId =
        ((Number) com.jayway.jsonpath.JsonPath.read(empJson, "$.employeeId")).longValue();

    // 2. Act: Carを作成
    Car car = Car.builder().employeeId(employeeId).build();

    String carJson =
        mockMvc
            .perform(
                post("/api/car")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(car)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long carId = ((Number) com.jayway.jsonpath.JsonPath.read(carJson, "$.carId")).longValue();

    // 3. Assert: 取得して内容を検証
    mockMvc
        .perform(get("/api/car/" + carId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.employeeId").value(employeeId));
  }
}
