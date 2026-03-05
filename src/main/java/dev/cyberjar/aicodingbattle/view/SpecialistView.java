package dev.cyberjar.aicodingbattle.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.grid.Grid;
import dev.cyberjar.aicodingbattle.entity.Doctor;
import dev.cyberjar.aicodingbattle.service.SpecialistSearchService;

import java.math.BigDecimal;
import java.util.List;

@Route("/specialists")
@PageTitle("Find Specialist - Healthcare Assistant")
public class SpecialistView extends Composite<VerticalLayout> {

    private final SpecialistSearchService specialistSearchService;
    private Grid<Doctor> doctorGrid;

    public SpecialistView(SpecialistSearchService specialistSearchService) {
        this.specialistSearchService = specialistSearchService;

        VerticalLayout layout = getContent();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.setWidth("100%");

        H1 title = new H1("Find Specialists");

        VaadinSession session = VaadinSession.getCurrent();
        String specialty = (String) session.getAttribute("selectedSpecialty");

        if (specialty == null || specialty.isEmpty()) {
            layout.add(title, new H2("No specialty selected."));
            Button backButton = new Button("Back",
                    event -> getUI().ifPresent(ui -> ui.navigate("/advice")));
            layout.add(backButton);
            return;
        }

        H2 subtitleH2 = new H2("Specialists in: " + specialty);

        doctorGrid = new Grid<>(Doctor.class, false);
        doctorGrid.addColumn(Doctor::getFirstName).setHeader("First Name");
        doctorGrid.addColumn(Doctor::getLastName).setHeader("Last Name");
        doctorGrid.addColumn(Doctor::getSpecialty).setHeader("Specialty");
        doctorGrid.addColumn(Doctor::getAddress).setHeader("Address");

        Button locateButton = new Button("Use My Location", event -> locateAndFindDoctors(specialty));
        Button backButton = new Button("Back",
                event -> getUI().ifPresent(ui -> ui.navigate("/advice")));

        layout.add(title, subtitleH2, new com.vaadin.flow.component.html.Paragraph(
                "Click the button below to share your location and find nearby specialists."
        ), locateButton, doctorGrid, backButton);
    }

    private void locateAndFindDoctors(String specialty) {
        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "if (navigator.geolocation) { " +
                "  navigator.geolocation.getCurrentPosition(position => { " +
                "    window.heathcareCoords = [position.coords.latitude, position.coords.longitude]; " +
                "  }, (error) => { " +
                "    alert('Location access denied. Using default location.'); " +
                "    window.heathcareCoords = [40.7128, -74.0060]; " +
                "  }); " +
                "} else { " +
                "  alert('Geolocation not supported'); " +
                "  window.heathcareCoords = [40.7128, -74.0060]; " +
                "}"
        ).then(result -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            getUI().ifPresent(ui2 -> ui2.getPage().executeJs(
                    "return window.heathcareCoords || [40.7128, -74.0060]"
            ).then(result2 -> {
                if (result2 != null) {
                    try {
                        String[] parts = result2.toString().replace("[", "").replace("]", "").split(",");
                        if (parts.length == 2) {
                            double lat = Double.parseDouble(parts[0].trim());
                            double lon = Double.parseDouble(parts[1].trim());
                            loadDoctors(specialty, BigDecimal.valueOf(lat), BigDecimal.valueOf(lon));
                        }
                    } catch (Exception e) {
                        loadDoctors(specialty, BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060));
                    }
                }
            }));
        }));
    }

    private void loadDoctors(String specialty, BigDecimal latitude, BigDecimal longitude) {
        List<Doctor> doctors = specialistSearchService.findNearbyDoctors(latitude, longitude, specialty);
        doctorGrid.setItems(doctors);

        if (doctors.isEmpty()) {
            new Notification("No doctors found in your area for this specialty.", 5000).open();
        }
    }
}
