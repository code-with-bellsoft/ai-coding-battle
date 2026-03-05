package dev.cyberjar.aicodingbattle.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.ui.UiSessionState;

@Route("share")
@PageTitle("Share")
public class ShareView extends VerticalLayout implements BeforeEnterObserver {

    private final TextArea shareTextArea;

    public ShareView() {
        setSizeFull();
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Share recommendation");

        this.shareTextArea = new TextArea("Share text");
        this.shareTextArea.setReadOnly(true);
        this.shareTextArea.setWidthFull();
        this.shareTextArea.setMinHeight("260px");

        Button copyButton = new Button("Copy to clipboard", event -> copyToClipboard());
        Button backButton = new Button("Back to advice", event -> UI.getCurrent().navigate("advice"));

        add(title, this.shareTextArea, copyButton, backButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String shareText = UiSessionState.getShareText(VaadinSession.getCurrent());
        if (shareText == null || shareText.isBlank()) {
            Notification.show("Please generate share text first.");
            event.forwardTo("advice");
            return;
        }

        this.shareTextArea.setValue(shareText);
    }

    private void copyToClipboard() {
        String text = this.shareTextArea.getValue();
        if (text == null || text.isBlank()) {
            Notification.show("Nothing to copy.");
            return;
        }

        getElement().executeJs("navigator.clipboard && navigator.clipboard.writeText($0)", text);
        Notification.show("Copied to clipboard.");
    }
}
