package dev.cyberjar.aicodingbattle.ui;

import com.vaadin.flow.server.VaadinSession;
import dev.cyberjar.aicodingbattle.dto.IllnessPredictionDto;

import java.util.List;

public final class UiSessionState {

    private static final String KEY_PREDICTIONS = "telemed.predictions";
    private static final String KEY_SELECTED_PREDICTION = "telemed.selectedPrediction";
    private static final String KEY_ADVICE_TEXT = "telemed.adviceText";
    private static final String KEY_SHARE_TEXT = "telemed.shareText";

    private UiSessionState() {
    }

    public static void setPredictions(VaadinSession session, List<IllnessPredictionDto> predictions) {
        session.setAttribute(KEY_PREDICTIONS, predictions == null ? List.of() : List.copyOf(predictions));
    }

    @SuppressWarnings("unchecked")
    public static List<IllnessPredictionDto> getPredictions(VaadinSession session) {
        Object value = session.getAttribute(KEY_PREDICTIONS);
        if (value instanceof List<?> list) {
            return (List<IllnessPredictionDto>) list;
        }
        return List.of();
    }

    public static void setSelectedPrediction(VaadinSession session, IllnessPredictionDto prediction) {
        session.setAttribute(KEY_SELECTED_PREDICTION, prediction);
    }

    public static IllnessPredictionDto getSelectedPrediction(VaadinSession session) {
        Object value = session.getAttribute(KEY_SELECTED_PREDICTION);
        if (value instanceof IllnessPredictionDto prediction) {
            return prediction;
        }
        return null;
    }

    public static void setAdviceText(VaadinSession session, String advice) {
        session.setAttribute(KEY_ADVICE_TEXT, advice);
    }

    public static String getAdviceText(VaadinSession session) {
        Object value = session.getAttribute(KEY_ADVICE_TEXT);
        return value instanceof String text ? text : null;
    }

    public static void setShareText(VaadinSession session, String shareText) {
        session.setAttribute(KEY_SHARE_TEXT, shareText);
    }

    public static String getShareText(VaadinSession session) {
        Object value = session.getAttribute(KEY_SHARE_TEXT);
        return value instanceof String text ? text : null;
    }
}
