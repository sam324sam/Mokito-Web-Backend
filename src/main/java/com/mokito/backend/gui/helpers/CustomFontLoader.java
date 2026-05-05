package com.mokito.backend.gui.helpers;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class CustomFontLoader {
    private CustomFontLoader() {
        /* This utility class should not be instantiated */
    }


    public static Font loadFont(String resourcePath, float size) {
        try (InputStream is = CustomFontLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.out.println("No se encontró la fuente: " + resourcePath);
                return new Font("Arial", Font.PLAIN, (int) size);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(size);
            System.out.println("Se a cargado la fuente");
            return font;
        } catch (FontFormatException | IOException e) {
            System.out.println("No se a cargado la fuente");
            return new Font("Arial", Font.PLAIN, (int) size);
        }
    }
}