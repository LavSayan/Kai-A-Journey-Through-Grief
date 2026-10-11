package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.Theme;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

/**
 * The two flat, bordered button styles used across the battle screens.
 */
public final class StyledButtons {

    private StyledButtons() {}

    /** Small square-ish button for the title bar. */
    public static Button menu(String label, Runnable action) {
        return build(label, Theme.META_9, Theme.META_TEXT,
                "-fx-padding: 4 10;", 3, Theme.ACTIVE_TITLE, action, true);
    }

    /** Wide button for the victory/defeat card. */
    public static Button overlay(String label, Color accentColor, Runnable action) {
        return build(label, Theme.META_12, Theme.ACTIVE_TITLE,
                "-fx-padding: 8 28;", 5, accentColor, action, false);
    }

    private static Button build(String label, javafx.scene.text.Font font, Color textColor,
                                String padding, double radius, Color hoverBorder,
                                Runnable action, boolean hoverAlsoTintsText) {
        Button btn = new Button(label);
        btn.setFont(font);
        btn.setTextFill(textColor);

        String base = "-fx-background-color: transparent;"
                + "-fx-border-color: " + toHex(Theme.BAND_DIVIDER) + ";"
                + "-fx-border-width: 1;-fx-border-radius: " + radius + ";"
                + padding + "-fx-cursor: hand;";
        String hover = "-fx-background-color: " + toHex(Theme.BAND_DIVIDER) + ";"
                + "-fx-border-color: " + toHex(hoverBorder) + ";"
                + "-fx-border-width: 1;-fx-border-radius: " + radius + ";"
                + padding + "-fx-cursor: hand;";

        btn.setStyle(base);
        btn.setOnMouseEntered(e -> {
            btn.setStyle(hover);
            if (hoverAlsoTintsText) btn.setTextFill(hoverBorder);
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(base);
            if (hoverAlsoTintsText) btn.setTextFill(textColor);
        });
        if (action != null) btn.setOnAction(e -> action.run());
        return btn;
    }

    static String toHex(Color color) {
        return "#" + String.format("%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255));
    }
}
