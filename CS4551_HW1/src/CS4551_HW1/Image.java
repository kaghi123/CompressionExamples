package CS4551_HW1;

/*******************************************************
CS4551 Multimedia Software Systems
@ Author: Elaine Kang

This image class is for a 24bit RGB image only.
*******************************************************/

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.stream.FileImageInputStream;

//A wrapper class of BufferedImage
//Provide a couple of utility functions such as reading from and writing to PPM file

public class Image
{
 private BufferedImage img;
 private String fileName;			// Input file name
 private int pixelDepth=3;			// pixel depth in byte

 public Image(int w, int h)
 // create an empty image with w(idth) and h(eight)
 {
	fileName = "";
	img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	System.out.println("Created an empty image with size " + w + "x" + h);
 }

 public Image(String fn)
 // Create an image and read the data from the file
 {
	  fileName = fn;
	  readPPM(fileName);
	  System.out.println("Created an image from " + fileName+ " with size "+getW()+"x"+getH());
 }

 public int getW()
 {
	return img.getWidth();
 }

 public int getH()
 {
	return img.getHeight();
 }

 public int getSize()
 // return the image size in byte
 {
	return getW()*getH()*pixelDepth;
 }

 public void setPixel(int x, int y, byte[] rgb)
 // set byte rgb values at (x,y)
 {
	int pix = 0xff000000 | ((rgb[0] & 0xff) << 16) | ((rgb[1] & 0xff) << 8) | (rgb[2] & 0xff);
	img.setRGB(x,y,pix);
 }

 public void setPixel(int x, int y, int[] irgb)
 // set int rgb values at (x,y)
 {
	byte[] rgb = new byte[3];

	for(int i=0;i<3;i++)
	  rgb[i] = (byte) irgb[i];

	setPixel(x,y,rgb);
 }

 public void getPixel(int x, int y, byte[] rgb)
 // retreive rgb values at (x,y) and store in the byte array
 {
 	int pix = img.getRGB(x,y);

 	rgb[2] = (byte) pix;
 	rgb[1] = (byte)(pix>>8);
 	rgb[0] = (byte)(pix>>16);
 }


 public void getPixel(int x, int y, int[] rgb)
 // retreive rgb values at (x,y) and store in the int array
 {
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

   // converts singed byte value (~128-127) to unsigned byte value (0~255)
	rgb[0]= (int) (0xFF & r);
	rgb[1]= (int) (0xFF & g);
	rgb[2]= (int) (0xFF & b);
 }
 
 public int getR(int x, int y)
 // retreive r values at (x,y) 
 {
	int pix = img.getRGB(x,y);

	byte red = (byte)(pix>>16);
	
	int r = (int) (0xFF & red);
	
	return r;

 }
 
 public int getG(int x, int y)
 // retreive r values at (x,y) 
 {
	int pix = img.getRGB(x,y);

	byte green = (byte)(pix>>8);
	
	int g = (int) (0xFF & green);
	
	return g;

 }
 
 public int getB(int x, int y)
 // retreive r values at (x,y) 
 {
	int pix = img.getRGB(x,y);

	byte blue = (byte) pix;
	
	int b = (int) (0xFF & blue);
	
	return b;

 }
 public void displayPixelValue(int x, int y)
 // Display rgb pixel in unsigned byte value (0~255)
 {
	int pix = img.getRGB(x,y);

	byte b = (byte) pix;
	byte g = (byte)(pix>>8);
	byte r = (byte)(pix>>16);

   System.out.println("RGB Pixel value at ("+x+","+y+"):"+(0xFF & r)+","+(0xFF & g)+","+(0xFF & b));
  }
 
 public void grayScale(Image img){
	 for(int x = 0; x < img.getW(); x++){
			for(int y = 0; y < img.getH(); y++){
				long gray = Math.round(0.299 * img.getR(x, y) + 0.587 * img.getG(x, y) + 0.114 * img.getB(x, y));
				
				int r = (int) gray;
				int g = (int) gray;
				int b = (int) gray;
				
				
				int[] rgb = new int[3];
				rgb[0] = r;
				rgb[1] = g;
				rgb[2] = b;
				
				img.setPixel(x, y, rgb);
			}
		}
		img.display();
		img.write2PPM("out.ppm");
		
		System.out.println("");
 }
 
