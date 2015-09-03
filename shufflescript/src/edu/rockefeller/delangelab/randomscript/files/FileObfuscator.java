package edu.rockefeller.delangelab.randomscript.files;

import edu.rockefeller.delangelab.randomscript.constants.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Random;

/**
 * Created by aduda on 8/31/15.
 */
public class FileObfuscator extends FileManipulator {

    public FileObfuscator(File inputDirectory, File outputDirectory) throws IOException {
        super(inputDirectory, outputDirectory);
        this.csvFile = new File(this.outputDirectory, Constants.CSV_FILE_NAME);
        this.bufferedWriter = initBufferedWriter();
    }

    private BufferedWriter initBufferedWriter() throws IOException {
        BufferedWriter tmpWriter = new BufferedWriter(new FileWriter(csvFile));
        tmpWriter.write("Original Name,Obfuscated Name,");
        return tmpWriter;
    }

    @Override
    public void manipulate() {
        super.manipulate();
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String genName(String fileName) {
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
        String newName = Base64.getEncoder().encodeToString(encoded);
        maybeWriteNameToCsv(newName);
        return Base64.getEncoder().encodeToString(encoded);
    }

    /**
     * Copies the input file to the location of the output file. Informs the user if the file already exists.
     * @param inFile location of the input file
     * @param outFile location of the out   put file
     */
    @Override
    public void transferFile(File inFile, File outFile) {
        try {
            Files.copy(inFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            System.out.println("The File Already Exists");
        } catch (Exception e){
            System.out.println("Unknown error occured while copying " + inFile.getName() + " to " + outFile.getName());
            e.printStackTrace();
        }
    }

    public void maybeWriteNameToCsv(String newFileName) {
        try {
            bufferedWriter.newLine();
            bufferedWriter.write("," + newFileName);
            bufferedWriter.flush();
        } catch (IOException ex) {
            System.out.println("Error Filling .csv");
        }
    }
}
