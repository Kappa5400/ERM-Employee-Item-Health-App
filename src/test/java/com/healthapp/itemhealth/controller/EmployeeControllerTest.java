package com.healthapp.itemhealth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthapp.itemhealth.model.Employee;
import com.healthapp.itemhealth.security.SecurityConfig;
import com.healthapp.itemhealth.service.CarService;
import com.healthapp.itemhealth.service.EmployeeService;
import com.healthapp.itemhealth.service.ExcelService;
import com.healthapp.itemhealth.service.IDCardService;
import com.healthapp.itemhealth.service.LaptopService;
import java.util.List;
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

@WebMvcTest(EmployeeController.class)
@Import({ObjectMapper.class, SecurityConfig.class})
public class EmployeeControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private EmployeeService employeeService;

  @MockitoBean private ExcelService ExcelService;

  @MockitoBean private CarService carService;

  @MockitoBean private LaptopService laptopService;

  @MockitoBean private IDCardService idCardService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private Employee sampleEmployee;

  @Autowired private WebApplicationContext context;

  @BeforeEach
  void setUp() {

    mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(
                org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
                    .springSecurity())
            .build();

    sampleEmployee =
        Employee.builder()
            .employeeId(1L)
            .name("Test User")
            .title("Employee")
            .email("Test@gmail.com")
            .bossRole(false)
            .hasBoss(true)
            .bossUserId(1L)
            .username("testuser123")
            .password("password123")
            .build();
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/employee/{id} - Success Path")
  void getById_Success() throws Exception {
    when(employeeService.getById(1L)).thenReturn(sampleEmployee);

    mockMvc
        .perform(get("/api/employee/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test User"))
        .andExpect(jsonPath("$.username").value("testuser123"));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/employee - Success Path")
  void findAll_Success() throws Exception {
    when(employeeService.findAllEmployees()).thenReturn(List.of(sampleEmployee));

    mockMvc
        .perform(get("/api/employee"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name").value("Test User"));
  }

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("POST /api/employee - Success Path")
  void insert_Success() throws Exception {
    mockMvc
        .perform(
            post("/api/employee")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleEmployee)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.employeeId").isNotEmpty());

    verify(employeeService, times(1)).insert(any(Employee.class));
  }

  // --- 2. NULL & EMPTY PATHS ---

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("GET /api/employee/{id} - Not Found Path")
  void getById_NotFound_ReturnsEmpty() throws Exception {
    when(employeeService.getById(99L)).thenReturn(null);

    mockMvc
        .perform(get("/api/employee/99"))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
  }

  // --- 3. ABNORMAL PATHS (FAILURES) ---

  @Test
  @WithMockUser(roles = "BOSS")
  @DisplayName("POST /api/employee - Fail Path (Validation Error)")
  void insert_ValidationError() throws Exception {
    sampleEmployee.setUsername(""); // Triggers @NotBlank

    mockMvc
        .perform(
            post("/api/employee")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleEmployee)))
        .andExpect(status().isBadRequest());
  }

  // --- 4. SECURITY PATHS ---

  @Test
  @DisplayName("ANY /api/employee - Fail Path (Unauthorized)")
  void unauthorized_Access_redirects() throws Exception {

    mockMvc.perform(get("/api/employee")).andExpect(redirectedUrlPattern("/*login"));
  }

  @Test
  @WithMockUser(roles = "USER") // Authenticated but lacks BOSS role
  @DisplayName("DELETE /api/employee - Fail Path (Forbidden)")
  void delete_WrongRole() throws Exception {
    // This expects 403 Forbidden if your SecurityConfig protects the route
    mockMvc.perform(delete("/api/employee/1").with(csrf())).andExpect(status().isForbidden());
  }
}
