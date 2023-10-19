package com.jvavateam.carsharingapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health controller",
        description = "API health management")
public class HealthController {
    @GetMapping("/check")
    public String check() {
        return "The car rental application is running stably";
    }
}
