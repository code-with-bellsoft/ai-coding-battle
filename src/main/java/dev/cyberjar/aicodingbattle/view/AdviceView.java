package dev.cyberjar.aicodingbattle.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.model.Illness;
import dev.cyberjar.aicodingbattle.service.AdviceService;

import java.util.List;

@Route("/advice")
@PageTitle("Medical Advice - Healthcare Assistant")
public class AdviceView extends Composite<VerticalLayout> {

    private final AdviceService adviceService;

    public AdviceView(AdviceService adviceService) {
        this.adviceService = adviceService;

        VerticalLayout layout = getContent();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.setWidth("100%");

        H1 title = new H1("Medical Advice");

        VaadinSession session = VaadinSession.getCurrent();
        Illness selectedIllness = (Illness) session.getAttribute("selectedIllness");

        if (selectedIllness != null) {
            H2 illnessTitle = new H2("Condition: " + selectedIllness.getName());
            layout.add(title, illnessTitle);

            try {
                List<String> advice = adviceService.generateAdvice(selectedIllness.getName());

                UnorderedList adviceList = new UnorderedList();
                for (String item : advice) {
                    adviceList.add(new ListItem(item));
                }

                layout.add(new H2("Recommended Actions:"), adviceList);
            } catch (Exception e) {
                layout.add(new H2("Error generating advice: " + e.getMessage()));
            }

            layout.add(new Hr());

            Button findSpecialistButton = new Button("Find " + selectedIllness.getSpecialty() + " Specialist",
                    event -> findSpecialist(selectedIllness.getSpecialty()));

            Button shareButton = new Button("Generate Share Text",
                    event -> generateShare(selectedIllness.getName()));

            Button backButton = new Button("Back",
                    event -> getUI().ifPresent(ui -> ui.navigate("/illness-selection")));

            layout.add(findSpecialistButton, shareButton, backButton);
        } else {
            layout.add(title, new H2("No illness selected. Please go back."));
            Button backButton = new Button("Back",
                    event -> getUI().ifPresent(ui -> ui.navigate("/illness-selection")));
            layout.add(backButton);
        }
    }

    private void findSpecialist(String specialty) {
        VaadinSession.getCurrent().setAttribute("selectedSpecialty", specialty);
        getUI().ifPresent(ui -> ui.navigate("/specialists"));
    }

    private void generateShare(String illnessName) {
        VaadinSession.getCurrent().setAttribute("shareText", 
                "I received a healthcare recommendation for: " + illnessName + 
                ". Please consult a medical professional for proper diagnosis.");
        getUI().ifPresent(ui -> ui.navigate("/share"));
    }
}
