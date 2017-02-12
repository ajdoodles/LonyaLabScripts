package edu.rockefeller.delangelab.focusfinder;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Plugin that provides the focus finder functionality.
 */
@Plugin(type = Command.class, menuPath = "Plugins>Focus Finder")
public class FocusFinder implements Command {

    @Parameter(label = "What is your name?")
    private String name = "J. Doe";

    @Parameter(type = ItemIO.OUTPUT)
    private String greeting;

    @Override
    public void run() {
        greeting = "Hello " + name + ".";
    }
}
