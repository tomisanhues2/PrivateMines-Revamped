package me.tomisanhues2.pmines.utils;

import org.bukkit.ChatColor;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public final class HexColor {

    private static final String REGEX_COLOR_COMPONENT = "[0-9a-zA-Z][0-9a-zA-Z]";
    private int red;
    private int green;
    private int blue;

    public HexColor(int red, int green, int blue) {
        try {
            Validate.inclusiveBetween(0, 255, red);
            Validate.inclusiveBetween(0, 255, green);
            Validate.inclusiveBetween(0, 255, blue);
            this.setRed(red);
            this.setGreen(green);
            this.setBlue(blue);
        } catch (Throwable var5) {
            throw var5;
        }
    }

    public HexColor(String hex) {
        this(hex.substring(0, 2), hex.substring(2, 4), hex.substring(4, 6));
    }

    public HexColor(String red, String green, String blue) {
        Validate.matchesPattern(red, "[0-9a-zA-Z][0-9a-zA-Z]");
        Validate.matchesPattern(green, "[0-9a-zA-Z][0-9a-zA-Z]");
        Validate.matchesPattern(blue, "[0-9a-zA-Z][0-9a-zA-Z]");
        this.setRed(Integer.parseInt(red, 16));
        this.setGreen(Integer.parseInt(green, 16));
        this.setBlue(Integer.parseInt(blue, 16));
    }

    public static String applyGradient(String text, HexColor start, HexColor end) {
        char[] chars = text.toCharArray();
        int length = text.length();
        StringBuilder sb = new StringBuilder();
        String nextFormat = "";

        for (int i = 0; i < length; ++i) {
            if (nextFormat.length() % 2 == 1) {
                if (chars[i] != 'r' && chars[i] != 'R') {
                    nextFormat = nextFormat + chars[i];
                } else {
                    sb.append(ChatColor.translateAlternateColorCodes('&', "&r"));
                    nextFormat = "";
                }
            } else if (chars[i] != '&' && chars[i] != 167) {
                sb.append(getHexAtPositionInGradient(start, end, length, i).toColorCode()).append(ChatColor.translateAlternateColorCodes('&', nextFormat)).append(chars[i]);
            } else {
                nextFormat = nextFormat + "&";
            }
        }

        return sb.toString();
    }

    public String toColorCode() {
        StringBuilder sb = new StringBuilder("&x");
        char[] chars = this.toHex().toCharArray();
        char[] var3 = chars;
        int var4 = chars.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Character aChar = var3[var5];
            sb.append('&').append(aChar);
        }

        return sb.toString();
    }

    public static HexColor getHexAtPositionInGradient(HexColor start, HexColor end, int textLengh, int position) {
        if (position == 0) {
            return start;
        } else if (position == textLengh - 1) {
            return end;
        } else {
            int colorsNeeded = textLengh - 1;
            int r = getSingleValueAtPositionInGradient(start.getRed(), end.getRed(), colorsNeeded, position);
            int g = getSingleValueAtPositionInGradient(start.getGreen(), end.getGreen(), colorsNeeded, position);
            int b = getSingleValueAtPositionInGradient(start.getBlue(), end.getBlue(), colorsNeeded, position);
            return new HexColor(r, g, b);
        }
    }

    public String toHex() {
        return String.format("%02x", this.getRed()) + String.format("%02x", this.getGreen()) + String.format("%02x", this.getBlue());
    }

    private static int getSingleValueAtPositionInGradient(int start, int end, int colorsNeeded, int position) {
        if (position == 0) {
            return start;
        } else if (position == colorsNeeded) {
            return end;
        } else if (start == end) {
            return start;
        } else {
            int diff = start - end;
            return start - diff / colorsNeeded * position;
        }
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        Validate.inclusiveBetween(0, 255, red);
        this.red = red;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) {
        Validate.inclusiveBetween(0, 255, green);
        this.green = green;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) {
        Validate.inclusiveBetween(0, 255, blue);
        this.blue = blue;
    }

    public int hashCode() {
        return Objects.hash(this.red, this.green, this.blue);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            HexColor hexColor = (HexColor) o;
            return this.red == hexColor.red && this.green == hexColor.green && this.blue == hexColor.blue;
        } else {
            return false;
        }
    }

    public String toString() {
        return "HexColor{r=" + this.red + ", g=" + this.green + ", b=" + this.blue + '}';
    }
}

