
public class QuantizationDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		demo1();
		//demo2();
		
	}
	
	public static void demo1() {	
		System.out.println("Demo 1()");
	
		double[][]  imgBlock = getLec08_JPEGsupplimentalPage_SampleBlock();
		double[][] lumaTable = getLec08_JPEGsupplimentalPage_QuantizeTable();
		
		System.out.println("\ninitial image block");
		printArray(imgBlock);
		
		System.out.println("DCT conversion");
		double[][] DCT = runDCTconvertion(imgBlock);
		printArray(DCT);
		
		System.out.println("reverse DCT conversion - just to test lossless process");
		double[][] reverseDCT = reverseDCTconvertion(DCT);
		printArray(reverseDCT);
		
		System.out.println("quantization factor table");
		printArray(lumaTable);

		System.out.println("quantization of DCT conversion");
		double[][] quant = quantize(DCT, 0, lumaTable);
		printArray(quant);
		
		System.out.println("quantization block sequenced");
		double[] sequ = blockToSequence(quant);
		for(int x = 0; x < 64; x++){
			System.out.println(sequ[x] + " ");
		}
		
		System.out.println("\nnumber of pairs = " + runLengthEncode(sequ));
	}
	
	public static void demo2() {
		System.out.println("Demo 2()");
		double[][] imgBlock = getLect08page10_SampleBlock();
		double[][] lumaTable = getLect08page10_LumaTable();
		double[][] chromaTable = getLect08page10_ChromaTable();		
		
		System.out.println("\ninitial image block");
		printArray(imgBlock);
		
		System.out.println("\nLuma Table");
		printArray(lumaTable);
		
		System.out.println("\nChroma Table");
		printArray(chromaTable);
		
		System.out.println("Luma Quantization");
		printArray(quantize(imgBlock, 0, lumaTable));
		
		System.out.println("Chroma Quantization");
		printArray(quantize(imgBlock, 0, chromaTable));
		
		
	}
	
	public static double[][] runDCTconvertion(double[][] f) {			
		int sizeX = f.length;
		int sizeY = f[0].length;
		double[][] F = new double[sizeX][sizeY];
		int u, v, x, y;
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
			
				for(int u1 = xx; u1 < xx + 8; u1++){
					u = u1 - xx;
					for(int v1 = yy; v1 < yy + 8; v1++){
						v = v1 - yy;
						double subSum = 0.0;
						for(int x1 = xx; x1 < xx + 8; x1++){
							x = x1 - xx;
							for(int y1 = yy; y1 < yy + 8; y1++){
								y = y1 - yy;
								
								subSum += Math.cos(((2.0*x + 1.0)*u*Math.PI)/(16.0)) * Math.cos(((2.0*y + 1.0)*v*Math.PI)/(16.0)) * f[x1][y1];;
							}
						}
						F[u1][v1] = Math.max(Math.min(((Cfunc(u) * Cfunc(v)) / 4.0) * subSum, 1024.0), -1024.0);
					}
				}	
			}
		}	
		return F;
	}
	

	
	public static double[][] reverseDCTconvertion(double[][] Fp) {	// inverses DCT on 8x8 array blocks	
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
	
	public static double Cfunc(double x) {
		if(x == 0)
			return Math.sqrt(2.0) / 2.0;
		else
			return 1.0;
	}
	
	public static double[][] quantize(double[][] input, int n, double[][] quantizeTable){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];	
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){			
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;
						output[x1][y1] = Math.round(input[x1][y1] / (quantizeTable[x][y] * Math.pow(2.0, n)));
					}
				}
			}
		}
		return output;
	}
	
	public static double[] blockToSequence(double[][] input) {
		double[] output = new double[64];
		int[][] zigzag = generateZigzagPattern();	
		for(int i = 0; i < 64; i++){
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
		}	
		return output;
	}
	
	public static double runLengthEncode(double[] inSequence){
		int pairCount = 0;
		double[][] RLEpairs = new double[64][2];
		double preValue = inSequence[1];
		int cc = 1;
		int i = 0;
		for(i = 2; i < 64; i++){
			if(inSequence[i] == preValue){
				cc++;
			}
			else{
				RLEpairs[pairCount][0] = preValue;
				RLEpairs[pairCount][1] = cc;
				pairCount++;
				preValue = inSequence[i];
				cc = 1;
			}
		}
		RLEpairs[pairCount][0] = preValue;
		RLEpairs[pairCount][1] = cc;
		pairCount++;
		
		return pairCount;
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
	
	public static void mathTest(double [][] DCT, double[][] quantFact){
		System.out.println("math test DCT[0][0] / quant[0][0] = " + DCT[0][0] / quantFact[0][0] + "\n");
	}
	
	
	public static double[][] getLec08_JPEGsupplimentalPage_SampleBlock() {
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
	
	public static double[][] getLec08_JPEGsupplimentalPage_QuantizeTable() {
		double[][] table = new double[8][8];
		
		table[0][0] = 16;
		table[1][0] = 11;
		table[2][0] = 10;
		table[3][0] = 16;
		table[4][0] = 24;
		table[5][0] = 40;
		table[6][0] = 51;
		table[7][0] = 61;
		
		table[0][1] = 12;
		table[1][1] = 12;
		table[2][1] = 14;
		table[3][1] = 19;
		table[4][1] = 26;
		table[5][1] = 58;
		table[6][1] = 60;
		table[7][1] = 55;
		
		table[0][2] = 14;
		table[1][2] = 13;
		table[2][2] = 16;
		table[3][2] = 24;
		table[4][2] = 40;
		table[5][2] = 57;
		table[6][2] = 69;
		table[7][2] = 56;
		
		table[0][3] = 14;
		table[1][3] = 17;
		table[2][3] = 22;
		table[3][3] = 29;
		table[4][3] = 51;
		table[5][3] = 87;
		table[6][3] = 80;
		table[7][3] = 62;
		
		table[0][4] = 18;
		table[1][4] = 22;
		table[2][4] = 37;
		table[3][4] = 56;
		table[4][4] = 68;
		table[5][4] = 109;
		table[6][4] = 103;
		table[7][4] = 77;
		
		table[0][5] = 24;
		table[1][5] = 35;
		table[2][5] = 55;
		table[3][5] = 64;
		table[4][5] = 81;
		table[5][5] = 104;
		table[6][5] = 113;
		table[7][5] = 92;
		
		table[0][6] = 49;
		table[1][6] = 64;
		table[2][6] = 78;
		table[3][6] = 87;
		table[4][6] = 103;
		table[5][6] = 121;
		table[6][6] = 120;
		table[7][6] = 101;
		
		table[0][7] = 72;
		table[1][7] = 92;
		table[2][7] = 95;
		table[3][7] = 98;
		table[4][7] = 112;
		table[5][7] = 100;
		table[6][7] = 103;
		table[7][7] = 99; 
		
		return table;
	}
	
	public static double[][] getLect08page10_LumaTable() {
		double[][] table = new double[8][8];
		
		table[0][0] = 16;
		table[1][0] = 11;
		table[2][0] = 10;
		table[3][0] = 16;
		table[4][0] = 24;
		table[5][0] = 40;
		table[6][0] = 51;
		table[7][0] = 61;
		
		table[0][1] = 12;
		table[1][1] = 12;
		table[2][1] = 14;
		table[3][1] = 19;
		table[4][1] = 26;
		table[5][1] = 58;
		table[6][1] = 60;
		table[7][1] = 55;
		
		table[0][2] = 14;
		table[1][2] = 13;
		table[2][2] = 16;
		table[3][2] = 24;
		table[4][2] = 40;
		table[5][2] = 57;
		table[6][2] = 69;
		table[7][2] = 56;
		
		table[0][3] = 14;
		table[1][3] = 17;
		table[2][3] = 22;
		table[3][3] = 29;
		table[4][3] = 51;
		table[5][3] = 87;
		table[6][3] = 80;
		table[7][3] = 62;
		
		table[0][4] = 18;
		table[1][4] = 22;
		table[2][4] = 37;
		table[3][4] = 56;
		table[4][4] = 68;
		table[5][4] = 109;
		table[6][4] = 103;
		table[7][4] = 77;
		
		table[0][5] = 24;
		table[1][5] = 35;
		table[2][5] = 55;
		table[3][5] = 64;
		table[4][5] = 81;
		table[5][5] = 104;
		table[6][5] = 113;
		table[7][5] = 92;
		
		table[0][6] = 49;
		table[1][6] = 64;
		table[2][6] = 78;
		table[3][6] = 87;
		table[4][6] = 103;
		table[5][6] = 121;
		table[6][6] = 120;
		table[7][6] = 101;
		
		table[0][7] = 72;
		table[1][7] = 92;
		table[2][7] = 95;
		table[3][7] = 98;
		table[4][7] = 112;
		table[5][7] = 100;
		table[6][7] = 103;
		table[7][7] = 99; 
		
		return table;
	}
	
	public static double[][] getLect08page10_ChromaTable() {
		double[][] table = new double[8][8];
		
		table[0][0] = 17;
		table[1][0] = 18;
		table[2][0] = 24;
		table[3][0] = 47;
		table[4][0] = 99;
		table[5][0] = 99;
		table[6][0] = 99;
		table[7][0] = 99;
		
		table[0][1] = 18;
		table[1][1] = 21;
		table[2][1] = 26;
		table[3][1] = 66;
		table[4][1] = 99;
		table[5][1] = 99;
		table[6][1] = 99;
		table[7][1] = 99;
		
		table[0][2] = 24;
		table[1][2] = 26;
		table[2][2] = 56;
		table[3][2] = 99;
		table[4][2] = 99;
		table[5][2] = 99;
		table[6][2] = 99;
		table[7][2] = 99;
		
		table[0][3] = 47;
		table[1][3] = 66;
		table[2][3] = 99;
		table[3][3] = 99;
		table[4][3] = 99;
		table[5][3] = 99;
		table[6][3] = 99;
		table[7][3] = 99;
		
		table[0][4] = 99;
		table[1][4] = 99;
		table[2][4] = 99;
		table[3][4] = 99;
		table[4][4] = 99;
		table[5][4] = 99;
		table[6][4] = 99;
		table[7][4] = 99;
		
		table[0][5] = 99;
		table[1][5] = 99;
		table[2][5] = 99;
		table[3][5] = 99;
		table[4][5] = 99;
		table[5][5] = 99;
		table[6][5] = 99;
		table[7][5] = 99;
		
		table[0][6] = 99;
		table[1][6] = 99;
		table[2][6] = 99;
		table[3][6] = 99;
		table[4][6] = 99;
		table[5][6] = 99;
		table[6][6] = 99;
		table[7][6] = 99;
		
		table[0][7] = 99;
		table[1][7] = 99;
		table[2][7] = 99;
		table[3][7] = 99;
		table[4][7] = 99;
		table[5][7] = 99;
		table[6][7] = 99;
		table[7][7] = 99; 
		
		return table;
	}
	
	public static double[][] getLect08page10_SampleBlock() {
		double[][] table = new double[8][8];
		
		table[0][0] = 200;
		table[1][0] = 202;
		table[2][0] = 189;
		table[3][0] = 188;
		table[4][0] = 189;
		table[5][0] = 175;
		table[6][0] = 175;
		table[7][0] = 175;
		
		table[0][1] = 200;
		table[1][1] = 203;
		table[2][1] = 198;
		table[3][1] = 188;
		table[4][1] = 189;
		table[5][1] = 182;
		table[6][1] = 178;
		table[7][1] = 175;
		
		table[0][2] = 203;
		table[1][2] = 200;
		table[2][2] = 200;
		table[3][2] = 195;
		table[4][2] = 200;
		table[5][2] = 187;
		table[6][2] = 185;
		table[7][2] = 175;
		
		table[0][3] = 200;
		table[1][3] = 200;
		table[2][3] = 200;
		table[3][3] = 200;
		table[4][3] = 197;
		table[5][3] = 187;
		table[6][3] = 187;
		table[7][3] = 187;
		
		table[0][4] = 200;
		table[1][4] = 205;
		table[2][4] = 200;
		table[3][4] = 200;
		table[4][4] = 195;
		table[5][4] = 188;
		table[6][4] = 187;
		table[7][4] = 175;
		
		table[0][5] = 200;
		table[1][5] = 200;
		table[2][5] = 200;
		table[3][5] = 200;
		table[4][5] = 200;
		table[5][5] = 190;
		table[6][5] = 187;
		table[7][5] = 175;
		
		table[0][6] = 205;
		table[1][6] = 200;
		table[2][6] = 199;
		table[3][6] = 200;
		table[4][6] = 191;
		table[5][6] = 187;
		table[6][6] = 187;
		table[7][6] = 175;
		
		table[0][7] = 210;
		table[1][7] = 200;
		table[2][7] = 200;
		table[3][7] = 200;
		table[4][7] = 188;
		table[5][7] = 185;
		table[6][7] = 187;
		table[7][7] = 186;
		
		return table;
	}
	
	public static double[][] getBlankTable() {
		double[][] table = new double[8][8];
		
//		table[0][0] = ;
//		table[1][0] = ;
//		table[2][0] = ;
//		table[3][0] = ;
//		table[4][0] = ;
//		table[5][0] = ;
//		table[6][0] = ;
//		table[7][0] = ;
//		
//		table[0][1] = ;
//		table[1][1] = ;
//		table[2][1] = ;
//		table[3][1] = ;
//		table[4][1] = ;
//		table[5][1] = ;
//		table[6][1] = ;
//		table[7][1] = ;
//		
//		table[0][2] = ;
//		table[1][2] = ;
//		table[2][2] = ;
//		table[3][2] = ;
//		table[4][2] = ;
//		table[5][2] = ;
//		table[6][2] = ;
//		table[7][2] = ;
//		
//		table[0][3] = ;
//		table[1][3] = ;
//		table[2][3] = ;
//		table[3][3] = ;
//		table[4][3] = ;
//		table[5][3] = ;
//		table[6][3] = ;
//		table[7][3] = ;
//		
//		table[0][4] = ;
//		table[1][4] = ;
//		table[2][4] = ;
//		table[3][4] = ;
//		table[4][4] = ;
//		table[5][4] = ;
//		table[6][4] = ;
//		table[7][4] = ;
//		
//		table[0][5] = ;
//		table[1][5] = ;
//		table[2][5] = ;
//		table[3][5] = ;
//		table[4][5] = ;
//		table[5][5] = ;
//		table[6][5] = ;
//		table[7][5] = ;
//		
//		table[0][6] = ;
//		table[1][6] = ;
//		table[2][6] = ;
//		table[3][6] = ;
//		table[4][6] = ;
//		table[5][6] = ;
//		table[6][6] = ;
//		table[7][6] = ;
//		
//		table[0][7] = ;
//		table[1][7] = ;
//		table[2][7] = ;
//		table[3][7] = ;
//		table[4][7] = ;
//		table[5][7] = ;
//		table[6][7] = ;
//		table[7][7] = ;
		
		return table;
	}
	
}
