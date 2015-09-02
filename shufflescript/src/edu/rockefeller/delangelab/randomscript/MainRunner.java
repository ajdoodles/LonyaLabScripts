package edu.rockefeller.delangelab.randomscript;

import edu.rockefeller.delangelab.randomscript.ui.MainWindow;

import javax.swing.*;

/**
 * This software is used to obfuscate files during procedures where human readable filenames might lead to human bias.
 * The user will be presented with a simple UI to determine if they want to obfsucate or de-obfuscate files, be asked
 * to choose input and output directories, and will be presented with a basic activity log and a generated CSV file.
 *
 * This is the runner for the file obfuscation logic. We instantiate a {@link MainWindow} on the UI thread. The UI
 * elements trigger the file obfuscation logic, the bulk of which lies in
 * {@link edu.rockefeller.delangelab.randomscript.files.FileManipulator} and its subclasses.
 *
 * Developed by ltimashev and aduda.
 */
public class MainRunner {

    public static void main(String[] args) {
        createAndShowGUI();
    }

    public static void createAndShowGUI(){
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new MainWindow();
            }
        });
    }
}
