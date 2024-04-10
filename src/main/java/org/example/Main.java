package org.example;

import io.github.sashirestela.openai.BaseSimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.chat.content.ContentPartText;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgSystem;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.PurposeType;
import io.github.sashirestela.openai.domain.finetuning.FineTuningRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.GPTConfigHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class Main implements CommandLineRunner {
    public static final String SUFFIX = "ftjob-myJob";
    public static final String DATA_FILE = "src/main/resources/data.jsonl";
    private final BaseSimpleOpenAI openAI;
    private final GPTConfigHolder holder;
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Override
    public void run(String... args) throws URISyntaxException {

        fineTuning();
//        usingFineTunedModel();
    }


    private void fineTuning() throws URISyntaxException {
        var fileRequest = FileRequest.builder()
                .file(Paths.get(DATA_FILE))
                .purpose(PurposeType.FINE_TUNE)
                .build();

        var id = openAI.files().create(fileRequest).join().getId();
        System.out.println(id);

        var tuningRequest = FineTuningRequest.builder()
                .model(holder.getModel())
                .suffix(SUFFIX)
                .trainingFile(id).build();
        var resp = openAI.fineTunings().create(tuningRequest).join();
        System.out.println(resp);
    }

    private void usingFineTunedModel() {
        var system = new ChatMsgSystem("You are a funny person who always keeps cracking dad jokes.");
        var user = new ChatMsgUser(List.of(new ContentPartText("Why a raven is like a writing desk?")));
        var chatRequest = ChatRequest.builder()
                .model("ft:gpt-3.5-turbo-0125:personal:ftjob-myjob:9CRJJ9NF")
                .messages(List.of(system, user))
                .temperature(holder.getTemperature())
                .maxTokens(holder.getMaxTokens())
                .build();
        var response = openAI.chatCompletions().createStream(chatRequest).join();
        System.out.println(response.map(ChatResponse::firstContent).filter(Objects::nonNull)
                .collect(Collectors.joining()));
    }
}