public class DCTdemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//demo1();
		demo2();

	}
	
	public static void demo1(){
		int[][] input1 = new int[8][8];

		loadInputSample(input1);
		printArray(input1, 8);

		System.out.println("output when not subtracting 128");
		System.out.println("uses homework algorithm - does not match example");
		printArray(runDCTtransform(input1), 8);

		int[][] input2 = new int[8][8];
		loadInputSample(input2);
		System.out.println("output when subtracting 128");
		System.out.println("uses found algorithm - does match example");
		printArray(dct3(input2), 8);
	}
	
	public static void demo2() {
		double[][] arr = new double[8][8];

		loadInputSample2(arr);
		printArray(arr, 8);

		System.out.println("runDCTconvertion(arr) = ");
		printArray(runDCTconvertion(arr), 8);
	}

	// This is my original code
	public static double[][] runDCTtransform(int[][] f) {
		double[][] F = new double[8][8];
		for (int u = 0; u < 8; u++) {
			for (int v = 0; v < 8; v++) {

				double subSum = 0.0;
				for (int x = 0; x < 8; x++) {
					for (int y = 0; y < 8; y++) {
						// not subtracting 128
						subSum += Math.cos(((2.0 * x + 1.0) * u * Math.PI) / (16.0)) * Math.cos(((2.0 * y + 1.0) * v * Math.PI) / (16.0)) * f[x][y];
					}
				}
				F[u][v] = ((Cfunc(u) * Cfunc(v)) / 4.0) * subSum;
			}
		}
		return F;
	}

	public static double Cfunc(double x) {
		if (x == 0)
			return Math.sqrt(2.0) / 2.0;
		else
			return 1.0;
	}

	// ## I modified this code from an example I found at
	// http://www.cyut.edu.tw/~yltang/program/Dct.java
	public static double[][] dct3(int[][] img) {
		double in[][] = new double[8][8];
		double dct[][] = new double[8][8];
		double sum;
		final int N = 8; // Block size

		for (int u = 0; u < N; u++) {
			for (int v = 0; v < N; v++) {

				sum = 0;
				for (int x = 0; x < N; x++) {
					for (int y = 0; y < N; y++) {
						in[x][y] = img[x][y] - 128.0; // Subtract by 128
						sum += Math.cos((2 * (x) + 1) * (u) * Math.PI / (2 * N)) * Math.cos((2 * (y) + 1) * (v) * Math.PI / (2 * N)) * in[x][y];
					}
				}
				// dct[u][v] = CC(u) * CC(v) * sum;
				// I'm using my version of the support method to demo that the
				// two versions are exactly
				// the same except for subtracting 128 from the input value
				dct[u][v] = ((Cfunc(u) * Cfunc(v)) / 4.0) * sum;

			} // for v
		} // for u
		return dct;
	}
	
	public static double[][] runDCTconvertion(double[][] f) {			
		int sizeX = f.length;
		int sizeY = f[0].length;
		double[][] F = new double[sizeX][sizeY];
		int u, v, x, y;
		for(int yy = 0; yy < sizeY; yy += 8){
			for(int xx = 0; xx < sizeX; xx += 8){
				
					for(int u1 = xx; u1 < xx + 8; u1++){
						u = u1 - xx;
						for(int v1 = yy; v1 < yy + 8; v1++){
							v = v1 - yy;
							double subSum = 0.0;
							for(int x1 = xx; x1 < xx + 8; x1++){
								x = x1 - xx;
								for(int y1 = yy; y1 < yy + 8; y1++){
									y = y1 - yy;
									subSum += Math.cos(((2.0*x + 1.0)*u*Math.PI)/(16.0)) * Math.cos(((2.0*y + 1.0)*v*Math.PI)/(16.0)) * f[x1][y1];
								}
							}
					F[u1][v1] = Math.max(Math.min(((Cfunc(u) * Cfunc(v)) / 4.0) * subSum, 1024.0), -1024.0);
					}
				}	
			}
		}	
		return F;
	}
	

	
	public static double[][] inverseDCTconvertion(double[][] Fp) {	// inverses DCT on 8x8 array blocks	
		int sizeX = Fp.length;
		int sizeY = Fp[0].length;
		double[][] fp = new double[sizeX][sizeY];
		int u, v, x, y;
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;
						double subSum = 0.0;
						for(int u1 = xx; u1 < xx + 8; u1++){
							u = u1 - xx;
							for(int v1 = yy; v1 < yy + 8; v1++){
								v = v1 - yy;
								subSum += Cfunc(u) * Cfunc(v) * Fp[u1][v1] * Math.cos(((2.0*x + 1.0)*u*Math.PI)/(16.0)) * Math.cos(((2.0*y + 1.0)*v*Math.PI)/(16.0));
							}
						}
						fp[x1][y1] = ((1.0 / 4.0) * subSum);
					}
				}
			}
		}
		return fp;
	}

	// his suport function works the same - just arrange differently
	public static double CC(double k) {
		double out = 0.0;
		if (k == 0)
			out = Math.sqrt(1.0 / 8.0);
		else
			out = Math.sqrt(2.0 / 8.0);
		return out;
	}
	
	public static void printArray(double[][] F, int N) {
		for (int y = 0; y < N; y++) {
			for (int x = 0; x < N; x++) {
				System.out.format("%.1f", F[x][y]);
				System.out.print("\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public static void printArray(int[][] F, int N){
		for(int y = 0; y < N; y++){
			for(int x = 0; x < N; x++){
				//System.out.format("%.1d", F[u][v]);
				System.out.print(F[x][y] + "  ");
				System.out.print("\t");
			}
			System.out.println("");
		}	
		System.out.println("");
	}

	private static void loadInputSample(int[][] f) {
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
	
	private static void loadInputSample2(double[][] f) {
		f[0][0] = -51.755;
		f[1][0] = -51.755;
		f[2][0] = -128.0;
		f[3][0] = -128.0;
		f[4][0] = -51.755;
		f[5][0] = -51.755;
		f[6][0] = -128.0;
		f[7][0] = -128.0;

		f[0][1] = -51.755;
		f[1][1] = -51.755;
		f[2][1] = -128.0;
		f[3][1] = -128.0;
		f[4][1] = -51.755;
		f[5][1] = -51.755;
		f[6][1] = -128.0;
		f[7][1] = -128.0;

		f[0][2] = -51.755;
		f[1][2] = -51.755;
		f[2][2] = -128.0;
		f[3][2] = -128.0;
		f[4][2] = -51.755;
		f[5][2] = -51.755;
		f[6][2] = -128.0;
		f[7][2] = -128.0;

		f[0][3] = -51.755;
		f[1][3] = -51.755;
		f[2][3] = -128.0;
		f[3][3] = -128.0;
		f[4][3] = -51.755;
		f[5][3] = -51.755;
		f[6][3] = -128.0;
		f[7][3] = -128.0;

		f[0][4] = -51.755;
		f[1][4] = -51.755;
		f[2][4] = -128.0;
		f[3][4] = -128.0;
		f[4][4] = -51.755;
		f[5][4] = -51.755;
		f[6][4] = -128.0;
		f[7][4] = -128.0;

		f[0][5] = -51.755;
		f[1][5] = -51.755;
		f[2][5] = -128.0;
		f[3][5] = -128.0;
		f[4][5] = -51.755;
		f[5][5] = -51.755;
		f[6][5] = -128.0;
		f[7][5] = -128.0;

		f[0][6] = -51.755;
		f[1][6] = -51.755;
		f[2][6] = -128.0;
		f[3][6] = -128.0;
		f[4][6] = -51.755;
		f[5][6] = -51.755;
		f[6][6] = -128.0;
		f[7][6] = -128.0;

		f[0][7] = -51.755;
		f[1][7] = -51.755;
		f[2][7] = -128.0;
		f[3][7] = -128.0;
		f[4][7] = -51.755;
		f[5][7] = -51.755;
		f[6][7] = -128.0;
		f[7][7] = -128.0;
	}

	
}
