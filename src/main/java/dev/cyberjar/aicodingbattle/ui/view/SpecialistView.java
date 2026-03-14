package dev.cyberjar.aicodingbattle.ui.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;
import dev.cyberjar.aicodingbattle.dto.NearbyDoctorDto;
import dev.cyberjar.aicodingbattle.ui.TelemedUiService;
import dev.cyberjar.aicodingbattle.ui.UiSessionState;

import java.util.List;

@Route("specialists")
@PageTitle("Specialists")
public class SpecialistView extends VerticalLayout implements BeforeEnterObserver {

    private final TelemedUiService telemedUiService;
    private final Paragraph statusParagraph;
    private final Grid<NearbyDoctorDto> doctorsGrid;

    private IllnessPredictionDto selectedPrediction;

    public SpecialistView(TelemedUiService telemedUiService) {
        this.telemedUiService = telemedUiService;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Nearby specialists");
        this.statusParagraph = new Paragraph("Allow location access to find nearby specialists.");

        this.doctorsGrid = new Grid<>(NearbyDoctorDto.class, false);
        this.doctorsGrid.addColumn(NearbyDoctorDto::firstName).setHeader("First name");
        this.doctorsGrid.addColumn(NearbyDoctorDto::lastName).setHeader("Last name");
        this.doctorsGrid.addColumn(NearbyDoctorDto::specialty).setHeader("Specialty");
        this.doctorsGrid.addColumn(NearbyDoctorDto::address).setHeader("Address");
        this.doctorsGrid.addColumn(item -> String.format("%.2f km", item.distanceInKilometers())).setHeader("Distance");
        this.doctorsGrid.setWidthFull();
        this.doctorsGrid.setHeight("360px");

        Button backButton = new Button("Back to advice", event -> UI.getCurrent().navigate("advice"));

        add(title, this.statusParagraph, this.doctorsGrid, backButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.selectedPrediction = UiSessionState.getSelectedPrediction(VaadinSession.getCurrent());
        if (this.selectedPrediction == null) {
            Notification.show("Please select an illness first.");
            event.forwardTo("illness-selection");
            return;
        }

        getElement().executeJs("""
            const el = this;
            if (!navigator.geolocation) {
              el.$server.onGeolocationError('Geolocation is not supported in this browser.');
            } else {
              navigator.geolocation.getCurrentPosition(
                position => el.$server.onGeolocationSuccess(position.coords.latitude, position.coords.longitude),
                error => el.$server.onGeolocationError(error.message)
              );
            }
            """);
    }

    @ClientCallable
    public void onGeolocationSuccess(double latitude, double longitude) {
        if (this.selectedPrediction == null) {
            this.statusParagraph.setText("No illness selected.");
            this.doctorsGrid.setItems(List.of());
            return;
        }

        List<NearbyDoctorDto> doctors = this.telemedUiService.findNearbyDoctors(
                latitude,
                longitude,
                this.selectedPrediction.specialty());

        if (doctors.isEmpty()) {
            this.statusParagraph.setText("No nearby specialists found for " + this.selectedPrediction.specialty() + ".");
            this.doctorsGrid.setItems(List.of());
            return;
        }

        this.statusParagraph.setText("Found " + doctors.size() + " nearby specialist(s).");
        this.doctorsGrid.setItems(doctors);
    }

    @ClientCallable
    public void onGeolocationError(String errorMessage) {
        this.statusParagraph.setText("Could not access location: " + (errorMessage == null ? "Unknown error" : errorMessage));
        this.doctorsGrid.setItems(List.of());
    }
}
