package edu.rockefeller.delangelab.randomscript.ui;

import edu.rockefeller.delangelab.randomscript.constants.Constants;

import javax.swing.*;

/**
 * Frame that holds the file obfuscation UI. Nothing interesting here, this is just a system window.
 */
public class MainWindow extends JFrame {

    JPanel mainPanel = new MainPanel();

    public MainWindow() {
        super(Constants.MAIN_WINDOW_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(mainPanel);

        pack();
        setVisible(true);
    }
}
