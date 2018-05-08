package HW4;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import HW4.Image;

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
	   System.out.println("1. Block-Based Motion Compensation");
	   System.out.println("2. Removing Moving Objects");
	   System.out.println("3. Quit");
	   System.out.println("");
	   System.out.println("Please enter the task number [1-3]:");
	   
	   Scanner in = new Scanner(System.in);
	   String option = "";
	   
	   while(true){
		   option = in.nextLine();
		   switch(option){
		   		case "1":
		   			
		   			Image target = new Image("Walk_092.ppm");
			   		Image reference = new Image("Walk_090.ppm");
		   		
			   		MotionCompensation mc = new MotionCompensation();
			   		mc.motionCompensation(target, reference);
		   			
		   			menu();
		   			break;
		   			
		   		case "2":

		   			System.out.println("Please enter a number value between 19 and 179:");
			   		int num = Integer.parseInt(in.nextLine());
			   		
			   		System.out.println("Please enter a 1 if you would like to mark the blocks or a 2 if you would like to remove them:");
			   		int n = Integer.parseInt(in.nextLine());
			   		
			   		MovingObjects mo = new MovingObjects();
			   		mo.movingObjects(num, n);
		   			
		   			menu();
		 		   break;
		 		   
		   		case "3":
		   		 System.out.println("--Good Bye--");
		   		 System.exit(0);
			
		   }  
	   }   
 	}
}
