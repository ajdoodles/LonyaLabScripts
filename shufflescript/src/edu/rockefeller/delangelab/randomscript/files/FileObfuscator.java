package edu.rockefeller.delangelab.randomscript.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/**
 * Created by aduda on 8/31/15.
 */
public class FileObfuscator extends FileManipulator {

    public FileObfuscator(File inputDirectory, File outputDirectory) {
        super(inputDirectory, outputDirectory);
    }

    @Override
    public String genName(String fileName) {
        byte[] encoded = fileName.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(encoded);
    }

    /**
     * Copies the input file to the location of the output file. Informs the user if the file already exists.
     * @param inFile location of the input file
     * @param outFile location of the output file
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
}
