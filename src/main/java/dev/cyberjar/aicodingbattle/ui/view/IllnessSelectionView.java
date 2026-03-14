package dev.cyberjar.aicodingbattle.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import dev.cyberjar.aicodingbattle.ui.UiSessionState;

import java.util.List;

@Route("illness-selection")
@PageTitle("Illness Selection")
public class IllnessSelectionView extends VerticalLayout implements BeforeEnterObserver {

    private final RadioButtonGroup<IllnessPredictionDto> predictionGroup;

    public IllnessSelectionView() {
        setSizeFull();
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Select the most likely illness");

        this.predictionGroup = new RadioButtonGroup<>();
        this.predictionGroup.setWidthFull();
        this.predictionGroup.setLabel("Predictions");
        this.predictionGroup.setItemLabelGenerator(item -> item.name()
                + " ("
                + Math.round(item.certainty())
                + "%, "
                + item.specialty()
                + ")");

        Button continueButton = new Button("Continue", event -> selectAndContinue());

        add(title, this.predictionGroup, continueButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<IllnessPredictionDto> predictions = UiSessionState.getPredictions(VaadinSession.getCurrent());
        if (predictions.isEmpty()) {
            Notification.show("Please analyze symptoms first.");
            event.forwardTo("symptoms");
            return;
        }

        this.predictionGroup.setItems(predictions);
    }

    private void selectAndContinue() {
        IllnessPredictionDto selectedPrediction = this.predictionGroup.getValue();
        if (selectedPrediction == null) {
            Notification.show("Please select an illness.");
            return;
        }

        VaadinSession session = VaadinSession.getCurrent();
        UiSessionState.setSelectedPrediction(session, selectedPrediction);
        UiSessionState.setShareText(session, null);

        UI.getCurrent().navigate("advice");
    }
}
