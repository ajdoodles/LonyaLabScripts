package edu.rockefeller.delangelab.randomscript.files;

import edu.rockefeller.delangelab.randomscript.constants.Constants;
import edu.rockefeller.delangelab.randomscript.utils.FileNameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aduda on 8/31/15.
 */
public class FileDeObfuscator extends FileManipulator{

    private static final Logger LOGGER = Logger.getLogger(FileDeObfuscator.class.getName());

    public FileDeObfuscator(File directory) throws IOException {
        super(directory, directory);
        File csvFile = new File(outputDirectory, Constants.CSV_FILE_NAME_REVERSE);
        this.bufferedWriter = new BufferedWriter(new FileWriter(csvFile));
        File inputCsvFile = new File(this.outputDirectory, Constants.CSV_FILE_NAME);
        this.bufferedReader = new BufferedReader(new FileReader(inputCsvFile));
    }

    @Override
    public void manipulate() {
        super.manipulate();
        csvWriterD();
    }

    @Override
    public String genName(String fileName) {
        return FileNameUtils.deobfuscate(fileName);
    }

    /**
     * Copies the input file to the location of the output file. Informs the user if the file already exists.
     * @param inFile location of the input file
     * @param outFile location of the output file
     */
    @Override
    public void transferFile(File inFile, File outFile) {
        try {
            Files.move(inFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to move " + outFile.getName() + " to " + outFile.getName(), e);
        }
    }

    private void csvWriterD() {
        try {
            String readline = bufferedReader.readLine();
            bufferedWriter.write(readline);
            bufferedWriter.newLine();

            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] linearray = line.split(",");
                String originalexcelname = FileNameUtils.deobfuscate(linearray[1]);
                linearray[0] = originalexcelname;
                bufferedWriter.write(String.join(",", linearray));
                bufferedWriter.newLine();
            }

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Cannot find " + Constants.CSV_FILE_NAME, e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read " + Constants.CSV_FILE_NAME, e);
        }

//        try {
//            Deleter();
//        } finally {
//
//        }
//
//        return bufferedWriter;
    }

   private void Deleter () {
        File todelete = new File(this.outputDirectory, Constants.CSV_FILE_NAME);
        todelete.delete();
        System.out.println(todelete);
    }

}
