package dev.cyberjar.aicodingbattle.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import dev.cyberjar.aicodingbattle.ui.TelemedUiService;
import dev.cyberjar.aicodingbattle.ui.UiSessionState;

@Route("advice")
@PageTitle("Advice")
public class AdviceView extends VerticalLayout implements BeforeEnterObserver {

    private final TelemedUiService telemedUiService;
    private final Paragraph illnessParagraph;
    private final Paragraph adviceParagraph;

    public AdviceView(TelemedUiService telemedUiService) {
        this.telemedUiService = telemedUiService;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Recommended advice");
        this.illnessParagraph = new Paragraph();
        this.adviceParagraph = new Paragraph();
        this.adviceParagraph.getStyle().set("white-space", "pre-wrap");

        Button specialistsButton = new Button("Find specialist", event -> UI.getCurrent().navigate("specialists"));
        Button shareButton = new Button("Generate share text", event -> generateShareTextAndNavigate());

        add(title, this.illnessParagraph, this.adviceParagraph, new HorizontalLayout(specialistsButton, shareButton));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        VaadinSession session = VaadinSession.getCurrent();
        IllnessPredictionDto selectedPrediction = UiSessionState.getSelectedPrediction(session);
        if (selectedPrediction == null) {
            Notification.show("Please select an illness first.");
            event.forwardTo("illness-selection");
            return;
        }

        String advice = this.telemedUiService.generateAdvice(selectedPrediction.name());
        UiSessionState.setAdviceText(session, advice);

        this.illnessParagraph.setText("Selected illness: " + selectedPrediction.name());
        this.adviceParagraph.setText(advice);
    }

    private void generateShareTextAndNavigate() {
        VaadinSession session = VaadinSession.getCurrent();
        IllnessPredictionDto selectedPrediction = UiSessionState.getSelectedPrediction(session);
        if (selectedPrediction == null) {
            Notification.show("Please select an illness first.");
            UI.getCurrent().navigate("illness-selection");
            return;
        }

        String advice = UiSessionState.getAdviceText(session);
        String shareText = this.telemedUiService.buildShareText(selectedPrediction, advice);
        UiSessionState.setShareText(session, shareText);
        UI.getCurrent().navigate("share");
    }
}
