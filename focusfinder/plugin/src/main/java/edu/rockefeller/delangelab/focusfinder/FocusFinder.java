package edu.rockefeller.delangelab.focusfinder;

import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.plugin.HyperStackConverter;
import ij.plugin.RGBStackMerge;
import loci.formats.FormatException;
import loci.plugins.BF;
import net.imagej.ImageJ;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Plugin that provides the focus finder functionality.
 */
@Plugin(type = Command.class, menuPath = "Plugins>Focus Finder")
public class FocusFinder implements Command {

  @Parameter(label = "Input Directory", style = "directory")
  private File inputDir;

  @Parameter(label = "Output Directory", style = "directory")
  private File outputDir;

  @Parameter(label = "How many channels?", choices = {"1", "2", "3", "4"}, style ="radioButtonHorizontal")
  private String numChannelsString = "3";

  @Parameter(label = "Which channel is DAPI?", choices = {"1", "2", "3", "4"}, style ="radioButtonHorizontal")
  private String dapiChannelString = "3";

  @Parameter(label = "Coloc 1?")
  private boolean shouldColoc1;

  @Parameter(label = "Coloc 2?")
  private boolean shouldColoc2;

  @Parameter(label = "Coloc 3?")
  private boolean shouldColoc3;

  @Parameter(label = "Coloc 4?")
  private boolean shouldColoc4;

  @Parameter(label = "Use autocontrast on first foci?")
  private boolean shouldAutoContrastFirstFoci;

  @Parameter(label = "Enter min value")
  private int firstFociAutoContrastMin = 0;

  @Parameter(label = "Enter max value")
  private int firstFociAutoContrastMax = 255;

  @Parameter(label = "Use autocontrast on second foci?")
  private boolean shouldAutoContrastSecondFoci;

  @Parameter(label = "Enter min value")
  private int secondFociAutoContrastMin = 0;

  @Parameter(label = "Enter max value")
  private int secondFociAutoContrastMax = 255;

  @Parameter(label = "Threshold value for first foci")
  private int firstFociThreshold = -180;

  @Parameter(label = "Threshold value for second foci")
  private int secondFociThreshold = -180;

  @Parameter(type = ItemIO.OUTPUT)
  private String outputMessage = "";

  private int numChannels;
  private int dapiChannel;
  private boolean[] channelBools;
  private ArrayList<Integer> colocChannels = new ArrayList<>(3);

  @Override
  public void run() {
    numChannels = Integer.valueOf(numChannelsString);
    dapiChannel = Integer.valueOf(dapiChannelString);
    channelBools = new boolean[] {shouldColoc1, shouldColoc2, shouldColoc3, shouldColoc4};
    for (int channel = 0; channel < channelBools.length; channel++) {
      if (channelBools[channel]) {
        colocChannels.add(channel + 1);
      }
    }

    Duplicator duplicator = new Duplicator();
    RGBStackMerge rgbStackMerge = new RGBStackMerge();

    for (File file : inputDir.listFiles(new InputFileFilter())) {
      ImagePlus imagePlus = openImage(file);
      ImagePlus hyperStack = HyperStackConverter.toHyperStack(imagePlus, 4, 1, 1, null, "color");
      ImagePlus dapi = duplicator.run(hyperStack, dapiChannel, dapiChannel, 1, 1, 1, 1);

      for (int firstChannel : colocChannels) {
        for (int secondChannel : colocChannels) {
          if (firstChannel != secondChannel) {
            ImagePlus firstFociChannel = duplicator.run(hyperStack, firstChannel, secondChannel, 1, 1, 1, 1);
            ImagePlus secondFociChannel = duplicator.run(hyperStack, firstChannel, secondChannel, 1, 1, 1, 1);
            ImagePlus composite =
                rgbStackMerge.mergeHyperstacks(
                    new ImagePlus[]{dapi, firstFociChannel, secondFociChannel},
                    false);
            composite.show("Composite channels " + firstChannel + " and " + secondChannel);
          }
        }
      }
    }
  }

  private ImagePlus openImage(File file) {
    ImagePlus image = null;
    if (file.getName().endsWith(".dv")) {
      try {
        image = BF.openImagePlus(file.getAbsolutePath())[0];
      } catch (FormatException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      image = new ImagePlus(file.getAbsolutePath());
    }
    return image;
  }

  public static final void main(String[] args) {
    final ImageJ ij = net.imagej.Main.launch(args);
    ij.command().run(FocusFinder.class, true);
  }
}
