package br.com.ravenstore.server.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
  @ExceptionHandler({ MethodArgumentNotValidException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handlerValidationException(
      MethodArgumentNotValidException exception,
      HttpServletRequest request) {
    BindingResult bindingResult = exception.getBindingResult();
    Map<String, String> validationErrors = new HashMap<>();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return new ApiError(
        HttpStatus.BAD_REQUEST.value(),
        "validation error",
        request.getServletPath(),
        validationErrors);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
      String message = ex.getRootCause().getMessage();
      
      Map<String, String> validationErrors = new HashMap<>();
      
      if (message.contains("TB_USER(CPF")) {
          validationErrors.put("cpf", "Este CPF já está cadastrado");
      }
      if (message.contains("TB_USER(EMAIL")) {
          validationErrors.put("email", "Este e-mail já está cadastrado");
      }
      
      return new ApiError(
          HttpStatus.BAD_REQUEST.value(),
          "Erro de integridade de dados",
          request.getServletPath(),
          validationErrors
      );
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
    return new ApiError(
        HttpStatus.BAD_REQUEST.value(),
        exception.getMessage(),
        request.getServletPath());
  }

  @ExceptionHandler({ EntityNotFoundException.class })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiError handleEntityNotFoundException(
      EntityNotFoundException exception,
      HttpServletRequest request) {

    return new ApiError(
        HttpStatus.NOT_FOUND.value(),
        exception.getMessage(),
        request.getServletPath());
  }
}