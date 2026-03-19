package main.java.com.secretsvault.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/secrets")
public class SecretsController {

    private final CryptoService cryptoService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File vaultFile = new File("vault-data.json");

    public SecretsController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping("/{key}")
    public String saveSecret(@PathVariable String key, @RequestBody String plaintextSecret) throws Exception {
        String encryptedData = cryptoService.encrypt(plaintextSecret);
        
        Map<String, String> vault = loadVault();
        vault.put(key, encryptedData);
        saveVault(vault);

        return "Secret successfully encrypted and stored.\n";
    }

    @GetMapping("/{key}")
    public String getSecret(@PathVariable String key) throws Exception {
        Map<String, String> vault = loadVault();
        String encryptedData = vault.get(key);
        
        if (encryptedData == null) {
            return "Secret not found.\n";
        }
        
        return cryptoService.decrypt(encryptedData) + "\n";
    }

    // --- Helper methods for File I/O ---
    private Map<String, String> loadVault() {
        if (!vaultFile.exists()) return new HashMap<>();
        try {
            return objectMapper.readValue(vaultFile, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read vault file", e);
        }
    }

    private void saveVault(Map<String, String> vault) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(vaultFile, vault);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write vault file", e);
        }
    }
}