package edu.rockefeller.delangelab.randomscript.utils;

import edu.rockefeller.delangelab.randomscript.constants.Constants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * Class containing static utilities for handling file names.
 */
public class FileNameUtils {

    /**
     * Takes a file name (without an extension) and obfuscates it.
     *
     * IMPORTANT: {@link #deobfuscate(String)} MUST complement this method. Given any {@link String s},
     * {@code s.equals(deobfuscate(obfuscate(s)))} must ALWAYS be true.
     *
     * @param fileName without extension
     * @return obfuscated filename
     */
    public static String obfuscate(String fileName) {
        char[] original = fileName.toCharArray();
        char[] obfuscated = new char[original.length * 2 + 1];
        Random numberGenerator = new Random();

        for (int i = 0; i < obfuscated.length; i++) {
            if (i % 2 == 0) { // We are at an even position (0, 2, ...)
                int randomIndex = numberGenerator.nextInt(Constants.ALPHABET.length());
                char randomChar = Constants.ALPHABET.charAt(randomIndex);
                obfuscated[i] = randomChar;
            } else { // We are at an odd position
                obfuscated[i] = original[i / 2];
            }
        }

        String toEncode = new String(obfuscated);
        byte[] encoded = toEncode.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(encoded);
    }

    /**
     * Takes an obfuscated file name (without and extension) and deobfuscates it to its original form.
     *
     * IMPORTANT: This method MUST complement {@link #obfuscate(String)}. Given any {@link String s},
     * {@code s.equals(deobfuscate(obfuscate(s)))} must ALWAYS be true.
     *
     * @param fileName obfuscated, without extension
     * @return original file name
     */
    public static String deobfuscate(String fileName) {
        byte[] decoded = Base64.getDecoder().decode(fileName);
        String decodedName = new String(decoded, StandardCharsets.UTF_8);

        char[] obfuscated = decodedName.toCharArray();
        char[] original = new char[obfuscated.length / 2];
        for (int i = 1; i < obfuscated.length; i += 2) {
            original[i / 2] = obfuscated[i];
        }

        return new String(original);
    }
}
