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
  private ArrayList<Integer> colocChannelsIndices = new ArrayList<>();
  private ArrayList<ImagePlus> colocChannels = new ArrayList<>();

  @Override
  public void run() {
    Duplicator duplicator = new Duplicator();
    RGBStackMerge rgbStackMerge = new RGBStackMerge();

    numChannels = Integer.valueOf(numChannelsString);
    dapiChannel = Integer.valueOf(dapiChannelString);
    channelBools = new boolean[] {shouldColoc1, shouldColoc2, shouldColoc3, shouldColoc4};
    for (int channelIndex = 0; channelIndex < numChannels; channelIndex++) {
      int channel = channelIndex + 1;
      if (channel != dapiChannel && channelBools[channelIndex]) {
        colocChannelsIndices.add(channel);
      }
    }

    for (File file : inputDir.listFiles(new InputFileFilter())) {
      outputMessage += "Beginning colocalization of " + file.getName() + "\n";

      ImagePlus imagePlus = openImage(file);
      ImagePlus hyperStack = HyperStackConverter.toHyperStack(imagePlus, 4, 1, 1, null, "color");
      ImagePlus dapi = duplicator.run(hyperStack, dapiChannel, dapiChannel, 1, 1, 1, 1);

      // Extract each channel for colocalization
      colocChannels.clear();
      for (int channelIndex : colocChannelsIndices) {
        colocChannels.add(duplicator.run(hyperStack, channelIndex, channelIndex, 1, 1, 1, 1));
      }

      outputMessage += "Extracted " + (colocChannels.size() + 1) + " channels: ";
      outputMessage += "dapi(" + dapiChannel + ") and colocChannels " + colocChannelsIndices.toString() + "\n";

      for (int i = 0; i < colocChannels.size() - 1; i++) {
        for (int j = i+1; j < colocChannels.size(); j ++) {
          ImagePlus composite =
              rgbStackMerge.mergeHyperstacks(new ImagePlus[]{colocChannels.get(i), colocChannels.get(j), dapi}, true);
          composite.show("Composite channels " + i + " and " + j);

          outputMessage += "Created composite of channels dapi(" + dapiChannel + "), ";
          outputMessage += colocChannelsIndices.get(i) + " and " + colocChannelsIndices.get(j) + "\n";
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