 public void nLevel(Image img, String level){
	 if(level.equals("2")){
			for(int x = 0; x < img.getW(); x++){
				for(int y = 0; y < img.getH(); y++){
					long gray = Math.round((img.getR(x, y) + img.getG(x, y) + img.getB(x, y))/3);
					
					if(gray >= 128){
						gray = 255;
					}else{
						gray = 0;
					}
					
					int r = (int) gray;
					int g = (int) gray;
					int b = (int) gray;
					
					
					int[] rgb = new int[3];
					rgb[0] = r;
					rgb[1] = g;
					rgb[2] = b;
					
				
					img.setPixel(x, y, rgb);
				}
			}
			
			
		}else if(level.equals("4")){
			for(int x = 0; x < img.getW(); x++){
				for(int y = 0; y < img.getH(); y++){
					long gray = Math.round((img.getR(x, y) + img.getG(x, y) + img.getB(x, y))/3);
					
					if( 255 >= gray && gray > 189){
						gray = 222;
					}
					else if(189 >= gray && gray > 126){
						gray = 158;
					}
					else if(126 >= gray && gray > 63){
						gray = 95;
					}
					else{
					
						gray = 31;
					}
					
					int r = (int) gray;
					int g = (int) gray;
					int b = (int) gray;
					
					
					int[] rgb = new int[3];
					rgb[0] = r;
					rgb[1] = g;
					rgb[2] = b;
					
				
					img.setPixel(x, y, rgb);
				}
			}
			
			
		}else if(level.equals("8")){
			for(int x = 0; x < img.getW(); x++){
				for(int y = 0; y < img.getH(); y++){
					long gray = Math.round((img.getR(x, y) + img.getG(x, y) + img.getB(x, y))/3);
					
					if( 255 >= gray && gray > 224){
						gray = 240;
					}
					else if(224 >= gray && gray > 192){
						gray = 208;
					}
					else if(192 >= gray && gray > 160){
						gray = 176;
					}
					else if(160 >= gray && gray > 128){
						gray = 144;
					}
					else if(128 >= gray && gray > 96){
						gray = 112;
					}
					else if(96 >= gray && gray > 64){
						gray = 80;
					}
					else if(64 >= gray && gray > 32){
						gray = 48;
					}
					else{
					
						gray = 16;
					}
					
					int r = (int) gray;
					int g = (int) gray;
					int b = (int) gray;
					
					
					int[] rgb = new int[3];
					rgb[0] = r;
					rgb[1] = g;
					rgb[2] = b;
					
				
					img.setPixel(x, y, rgb);
				}
			}
			
			
		}else if(level.equals("16")){
			for(int x = 0; x < img.getW(); x++){
				for(int y = 0; y < img.getH(); y++){
					long gray = Math.round((img.getR(x, y) + img.getG(x, y) + img.getB(x, y))/3);
					
					if( 255 >= gray && gray > 240){
						gray = 248;
					}
					else if(240 >= gray && gray > 224){
						gray = 232;
					}
					else if(224 >= gray && gray > 208){
						gray = 216;
					}
					else if(208 >= gray && gray > 192){
						gray = 200;
					}
					else if(192 >= gray && gray > 176){
						gray = 184;
					}
					else if(176 >= gray && gray > 160){
						gray = 168;
					}
					else if(160 >= gray && gray > 144){
						gray = 152;
					}
					else if(144 >= gray && gray > 128){
						gray = 136;
					}
					else if(128 >= gray && gray > 112){
						gray = 120;
					}
					else if(112 >= gray && gray > 96){
						gray = 104;
					}
					else if(96 >= gray && gray > 80){
						gray = 88;
					}
					else if(80 >= gray && gray > 64){
						gray = 72;
					}
					else if(64 >= gray && gray > 48){
						gray = 56;
					}
					else if(48 >= gray && gray > 32){
						gray = 40;
					}
					else if(32 >= gray && gray > 16){
						gray = 24;
					}
					else{
					
						gray = 8;
					}
					
					int r = (int) gray;
					int g = (int) gray;
					int b = (int) gray;
					
					
					int[] rgb = new int[3];
					rgb[0] = r;
					rgb[1] = g;
					rgb[2] = b;
					
				
					img.setPixel(x, y, rgb);
				}
			}
			
			
		}
		
		
		
		img.display();
		img.write2PPM("out.ppm");
 }
 
