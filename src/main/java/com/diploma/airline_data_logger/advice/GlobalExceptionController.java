package com.diploma.airline_data_logger.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public String exceptionHandler(HttpServletRequest request, Exception exception,
                                   RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        String path = request.getHeader("Referer");
        String previousPath = null;

        if (path != null) {
            URI uri = URI.create(path);
            previousPath = uri.getPath();
        }
        return "redirect:%s".formatted(previousPath != null ? previousPath : "/dashboard");
    }

}
