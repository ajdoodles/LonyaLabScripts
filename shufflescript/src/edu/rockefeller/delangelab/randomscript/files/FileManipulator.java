package edu.rockefeller.delangelab.randomscript.files;

import java.io.File;

/**
 * Created by aduda on 8/31/15.
 */
public abstract class FileManipulator {

    File inputDirectory, outputDirectory;

    FileManipulator(File inputDirectory, File outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    public void manipulate() {
        // Process each file
        File[] listOfInFiles = inputDirectory.listFiles();
        System.out.println("Beginning process on " + listOfInFiles.length + " files from " + inputDirectory.getAbsolutePath());
        for (File inFile : listOfInFiles) {
            String fileFullName = inFile.getName();
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
            File outFile = new File(outputDirectory.getAbsoluteFile() + "/" + newFileName);

            transferFile(inFile, outFile);
        }
    }

    /**
     * Generate the new name of the given file based on the direction in which we're going.
     * @param fileName filename to alter
     * @return final filename
     */
    public abstract String genName(String fileName);

    public abstract void transferFile(File inFile, File outFile);
}
