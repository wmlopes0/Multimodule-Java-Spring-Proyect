package com.example.contract.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.boot.app.App;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
class GlobalExceptionHandlerTestIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("When validation fails, should return BadRequest with error details")
  void validationExceptionTest() throws Exception {
    String jsonContent = "{}";

    mockMvc.perform(post("/test/validation")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name").value("Name cannot be null"));
  }

  @Test
  @DisplayName("When a RuntimeException occurs, should return InternalServerError with error details")
  void runtimeExceptionTest() throws Exception {
    mockMvc.perform(get("/test/runtimeException"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.message").value("Runtime exception occurred"));
  }

  @Test
  @DisplayName("When an employee is not found, should return NotFound")
  void employeeNotFoundExceptionTest() throws Exception {
    mockMvc.perform(get("/test/employeeNotFound"))
        .andExpect(status().isNotFound());
  }

}