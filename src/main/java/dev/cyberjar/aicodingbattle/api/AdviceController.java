package dev.cyberjar.aicodingbattle.api;

import dev.cyberjar.aicodingbattle.service.AdviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdviceController {

    private final AdviceService adviceService;

    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @GetMapping("/advice/{illness}")
    public ResponseEntity<List<String>> getAdvice(@PathVariable String illness) {
        List<String> advice = adviceService.generateAdvice(illness);
        return ResponseEntity.ok(advice);
    }
}
