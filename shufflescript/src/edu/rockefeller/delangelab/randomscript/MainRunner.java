package edu.rockefeller.delangelab.randomscript;

import edu.rockefeller.delangelab.randomscript.ui.MainWindow;

import javax.swing.*;

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
