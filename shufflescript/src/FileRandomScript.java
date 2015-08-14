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
 * and the source files will remain unchanged as backups.
 */
public class FileRandomScript {

    //TODO: Programmatically set the source directory.
    public static String DIR_PREFIX = "D:\\Users\\Leonid\\Documents\\Science\\";
    public static String MOD_FOLDER = "\\out";
    public static String ORIGINAL_FOLDER = "\\in";

    public static void main(String[] args) {
        String firstArguement = args[0];
        if (!firstArguement.equals("r") && !firstArguement.equals("d")) {
            System.out.println("Please enter r or d");
            return;
        }

        if (firstArguement.equals("r")) {
            System.out.println("Randomizing");
        } else {
            System.out.println("Derandomizing");
        }

        String inDirSuffix = (firstArguement.equals("r")) ? ORIGINAL_FOLDER : MOD_FOLDER;
        File inDir = new File(DIR_PREFIX + inDirSuffix);
        if (!inDir.exists()){
            inDir.mkdir();
        }

        File outDir = new File(DIR_PREFIX + MOD_FOLDER);
        if (!outDir.exists()){
            outDir.mkdir();
        }



        File[] listOfInFiles = inDir.listFiles();
        for (File inFile : listOfInFiles) {
            String fileFullName = inFile.getName();

            String[] fileNameArray = fileFullName.split("\\.");


            String fileName = "";
            for (int i = 0; i < fileNameArray.length -1; i++){
                if (i != 0 ){
                    fileName +=".";
                }
                fileName += fileNameArray[i];

            }

            String fileExt =  fileNameArray[fileNameArray.length -1];

            String newFileName = genName(fileName, firstArguement) + "." + fileExt;
            File outFile = new File(outDir.getAbsoluteFile() + "\\" + newFileName);

            if (firstArguement.equals("r")) {
                copyFile(inFile, outFile);
            }else  {
                moveFile(inFile, outFile);
            }

        }

            }


    public static String genName (String fileName, String direction) {
        String editedName = null;
        if (direction.equals("r")) {
            byte[] encoded = fileName.getBytes(StandardCharsets.UTF_16);
            editedName = Base64.getEncoder().encodeToString(encoded);
            byte[] encodedagain = editedName.getBytes(StandardCharsets.UTF_16);
            return editedName;

        } else {
            byte[] decoded = Base64.getDecoder().decode(fileName);
            String decodedName = new String(decoded, StandardCharsets.UTF_16);
            return decodedName;
        }

    }


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

    public static void moveFile(File inFile, File outFile) {
        try {
            Files.move(inFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Input Output Exception occured while moving" + outFile.getName());
            e.printStackTrace();
        }


    }

}
