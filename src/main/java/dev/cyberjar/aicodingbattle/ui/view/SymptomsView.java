package dev.cyberjar.aicodingbattle.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import dev.cyberjar.aicodingbattle.ui.TelemedUiService;
import dev.cyberjar.aicodingbattle.ui.UiSessionState;

import java.util.List;

@Route("symptoms")
@PageTitle("Symptoms")
public class SymptomsView extends VerticalLayout {

    private final TelemedUiService telemedUiService;
    private final TextArea symptomsTextArea;

    public SymptomsView(TelemedUiService telemedUiService) {
        this.telemedUiService = telemedUiService;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Describe your symptoms");
        this.symptomsTextArea = new TextArea("Symptoms");
        this.symptomsTextArea.setWidthFull();
        this.symptomsTextArea.setMinHeight("220px");
        this.symptomsTextArea.setPlaceholder("Example: fever, sore throat, headache");

        Button analyzeButton = new Button("Analyze", event -> analyzeSymptoms());

        add(title, this.symptomsTextArea, analyzeButton);
    }

    private void analyzeSymptoms() {
        String symptoms = this.symptomsTextArea.getValue();
        if (symptoms == null || symptoms.isBlank()) {
            Notification.show("Please enter symptoms first.");
            return;
        }

        List<IllnessPredictionDto> predictions = this.telemedUiService.analyzeSymptoms(symptoms);
        if (predictions.isEmpty()) {
            Notification.show("No illness predictions were returned. Please try again.");
            return;
        }

        VaadinSession session = VaadinSession.getCurrent();
        UiSessionState.setPredictions(session, predictions);
        UiSessionState.setSelectedPrediction(session, null);
        UiSessionState.setAdviceText(session, null);
        UiSessionState.setShareText(session, null);

        UI.getCurrent().navigate("illness-selection");
    }
}
