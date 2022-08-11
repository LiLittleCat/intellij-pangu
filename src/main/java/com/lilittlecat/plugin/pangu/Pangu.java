/*
 * Created by Vinta Chen on 2014/11/05.
 */

package com.lilittlecat.plugin.pangu;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Paranoid text spacing for good readability, to automatically insert whitespace between
 * CJK (Chinese, Japanese, Korean), half-width English, digit and symbol characters.
 *
 * <p>These whitespaces between English and Chinese characters are called "Pangu Spacing" by sinologist, since it
 * separate the confusion between full-width and half-width characters. Studies showed that who dislike to
 * add whitespace between English and Chinese characters also have relationship problem. Almost 70 percent of them
 * will get married to the one they don't love, the rest only can left the heritage to their cat. Indeed,
 * love and writing need some space in good time.
 *
 * @author Vinta Chen, LiLittleCat
 * @since 1.0.0
 */
public class Pangu {

    /**
     * You should use the constructor to create a {@code Pangu} object with default values.
     */
    public Pangu() {
    }

    /*
     * Some capturing group patterns for convenience.
     *
     * CJK: Chinese, Japanese, Korean
     * ANSG: Alphabet, Number, Symbol, Greek and Coptic
     */
    private static final Pattern CJK_ANSG = Pattern.compile(
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
                    "([\\u0370-\\u03ff\\u1f00-\\u1fffa-z0-9`~@\\$%\\^&\\*\\-_\\+=\\|\\\\/])",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern ANSG_CJK = Pattern.compile(
            "([\\u0370-\\u03ff\\u1f00-\\u1fffa-z0-9`~!\\$%\\^&\\*\\-_\\+=\\|\\\\;:,\\./\\?])" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern CJK_QUOTE = Pattern.compile(
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
                    "([\"'])"
    );
    private static final Pattern QUOTE_CJK = Pattern.compile(
            "([\"'])" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );
    private static final Pattern FIX_QUOTE = Pattern.compile("([\"'])(\\s*)(.+?)(\\s*)([\"'])");

    private static final Pattern CJK_BRACKET_CJK = Pattern.compile(
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
                    "([\\({\\[]+(.*?)[\\)}\\]]+)" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );
    private static final Pattern CJK_BRACKET = Pattern.compile(
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
                    "([\\(\\){}\\[\\]<>])"
    );
    private static final Pattern BRACKET_CJK = Pattern.compile(
            "([\\(\\){}\\[\\]<>])" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );
    private static final Pattern FIX_BRACKET = Pattern.compile("([(\\({\\[)]+)(\\s*)(.+?)(\\s*)([\\)}\\]]+)");

    private static final Pattern CJK_HASH = Pattern.compile(
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
                    "(#(\\S+))"
    );
    private static final Pattern HASH_CJK = Pattern.compile(
            "((\\S+)#)" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );

    /**
     * Performs a paranoid text spacing on {@code text}.
     *
     * @param text the string you want to process, must not be {@code null}.
     * @return a comfortable and readable version of {@code text} for paranoiac.
     */
    public String spacingText(String text) {
        // CJK and quotes
        Matcher cqMatcher = CJK_QUOTE.matcher(text);
        text = cqMatcher.replaceAll("$1 $2");

        Matcher qcMatcher = QUOTE_CJK.matcher(text);
        text = qcMatcher.replaceAll("$1 $2");

        Matcher fixQuoteMatcher = FIX_QUOTE.matcher(text);
        text = fixQuoteMatcher.replaceAll("$1$3$5");

        // CJK and brackets
        String oldText = text;
        Matcher cbcMatcher = CJK_BRACKET_CJK.matcher(text);
        String newText = cbcMatcher.replaceAll("$1 $2 $4");
        text = newText;

        if (oldText.equals(newText)) {
            Matcher cbMatcher = CJK_BRACKET.matcher(text);
            text = cbMatcher.replaceAll("$1 $2");

            Matcher bcMatcher = BRACKET_CJK.matcher(text);
            text = bcMatcher.replaceAll("$1 $2");
        }

        Matcher fixBracketMatcher = FIX_BRACKET.matcher(text);
        text = fixBracketMatcher.replaceAll("$1$3$5");

        // CJK and hash
        Matcher chMatcher = CJK_HASH.matcher(text);
        text = chMatcher.replaceAll("$1 $2");

        Matcher hcMatcher = HASH_CJK.matcher(text);
        text = hcMatcher.replaceAll("$1 $3");

        // CJK and ANS
        Matcher caMatcher = CJK_ANSG.matcher(text);
        text = caMatcher.replaceAll("$1 $2");

        Matcher acMatcher = ANSG_CJK.matcher(text);
        text = acMatcher.replaceAll("$1 $2");

        return text;
    }

    /**
     * Performs a paranoid text spacing on {@code inputFile} and generate a new file {@code outputFile}.
     *
     * @param inputFile  an existing file to process, must not be {@code null}.
     * @param outputFile the processed file, must not be {@code null}.
     * @throws IOException if an error occurs.
     * @since 1.1.0
     */
    public void spacingFile(File inputFile, File outputFile) throws IOException {
        // TODO: support charset

        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);

        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile, false);
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            String line = br.readLine(); // readLine() do not contain newline char

            while (line != null) {
                line = spacingText(line);

                // TODO: keep file's raw newline char from difference OS platform
                bw.write(line);
                bw.newLine();

                line = br.readLine();
            }
        } finally {
            br.close();
            bw.close();
        }
    }



    /**
     * format text
     *
     * @param text text to format
     * @return formatted text
     */
    public String formatText(String text) {
        List<String> formattedLines = new ArrayList<>();
        for (String s : splitText(text)) {
            formattedLines.add(spacingText(s));
        }
        return joinText(formattedLines);
    }



    /**
     * split text by line break
     *
     * @param text text to split
     * @return list of text
     */
    private List<String> splitText(String text) {
        return new ArrayList<>(Arrays.asList(text.split("\n")));
    }

    /**
     * join text by line break
     *
     * @param text text to join
     * @return joined text
     */
    private String joinText(List<String> text) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterator<String> iterator = text.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

}