package CS4551_HW2;

/*******************************************************
CS4551 Multimedia Software Systems
@ Author: Elaine Kang
additions by George Kassar
This image class is for a 24bit RGB image only.
*******************************************************/

import java.io.*;
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
	img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	System.out.println("Created an empty image with size " + w + "x" + h);

	
	//Graphics2D img1 = img.createGraphics();
	//img1.setBackground(Color.WHITE);
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
 
 public void aliasing(Image img, int m, int n, int k){
	 int r = n - m;
	 int cx = img.getW()/2;
	 int cy = img.getH()/2;
	 byte[] black = {0, 0, 0};
	 
	 int numOfCir = cx /(m+n);
	 
	 //make background white
		for(int x = 0; x < img.getW(); x++){
			for(int y = 0; y < img.getH(); y++){
				 int[] rgb = new int[3];
				 rgb[0] = 255;
			   	 rgb[1] = 255;
				 rgb[2] = 255;
					
				 img.setPixel(x, y, rgb);
			}
		}
	 
	 for(int i = 0; i < numOfCir; i++){
		 
		 for(int j = 0; j < m; j++){
			 
			 for(double l = 0; l < 360; l = l+.01){
				 
				 int x = (int) (r * Math.cos(l) + cx);
				 int y = (int) (r * Math.sin(l) + cy);
				 
				img.setPixel(x, y, black);
			 }
			 
			 r++;
		 }
		 
		 r = r + n;
	 } 
		 
	img.display();
	img.write2PPM("out.ppm");
					
	System.out.println("");
	
	resize(img, k);
	filter1(img, k);
	filter2(img, k);
 }
 
 public void resize(Image img, int k){
	 Image img1 = new Image(512/k, 512/k);
	 
	 for(int x = 0; x < img1.getW(); x++){
		 for(int y = 0; y < img1.getH(); y++){
			 
			 int r = (int) img.getR(x * k, y * k);
			 int g = (int) img.getG(x * k, y * k);
			 int b = (int) img.getB(x * k, y * k);
				
				
			 int[] rgb = new int[3];
			 rgb[0] = r;
		   	 rgb[1] = g;
			 rgb[2] = b;
				
			 img1.setPixel(x, y, rgb);
		 }
	 }
	 
	 img1.display();
	 img1.write2PPM("out1.ppm");
						
	System.out.println("");
	
 }
 
 public void filter1(Image img, int k){
	Image img2 = new Image(512/k, 512/k);
	 
	double nine = 0.1111111;
	double six = 0.16666667;
	double four = 0.25;
	double totalC = 0;
	
	
	for(int x = 0; x < img.getW()-1; x++){
		 for(int y = 0; y < img.getH()-1; y++){
			 
			 if(x - 1 < 0 && y - 1 < 0){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x, y) * four;
				 totalC = totalC + (int) img.getR(x + 1, y) * four;
				 totalC = totalC + (int) img.getR(x, y + 1) * four;
				 totalC = totalC + (int) img.getR(x + 1, y + 1) * four;
				 
				 if (totalC < 175){
					 totalC = 0;
				 }
				 
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
				 
				 
			 }
			 else if(y - 1 < 0){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x - 1, y) * six;
				 totalC = totalC + (int) img.getR(x, y) * six;
				 totalC = totalC + (int) img.getR(x + 1, y) * six;
				 totalC = totalC + (int) img.getR(x - 1, y + 1) * six;
				 totalC = totalC + (int) img.getR(x, y + 1) * six;
				 totalC = totalC + (int) img.getR(x + 1, y + 1) * six;

				 
				 if(totalC > 255){
					 totalC = 255;
				 }
				 else if (totalC < 175){
					 totalC = 0;
				 }
					
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
				 
			 }
			 else if(x + 1 > img.getW() && y - 1 < 0){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x - 1, y) * four;
				 totalC = totalC + (int) img.getR(x, y) * four;
				 totalC = totalC + (int) img.getR(x - 1, y + 1) * four;
				 totalC = totalC + (int) img.getR(x, y + 1) * four;

				 
				 if (totalC < 175){
					 totalC = 0;
				 }
				 
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
				 
			 }
			 else if(x - 1 < 0 && y + 1 <= img.getH()){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x, y - 1) * six;
				 totalC = totalC + (int) img.getR(x + 1, y - 1) * six;
				 totalC = totalC + (int) img.getR(x, y) * six;
				 totalC = totalC + (int) img.getR(x + 1, y) * six;
				 totalC = totalC + (int) img.getR(x, y + 1) * six;
				 totalC = totalC + (int) img.getR(x + 1, y + 1) * six;
				
				 
				 if(totalC > 255){
					 totalC = 255;
				 }
				 else if (totalC < 175){
					 totalC = 0;
				 }
				 
					
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
	 
			 }
			 else if(x + 1 > img.getW()){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x - 1, y - 1) * six;
				 totalC = totalC + (int) img.getR(x, y - 1) * six;
				 totalC = totalC + (int) img.getR(x - 1, y) * six;
				 totalC = totalC + (int) img.getR(x, y) * six;
				 totalC = totalC + (int) img.getR(x - 1, y + 1) * six;
				 totalC = totalC + (int) img.getR(x, y + 1) * six;
				 
				 if(totalC > 255){
					 totalC = 255;
				 }
				 else if (totalC < 175){
					 totalC = 0;
				 }
					
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
	 
			 }
			 else if(x - 1 < 0 && y + 1 > img.getH()){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x, y - 1) * four;
				 totalC = totalC + (int) img.getR(x + 1, y - 1) * four;
				 totalC = totalC + (int) img.getR(x, y) * four;
				 totalC = totalC + (int) img.getR(x + 1, y) * four;
				 
				 if (totalC < 175){
					 totalC = 0;
				 }
					
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
	 
			 }
			 else if(y + 1 > img.getH()){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x - 1, y - 1) * six;
				 totalC = totalC + (int) img.getR(x, y - 1) * six;
				 totalC = totalC + (int) img.getR(x + 1, y - 1) * six;
				 totalC = totalC + (int) img.getR(x, y) * six;
				 totalC = totalC + (int) img.getR(x - 1, y) * six;
				 totalC = totalC + (int) img.getR(x + 1, y) * six;

				 
				 if(totalC > 255){
					 totalC = 255;
				 }
				 else if (totalC < 175){
					 totalC = 0;
				 }
				 
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
	 
			 }
			 else if(x + 1 > img.getW() && y + 1 > img.getH()){
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x - 1, y - 1) * four;
				 totalC = totalC + (int) img.getR(x, y - 1) * four;
				 totalC = totalC + (int) img.getR(x, y) * four;
				 totalC = totalC + (int) img.getR(x - 1, y) * four;
				 
				 if(totalC > 127){
					 totalC = 255;
				 }
				 else if (totalC < 175){
					 totalC = 0;
				 }
				 
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
	 
			 }
			 else{
				 totalC = 0;
				 
				 totalC = totalC + (int) img.getR(x - 1, y - 1) * nine;
				 totalC = totalC + (int) img.getR(x, y - 1) * nine;
				 totalC = totalC + (int) img.getR(x + 1, y - 1) * nine;
				 totalC = totalC + (int) img.getR(x - 1, y) * nine;
				 totalC = totalC + (int) img.getR(x, y) * nine;
				 totalC = totalC + (int) img.getR(x + 1, y) * nine;
				 totalC = totalC + (int) img.getR(x - 1, y + 1) * nine;
				 totalC = totalC + (int) img.getR(x, y + 1) * nine;
				 totalC = totalC + (int) img.getR(x + 1, y + 1) * nine;

				 
				 if(totalC > 255){
					 totalC = 255;
				 }
				 else if (totalC < 175){
					 totalC = 0;
				 }
				 
				 int[] rgb = new int[3];
				 rgb[0] = (int) totalC;
			   	 rgb[1] = (int) totalC;
				 rgb[2] = (int) totalC;
					
				 img2.setPixel(x/k, y/k, rgb);
	
			 }
		 }
	 }
	
	
	
	
	img2.display();
	img2.write2PPM("outF1.ppm");
						
	System.out.println("");
	 
 }
 
 public void filter2(Image img, int k){
	 Image img3 = new Image(512/k, 512/k);
	 
	 double F1 = 0.0625;
	 double F2 = 0.125;
	 double F3 = 0.0625;
	 double F4 = 0.125;
	 double C = .25;
	 double F5 = 0.125;
	 double F6 = 0.0625;
	 double F7 = 0.125;
	 double F8 = 0.0625;
	 
	 double totalC = 0;
		
		
		for(int x = 0; x < img.getW()-1; x++){
			 for(int y = 0; y < img.getH()-1; y++){
				 
				 if(x - 1 < 0 && y - 1 < 0){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x + 1, y) * F5;
					 totalC = totalC + (int) img.getR(x, y + 1) * F7;
					 totalC = totalC + (int) img.getR(x + 1, y + 1) * F8;
					 
					 if (totalC < 175){
						 totalC = 0;
					 }
					 
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
					 
					 
				 }
				 else if(y - 1 < 0){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x - 1, y) * F4;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x + 1, y) * F5;
					 totalC = totalC + (int) img.getR(x - 1, y + 1) * F6;
					 totalC = totalC + (int) img.getR(x, y + 1) * F7;
					 totalC = totalC + (int) img.getR(x + 1, y + 1) * F8;

					 
					 if(totalC > 255){
						 totalC = 255;
					 }
					 else if (totalC < 175){
						 totalC = 0;
					 }
						
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
					 
				 }
				 else if(x + 1 > img.getW() && y - 1 < 0){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x - 1, y) * F4;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x - 1, y + 1) * F6;
					 totalC = totalC + (int) img.getR(x, y + 1) * F7;

					 
					 if (totalC < 175){
						 totalC = 0;
					 }
					 
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
					 
				 }
				 else if(x - 1 < 0 && y + 1 <= img.getH()){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x, y - 1) * F2;
					 totalC = totalC + (int) img.getR(x + 1, y - 1) * F3;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x + 1, y) * F5;
					 totalC = totalC + (int) img.getR(x, y + 1) * F7;
					 totalC = totalC + (int) img.getR(x + 1, y + 1) * F8;
					
					 
					 if(totalC > 255){
						 totalC = 255;
					 }
					 else if (totalC < 175){
						 totalC = 0;
					 }
					 
						
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
		 
				 }
				 else if(x + 1 > img.getW()){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x - 1, y - 1) * F1;
					 totalC = totalC + (int) img.getR(x, y - 1) * F2;
					 totalC = totalC + (int) img.getR(x - 1, y) * F4;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x - 1, y + 1) * F6;
					 totalC = totalC + (int) img.getR(x, y + 1) * F7;
					 
					 if(totalC > 255){
						 totalC = 255;
					 }
					 else if (totalC < 175){
						 totalC = 0;
					 }
						
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
		 
				 }
				 else if(x - 1 < 0 && y + 1 > img.getH()){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x, y - 1) * F2;
					 totalC = totalC + (int) img.getR(x + 1, y - 1) * F3;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x + 1, y) * F5;
					 
					 if (totalC < 175){
						 totalC = 0;
					 }
						
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
		 
				 }
				 else if(y + 1 > img.getH()){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x - 1, y - 1) * F1;
					 totalC = totalC + (int) img.getR(x, y - 1) * F2;
					 totalC = totalC + (int) img.getR(x + 1, y - 1) * F3;
					 totalC = totalC + (int) img.getR(x - 1, y) * F4;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x + 1, y) * F5;

					 
					 if(totalC > 255){
						 totalC = 255;
					 }
					 else if (totalC < 175){
						 totalC = 0;
					 }
					 
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
		 
				 }
				 else if(x + 1 > img.getW() && y + 1 > img.getH()){
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x - 1, y - 1) * F1;
					 totalC = totalC + (int) img.getR(x, y - 1) * F2;
					 totalC = totalC + (int) img.getR(x - 1, y) * F4;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 
					 
					 if(totalC > 127){
						 totalC = 255;
					 }
					 else if (totalC < 175){
						 totalC = 0;
					 }
					 
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
		 
				 }
				 else{
					 totalC = 0;
					 
					 totalC = totalC + (int) img.getR(x - 1, y - 1) * F1;
					 totalC = totalC + (int) img.getR(x, y - 1) * F2;
					 totalC = totalC + (int) img.getR(x + 1, y - 1) * F3;
					 totalC = totalC + (int) img.getR(x - 1, y) * F4;
					 totalC = totalC + (int) img.getR(x, y) * C;
					 totalC = totalC + (int) img.getR(x + 1, y) * F5;
					 totalC = totalC + (int) img.getR(x - 1, y + 1) * F6;
					 totalC = totalC + (int) img.getR(x, y + 1) * F7;
					 totalC = totalC + (int) img.getR(x + 1, y + 1) * F8;

					 
					 if(totalC > 255){
						 totalC = 255;
					 }
					 else if (totalC < 175){
						 totalC = 0;
					 }
					 
					 int[] rgb = new int[3];
					 rgb[0] = (int) totalC;
				   	 rgb[1] = (int) totalC;
					 rgb[2] = (int) totalC;
						
					 img3.setPixel(x/k, y/k, rgb);
		
				 }
			 }
		 }
		
		
		
		
		img3.display();
		img3.write2PPM("outF2.ppm");
							
		System.out.println("");
		 
						
	System.out.println("");
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
