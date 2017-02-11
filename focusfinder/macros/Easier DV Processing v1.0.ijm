var	filenameIndex = 1;
macro "Make Composite and Overlay Images From Folder [F1]" {

{
rows = 1;
  columns = 4;
  rc = rows*columns;
  labels = newArray("Blue", "Green", "Red", "Far Red");
  defaults = newArray(rc);
  Dialog.create("Which Channels Do You Have?");
  Dialog.addCheckboxGroup(rows,columns,labels,defaults);
  Dialog.show();
       channels = newArray();
Blue = Dialog.getCheckbox();
Green = Dialog.getCheckbox();
Red = Dialog.getCheckbox();
FR = Dialog.getCheckbox();
}


dir1 = getDirectory("Choose Source Directory ");
dir2 = getDirectory("Choose Destination Directory ");

setBatchMode(true);
list = getFileList(dir1);
  for (n=0; n<list.length; n++) {
  showProgress(n+1, list.length);


run("Collect Garbage");


open(dir1+list[n]);


imagedir = File.directory;
imagename = File.name;





run("Make Composite", "display=Composite");

if (Blue == 1 && Green == 1 && Red == 1 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Yellow");
setSlice(4);
run("Red");

}

if (Blue == 1 && Green == 1 && Red == 1 && FR == 0) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Red");

}

if (Blue == 1 && Green == 0 && Red == 1 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Red");

}

if (Blue == 1 && Green == 1 && Red == 0 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Red");

}

if (Blue == 0 && Green == 1 && Red == 1 && FR == 1) {
setSlice(1);
run("Green");
setSlice(2);
run("Yellow");
setSlice(3);
run("Red");

}

if (Blue == 1 && Green == 1 && Red == 0 && FR == 0) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");

}

if (Blue == 1 && Green == 0 && Red == 1 && FR == 0) {
setSlice(1);
run("Blue");
setSlice(2);
run("Red");

}

if (Blue == 1 && Green == 0 && Red == 0 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Red");

}

if (Blue == 0 && Green == 1 && Red == 1 && FR == 0) {
setSlice(1);
run("Green");
setSlice(2);
run("Red");

}

if (Blue == 0 && Green == 1 && Red == 0 && FR == 1) {
setSlice(1);
run("Green");
setSlice(2);
run("Red");

}

if (Blue == 0 && Green == 0 && Red == 1 && FR == 1) {
setSlice(1);
run("Green");
setSlice(2);
run("Red");

}

saveAs("Tiff", dir2+list[n]);

close();
}

setBatchMode(false);
list = getFileList(dir2);
  for (m=0; m<list.length; m++) {
  showProgress(m+1, list.length);

open(dir2+list[m]);
  }



keep = getBoolean("Keep Images Open?");

if (keep == 0){
run("Close All");}
else {
run("Channels Tool...");
run("Color Balance...");
run("Cell Counter");
run("Tile");
}

}

var	filenameIndex = 1;
macro "Make Composite and Overlay Images [F2]" {

{
rows = 1;
  columns = 4;
  rc = rows*columns;
  labels = newArray("Blue", "Green", "Red", "Far Red");
  defaults = newArray(rc);
  Dialog.create("Which Channels Do You Have?");
  Dialog.addCheckboxGroup(rows,columns,labels,defaults);
  Dialog.show();
       channels = newArray();
Blue = Dialog.getCheckbox();
Green = Dialog.getCheckbox();
Red = Dialog.getCheckbox();
FR = Dialog.getCheckbox();
}

openimages = getList("image.titles");

numopen = nImages;
setBatchMode(false);
  for (p=0; p<openimages.length; p++) {
  showProgress(p+1, openimages.length);

selectWindow(openimages[p]);


run("Collect Garbage");











run("Make Composite", "display=Composite");

if (Blue == 1 && Green == 1 && Red == 1 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Yellow");
setSlice(4);
run("Red");

}

if (Blue == 1 && Green == 1 && Red == 1 && FR == 0) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Red");

}

if (Blue == 1 && Green == 0 && Red == 1 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Red");

}

if (Blue == 1 && Green == 1 && Red == 0 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");
setSlice(3);
run("Red");

}

if (Blue == 0 && Green == 1 && Red == 1 && FR == 1) {
setSlice(1);
run("Green");
setSlice(2);
run("Yellow");
setSlice(3);
run("Red");

}

if (Blue == 1 && Green == 1 && Red == 0 && FR == 0) {
setSlice(1);
run("Blue");
setSlice(2);
run("Green");

}

if (Blue == 1 && Green == 0 && Red == 1 && FR == 0) {
setSlice(1);
run("Blue");
setSlice(2);
run("Red");

}

if (Blue == 1 && Green == 0 && Red == 0 && FR == 1) {
setSlice(1);
run("Blue");
setSlice(2);
run("Red");

}

if (Blue == 0 && Green == 1 && Red == 1 && FR == 0) {
setSlice(1);
run("Green");
setSlice(2);
run("Red");

}

if (Blue == 0 && Green == 1 && Red == 0 && FR == 1) {
setSlice(1);
run("Green");
setSlice(2);
run("Red");

}

if (Blue == 0 && Green == 0 && Red == 1 && FR == 1) {
setSlice(1);
run("Green");
setSlice(2);
run("Red");

}



  }
run("Channels Tool...");
run("Color Balance...");
run("Cell Counter");
run("Tile");


}



macro "Auto Contrast Everybody [F3]" {

openimages = getList("image.titles");

numopen = nImages;


setBatchMode(false);
  for (p=0; p<openimages.length; p++) {
  showProgress(p+1, openimages.length);

selectWindow(openimages[p]);
getDimensions(width, height, channels, slices, frames);
setSlice(1);

n = channels;
for (q=0; q<n; q++){
run("Enhance Contrast", "saturated=0.35");
run("Next Slice [>]");

}
}
}


macro "Save Everybody [F4]" {
	dirsave = getDirectory("Choose a Directory to Save to"); 
ids=newArray(nImages); 
for (i=0;i<nImages;i++) { 
        selectImage(i+1); 
        title = getTitle; 
        ids[i]=getImageID; 

        saveAs("tiff", dirsave+title); 
} 
}
