package at.alex.timechanger.config.gui;

public class Color {
    private int color;

    private Color(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public static Color fromHex(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("Hex color cannot be null");
        }

        hex = hex.trim();

        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        int color;

        switch (hex.length()) {
            case 6 -> {
                color = (0xFF << 24) | Integer.parseInt(hex, 16);
            }
            case 8 -> {
                color = (int) Long.parseLong(hex, 16);
            }
            default -> throw new IllegalArgumentException(
                    "Invalid Color format");
        }

        return new Color(color);
    }
}
