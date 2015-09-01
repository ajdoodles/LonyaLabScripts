package edu.rockefeller.delangelab.randomscript.ui;

import javax.swing.*;

/**
 * Created by aduda on 8/31/15.
 */
public class MainWindow extends JFrame {

    static private final String newline = "\n";
    JPanel mainPanel = new MainPanel();

    public MainWindow() {
        super("File Obfuscation Script");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(mainPanel);

        pack();
        setVisible(true);
    }
}
