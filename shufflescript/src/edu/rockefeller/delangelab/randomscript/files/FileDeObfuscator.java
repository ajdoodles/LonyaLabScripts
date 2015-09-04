package edu.rockefeller.delangelab.randomscript.files;

import edu.rockefeller.delangelab.randomscript.constants.Constants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/**
 * Created by aduda on 8/31/15.
 */
public class FileDeObfuscator extends FileManipulator{

    public FileDeObfuscator(File directory) throws IOException {
        super(directory, directory);
        this.csvFile = new File(outputDirectory, Constants.CSV_FILE_NAME_REVERSE);
        this.bufferedWriter = new BufferedWriter(new FileWriter(csvFile));
    }

    @Override
    public void manipulate() {
        super.manipulate();
        csvWriterD();
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String genName(String fileName) {
        byte[] decoded = Base64.getDecoder().decode(fileName);
        String decodedName = new String(decoded, StandardCharsets.UTF_8);


        char[] encodedName = decodedName.toCharArray();
        int encodedLength = encodedName.length;
        int originalLength = (encodedLength / 2);
        char[] originalName = new char[originalLength];
        for (int i = 0; i < encodedName.length; i++) {
            if (i % 2 == 1) {
                originalName[i - (i / 2) - 1] = encodedName[i];
            }
        }

       return new String(originalName);
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

    public BufferedWriter csvWriterD() {
        try {
            FileReader excelreader = new FileReader(new File(this.outputDirectory, Constants.CSV_FILE_NAME));
            BufferedReader reader = new BufferedReader(excelreader);

            String readline = reader.readLine();
            String[] readlinearray = readline.split(",");
            for (int i = 0; i < readlinearray.length; i++) {
                bufferedWriter.write(readlinearray[i]);
                bufferedWriter.write(",");
            }
            bufferedWriter.newLine();

            String line;
            while((line = reader.readLine()) != null) {
                String[] linearray = line.split(",");
                String derandomizeforexcel = linearray[1];

                byte[] decoded = Base64.getDecoder().decode(derandomizeforexcel);
                String decodedName = new String(decoded, StandardCharsets.UTF_8);

                char[] EncodedName = decodedName.toCharArray();
                int EncodedLength = EncodedName.length;
                int OriginalLength = (EncodedLength / 2);
                char[] OriginalName = new char[OriginalLength];
                for (int i = 0; i < EncodedName.length; i++) {
                    if (i % 2 == 1) {
                        OriginalName[i - (i / 2) - 1] = EncodedName[i];
                    }
                }

                String originalexcelname = new String(OriginalName);

                bufferedWriter.write(originalexcelname);
                bufferedWriter.write(",");
                for (int i = 1; i < linearray.length; i++) {
                    bufferedWriter.write(linearray[i]);
                    bufferedWriter.write(",");

                }
                bufferedWriter.newLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find Randomization_List.csv");
        } catch (IOException e) {
            System.out.println("Can't read");
        }
        try {
            bufferedWriter.newLine();
            bufferedWriter.close();


        } catch (IOException ex) {
            System.out.println("Error Filling .csv");
        }

        try {
            Deleter();
        } finally {

        }

        return bufferedWriter;


    }

    public void Deleter () {
        File todelete = new File(this.outputDirectory, Constants.CSV_FILE_NAME);
        todelete.delete();
        System.out.println(todelete);

    }


}
