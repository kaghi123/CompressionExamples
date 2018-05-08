package CS4551_HW3;
import CS4551_HW3.Image;

public class Decompress {
	
	
public float[][] dequantize(float[][] arr, boolean isC, int n){
		
		if(!isC){
			float[][] A = new float[][]{
				{4, 4, 4, 8, 8, 16, 16, 32},
				{4, 4, 4, 8, 8, 16, 16, 32},
				{4, 4, 8, 8, 16, 16, 32, 32},
				{8, 8, 8, 16, 16, 32, 32, 32},
				{8, 8, 16, 16, 32, 32, 32, 32},
				{16, 16, 16, 32, 32, 32, 32, 32},
				{16, 16, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				};
			
				
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						arr[i][j] = arr[i][j] * A[i][j];
					}
				}
				
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						arr[i][j] = (float) (arr[i][j] / Math.pow(2, n));
					}
				}
			
		}
		else{
			float[][] A = new float[][]{
				{8, 8, 8, 16, 32, 32, 32, 32},
				{8, 8, 8, 16, 32, 32, 32, 32},
				{8, 8, 16, 32, 32, 32, 32, 32},
				{16, 16, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				};
				
				
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						arr[i][j] = arr[i][j] * A[i][j];
					}
				}
				
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						arr[i][j] = (float) (arr[i][j] / Math.pow(2, n));
					}
				}
		}

		return arr;
	}
	
	public float[][] idct(float[][] arr, int w, int h, boolean isC, int n) {
		//divide into 8x8 blocks do DCT change values then get the next block
				int i = 0;
				int j = 0;
				int k = 0;
				int l = 0;
				
				float[][] temp = new float[8][8];
				
				
						for(; i < 8 && k < w; i++, k++){
							for(; j < 8 && l < h; j++, l++){
								temp[i][j] = arr[k][l];
							}
							
							if(i == 7 && j == 8){
								
								float[][] B = new float[8][8]; 
								float sum = 0;
								
								for(int u = 0; u < 8; u++){
									for(int v = 0; v < 8; v++){
										sum = 0;
										for(int x = 0; x < 8; x++){
											for(int y = 0; y < 8; y++){
												sum = (float) (sum + ((getC(x) * getC(y))) * Math.cos((((2 * u) + 1) * x * Math.PI)/16) * Math.cos((((2 * v) + 1) * y * Math.PI)/16) * temp[x][y]);
											}
										}
										B[u][v] = (float) (sum / 4);
									}
								}

								
								//take the 8x8 and quantize
								dequantize(arr, isC, n);
								
								
								//get the DCT 8x8 and put it back into the original 2d array
								k = k - 7;
								l = l - 8;
								
								for(int a = 0; a < 8; a++, k++){
									for(int b = 0; b < 8; b++, l++){
										arr[k][l] = B[a][b];
									}
									if(k < w - 1 && l < h - 1){
										l = l - 8;
									}
									
									else if(l == h){
										k = w - 1;
										break;
									}
									else {
										k = 0;
									}
									
								}
								if(k == 1){
									k = 0;
								}
								i = 0;
								j = 0;
							}
							else{
								j = 0;
								l = l - 8;
							}
						}
				
				return arr;
	}
	
	public static double getC(int num){
		if(num == 0){
			return Math.sqrt(2)/2;
		}
		else{
			return 1;
		}
	}	
	
	public float[][] supersampling(float[][] c, int w, int h) {
		
		float[][] oriC = new float[w][h];
		float all;
		
		for(int x = 0; x < w; x++){
			for(int y = 0; y < h; y++){
				
				all = c[x/2][y/2];
				
				oriC[x][y] = all;
			}
		 }
		
		return oriC;
	}
	
	public Image iColorSpaceTrans(float[][] Y, float[][] Cb, float[][] Cr, int w, int h) {
		
		Image img = new Image(w, h);
		
		 for(int x = 0; x < w; x++){
			 for(int y = 0; y < h; y++){
				 
				 int r = (int) ((1 * (Y[x][y] + 128)) + (0 * (Cb[x][y] + 0.5)) + (1.4020 * (Cr[x][y] + 0.5)));
				 int g = (int) ((1 * (Y[x][y] + 128)) + (-0.3441 * (Cb[x][y] + 0.5)) + (-0.7141 * (Cr[x][y] + 0.5)));
				 int b = (int) ((1 * (Y[x][y] + 128)) + (1.7720 * (Cb[x][y] + 0.5)) + (0 * (Cr[x][y] + 0.5)));
					
					
				 int[] rgb = new int[3];
				 rgb[0] = r;
			   	 rgb[1] = g;
				 rgb[2] = b;
					
				 img.setPixel(x, y, rgb);
			 }
		 }

		return img;
	}
	
	public Image restore(Image img, int w, int h){
		 
		 Image img1 = new Image(w, h);
		 
		 for(int x = 0; x < w; x++){
			 for(int y = 0; y < h; y++){
				 
				 int r = (int) img.getR(x, y);
				 int g = (int) img.getG(x, y);
				 int b = (int) img.getB(x, y);
					
					
				 int[] rgb = new int[3];
				 rgb[0] = r;
			   	 rgb[1] = g;
				 rgb[2] = b;
					
				 img1.setPixel(x, y, rgb);
			 }
		 }
		 
		 return img1;
	}

}
