package dev.cyberjar.aicodingbattle;

import org.springframework.boot.SpringApplication;

public class TestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(AiCodingBattleApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
