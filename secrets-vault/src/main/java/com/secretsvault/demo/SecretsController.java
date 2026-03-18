package main.java.com.secretsvault.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretsController {

    // A simple endpoint to test if the client certificate was accepted
    @GetMapping("/ping")
    public String ping() {
        return "mTLS connection successful! Your client certificate is valid.\n";
    }
}