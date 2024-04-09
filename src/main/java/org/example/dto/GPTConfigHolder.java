package org.example.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai.api")
@Data
public class GPTConfigHolder {
    private String key;
    private String model;
    private Integer maxTokens;
    private Double temperature;
}
