package dev.cyberjar.aicodingbattle.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.model.Illness;

import java.util.List;

@Route("/illness-selection")
@PageTitle("Select Illness - Healthcare Assistant")
public class IllnessSelectionView extends Composite<VerticalLayout> {

    private final Select<Illness> ilnessGroup;
    private final Button continueButton;

    public IllnessSelectionView() {
        VerticalLayout layout = getContent();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.setWidth("100%");

        H1 title = new H1("Select Illness");
        H2 subtitle = new H2("Choose the condition that best matches your symptoms");

        ilnessGroup = new Select<>();
        ilnessGroup.setLabel("Illness");
        ilnessGroup.setItemLabelGenerator(illness -> 
                String.format("%s (Certainty: %.0f%%)", illness.getName(), illness.getCertainty())
        );

        continueButton = new Button("Continue", event -> selectIllness());

        Button backButton = new Button("Back", event -> getUI().ifPresent(ui -> ui.navigate("/symptoms")));

        layout.add(title, subtitle, ilnessGroup, continueButton, backButton);

        // Load illnesses from session
        VaadinSession session = VaadinSession.getCurrent();
        @SuppressWarnings("unchecked")
        List<Illness> illnesses = (List<Illness>) session.getAttribute("illnesses");

        if (illnesses != null && !illnesses.isEmpty()) {
            ilnessGroup.setItems(illnesses);
            ilnessGroup.setValue(illnesses.get(0));
        }
    }

    private void selectIllness() {
        Illness selected = ilnessGroup.getValue();
        if (selected == null) {
            new com.vaadin.flow.component.notification.Notification(
                    "Please select an illness", 3000
            ).open();
            return;
        }

        VaadinSession.getCurrent().setAttribute("selectedIllness", selected);
        getUI().ifPresent(ui -> ui.navigate("/advice"));
    }
}
