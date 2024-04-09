package org.example.config;

import io.github.sashirestela.openai.BaseSimpleOpenAI;
import io.github.sashirestela.openai.SimpleOpenAI;
import lombok.RequiredArgsConstructor;
import org.example.dto.GPTConfigHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final GPTConfigHolder holder;
    @Bean
    public BaseSimpleOpenAI simpleOpenAI() {
        return SimpleOpenAI.builder().apiKey(holder.getKey()).build();
    }
}
