package edu.rockefeller.delangelab.randomscript.logging;

import javax.swing.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by aduda on 9/3/15.
 */
public class UiHandler extends Handler{

    private final JTextArea logView;

    public UiHandler(JTextArea logView) {
        this.logView = logView;
    }

    @Override
    public void publish(LogRecord record) {
        System.out.println("HELLO MAYBE PLEASE");
        String message = getFormatter().format(record);
        logView.append(message);
    }

    @Override
    public void flush() {
        //no-op
    }

    @Override
    public void close() throws SecurityException {
        //no-op
    }
}
