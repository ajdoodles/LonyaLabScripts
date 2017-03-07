package edu.rockefeller.delangelab.focusfinder;

import java.io.File;
import java.io.FileFilter;

/**
 * Used to filter the input files for the focus finder.
 */
public class InputFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return false;
        }
        String[] tokens = pathname.getName().split("\\.");
        String extension = tokens[tokens.length-1];
        return extension.equalsIgnoreCase("dv") || extension.equalsIgnoreCase("tif");
    }
}
