package dev.cyberjar.aicodingbattle.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.service.SymptomAnalysisService;
import dev.cyberjar.aicodingbattle.model.Illness;

import java.util.List;

@Route("/symptoms")
@PageTitle("Symptoms - Healthcare Assistant")
public class SymptomsView extends VerticalLayout {

    private final SymptomAnalysisService symptomAnalysisService;
    private final TextArea symptomsInput;
    private final Button analyzeButton;

    public SymptomsView(SymptomAnalysisService symptomAnalysisService) {
        this.symptomAnalysisService = symptomAnalysisService;

        setSpacing(true);
        setPadding(true);
        setWidth("100%");

        H1 title = new H1("Healthcare Assistant");
        H2 subtitle = new H2("Describe Your Symptoms");

        symptomsInput = new TextArea();
        symptomsInput.setLabel("Symptoms");
        symptomsInput.setPlaceholder("Enter your symptoms here...");
        symptomsInput.setWidth("100%");
        symptomsInput.setHeight("200px");

        analyzeButton = new Button("Analyze Symptoms", event -> analyzeSymptoms());

        Paragraph info = new Paragraph(
                "Describe your symptoms in detail. Our AI will analyze them and suggest possible illnesses."
        );

        add(title, subtitle, symptomsInput, analyzeButton, info);
    }

    private void analyzeSymptoms() {
        String symptoms = symptomsInput.getValue();
        if (symptoms.isEmpty()) {
            new com.vaadin.flow.component.notification.Notification("Please enter symptoms", 3000).open();
            return;
        }

        try {
            List<Illness> illnesses = symptomAnalysisService.analyzeSymptoms(symptoms);
            VaadinSession.getCurrent().setAttribute("symptoms", symptoms);
            VaadinSession.getCurrent().setAttribute("illnesses", illnesses);
            getUI().ifPresent(ui -> ui.navigate("/illness-selection"));
        } catch (Exception e) {
            new com.vaadin.flow.component.notification.Notification(
                    "Error analyzing symptoms: " + e.getMessage(), 5000
            ).open();
        }
    }
}
