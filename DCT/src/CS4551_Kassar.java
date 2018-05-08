import java.text.DecimalFormat;

public class CS4551_Kassar {

	public static void main(String[] args) {
		
		//create double 2D array
		double[][] A = new double[][]{
			{139, 144, 149, 153, 155, 155, 155, 155},
			{144, 151, 153, 156, 159, 156, 156, 156},
			{150, 155, 160, 163, 158, 156, 156, 156},
			{159, 161, 162, 160, 160, 159, 159, 159},
			{159, 160, 161, 162, 162, 155, 155, 155},
			{161, 161, 161, 161, 160, 157, 157, 157},
			{162, 162, 161, 163, 162, 157, 157, 157},
			{162, 162, 161, 161, 163, 158, 158, 158},
			};
		
		//display array
		for(int i = 0; i < 8; i++){
			System.out.println("");
			for(int j = 0; j < 8; j++){
				System.out.println(A[i][j]);
			}
		}
		
		DCT(A);
	}
	
	public static void DCT(double[][] A){
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("DCT");
		
		double[][] B = new double[8][8]; 
		double sum = 0;
		
		for(int u = 0; u < 8; u++){
			for(int v = 0; v < 8; v++){
				sum = 0;
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						sum = sum + Math.cos((((2 * i) + 1) * u * Math.PI)/16) * Math.cos((((2 * j) + 1) * v * Math.PI)/16) * A[i][j];
					}
				}
				
				B[u][v] = ((getC(u) * getC(v))/4) * sum;
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.###");
		
		
		for(int i = 0; i < 8; i++){
			System.out.println("");
			for(int j = 0; j < 8; j++){
				System.out.println(df.format(B[i][j]));
			}
		}
		
		IDCT(B);
		
	}
	
	public static void IDCT(double[][] B){
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("IDCT");
		
		double[][] C = new double[8][8]; 
		double sum = 0;
		
		for(int u = 0; u < 8; u++){
			for(int v = 0; v < 8; v++){
				sum = 0;
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						sum = sum + ((getC(i) * getC(j))) * Math.cos((((2 * u) + 1) * i * Math.PI)/16) * Math.cos((((2 * v) + 1) * j * Math.PI)/16) * B[i][j];
					}
				}
				C[u][v] = sum / 4;
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.###");
		
		
		for(int i = 0; i < 8; i++){
			System.out.println("");
			for(int j = 0; j < 8; j++){
				System.out.println(df.format(C[i][j]));
			}
		}	
	}
	
	public static double getC(double num){
		if(num == 0){
			return Math.sqrt(2)/2;
		}
		else{
			return 1;
		}
	}
	

}


















