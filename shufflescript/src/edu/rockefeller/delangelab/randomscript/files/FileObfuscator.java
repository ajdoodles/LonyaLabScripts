package edu.rockefeller.delangelab.randomscript.files;

import edu.rockefeller.delangelab.randomscript.constants.Constants;
import edu.rockefeller.delangelab.randomscript.utils.FileNameUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aduda on 8/31/15.
 */
public class FileObfuscator extends FileManipulator {

    private static final Logger LOGGER = Logger.getLogger(FileObfuscator.class.getName());

    public FileObfuscator(File inputDirectory, File outputDirectory) throws IOException {
        super(inputDirectory, outputDirectory);
        File csvFile = new File(this.outputDirectory, Constants.CSV_FILE_NAME);
        this.bufferedWriter = initBufferedWriter(csvFile);
    }

    private BufferedWriter initBufferedWriter(File file) throws IOException {
        BufferedWriter tmpWriter = new BufferedWriter(new FileWriter(file));
        tmpWriter.write(Constants.CSV_TITLE_LINE);
        return tmpWriter;
    }

    @Override
    public String genName(String fileName) {
        String tmpName = FileNameUtils.obfuscate(fileName);
        maybeWriteNameToCsv(tmpName);
        return tmpName;
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
            LOGGER.log(Level.WARNING, outFile.getName() + " already exists.", e);
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Failed to copy " + inFile.getName() + " to " + outFile.getName(), e);
        }
    }

    private void maybeWriteNameToCsv(String newFileName) {
        try {
            bufferedWriter.newLine();
            bufferedWriter.write("," + newFileName);
            bufferedWriter.flush();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to write csv data for " + newFileName, ex);
        }
    }
}
