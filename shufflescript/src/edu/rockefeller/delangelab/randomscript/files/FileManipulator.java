package edu.rockefeller.delangelab.randomscript.files;

import edu.rockefeller.delangelab.randomscript.constants.Constants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public abstract class FileManipulator {

    final File inputDirectory, outputDirectory;
    File csvFile;
    BufferedWriter bufferedWriter;

    FileManipulator(File inputDirectory, File outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    /**
     *  This is where the actual file manipulation loop runs. It will go through each file the input directory, generate
     *  the new name for the file, and then transfer the file to the output directory under the new name.
     */
    public void manipulate() {
        File[] listOfInFiles = inputDirectory.listFiles();
        System.out.println("Beginning process on " + listOfInFiles.length + " files from " + inputDirectory.getAbsolutePath());
        for (File inFile : listOfInFiles) {
            String fileFullName = inFile.getName();
            if (fileFullName.equals("Randomization_List.csv")) {
                continue;
            }
            if (fileFullName.equals("Randomization_List_Final.csv")) {
                continue;
            }
            System.out.println("Processing " + fileFullName);



                // We only want to obfuscate the filename, the file extension needs to be extracted and added back in later
            String[] fileNameArray = fileFullName.split("\\.");
            String fileName = fileNameArray[0];
            for (int i = 1; i < fileNameArray.length - 1; i++){
                fileName += "." + fileNameArray[i];
            }

            // If the file name array only has one element, then there's no file extension
            String fileExt = "";
            if (fileNameArray.length > 1) {
                fileExt = "." + fileNameArray[fileNameArray.length - 1];
            }

            String newFileName = genName(fileName) + fileExt;
            File outFile = new File(outputDirectory, newFileName);

            transferFile(inFile, outFile);

        }
    }

    /**
     * Generate the new name of the given file based on the direction in which we're going.
     * @param fileName filename to alter
     * @return final filename
     */
    abstract String genName(String fileName);

    abstract void transferFile(File inFile, File outFile);
}
