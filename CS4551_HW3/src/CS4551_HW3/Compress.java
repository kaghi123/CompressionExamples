package CS4551_HW3;
import CS4551_HW3.Image;

public class Compress {
	
	public boolean checkSize(Image img){
		
		if(img.getW() % 8 == 0 && img.getH() % 8 == 0){
			 return true;
		 }else{
			 return false;
		 }
	}

	public Image resize(Image img){
		 int w;
		 int h;
		 int newW;
		 int newH;
		 
		 w = img.getW() % 8;
		 h = img.getH() % 8;
		 
		 newW = 8 - w;
		 newH = 8 - h;
		 
		 Image img1 = new Image(img.getW() + newW, img.getH() + newH);
		 
		 for(int x = 0; x < img.getW(); x++){
			 for(int y = 0; y < img.getH(); y++){
				 
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

	public float[][] colorSpaceTransY(Image img) {
		
		float[][] Y = new float[img.getW()][img.getH()];
		float all;
		
		for(int x = 0; x < img.getW(); x++){
			 for(int y = 0; y < img.getH(); y++){
				 
				 double r = img.getR(x, y);
				 double g = img.getG(x, y);
				 double b = img.getB(x, y);
					
				 r = r * 0.2990;
			   	 g = g * 0.5870;
				 b = b * 0.1140;
					
				 all = (float) ((r + g + b) - 128);
				 Y[x][y] = all;
			 }
		 }
		
		return Y;
	}
	
	public float[][] colorSpaceTransCb(Image img) {
		
		float[][] Cb = new float[img.getW()][img.getH()];
		float all;
		
		for(int x = 0; x < img.getW(); x++){
			 for(int y = 0; y < img.getH(); y++){
				 
				 double r = img.getR(x, y);
				 double g = img.getG(x, y);
				 double b = img.getB(x, y);
					
				 r = r * -0.1687;
			   	 g = g * -0.3313;
				 b = b * 0.5000;
					
				 all = (float) ((r + g + b) - 0.5);
				 Cb[x][y] = all;
			 }
		 }
		
		return Cb;
	}

	public float[][] colorSpaceTransCr(Image img) {
		
		float[][] Cr = new float[img.getW()][img.getH()];
		float all;
		
		for(int x = 0; x < img.getW(); x++){
			 for(int y = 0; y < img.getH(); y++){
				 
				 double r = img.getR(x, y);
				 double g = img.getG(x, y);
				 double b = img.getB(x, y);
					
				 r = r * 0.5000;
			   	 g = g * -0.4187;
				 b = b * -0.0813;
					
				 all = (float) ((r + g + b) - 0.5);
				 Cr[x][y] = all;
			 }
		 }
		
		return Cr;
	}

	public float[][] subsampling(float[][] c, int w, int h) {
		
//		float[][]cw = {{1,2,3,4,5,6},
//		{7,8,9,10,11,12},
//		{13,14,15,16,17, 18},
//		{19,20,21, 22,23, 24}};
		
		
		float[][] newC = new float[w/2][h/2];
		float i;
		float j;
		float k;
		float l;
		float all;
		
		for(int x = 0; x < w; x += 2){
			for(int y = 0; y < h; y += 2){
				
				i = c[x][y];
				j = c[x + 1][y];
				k = c[x][y + 1];
				l = c[x + 1][y + 1];
				
				all = (i + j + k + l)/4;
				newC[x/2][y/2] = all;
			}
		 }
		
		return newC;
	}

	public float[][] dct(float[][] arr, int w, int h, boolean isC, int n) {
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
										sum = (float) (sum + Math.cos((((2 * x) + 1) * u * Math.PI)/16) * Math.cos((((2 * y) + 1) * v * Math.PI)/16) * temp[x][y]);
									}
								}
								
								B[u][v] = (float) (((getC(u) * getC(v))/4) * sum);
							}
						}
						
						
						//take the 8x8 and quantize
						quantize(arr, isC, n);
						
						
						
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
	
	public float[][] quantize(float[][] arr, boolean isC, int n){
		
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
						arr[i][j] = arr[i][j] / A[i][j];
					}
				}
				
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						arr[i][j] = (float) (arr[i][j] * Math.pow(2, n));
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
						arr[i][j] = arr[i][j] / A[i][j];
					}
				}
				
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						arr[i][j] = (float) (arr[i][j] * Math.pow(2, n));
					}
				}
		}

		return arr;
	}
}