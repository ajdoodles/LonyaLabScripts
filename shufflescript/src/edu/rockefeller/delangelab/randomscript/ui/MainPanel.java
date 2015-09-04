package edu.rockefeller.delangelab.randomscript.ui;

import edu.rockefeller.delangelab.randomscript.constants.Constants;
import edu.rockefeller.delangelab.randomscript.files.FileDeObfuscator;
import edu.rockefeller.delangelab.randomscript.files.FileObfuscator;
import edu.rockefeller.delangelab.randomscript.logging.UiHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Panel holding the file obfuscation UI elements.
 */
public class MainPanel extends JPanel {

    private final JPanel buttonContainer;
    private final JButton obfuscationButton;
    private final JButton deObfuscationButton;
    private final JTextArea logView;

    private JFileChooser fileChooser;

    public static final Logger LOGGER = Logger.getLogger(MainPanel.class.getName());

    public MainPanel() {
        super(new BorderLayout());

        obfuscationButton = initObfuscateButton();
        deObfuscationButton = initDeObfuscateButton();
        buttonContainer = initButtonContainer();
        logView = initLogView();

        add(buttonContainer, BorderLayout.PAGE_START);
        add(new JScrollPane(logView), BorderLayout.CENTER);

        initUiLogging();

        LOGGER.info("UI Initialized");
    }

    private JButton initObfuscateButton() {
        JButton tmpButton = new JButton(Constants.OBFUSCATE_BUTTON_TEXT);
        tmpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                File inDir = getInputDirectory();
                File outDir = getOutputDirectory();
                try {
                    new FileObfuscator(inDir, outDir).run();
                } catch (IOException exception) {
                    LOGGER.log(Level.SEVERE, "Obfuscation failed", exception);
                }
            }
        });
        return tmpButton;
    }

    private JButton initDeObfuscateButton() {
        JButton tmpButton = new JButton(Constants.DEOBFUSCATE_BUTTON_TEXT);
        tmpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                File inDir = getInputDirectory();
                try {
                    new FileDeObfuscator(inDir).run();
                } catch (IOException exception) {
                    LOGGER.log(Level.SEVERE, "Deobfuscation failed", exception);
                }
            }
        });
        return tmpButton;
    }

    private JPanel initButtonContainer() {
        JPanel tmpPanel = new JPanel();
        tmpPanel.add(obfuscationButton);
        tmpPanel.add(deObfuscationButton);
        return tmpPanel;
    }

    private JTextArea initLogView() {
        JTextArea tmpTextArea = new JTextArea(20,100);
        tmpTextArea.setMargin(new Insets(5, 5, 5, 5));
        tmpTextArea.setEditable(false);
        return tmpTextArea;
    }

    private void initUiLogging() {
        UiHandler handler = new UiHandler(logView);
        handler.setFormatter(new SimpleFormatter());
        Logger.getLogger("").addHandler(handler);
    }

    /**
     * Prompts the user to select the input directory.
     */
    private File getInputDirectory() {
        LOGGER.info("Requesting input directory from user.");
        return getDirectory("Select Input Folder");
    }

    /**
     * Prompts the user to select the output directory.
     */
    private File getOutputDirectory() {
        LOGGER.info("Requesting output directory from user.");
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
        LOGGER.info("User selected: " + tmpFile.getAbsolutePath());
        return tmpFile;
    }
}
