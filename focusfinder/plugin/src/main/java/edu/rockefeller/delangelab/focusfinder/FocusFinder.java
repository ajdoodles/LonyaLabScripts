package edu.rockefeller.delangelab.focusfinder;

import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.Duplicator;
import ij.plugin.ImageCalculator;
import ij.plugin.RGBStackMerge;
import ij.plugin.filter.ImageMath;
import ij.plugin.filter.RankFilters;
import ij.process.ImageProcessor;
import ij.process.StackConverter;
import loci.formats.FormatException;
import loci.plugins.BF;
import net.imagej.ImageJ;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
  private int dapiChannelNum;
  private boolean[] channelBools;
  private ImagePlus dapiChannel;
  private Map<Integer, ImagePlus> channels;

  @Override
  public void run() {
    Duplicator duplicator = new Duplicator();
    RGBStackMerge rgbStackMerge = new RGBStackMerge();
    RankFilters filters = new RankFilters();
    ImageCalculator calculator = new ImageCalculator();

    numChannels = Integer.valueOf(numChannelsString);
    dapiChannelNum = Integer.valueOf(dapiChannelString);
    channelBools = new boolean[] {shouldColoc1, shouldColoc2, shouldColoc3, shouldColoc4};
    channels = new HashMap<>(numChannels - 1);

    for (File file : inputDir.listFiles(new InputFileFilter())) {
      outputMessage += "Beginning colocalization of " + file.getName() + "\n";

      ImagePlus original = openImage(file);

      new StackConverter(original).convertToGray32();

      // Extract all channels.
      dapiChannel = duplicator.run(original, dapiChannelNum, dapiChannelNum, 1, 1, 1, 1);
      for (int channelIndex = 0; channelIndex < numChannels; channelIndex++) {
        int channel = channelIndex + 1;
        if (channel != dapiChannelNum) {
          channels.put(channel, duplicator.run(original, channel, channel, 1, 1, 1, 1));
        }
      }

      outputMessage += "Extracted " + (channels.size() + 1) + " channels: ";
      outputMessage += "dapi(" + dapiChannelNum + ") and channels " + channels.keySet().toString() + "\n";

      // Subtract the result of a median filter from each non-dapi channel.
      for (Map.Entry<Integer, ImagePlus> channelEntry : channels.entrySet()) {
        ImagePlus median = duplicator.run(channelEntry.getValue());
        filters.rank(median.getProcessor(), 10, RankFilters.MEDIAN);
        calculator.run("subtract create", channelEntry.getValue(), median).show();
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

  private void showAllChannels() {
    dapiChannel.show("DAPI Channel (" + dapiChannelNum + ")");
    for (Map.Entry<Integer, ImagePlus> channelEntry : channels.entrySet()) {
      channelEntry.getValue().show("Channel: " + channelEntry.getKey().toString());
    }
  }

  public static final void main(String[] args) {
    final ImageJ ij = net.imagej.Main.launch(args);
    ij.command().run(FocusFinder.class, true);
  }
}
