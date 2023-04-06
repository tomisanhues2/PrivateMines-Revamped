package me.tomisanhues2.pmines.utils;

import org.bukkit.ChatColor;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final Pattern PATTERN_HEX_GRADIENT =
            Pattern.compile("<#([\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F])>(.*?)<#/([\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F])>");
    private static final Pattern PATTERN_AMPERSAND_HASH =
            Pattern.compile("&#([\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F])");
    private static final Pattern PATTERN_XML_LIKE_HASH =
            Pattern.compile("<#([\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F])>");

    public static String color(String text) {
        text = text.replace("&&", "{ampersand}");
        text = replaceGradients(text);
        text = replaceRegexWithGroup(text, PATTERN_XML_LIKE_HASH, 1, TextUtils::addAmpersandsToHex);
        text = replaceRegexWithGroup(text, PATTERN_AMPERSAND_HASH, 1, TextUtils::addAmpersandsToHex);
        text = ChatColor.translateAlternateColorCodes('&', text);
        text = text.replace("{ampersand}", "&");
        return text;
    }

    private static String replaceGradients(String text) {
        text =
                text.replaceAll("<#/([\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F][\\da-fA-F])>", "<#/$1><#$1>");
        Matcher matcher = PATTERN_HEX_GRADIENT.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            HexColor startColor = new HexColor(matcher.group(1));
            HexColor endColor = new HexColor(matcher.group(3));
            String partText = matcher.group(2);
            matcher.appendReplacement(sb, HexColor.applyGradient(partText, startColor, endColor));
        }

        matcher.appendTail(sb);

        String result;
        for (result =
                     sb.toString(); result.matches(".*&x&[\\da-zA-Z]&[\\da-zA-Z]&[\\da-zA-Z]&[\\da-zA-Z]&[\\da-zA-Z]&[\\da-zA-Z]$");
             result = result.substring(0, result.length() - 14)) {
        }

        return result;
    }

    private static String replaceRegexWithGroup(CharSequence text, Pattern pattern, int group, Function<String, String> function) {
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, function.apply(matcher.group(group)));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String addAmpersandsToHex(String hex) {
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Hex-Codes must always have 6 letters.");
        } else {
            char[] chars = hex.toCharArray();
            StringBuilder sb = new StringBuilder("&x");
            char[] var3 = chars;
            int var4 = chars.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                char aChar = var3[var5];
                sb.append("&").append(aChar);
            }

            return sb.toString();
        }
    }
}

