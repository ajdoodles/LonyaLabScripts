package edu.rockefeller.delangelab.randomscript.files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/**
 * Created by aduda on 8/31/15.
 */
public class FileDeObfuscator extends FileManipulator{

    public FileDeObfuscator(File directory) {
        super(directory, directory);
    }

    @Override
    public String genName(String fileName) {
        byte[] decoded = Base64.getDecoder().decode(fileName);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    /**
     * Copies the input file to the location of the output file. Informs the user if the file already exists.
     * @param inFile location of the input file
     * @param outFile location of the output file
     */
    public void transferFile(File inFile, File outFile) {
        try {
            Files.move(inFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("IOException occured while moving " + outFile.getName() + " to " + outFile.getName());
        } catch (Exception e) {
            System.out.println("Unknown error occured while moving " + inFile.getName() + " to " + outFile.getName());
            e.printStackTrace();
        }
    }
}
