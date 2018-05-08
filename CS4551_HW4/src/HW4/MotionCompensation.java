package HW4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MotionCompensation {
	
	public int n = 0;
	public int p = 0;
	public float[][] target;
	public float[][] reference;
	public int px = 0;
	public int py = 0;
	public int MSD = 0;
	public int lowestMSD = 0;
	public boolean movingObj = false;
	public int num = 0;
	
	public void motionCompensation(int num, boolean isMovingObj, Image img1, Image img2){
		this.num = num;
		movingObj = isMovingObj;
		motionCompensation(img1,  img2);
		
	}

	@SuppressWarnings("unused")
	public void motionCompensation(Image img1, Image img2){
		Scanner in = new Scanner(System.in);
		
		
		System.out.println("Please enter the N value(Macro Block Size)(Must be 8, 16, or 24):");
   		n = Integer.parseInt(in.nextLine());
   		
   		System.out.println("Please enter the P value(Search Window)(Must be 4, 8 , 12, or 16):");
   		p = Integer.parseInt(in.nextLine());
   		
   		img1.grayScale(img1);
   		target = img1.grayImgToFloat(img1);
   		
   		img2.grayScale(img2);
   		reference = img2.grayImgToFloat(img2);
   		
   		int i = 0;
   		int j = 0;
   		int k = 0;
		int l = 0;
   		int orik = k;
   		int oril = l;
   		
		
   		float[][] temp = new float[n][n];
   		float[][] error = new float[img1.getW()][img1.getH()];
   		String motionV ="";
   		
   		for(; i < n && k < img1.getW(); i++, k++){
			for(; j < n && l < img1.getH() -1; j++, l++){
				temp[i][j] = target[k][l];
			}
			//when the temp is full then we find the best match
			if(i == n-1 && j == n){

				px = 0;
				py = 0;
				lowestMSD = 0;
				MSD = 0;
   				
				for(int x = 0; x < p; x++){
					for (int y = 0; y < p; y++){
						
						if(x == 0 && y == 0){
							//MSD = 0;
							MSD(x, y, orik, oril);
							if(MSD == 0){
								break;
							}
						}
						
						if((x > 0 || y > 0) && (orik + n + x < img1.getW() && oril + n + y < img1.getH())){
							//MSD = 0;
							MSD(x, y, orik, oril);
							if(MSD == 0){
								break;
							}
						}
						
						if((x > 0 || y > 0) && (orik - x >= 0 && oril + n + y < img1.getH())){
							//MSD = 0;
							MSD(x, y, orik, oril);
							if(MSD == 0){
								break;
							}
						}
						
						if((x > 0 || y > 0) && (orik + n + x < img1.getW() && oril - y >= 0)){
							//MSD = 0;
							MSD(x, y, orik, oril);
							if(MSD == 0){
								break;
							}
						}
					}
					
//					if(MSD == 0){
//						break;
//					}
				}
					//compute the motion vector
					int dx = orik - (orik + px);
					int dy = oril - (oril + py);
					
					if(movingObj){
						if(num == 1){
							//make the motion in red if for movingObjects
							if(!(dx == 0) || !(dy == 0)){
								for(int a = 0; a < n; a++){
									for(int b = 0; b < n; b++){
										target[orik + a][oril + b] = target[orik + a][oril + b] + 256;
									}
								}
							}
						}
						else{
							//replace moving blocks
							if(!(dx == 0) || !(dy == 0)){
								for(int a = 0; a < n; a++){
									for(int b = 0; b < n; b++){
										target[orik + a][oril + b] = target[orik + a][oril + b] + 256;
									}
								}
							}
						}
					}
					
					motionV = motionV + "[" + Integer.toString(dx) + ", " + Integer.toString(dy) + "]";
					
					//populate the error block
					for(int a = 0; a < n; a++){
						for(int b = 0; b < n; b++){
							error[orik + a][oril + b] = Math.abs(target[orik + a][oril + b] - reference[orik + dx + a][oril + dy + b]);
						}
					}
				//reset the temp arr
				i = 0;
				j = 0;
				
				if(l == img1.getH() - 1){
					l = 0;
					orik = k + 1;
				}
				else{
					k = k - n + 1;
					oril = l;
				}
			}
			else{
				//reset j and l so we can go down to the next line
				j = 0;
				l = l - n;
			}
		}
   		for(int x = 0; x < img1.getW() ; x++){
			for(int y = 0; y < img1.getH(); y++){
				if(error[x][y] > 255){
					error[x][y] = 255;
				}
				else if(error[x][y] < 0){
					error[x][y] = 0;
				}
			}
		}
   		
   		Image img = new Image(img1.getW(), img1.getH());
   		
   		if(movingObj){
   			if(num == 1){
   				img = img.floatToRed(img, target, img.getW(), img.getH());
   			}
   			else{
   				img = img.floatToCover(img, n ,target, img.getW(), img.getH());
   			}
   			
   		}
   		else{
   			img = img.floatToGray(img, error, img.getW(), img.getH());
   		}
   		
   		
   		try (PrintWriter out = new PrintWriter("Motion Vector.txt")) {
   		    out.println(motionV);
   		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		
   		
   		img.display();
	}
	
	public void MSD(int x, int y, int orik, int oril){
		//MSD
		for(int a = 0; a < n ; a++){
			for(int b = 0; b < n ; b++){
				MSD += Math.pow(target[orik + a][oril + b] - reference[orik + a][oril + b], 2);
			}
		}
		
		MSD = MSD/(n*n);
		
		if(MSD == 0){
			px = x;
			py = y;
		}
		else if(MSD < lowestMSD || lowestMSD == 0){
			lowestMSD = MSD;
			px = x;
			py = y;
		}
	}
}
