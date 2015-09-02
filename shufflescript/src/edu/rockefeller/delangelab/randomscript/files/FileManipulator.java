package edu.rockefeller.delangelab.randomscript.files;

import java.io.File;

/**
 * Class containing the logic for the file obfuscation software
 *
 * This logic is used to obfuscate files during procedures where human readable filenames might lead to human bias.
 * Running this script in the forward direction will create a temporary directory with copies of all files from a source
 * directory. The files in the new directory will have their names obfuscated. After the user interacts with the files
 * in whatever way they need to, running this script in the reverse direction will de-obfuscate the files. The
 * de-obfuscated files will remain in the new directory to preserve any transformative actions the user may have taken,
 * and the source files will remain unchanged as backups. The work of obfuscation is done in
 * {@link #genName(String, String)}
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
