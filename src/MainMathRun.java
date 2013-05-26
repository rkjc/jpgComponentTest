

public class MainMathRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//int[][] zigzag = new  int[64][2]; //L = 0; R = 1
		
		int[][] zigzag = generateZigzagPattern();
		
		for(int i = 0; i < 64; i++){
			System.out.println("zigzag " + i + " = [" + zigzag[i][0] + "][" + zigzag[i][1] + "]");
		}
//		zigzag[0][0] = 0;
//		zigzag[0][1] = 0;
//		zigzag[1][0] = 1;
//		zigzag[1][1] = 0;
//		
//		int L = 1;
//		int R = 0;
//		int index = 0;	
//		System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//		index++;
//		System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//		
//		while(true){
//			while(L != 0){
//				index++;
//				L--;
//				R++;
//				zigzag[index][0] = L;
//				zigzag[index][1] = R;
//				System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//			}
//			if(L == 0 && R == 7)
//				break;
//			index++;
//			R++;
//			zigzag[index][0] = L;
//			zigzag[index][1] = R;
//			System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//			while(R != 0 ){
//				index++;
//				L++;
//				R--;
//				zigzag[index][0] = L;
//				zigzag[index][1] = R;
//				System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//			}
//			index++;
//			L++;
//			zigzag[index][0] = L;
//			zigzag[index][1] = R;
//			System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//		}
//		
//		index++;
//		L++;
//		zigzag[index][0] = L;
//		zigzag[index][1] = R;
//		System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//		
//		while(true){
//			while(L != 7){
//				index++;
//				L++;
//				R--;
//				zigzag[index][0] = L;
//				zigzag[index][1] = R;
//				System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//			}
//			index++;
//			R++;
//			zigzag[index][0] = L;
//			zigzag[index][1] = R;
//			System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//			
//			while(R != 7){
//				index++;
//				L--;
//				R++;
//				zigzag[index][0] = L;
//				zigzag[index][1] = R;
//				System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//			}
//			index++;
//			L++;
//			zigzag[index][0] = L;
//			zigzag[index][1] = R;
//			System.out.println("zigzag " + index + " = [" + zigzag[index][0] + "][" + zigzag[index][1] + "]");
//			
//			if(L == 7 && R == 7)
//				break;
//		}
		
		

		
//		double[][] dctOut1 = new double[8][8];
//		dctOut1 = dct3(input1);
//		printArray(dctOut1, 8);
//		
//		double[][] back1 = new double[8][8];
//		back1 = inverseDCTconvertion(dctOut1);
//		printArray(back1, 8);
//		
//		
//		
//		double[][] input2 = new double[8][8];
//		loadInputSample(input2);
//		
//		printPartArray(input2, 2, 2);
//
//		double[][] dctOut2 = new double[8][8];
//		dctOut2 = runDCTtransform(input2);
//		printArray(dctOut2, 8);
//		
//		printPartArray(dctOut2, 2, 2);
//		
//		double[][] back2 = new double[8][8];
//		back2 = inverseDCTconvertion(dctOut2);
//		printArray(back2, 8);
//		
//		printPartArray(back2, 2, 2);
//		
//		int[] rgb = new int[3];
//		double[] YCbCr = new double[3];
//		
//		rgb[0] = 255;
//		rgb[1] = 251;
//		rgb[2] = 238;
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		YCbCr = matrixRGBtoYCbCR(rgb);
//		
//		System.out.println("\nY = " + YCbCr[0]);
//		System.out.println("Cb = " + YCbCr[1]);
//		System.out.println("Cr = " + YCbCr[2]);
//		
//		rgb = matrixYCbCRtoRGB(YCbCr);
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		System.out.println("\n================");
//		
//		rgb[0] = 0;
//		rgb[1] = 0;
//		rgb[2] = 0;
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		YCbCr = matrixRGBtoYCbCR(rgb);
//		
//		System.out.println("\nY = " + YCbCr[0]);
//		System.out.println("Cb = " + YCbCr[1]);
//		System.out.println("Cr = " + YCbCr[2]);
//		
//		rgb = matrixYCbCRtoRGB(YCbCr);
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		System.out.println("\n================");
//		
//		
//		rgb[0] = 128;
//		rgb[1] = 128;
//		rgb[2] = 128;
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		YCbCr = matrixRGBtoYCbCR(rgb);
//		
//		System.out.println("\nY = " + YCbCr[0]);
//		System.out.println("Cb = " + YCbCr[1]);
//		System.out.println("Cr = " + YCbCr[2]);
//		
//		rgb = matrixYCbCRtoRGB(YCbCr);
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		System.out.println("\n================");
//		
//		rgb[0] = 0;
//		rgb[1] = 128;
//		rgb[2] = 255;
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		YCbCr = matrixRGBtoYCbCR(rgb);
//		
//		System.out.println("\nY = " + YCbCr[0]);
//		System.out.println("Cb = " + YCbCr[1]);
//		System.out.println("Cr = " + YCbCr[2]);
//		
//		rgb = matrixYCbCRtoRGB(YCbCr);
//		
//		System.out.println("\nR = " + rgb[0]);
//		System.out.println("G = " + rgb[1]);
//		System.out.println("B = " + rgb[2]);
//		
//		System.out.println("\n================");
//		
//		int testPx[] = new int[3];
//		
//		int[] thing1 = {255, 255, 255};
//		System.out.println("\nthing1[0] = " + thing1[0] + "  thing1[1] = " + thing1[1] + "  thing1[2] = " + thing1[2] + "\n");
//		
//		double[] thing2 = matrixRGBtoYCbCR(thing1);
//		System.out.println("\nthing2[0] = " + thing2[0] + "  thing2[1] = " + thing2[1] + "  thing2[2] = " + thing2[2] + "\n");
//		
//		int[] thing3 =  matrixYCbCRtoRGB(thing2);
//		System.out.println("\nRthing[0] = " + thing3[0] + "  thing3[1] = " + thing3[1] + "  thing3[2] = " + thing3[2] + "\n");

	}
	
	
	//public static int[] matrixRGBtoYCbCRwiki(int[] rgb) {
	public static double[] matrixRGBtoYCbCR(int[] rgb) {
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
	
	public static int[] matrixYCbCRtoRGB(double[] ycbcr) {
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
		
		int r = (int)Math.max(Math.min(R, 255), 0);
		int g = (int)Math.max(Math.min(G, 255), 0);
		int b = (int)Math.max(Math.min(B, 255), 0);

		return new int[] {r, g, b};
	}
	
	
	
	public static double[][] runDCTtransform(double[][] f) {	
		double[][] F = new double[8][8];
		double[][] temp = new double[8][8];
		for(int u = 0; u < 8; u++){
			for(int v = 0; v < 8; v++){
				
				double subSum = 0.0;
				for(int x = 0; x < 8; x++){
					for(int y = 0; y < 8; y++){
						temp[x][y] = f[x][y];// - 128;
						subSum += Math.cos(((2.0*x + 1.0)*u*Math.PI)/(16.0)) * Math.cos(((2.0*y + 1.0)*v*Math.PI)/(16.0)) * temp[x][y];
					}
				}
				F[u][v] = ((Cfunc(u) * Cfunc(v)) / 4.0) * subSum;
			}
		}	
		return F;
	}
		
	public static double Cfunc(double x) {
		if(x == 0)
			return Math.sqrt(2.0) / 2.0;
		else
			return 1.0;
	}
	
	public static double[][] inverseDCTconvertion(double[][] Fp) {	
		double[][] fp = new double[8][8];
		double[][] temp = new double[8][8];
		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 8; y++){
				
				double subSum = 0.0;
				for(int u = 0; u < 8; u++){
					for(int v = 0; v < 8; v++){
						//temp[x][y] = f[x][y];// - 128;
						subSum += Cfunc(u) * Cfunc(v) * Fp[u][v] * Math.cos(((2.0*x + 1.0)*u*Math.PI)/(16.0)) * Math.cos(((2.0*y + 1.0)*v*Math.PI)/(16.0));
					}
				}
				//fp[x][y] = ((1.0 / 4.0) * subSum) + 128;
				fp[x][y] = ((1.0 / 4.0) * subSum);
			}
		}	
		return fp;
	}
	
	
	
	public static double[][] dct3(double[][] input ) {
		double  in[][] = new double[8][8];
		double dct[][] = new double[8][8];
		double sum, au, av;
		final int N = 8;  // Block size
		
        for (int u = 0; u < N; u++) {
          for (int v = 0; v < N; v++) {    

            sum = 0;
            for (int x = 0; x < N; x++) {
              for (int y = 0; y < N; y++) {
                in[x][y] = input[x][y] - 128.0;  // Subtract by 128
                sum += Math.cos((2*(x)+1)*(u)*Math.PI/(2*N)) * Math.cos((2*(y)+1)*(v)*Math.PI/(2*N)) * in[x][y];
              }
            }
            dct[u][v] = CC(u) * CC(v) * sum;
	   
          } // for v
        } // for u
        return dct;
	}
	
	public static double CC(double k) {
		double out = 0.0;
		if(k == 0)
			out = Math.sqrt(1.0/8.0);
		else
			out = Math.sqrt(2.0/8.0);	
		return out;
	}
	
	private static void loadInputSample(double[][] f) {
		f[0][0] = 139;
		f[1][0] = 144;
		f[2][0] = 149;
		f[3][0] = 153;
		f[4][0] = 155;
		f[5][0] = 155;
		f[6][0] = 155;
		f[7][0] = 155;
		
		f[0][1] = 144;
		f[1][1] = 151;
		f[2][1] = 153;
		f[3][1] = 156;
		f[4][1] = 159;
		f[5][1] = 156;
		f[6][1] = 156;
		f[7][1] = 156;
		
		f[0][2] = 150;
		f[1][2] = 155;
		f[2][2] = 160;
		f[3][2] = 163;
		f[4][2] = 158;
		f[5][2] = 156;
		f[6][2] = 156;
		f[7][2] = 156;
		
		f[0][3] = 159;
		f[1][3] = 161;
		f[2][3] = 162;
		f[3][3] = 160;
		f[4][3] = 160;
		f[5][3] = 159;
		f[6][3] = 159;
		f[7][3] = 159;
		
		f[0][4] = 159;
		f[1][4] = 160;
		f[2][4] = 161;
		f[3][4] = 162;
		f[4][4] = 162;
		f[5][4] = 155;
		f[6][4] = 155;
		f[7][4] = 155;
		
		f[0][5] = 161;
		f[1][5] = 161;
		f[2][5] = 161;
		f[3][5] = 161;
		f[4][5] = 160;
		f[5][5] = 157;
		f[6][5] = 157;
		f[7][5] = 157;
		
		f[0][6] = 162;
		f[1][6] = 162;
		f[2][6] = 161;
		f[3][6] = 163;
		f[4][6] = 162;
		f[5][6] = 157;
		f[6][6] = 157;
		f[7][6] = 157;
		
		f[0][7] = 162;
		f[1][7] = 162;
		f[2][7] = 161;
		f[3][7] = 161;
		f[4][7] = 163;
		f[5][7] = 158;
		f[6][7] = 158;
		f[7][7] = 158;
    }
	
	public static void printArray(int[][] F, int N){
		for(int u = 0; u < N; u++){
			for(int v = 0; v < N; v++){
				//System.out.format("%.1d", F[u][v]);
				System.out.print(F[u][v] + "  ");
				System.out.print("\t");
			}
			System.out.println("");
		}	
		System.out.println("");
	}
	
	public static void printArray(double[][] F, int N){
		for(int u = 0; u < N; u++){
			for(int v = 0; v < N; v++){
				System.out.format("%.1f", F[u][v]);
				System.out.print("\t");
			}
			System.out.println("");
		}		
		System.out.println("");
	}
	
	public static void printPartArray(double[][] arr, int startX, int startY){
		for(int y = startY; y < 5 + startY; y++){
			for(int x = startX; x < 5 + startX; x++){
				System.out.format("%.1f", arr[x][y]);
				System.out.print("\t");
			}
			System.out.println("");
		}		
		System.out.println("");
	}
	
	public static int[][] generateZigzagPattern() {
		int[][] zigzag = new  int[64][2]; //L = 0; R = 1
		zigzag[0][0] = 0;
		zigzag[0][1] = 0;
		zigzag[1][0] = 1;
		zigzag[1][1] = 0;
		
		int L = 1;
		int R = 0;
		int index = 1;	
		
		while(true){
			while(L != 0){
				index++;
				L--;
				R++;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			if(L == 0 && R == 7)
				break;
			index++;
			R++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
	
			while(R != 0 ){
				index++;
				L++;
				R--;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			
			index++;
			L++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
	
		}
			
			index++;
			L++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
			
		while(true){
			while(L != 7){
				index++;
				L++;
				R--;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			index++;
			R++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
			
			while(R != 7){
				index++;
				L--;
				R++;
				zigzag[index][0] = L;
				zigzag[index][1] = R;
			}
			index++;
			L++;
			zigzag[index][0] = L;
			zigzag[index][1] = R;
			
			if(L == 7 && R == 7)
				break;
		}
		return zigzag;
	}
}
