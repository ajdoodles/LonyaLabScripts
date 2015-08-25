import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/**
 * Class containing the logic for the file obfuscation script
 *
 * This script is used to obfuscate files during procedures where human readable filenames might lead to human bias.
 * Running this script in the forward direction will create a temporary directory with copies of all files from a source
 * directory. The files in the new directory will have their names obfuscated. After the user interacts with the files
 * in whatever way they need to, running this script in the reverse direction will de-obfuscate the files. The
 * de-obfuscated files will remain in the new directory to preserve any transformative actions the user may have taken,
 * and the source files will remain unchanged as backups. The work of obfuscation is done in
 * {@link #genName(String, String)}
 */
public class FileRandomScript {

    //TODO: Programmatically set the source directory.
    //public static String DIR_PREFIX = "D:\\Users\\Leonid\\Documents\\Science\\";
//    public static String MOD_FOLDER = "\\out";
//    public static String ORIGINAL_FOLDER = "\\in";

    public static String DIR_PREFIX = "/home/aduda/Programming/LonyaLabScripts/testFiles";
    public static String MOD_FOLDER = "/out";
    public static String ORIGINAL_FOLDER = "/in";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide only one argument, r or d");
            return;
        }

        String firstArgument = args[0];

        // First decide what direction the user wants us to go
        if (!firstArgument.equals("r") && !firstArgument.equals("d")) {
            System.out.println("Please enter r or d");
            return;
        }

        // Print the direction in which we're going to the user.
        if (firstArgument.equals("r")) {
            System.out.println("Randomizing");
        } else {
            System.out.println("Derandomizing");
        }

        // Set up our input and output targets. If we are randomizing them the output folder will be a separate location
        // from the input folder. If we're derandomizing (de-obfuscating) then we're staying in the same folder.
        String inDirSuffix = (firstArgument.equals("r")) ? ORIGINAL_FOLDER : MOD_FOLDER;
        File inDir = new File(DIR_PREFIX + inDirSuffix);
        if (!inDir.exists()){
            inDir.mkdir();
        }

        File outDir = new File(DIR_PREFIX + MOD_FOLDER);
        if (!outDir.exists()){
            outDir.mkdir();
        }

        // Process each file
        File[] listOfInFiles = inDir.listFiles();
        System.out.println("Beginning process on " + listOfInFiles.length + " files from " + inDir.getAbsolutePath());
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

            String newFileName = genName(fileName, firstArgument) + fileExt;
            File outFile = new File(outDir.getAbsoluteFile() + "/" + newFileName);

            if (firstArgument.equals("r")) {
                copyFile(inFile, outFile);
            } else {
                moveFile(inFile, outFile);
            }
        }
    }

    /**
     * Generate the new name of the given file based on the direction in which we're going.
     * @param fileName filename to alter
     * @param direction "r" for randomize or "d" for derandomize
     * @return final filename
     */
    public static String genName (String fileName, String direction) {
        String editedName = null;
        if (direction.equals("r")) {
            byte[] encoded = fileName.getBytes(StandardCharsets.UTF_8);
            editedName = Base64.getEncoder().encodeToString(encoded);
            return editedName;
        } else {
            byte[] decoded = Base64.getDecoder().decode(fileName);
            String decodedName = new String(decoded, StandardCharsets.UTF_8);
            return decodedName;
        }
    }

    /**
     * Copies the input file to the location of the output file. Informs the user if the file already exists.
     * @param inFile location of the input file
     * @param outFile location of the output file
     */
    public static void copyFile(File inFile, File outFile) {
        try {
            Files.copy(inFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            System.out.println("The File Already Exists");
        } catch (Exception e){
            System.out.println("Unknown error");
            e.printStackTrace();
        }
    }

    /**
     * Copies the input file to the location of the output file. Informs the user if the file already exists.
     * @param inFile location of the input file
     * @param outFile location of the output file
     */
    public static void moveFile(File inFile, File outFile) {
        try {
            Files.move(inFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("IOException occured while moving " + outFile.getName());
            e.printStackTrace();
        }
    }

}
