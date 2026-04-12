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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new LinkedHashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              // This line puts the validation failure into your item-health.log
              log.warn(
                  "Validation failed: Field '{}' - {}",
                  error.getField(),
                  error.getDefaultMessage());
              errors.put(error.getField(), error.getDefaultMessage());
            });

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public String handleAccessDeniedException(
      AccessDeniedException ex, RedirectAttributes redirectAttributes) {
    // This adds a temporary message to the next page
    redirectAttributes.addFlashAttribute(
        "message", "Access Denied: You do not have Boss privileges.");

    // Redirect to the standard dashboard instead of showing an error page
    return "redirect:/";
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleAllExceptions(Exception ex) {
    // This captures unexpected crashes (500 errors) with the full stack trace
    log.error("Unexpected error occurred: ", ex);
    return ResponseEntity.internalServerError().body("An internal error occurred.");
  }
}
