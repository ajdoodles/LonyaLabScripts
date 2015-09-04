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
        char[] nameToArray = fileName.toCharArray();
        int nameArrayLength = nameToArray.length;
        int extendedLength = nameArrayLength * 2 + 1;
        char[] nameForEncoding;
        nameForEncoding = new char[extendedLength];
        Random numberGenerator = new Random();

        for (int i = 0; i < nameForEncoding.length; i++) {
            if (i % 2 == 0) { // We are at an even position (0, 2, ...)
                int randomIndex = numberGenerator.nextInt(Constants.ALPHABET.length());
                char randomChar = Constants.ALPHABET.charAt(randomIndex);
                nameForEncoding[i] = randomChar;
            } else { // We are at an odd position
                nameForEncoding[i] = nameToArray[i / 2];
            }
        }
        String toEncode = new String(nameForEncoding);

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

        char[] encodedName = decodedName.toCharArray();
        int encodedLength = encodedName.length;
        int originalLength = (encodedLength / 2);
        char[] originalName = new char[originalLength];
        for (int i = 0; i < encodedName.length; i++) {
            if (i % 2 == 1) {
                originalName[i - (i / 2) - 1] = encodedName[i];
            }
        }

        return new String(originalName);
    }
}
