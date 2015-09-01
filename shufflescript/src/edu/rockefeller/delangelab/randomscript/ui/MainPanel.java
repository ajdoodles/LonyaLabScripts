package edu.rockefeller.delangelab.randomscript.ui;

import edu.rockefeller.delangelab.randomscript.files.FileDeObfuscator;
import edu.rockefeller.delangelab.randomscript.files.FileObfuscator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by aduda on 8/31/15.
 */
public class MainPanel extends JPanel {

    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fileChooser;

    public MainPanel() {
        super(new BorderLayout());

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        openButton = new JButton("Randomize");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File inDir = getInputDirectory();
                File outDir = getOutputDirectory();
                new FileObfuscator(inDir, outDir).manipulate();
            }
        });

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("De-Randomize");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File inDir = getInputDirectory();
                new FileDeObfuscator(inDir).manipulate();
            }
        });

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    private void logLn(String message) {
        log.append(message + "\n");
    }

    /**
     * Prompts the user to select the input directory.
     */
    private File getInputDirectory() {
        logLn("Requesting input directory from user.");
        return getDirectory("Select Input Folder");
    }

    /**
     * Prompts the user to select the output directory.
     */
    private File getOutputDirectory() {
       logLn("Requesting output directory from user.");
        return getDirectory("Select Output Folder");
    }

    private void ensureFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
    }

    /**
     * Using a {@link JFileChooser}, prompts the user for a directory selection using the given message.
     * @param message user prompt to guide the user's directory decision
     */
    private File getDirectory(String message) {
        ensureFileChooser();
        fileChooser.setDialogTitle(message);
        fileChooser.showSaveDialog(null);
        File tmpFile = fileChooser.getSelectedFile();
        logLn("User selected: " + tmpFile.getAbsolutePath());
        return tmpFile;
    }
}
