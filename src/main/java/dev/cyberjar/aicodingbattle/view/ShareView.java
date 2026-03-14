package dev.cyberjar.aicodingbattle.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("/share")
@PageTitle("Share Results - Healthcare Assistant")
public class ShareView extends Composite<VerticalLayout> {

    public ShareView() {
        VerticalLayout layout = getContent();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.setWidth("100%");

        H1 title = new H1("Share Your Results");
        H2 subtitle = new H2("Copy the text below to share with others");

        VaadinSession session = VaadinSession.getCurrent();
        String shareTextValue = (String) session.getAttribute("shareText");

        if (shareTextValue == null || shareTextValue.isEmpty()) {
            shareTextValue = "I received a healthcare recommendation. Please consult a medical professional.";
        }

        final String shareText = shareTextValue;

        TextArea textArea = new TextArea();
        textArea.setValue(shareText);
        textArea.setReadOnly(true);
        textArea.setWidth("100%");
        textArea.setHeight("150px");

        Button copyButton = new Button("Copy to Clipboard", event -> copyToClipboard(shareText));
        Button backButton = new Button("Back", event -> getUI().ifPresent(ui -> ui.navigate("/advice")));
        Button homeButton = new Button("Start Over", event -> getUI().ifPresent(ui -> ui.navigate("/symptoms")));

        layout.add(title, subtitle, textArea, copyButton, backButton, homeButton);
    }

    private void copyToClipboard(String text) {
        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "navigator.clipboard.writeText('" + escapeJavaScript(text) + "').then(() => {" +
                "  console.log('Text copied to clipboard');" +
                "});"
        ));
        new Notification("Text copied to clipboard!", 3000).open();
    }

    private String escapeJavaScript(String text) {
        return text.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
