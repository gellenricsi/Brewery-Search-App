package com.brewery.app.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorHandler implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        int status = (int) request.getAttribute("jakarta.servlet.error.status_code");

        if(status == 404) {
            return ResponseEntity.status(404).body("Hibás API hívás.");
        }

        return ResponseEntity.status(status).body("Hiba történt: " + status);
    }
}
