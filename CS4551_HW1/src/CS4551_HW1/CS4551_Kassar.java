package CS4551_HW1;

import java.util.Scanner;

import CS4551_HW1.Image;

/*******************************************************
CS4551 Multimedia Software Systems
@ Author: Elaine Kang
additions by George Kassar
*******************************************************/

//
//Template Code - demonstrate how to use Image class

public class CS4551_Kassar
{
	 
 public static void main(String[] args)
 {
	// if there is no commandline argument, exit the program
   if(args.length != 1)
   {
     usage();
     System.exit(1);
   }

   System.out.println("--Welcome to Multimedia Software System--");
   String input = args[0];
   menu(input);
   
 }

 public static void usage()
 {
   System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
 }
 
 public static void menu(String input){
 
	 Image img = new Image(input);
	 img.display();
	   
	   System.out.println("");
	   System.out.println("Main Menu-----------------------------------");
	   System.out.println("1. Conversion to Gray-scale Image (24bits->8bits)");
	   System.out.println("2. Conversion to N-level Image");
	   System.out.println("3. Conversion to 8bit Indexed Color Image using Uniform Color Quantization (24bits->8bits)");
	   //(EXTRA CREDIT) System.out.println("4. Conversion to 8bit Indexed Color Image using [your selected method](24bits->8bits)");
	   System.out.println("4. Quit");
	   System.out.println("");
	   System.out.println("Please enter the task number [1-4]:");
	   
	   Scanner in = new Scanner(System.in);
	   String option = "";
	   
	   
	   
	   
	   
	   
	  while(true){
		   option = in.nextLine();
		   switch(option){
		   		case "1":
		   			
		   			img.grayScale(img);
		   			
		   			menu(input);
		   			break;
		   			
		   		case "2":
		   			
		   			//task 1  process
		   			img.grayScale(img);
		   			
		   			
		   			//nlevel
		   			System.out.println("Please enter a number (2, 4, 8, 16) :");
		   			Scanner num = new Scanner(System.in);
		   			String op = in.nextLine();
		   			
		   			if(op.equals("2")||op.equals("4")||op.equals("8")||op.equals("16")){
		   				img.nLevel(img, op);
		   			}else{
		   				menu(input);
		   			}
		   			
		   			menu(input);
		 		   break;
		 		   
		   		case "3":
		   			
		   			System.out.println("Not implemented");
		   			img.colorQ(img);
		   			
		   			//img.display();
		   			//img.write2PPM("out.ppm");
		   			
		   			menu(input);
		 		   break;
		 		   
		   		case "4":
		   		 System.out.println("--Good Bye--");
		   		 System.exit(0);
			
		   }
		   
		   
	   }
	   

	  
	   
	   
 }
 
}
