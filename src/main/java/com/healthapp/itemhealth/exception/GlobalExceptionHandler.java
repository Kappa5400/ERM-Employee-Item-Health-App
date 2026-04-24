package com.healthapp.itemhealth.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// To handle exceptions on the frontend
@ControllerAdvice
public class GlobalExceptionHandler {
  //  Logger object to make logs.
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // For when a argument not valid exception occurs
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    // keep errs in list in order they occur
    Map<String, String> errors = new LinkedHashMap<>();
    // For each error, get the fields in the error and have logger print them to the log
    // for debugging
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              log.warn(
                  "Validation failed: Field '{}' - {}",
                  error.getField(),
                  error.getDefaultMessage());
              errors.put(error.getField(), error.getDefaultMessage());
            });

    // return to frontend the faulty request and the body in the error
    return ResponseEntity.badRequest().body(errors);
  }

  // For when an auth error occurs, redirect to different page
  @ExceptionHandler(AccessDeniedException.class)
  public String handleAccessDeniedException(
      AccessDeniedException ex, RedirectAttributes redirectAttributes) {
    // Flashes message when nonboss user attempts to access boss restricted portion of webstie
    redirectAttributes.addFlashAttribute(
        "message", "Access Denied: You do not have Boss privileges.");

    // Redirect to website root
    return "redirect:/";
  }

  // For handling response errors
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
    // logs it
    log.warn("Blocked request: {}", ex.getReason());

    // returns error message to front end
    return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
  }

  // all exception handler
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleAllExceptions(Exception ex) {
    // logs it
    log.error("Unexpected error occurred: ", ex);
    // responds with error message
    return ResponseEntity.internalServerError().body("An internal error occurred.");
  }
}
