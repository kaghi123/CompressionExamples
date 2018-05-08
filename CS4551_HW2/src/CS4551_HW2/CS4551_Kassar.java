package CS4551_HW2;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import CS4551_HW2.Image;
import CS4551_HW2.LZW;

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
   menu();
   
 }

 public static void usage()
 {
   System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
 }
 
 public static void menu() throws IOException{
 

	   
	   System.out.println("");
	   System.out.println("Main Menu-----------------------------------");
	   System.out.println("1. Aliasing");
	   System.out.println("2. Dictionary Coding");
	   System.out.println("3. Quit");
	   System.out.println("");
	   System.out.println("Please enter the task number [1-3]:");
	   
	   Scanner in = new Scanner(System.in);
	   String option = "";
	   
	   while(true){
		   option = in.nextLine();
		   switch(option){
		   		case "1":
		   			
		   		System.out.println("Please enter the M value(Thickness of line):");
		   		int m = Integer.parseInt(in.nextLine());
		   		
		   		System.out.println("Please enter the N value(Space between circles):");
		   		int n = Integer.parseInt(in.nextLine());
		   		
		   		System.out.println("Please enter the K value(Subsample size):");
		   		int k = Integer.parseInt(in.nextLine());
		   		
		   		Image img = new Image(512, 512);
		   		
		   		img.aliasing(img, m, n, k);
		   			
		   			menu();
		   			break;
		   			
		   		case "2":

		   			System.out.println("Enter a file name(LZW_test1.txt, LZW_test2.txt, LZW_test3.txt, LZW_test4.txt):");
		   			String file = in.nextLine();
		   			
		   			LZW lzw = new LZW(file);
		   			
		   			menu();
		 		   break;
		 		   
		   		case "3":
		   		 System.out.println("--Good Bye--");
		   		 System.exit(0);
			
		   }  
	   }   
 	}
}
