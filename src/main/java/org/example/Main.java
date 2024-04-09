package org.example;

import io.github.sashirestela.openai.BaseSimpleOpenAI;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.finetuning.FineTuningRequest;
import io.github.sashirestela.openai.domain.finetuning.FineTuningResponse;
import lombok.RequiredArgsConstructor;
import org.example.dto.GPTConfigHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.sashirestela.openai.domain.file.PurposeType.FINE_TUNE;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private final BaseSimpleOpenAI openAI;
    private final GPTConfigHolder holder;
    @Override
    public void run(String... args) {
        var path = Paths.get("data.json");
        var fileReq = FileRequest.builder().file(path).purpose(FINE_TUNE).build();
        var id = openAI.files().create(fileReq).join().getId();
        System.out.println(id);
        var request = FineTuningRequest.builder()
                .model(holder.getModel())
                .trainingFile(id).build();
        FineTuningResponse resp = openAI.fineTunings().create(request).join();
        System.out.println(resp);


    }
}