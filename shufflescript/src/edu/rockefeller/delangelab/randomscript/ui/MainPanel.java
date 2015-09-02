package edu.rockefeller.delangelab.randomscript.ui;

import edu.rockefeller.delangelab.randomscript.constants.Constants;
import edu.rockefeller.delangelab.randomscript.files.FileDeObfuscator;
import edu.rockefeller.delangelab.randomscript.files.FileObfuscator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Panel holding the file obfuscation UI elements.
 */
public class MainPanel extends JPanel {

    JPanel buttonContainer;
    JButton obfuscationButton, deObfuscationButton;
    JTextArea logView;
    JFileChooser fileChooser;

    public MainPanel() {
        super(new BorderLayout());

        initButtonContainer();
        JScrollPane log = initLogView();

        add(buttonContainer, BorderLayout.PAGE_START);
        add(log, BorderLayout.CENTER);
    }

    private void initButtonContainer() {
        obfuscationButton = new JButton(Constants.OBFUSCATE_BUTTON_TEXT);
        obfuscationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File inDir = getInputDirectory();
                File outDir = getOutputDirectory();
                new FileObfuscator(inDir, outDir).manipulate();
            }
        });

        deObfuscationButton = new JButton(Constants.DEOBFUSCATE_BUTTON_TEXT);
        deObfuscationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File inDir = getInputDirectory();
                new FileDeObfuscator(inDir).manipulate();
            }
        });

        buttonContainer = new JPanel();
        buttonContainer.add(obfuscationButton);
        buttonContainer.add(deObfuscationButton);
    }

    private JScrollPane initLogView() {
        logView = new JTextArea(5,20);
        logView.setMargin(new Insets(5, 5, 5, 5));
        logView.setEditable(false);
        return new JScrollPane(logView);
    }

    private void logLn(String message) {
        logView.append(message + "\n");
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
