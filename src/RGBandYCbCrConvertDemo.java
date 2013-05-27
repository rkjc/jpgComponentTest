
public class RGBandYCbCrConvertDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		double[][][] block = gen16xBlockRedBlack();		
		printArray(block);

		System.out.println("convert RGB to YCbCr");
		
		
		// Transform each pixel	from RGB to YCbCr		
		double[][][] arrayYCbCr = new double[16][16][3];
		for(int y = 0; y < 16; y++){
			for(int x = 0; x < 16; x++){	
				//ycbcr[][][3] = convert(rgb[][][3])
				arrayYCbCr[x][y] = matrixRGBtoYCbCR(block[x][y]);
			}
		}
		printArray(arrayYCbCr);
				
		// Transform each pixel	from RGB to YCbCr	
		System.out.println("convert YCbCr to RGB");
		double[][][] outPadImgArrayRGB = new double[16][16][3];
		
		for(int y = 0; y < 16; y++){
			for(int x = 0; x < 16; x++){
				//outPadImgArrayRGB[x][y] =  matrixYCbCRtoRGB(outYCbCrArray[x][y]);
				//debug bypass
				outPadImgArrayRGB[x][y] =  matrixYCbCRtoRGB(arrayYCbCr[x][y]);
			}
		}
		printArray(outPadImgArrayRGB);
	

	}
	
	public static double[] matrixRGBtoYCbCR(double[] rgb) {
		double Y = 0.0; double Cb = 0.0; double Cr = 0.0;
		double R = (double)rgb[0]; double G = (double)rgb[1]; double B = (double)rgb[2];
		
		Y = R*(0.2990) + G*(0.5870) + B*(0.1140);
		Cb = R*(-0.168736) + G*(-0.331264) + B*(0.5000);
		Cr = R*(0.5000) + G*(-0.418688) + B*(-0.081312);
		
		Y = Math.max(Math.min(Y, 255), 0);
		Cb = Math.max(Math.min(Cb, 127.5), -127.5);
		Cr = Math.max(Math.min(Cr, 127.5), -127.5);
		
		Y = Y - 128.0;
		Cb = Cb - 0.5;
		Cr = Cr - 0.5;
	
		return new double[] {Y, Cb, Cr};
	}
	
	public static double[] matrixYCbCRtoRGB(double[] ycbcr) {
		double R = 0.0; double G = 0.0; double B = 0.0;
		double Y = ycbcr[0]; double Cb = ycbcr[1]; double Cr = ycbcr[2];
		Y = Y + 128.0;
		Cb = Cb + 0.5;
		Cr = Cr + 0.5;
		
		R = Y*(1.0000) + Cb*(0.0) + Cr*(1.4020);
		G = Y*(1.0000) + Cb*(-0.34414) + Cr*(-0.71414);
		B = Y*(1.0000) + Cb*(1.7720) + Cr*(0.0);
		
		R = Math.round(R);
		G = Math.round(G);
		B = Math.round(B);
		
		R = Math.max(Math.min(R, 255), 0);
		G = Math.max(Math.min(G, 255), 0);
		B = Math.max(Math.min(B, 255), 0);

		return new double[] {R, G, B};
	}
	
	public static void printArray(double[] F){
		int X = F.length;
		for(int x = 0; x < X; x++){
			//System.out.format("%.1d", F[u][v]);
			System.out.print(F[x] + "  ");
			System.out.print("\t");
		}
		System.out.println("");
	}

	public static void printArray(double[][] F){
		int X = F.length;
		int Y = F[0].length;
		for(int y = 0; y < Y; y++){
			for(int x = 0; x < X; x++){
				System.out.format("%.2f\t\t", F[x][y]);
				//System.out.print(F[x][y] + "  ");
				//System.out.print("\t");
			}
			System.out.println("");
		}	
		System.out.println("");
	}
	
	public static void printArray(double[][][] F){
		int X = F.length;
		int Y = F[0].length;
			for(int m = 0; m < 3; m++){
			for(int y = 0; y < Y; y++){
				for(int x = 0; x < X; x++){
					System.out.format("%.2f\t\t", F[x][y][m]);
					//System.out.print(F[x][y][m] + "  ");
					//System.out.print("\t\t");
				}
				System.out.println("");
			}	
			System.out.println("");
		}
		System.out.println("");
	}
	
	public static double[][] getSampleBlock() {
		double[][] table = new double[8][8];
		
		table[0][0] = 49;
		table[1][0] = 61;
		table[2][0] = 69;
		table[3][0] = 61;
		table[4][0] = 78;
		table[5][0] = 89;
		table[6][0] = 100;
		table[7][0] = 112;
		
		table[0][1] = 68;
		table[1][1] = 60;
		table[2][1] = 51;
		table[3][1] = 42;
		table[4][1] = 62;
		table[5][1] = 69;
		table[6][1] = 80;
		table[7][1] = 89;
		
		table[0][2] = 90;
		table[1][2] = 81;
		table[2][2] = 58;
		table[3][2] = 49;
		table[4][2] = 69;
		table[5][2] = 72;
		table[6][2] = 68;
		table[7][2] = 69;
		
		table[0][3] = 100;
		table[1][3] = 91;
		table[2][3] = 79;
		table[3][3] = 72;
		table[4][3] = 69;
		table[5][3] = 68;
		table[6][3] = 59;
		table[7][3] = 58;
		
		table[0][4] = 111;
		table[1][4] = 100;
		table[2][4] = 101;
		table[3][4] = 91;
		table[4][4] = 82;
		table[5][4] = 71;
		table[6][4] = 59;
		table[7][4] = 49;
		
		table[0][5] = 131;
		table[1][5] = 119;
		table[2][5] = 120;
		table[3][5] = 102;
		table[4][5] = 90;
		table[5][5] = 90;
		table[6][5] = 81;
		table[7][5] = 59;
		
		table[0][6] = 148;
		table[1][6] = 140;
		table[2][6] = 129;
		table[3][6] = 99;
		table[4][6] = 92;
		table[5][6] = 78;
		table[6][6] = 59;
		table[7][6] = 39;
		
		table[0][7] = 151;
		table[1][7] = 140;
		table[2][7] = 142;
		table[3][7] = 119;
		table[4][7] = 98;
		table[5][7] = 90;
		table[6][7] = 72;
		table[7][7] = 39;  
		
		return table;
	}
	
	public static double[][][] gen16xBlockRedBlack() {
		double[][][] redblackImg = new double[16][16][3];
		for(int y = 0; y < 16; y++){
			for(int x = 0; x < 16; x += 4){
				redblackImg[x][y][0] = 255;
				redblackImg[x][y][1] = 0;
				redblackImg[x][y][2] = 0;
				
				redblackImg[x + 1][y][0] = 255;
				redblackImg[x + 1][y][1] = 0;
				redblackImg[x + 1][y][2] = 0;
	
				redblackImg[x + 2][y][0] = 0;
				redblackImg[x + 2][y][1] = 0;
				redblackImg[x + 2][y][2] = 0;
				
				redblackImg[x + 3][y][0] = 0;
				redblackImg[x + 3][y][1] = 0;
				redblackImg[x + 3][y][2] = 0;
			}
		}
		return redblackImg;
	}

}
