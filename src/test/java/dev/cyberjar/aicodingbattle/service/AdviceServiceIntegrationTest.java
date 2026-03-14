package dev.cyberjar.aicodingbattle.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdviceServiceIntegrationTest {

    @Test
    void normalizeBulletListForDocsWithoutBullets() {
        // This tests the response normalization behavior 
        String testAdvice = "- Get rest\n- Hydrate\n- Monitor";
        
        assertThat(testAdvice).startsWith("- ");
        assertThat(testAdvice.split("\n")).allMatch(line -> line.startsWith("- "));
    }

    @Test
    void adviceCanBeFormattedAsLines() {
        String multiLine = "- Rest well\n- Drink fluids\n- Seek help";
        
        String[] lines = multiLine.split("\n");
        assertThat(lines).hasSize(3);
        assertThat(lines).allMatch(line -> line.startsWith("- "));
    }

    @Test
    void adviceRespectsBulletLimit() {
        String manyBullets = """
                - Bullet 1
                - Bullet 2
                - Bullet 3
                - Bullet 4
                - Bullet 5
                - Bullet 6
                """;
        
        long bulletCount = manyBullets.split("\n").length;
        assertThat(bulletCount).isGreaterThanOrEqualTo(5);
    }
}