 public void colorQ(Image img){
	 
	 //set up and display look up table
	 int index = 0;
	 int[] red = new int[]{16, 48, 80, 112, 144, 176, 208, 240};
	 int[] green = new int[]{16, 48, 80, 112, 144, 176, 208, 240};
	 int[] blue = new int[]{32, 96, 160, 224};
	 
	 //save data
	 int[][] lut = new int[256][3];
	 
	 System.out.println("Index |      R    G    B   ");
	 System.out.println("______|____________________");
	 
	 for(int r = 0; r < red.length; r++){
		 for(int g = 0; g < green.length; g++){
			 for(int b = 0; b < blue.length; b++){
				 System.out.println("" + index + " |      " + red[r] + "    " + green[g] + "    " + blue[b] + "   ");
				 lut[index][0] = red[r];
				 lut[index][1] = green[g];
				 lut[index][2] = blue[b];
				 
				 index++;
			 }
		 }
	 }
	 
	 
	 for(int x = 0; x < img.getW(); x++){
			for(int y = 0; y < img.getH(); y++){
				
				int r = img.getR(x, y);
				int g = img.getG(x, y);
				int b = img.getB(x, y);
				
				r = r/32;
				g = g/32;
				b = b/64;
				
				String binR = Integer.toBinaryString(r);
				String binG = Integer.toBinaryString(g);
				String binB = Integer.toBinaryString(b);
				
				if(binR.equals("0")){
					binR = "000";
				}
				
				if(binG.equals("0")){
					binG = "000";
				}
				
				if(binB.equals("0")){
					binB = "00";
				}
				
				String fullBin = binR.concat(binG.concat(binB));
				
				int lookUpInt = Integer.parseInt(fullBin, 2);
				
				int[] rgb = new int[3];
				rgb[0] = lut[lookUpInt][0];
				rgb[1] = lut[lookUpInt][1];
				rgb[2] = lut[lookUpInt][2];
				
				img.setPixel(x, y, rgb);
			}
		}
		img.display();
		img.write2PPM("out-QT8.ppm");

	 
 }

 public void readPPM(String fileName)
 // read a data from a PPM file
 {
	File fIn = null;
	FileImageInputStream fis = null;

	try{
		fIn = new File(fileName);
		fis = new FileImageInputStream(fIn);

		System.out.println("Reading "+fileName+"...");

		// read Identifier
		if(!fis.readLine().equals("P6"))
		{
			System.err.println("This is NOT P6 PPM. Wrong Format.");
			System.exit(0);
		}

		// read Comment line
		String commentString = fis.readLine();

		// read width & height
		String[] WidthHeight = fis.readLine().split(" ");
		int width = Integer.parseInt(WidthHeight[0]);
		int height = Integer.parseInt(WidthHeight[1]);

		// read maximum value
		int maxVal = Integer.parseInt(fis.readLine());

		if(maxVal != 255)
		{
			System.err.println("Max val is not 255");
			System.exit(0);
		}

		// read binary data byte by byte and save it into BufferedImage object
		int x,y;
		byte[] rgb = new byte[3];
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(y=0;y<getH();y++)
		{
	  		for(x=0;x<getW();x++)
			{
				rgb[0] = fis.readByte();
				rgb[1] = fis.readByte();
				rgb[2] = fis.readByte();
				setPixel(x, y, rgb);
			}
		}

      	fis.close();

		System.out.println("Read "+fileName+" Successfully.");

	} // try
	catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
 }

 public void write2PPM(String fileName)
 // wrrite the image data in img to a PPM file
 {
	FileOutputStream fos = null;
	PrintWriter dos = null;

	try{
		fos = new FileOutputStream(fileName);
		dos = new PrintWriter(fos);

		System.out.println("Writing the Image buffer into "+fileName+"...");

		// write header
		dos.print("P6"+"\n");
		dos.print("#CS451"+"\n");
		dos.print(getW() + " "+ getH() +"\n");
		dos.print(255+"\n");
		dos.flush();

		// write data
		int x, y;
		byte[] rgb = new byte[3];
		for(y=0;y<getH();y++)
		{
			for(x=0;x<getW();x++)
			{
				getPixel(x, y, rgb);
				fos.write(rgb[0]);
				fos.write(rgb[1]);
				fos.write(rgb[2]);

			}
			fos.flush();
		}
		dos.close();
		fos.close();

		System.out.println("Wrote into "+fileName+" Successfully.");

	} // try
	catch(Exception e)
	{
		System.err.println(e.getMessage());
	}
 }

 public void display()
 // display the image on the screen
 {
    // Use a label to display the image
     //String title = "Image Name - " + fileName;
     String title = fileName;
     JFrame frame = new JFrame(title);
     JLabel label = new JLabel(new ImageIcon(img));
     frame.add(label, BorderLayout.CENTER);
     frame.pack();
     frame.setVisible(true);
     frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 }

} // Image class

