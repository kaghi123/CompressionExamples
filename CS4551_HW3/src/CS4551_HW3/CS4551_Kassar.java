package CS4551_HW3;

import java.io.IOException;
import java.util.Scanner;

import CS4551_HW3.Image;

/*******************************************************
CS4551 Multimedia Software Systems
@ Author: Elaine Kang
additions by George Kassar
*******************************************************/

//
//Template Code - demonstrate how to use Image class

public class CS4551_Kassar
{
	 
 public static void main(String[] args) throws IOException
 {
	// if there is no commandline argument, exit the program
   if(args.length != 1)
   {
     usage();
     System.exit(1);
   }

   System.out.println("--Welcome to Multimedia Software System--");
   String input = args[0];
   start(input);
   
 }

 public static void usage()
 {
   System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
 }
 
 public static void start(String input) throws IOException{
	 
	 int oriW;
	 int oriH;
	 float[][] Y;
	 float[][] Cb;
	 float[][] Cr;

	   Scanner in = new Scanner(System.in);
	   String option = "";
	 
	 Image img = new Image(input);
	 img.display();
	 oriW = img.getW();
	 oriH = img.getH();
	 
	 //compress
	 Compress c = new Compress();
	 
	 //read and resize
	 if(c.checkSize(img)){
		 
	 }else{
		 img = c.resize(img);
		 //img.display();
	 }
	 
	 //color space transformation
	 Y = c.colorSpaceTransY(img);
	 Cb = c.colorSpaceTransCb(img);
	 Cr = c.colorSpaceTransCr(img);
	 
	 //MPEG1 subsampling
	 Cb = c.subsampling(Cb, img.getW(), img.getH());
	 Cr = c.subsampling(Cr, img.getW(), img.getH());
 
	 //ask about what the user wants n to be
	 System.out.println("What would you like n to be? (0 to 5) : ");
	 option = in.nextLine();
	 in.close();
	 
	 //Divide into 8x8 and DCT and quantize
	 Y = c.dct(Y, img.getW(), img.getH(), false, Integer.parseInt(option));
	 Cb = c.dct(Cb, img.getW()/2, img.getH()/2, true, Integer.parseInt(option));
	 Cr = c.dct(Cr, img.getW()/2, img.getH()/2, true, Integer.parseInt(option));
	 
	 
	 
	 
	 //decompress
	 Decompress d = new Decompress();
	 
	 //Divide into 8x8 and IDCT
	 Y = d.idct(Y, img.getW(), img.getH(), false, Integer.parseInt(option));
	 Cb = d.idct(Cb, img.getW()/2, img.getH()/2, true, Integer.parseInt(option));
	 Cr = d.idct(Cr, img.getW()/2, img.getH()/2, true, Integer.parseInt(option));
	 
	 //Supersampling
	 Cb = d.supersampling(Cb, img.getW(), img.getH());
	 Cr = d.supersampling(Cr, img.getW(), img.getH());
	 
	 //Inverse color space transformation
	 img = d.iColorSpaceTrans(Y, Cb, Cr, img.getW(), img.getH());
	 
	 //remove padding
	 img = d.restore(img, oriW, oriH);
	 img.display();
	 

 	}
}
