
public class CRanalyticDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		//demo1();
			int imgOrigSizeX = 16;
			int imgOrigSizeY = 16;
			int sizeX = 16;
			int sizeY = 16;
			int chromaSizeX = 8;
			int chromaSizeY = 8;
			int quality = 0;
			
			// Transform each pixel	from RGB to YCbCr	
			double[][][] redblackImg = gen16xBlockRedBlack();
			System.out.println("input image - 16x16 version of redblack (vertical red stripes)");
			printArray(redblackImg);
			
			double[][][] arrayYCbCr = new double[sizeX][sizeY][3];
			for(int y = 0; y < sizeY; y++){
				for(int x = 0; x < sizeX; x++){	
					arrayYCbCr[x][y] = matrixRGBtoYCbCR(redblackImg[x][y]);
				}
			}
					
			// Seperate the YCbCr array into it's individule component arrays
			double[][] arrayY = new double[sizeX][sizeY];
			double[][] arrayCb = new double[sizeX][sizeY];
			double[][] arrayCr = new double[sizeX][sizeY];
			for(int y = 0; y < sizeY; y++){
				for(int x = 0; x < sizeX; x++){	
					arrayY[x][y] = arrayYCbCr[x][y][0];
					arrayCb[x][y] = arrayYCbCr[x][y][1];
					arrayCr[x][y] = arrayYCbCr[x][y][2];
				}
			}
			
			System.out.println("arrayY[0][0] = " + arrayY[0][0]);
			
			System.out.println("YCbCr components");
			printArray(arrayY);
			printArray(arrayCb);
			printArray(arrayCr);
			
			// Subsample Cb and	Cr using 4:2:0 (MPEG1) chrominance subsampling scheme.
			double[][] subSampleCb = new double[chromaSizeX][chromaSizeY];
			double[][] subSampleCr = new double[chromaSizeX][chromaSizeY];	
			for(int y = 0; y < chromaSizeY; y++){	// Initialize empty array to all 0's
				for(int x = 0; x < chromaSizeX; x++){	
					subSampleCb[x][y] = 0.0;
					subSampleCr[x][y] = 0.0;
				}
			}
			for(int y = 0; y < sizeY; y += 2){
				for(int x = 0; x < sizeX; x += 2){
					subSampleCb[x/2][y/2] = (arrayCb[x][y] + arrayCb[x + 1][y] + arrayCb[x][y + 1] + arrayCb[x + 1][y + 1]) / 4.0;
					subSampleCr[x/2][y/2] = (arrayCr[x][y] + arrayCr[x + 1][y] + arrayCr[x][y + 1] + arrayCr[x + 1][y + 1]) / 4.0;
				}
			}
			
			
			System.out.println("subSampleCb[0][0] = " + subSampleCb[0][0]);
			System.out.println("subSamples Cb");
			printArray(subSampleCb);
			System.out.println("subSamples Cr");
			printArray(subSampleCr);
			System.out.println("-------------------------------");
			
			// Perform the DCT for Y image, Cb image, and Cr image 
			double[][] YarrayDCT = runDCTconvertion(arrayY);
			double[][] CbArrayDCT = runDCTconvertion(subSampleCb);
			double[][] CrArrayDCT = runDCTconvertion(subSampleCr);

			System.out.println("DCT array[0][0] = " + YarrayDCT[0][0]);
			System.out.println("DCT output - luma Y values");
			printArray(YarrayDCT);
			
			
			System.out.println("luma Y quantizing table values");
			printArray(getYquantTable());
			System.out.println("-------------------------------");
			
			
			System.out.println("DCT output - chroma Cb values");
			printArray(CbArrayDCT);
			System.out.println("DCT output - chroma Cr values");
			printArray(CrArrayDCT);
			
			System.out.println("chroma quantizing table values");
			printArray(getCromeQuant());
			System.out.println("-------------------------------");
			
			// Quantize image components
			double[][] quantizedY = quantizeLuma(YarrayDCT, quality);
			double[][] quantizedCb = quantizeChroma(CbArrayDCT, quality);
			double[][] quantizedCr = quantizeChroma(CrArrayDCT, quality);
			
			
			System.out.println("quantizedY[0][0] = " + quantizedY[0][0]);
			System.out.println("quantized outputs");
			printArray(quantizedY);
			
			
			//display a block converted to sequence
			{ //scope limiter
				double[][] blk = new double[8][8];
				for(int y = 0; y < 8; y++){
					for(int x = 0; x < 8; x++){
						blk[x][y] = quantizedY[x][y];
					}
				}
				// Convert from 2D block to 1D sequence (zigzag)
				double[] seq = blockToSequence(blk);
				System.out.println("first block of quantized Y as sequence");
				printArray(seq);
				System.out.println("");
			}
			
			System.out.println("quantized Cb");
			printArray(quantizedCb);
			System.out.println("quantized Cr");
			printArray(quantizedCr);
			
			//####### analytics #############
			
			
			double totalCost = 0;
			double countY = 0;
			double countCb = 0;
			double countCr = 0;
			double bitCostY = 0;
			double bitCostCb = 0;
			double bitCostCr = 0;
			double origCost = 0;
			
			System.out.println("\nFor a quantization level n = " + quality);
			
			origCost = imgOrigSizeX * imgOrigSizeY * 24;
			System.out.println("The original image cost, (S), is " + origCost);
			
			for(int xx = 0; xx < sizeX; xx += 8){
				for(int yy = 0; yy < sizeY; yy += 8){

					double[][] block = new double[8][8];
					for(int y = 0; y < 8; y++){
						for(int x = 0; x < 8; x++){
							block[x][y] = quantizedY[xx + x][yy + y];
						}
					}
					// Convert from 2D block to 1D sequence (zigzag)
					double[] sequence = blockToSequence(block);
					
					// Perform a run length encode of 1D sequence and count number of sequence pairs
					bitCostY += (9 - quality);  // Adds the DC for that block
					countY = runLengthEncode(sequence);
					bitCostY += (15 - quality) * countY;
				}
			}
			System.out.println("The Y values cost is " + bitCostY + " bits.");
			

			for(int xx = 0; xx < chromaSizeX; xx += 8){
				for(int yy = 0; yy < chromaSizeY; yy += 8){
					
					double[][] block = new double[8][8];
					for(int y = 0; y < 8; y++){
						for(int x = 0; x < 8; x++){
							block[x][y] = quantizedCb[xx + x][yy + y];
						}
					}
					// Convert from 2D block to 1D sequence (zigzag)
					double[] sequence = blockToSequence(block);
					// Perform a run length encode of 1D sequence and count number of sequence pairs
					bitCostCb += (8 - quality);
					countCb = runLengthEncode(sequence);
					bitCostCb += (14 - quality) * countY;
				}
			}
			System.out.println("The Cb values cost is " + bitCostCb + " bits.");
			

			for(int xx = 0; xx < chromaSizeX; xx += 8){
				for(int yy = 0; yy < chromaSizeY; yy += 8){
					double[][] block = new double[8][8];
					for(int y = 0; y < 8; y++){
						for(int x = 0; x < 8; x++){
							block[x][y] = quantizedCr[xx + x][yy + y];
						}
					}
					// Convert from 2D block to 1D sequence (zigzag)
					double[] sequence = blockToSequence(block);
					// Perform a run length encode of 1D sequence and count number of sequence pairs
					bitCostCr += (8 - quality);
					countCr = runLengthEncode(sequence);
					bitCostCr += (14 - quality) * countY;
				}
			}
			System.out.println("The Cr values cost is " + bitCostCr + " bits.");
			
			// Calculate total compression costs
			totalCost = bitCostY + bitCostCb + bitCostCr;
			System.out.println("The total compressed image cost, (D), is " + totalCost + " bits.");
			
			System.out.println("The compression ratio, (S/D), is " + origCost/totalCost);
		
	}
	
	public static void demo1() {
		System.out.println("\nzigzag pattern demo\n");
		int count = 0;
		int[][] zigzag = generateZigzagPattern2();
		int[][] matrix = new int[8][8];
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				matrix[zigzag[count][0]][zigzag[count][1]] = count;
				count++;
			}
		}		
		printArray(matrix);
	}
	
	public static int[] blockToSequence(int[][] input) {
		int[] output = new int[64];
		int[][] zigzag = generateZigzagPattern2();	
		for(int i = 0; i < 64; i++){
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
		}	
		return output;
	}
	
	public static int[][] generateZigzagPattern1() {
		int[][] zigzag = new int [64][2];
		
		zigzag[0][0] = 0;   zigzag[0][1] = 0;
		
		zigzag[1][0] = 1;   zigzag[1][1] = 0;
		zigzag[2][0] = 0;   zigzag[2][1] = 1;
		
		zigzag[3][0] = 0;   zigzag[3][1] = 2;
		zigzag[4][0] = 1;   zigzag[4][1] = 1;
		zigzag[5][0] = 2;   zigzag[5][1] = 0;
		
		zigzag[6][0] = 3;   zigzag[6][1] = 0;
		zigzag[7][0] = 2;   zigzag[7][1] = 1;
		zigzag[8][0] = 1;   zigzag[8][1] = 2;
		zigzag[9][0] = 0;   zigzag[9][1] = 3;
		
		zigzag[10][0] = 0;   zigzag[10][1] = 4;
		zigzag[11][0] = 1;   zigzag[11][1] = 3;
		zigzag[12][0] = 2;   zigzag[12][1] = 2;
		zigzag[13][0] = 3;   zigzag[13][1] = 1;
		zigzag[14][0] = 4;   zigzag[14][1] = 0;
		
		zigzag[15][0] = 5;   zigzag[15][1] = 0;
		zigzag[16][0] = 4;   zigzag[16][1] = 1;
		zigzag[17][0] = 3;   zigzag[17][1] = 2;
		zigzag[18][0] = 2;   zigzag[18][1] = 3;
		zigzag[19][0] = 1;   zigzag[19][1] = 4;
		zigzag[20][0] = 0;   zigzag[20][1] = 5;
		
		zigzag[21][0] = 0;   zigzag[21][1] = 6;
		zigzag[22][0] = 1;   zigzag[22][1] = 5;
		zigzag[23][0] = 2;   zigzag[23][1] = 4;
		zigzag[24][0] = 3;   zigzag[24][1] = 3;
		zigzag[25][0] = 4;   zigzag[25][1] = 2;
		zigzag[26][0] = 5;   zigzag[26][1] = 1;
		zigzag[27][0] = 6;   zigzag[27][1] = 0;
		
		zigzag[28][0] = 7;   zigzag[28][1] = 0;
		zigzag[29][0] = 6;   zigzag[29][1] = 1;
		zigzag[30][0] = 5;   zigzag[30][1] = 2;
		zigzag[31][0] = 4;   zigzag[31][1] = 3;
		zigzag[32][0] = 3;   zigzag[32][1] = 4;
		zigzag[33][0] = 2;   zigzag[33][1] = 5;
		zigzag[34][0] = 1;   zigzag[34][1] = 6;
		zigzag[35][0] = 0;   zigzag[35][1] = 7;
		
		zigzag[36][0] = 1;   zigzag[36][1] = 7;
		zigzag[37][0] = 2;   zigzag[37][1] = 6;
		zigzag[38][0] = 3;   zigzag[38][1] = 5;
		zigzag[39][0] = 4;   zigzag[39][1] = 4;
		zigzag[40][0] = 5;   zigzag[40][1] = 3;
		zigzag[41][0] = 6;   zigzag[41][1] = 2;
		zigzag[42][0] = 7;   zigzag[42][1] = 1;
		
		zigzag[43][0] = 7;   zigzag[43][1] = 2;
		zigzag[44][0] = 6;   zigzag[44][1] = 3;
		zigzag[45][0] = 5;   zigzag[45][1] = 4;
		zigzag[46][0] = 4;   zigzag[46][1] = 5;
		zigzag[47][0] = 3;   zigzag[47][1] = 6;
		zigzag[48][0] = 2;   zigzag[48][1] = 7;
		
		zigzag[49][0] = 3;   zigzag[49][1] = 7;
		zigzag[50][0] = 4;   zigzag[50][1] = 6;
		zigzag[51][0] = 5;   zigzag[51][1] = 5;
		zigzag[52][0] = 6;   zigzag[52][1] = 4;
		zigzag[53][0] = 7;   zigzag[53][1] = 3;
		
		zigzag[54][0] = 7;   zigzag[54][1] = 4;
		zigzag[55][0] = 6;   zigzag[55][1] = 5;
		zigzag[56][0] = 5;   zigzag[56][1] = 6;
		zigzag[57][0] = 4;   zigzag[57][1] = 7;
		
		zigzag[58][0] = 5;   zigzag[58][1] = 7;	
		zigzag[59][0] = 6;   zigzag[59][1] = 6;
		zigzag[60][0] = 7;   zigzag[60][1] = 5;
		
		zigzag[61][0] = 7;   zigzag[61][1] = 6;
		zigzag[62][0] = 6;   zigzag[62][1] = 7;
		
		zigzag[63][0] = 7;   zigzag[63][1] = 7;
		
		return zigzag;
	}

	public static int[][] generateZigzagPattern2() {
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
	
	public static void printArray(double[][] F) {
		int X = F.length;
		int Y = F[0].length;
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++) {
				System.out.format("%.1f", F[x][y]);
				//System.out.print(F[x][y] + "  ");
				System.out.print("\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public static void printArray(int[][] F){
		int X = F.length;
		int Y = F[0].length;
		for(int y = 0; y < Y; y++){
			for(int x = 0; x < X; x++){
				//System.out.format("%.1d", F[u][v]);
				System.out.print(F[x][y] + "  ");
				System.out.print("\t");
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
					//System.out.format("%.1d", F[u][v]);
					System.out.print(F[x][y][m] + "  ");
					System.out.print("\t");
				}
				System.out.println("");
			}	
			System.out.println("");
		}
		System.out.println("");
	}

	
	
	public static double[][][] padImageArray(double[][][] origImg, int padX, int padY) {
		int orgX = origImg.length;
		int orgY = origImg[0].length;
		int padWX = orgX + padX;
		int padHY = orgY + padY;
		double[][][] padImg = new double[padWX][padHY][3];
		for(int y = 0; y < padHY; y++){
			for(int x = 0; x < padWX; x++){
				if(x < orgX && y < orgY){
					padImg[x][y][0] = origImg[x][y][0];
					padImg[x][y][1] = origImg[x][y][1];
					padImg[x][y][2] = origImg[x][y][2];
				} else {
					padImg[x][y][0] = 0;
					padImg[x][y][1] = 0;
					padImg[x][y][2] = 0;
				}
			}
		}
		return padImg;
	}
	
	public static double[][][] unPadImageArray(double[][][] origImg, int padX, int padY) {
		int orgX = origImg.length;
		int orgY = origImg[0].length;
		int padWX = orgX - padX;
		int padHY = orgY - padY;
		double[][][] unPadImg = new double[padWX][padHY][3];
		for(int y = 0; y < padHY; y++){
			for(int x = 0; x < padWX; x++){
				unPadImg[x][y][0] = origImg[x][y][0];
				unPadImg[x][y][1] = origImg[x][y][1];
				unPadImg[x][y][2] = origImg[x][y][2];	
			}
		}
		return unPadImg;
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
	
	public static double Cfunc(double x) {
		if(x == 0)
			return Math.sqrt(2.0) / 2.0;
		else
			return 1.0;
	}

	
	public static double[][] quantizeLuma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] YquantTable = getYquantTable();
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = Math.round(input[x1][y1]/(YquantTable[x][y] * Math.pow(2, n)));
					}
				}
			}
		}
		return output;
	}

	
	public static double[][] deQuantizeLuma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] YquantTable = getYquantTable();
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = Math.round(input[x1][y1] * (YquantTable[x][y] * Math.pow(2, n)));
					}
				}
			}
		}
		return output;
	}
	
	public static double[][] quantizeChroma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] CromeQuant = getCromeQuant();
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = Math.round(input[x1][y1]/(CromeQuant[x][y] * Math.pow(2, n)));
					}
				}
			}
		}
		return output;
	}
	
	public static double[][] deQuantizeChroma(double[][] input, int n){
		int x, y;
		int sizeX = input.length;
		int sizeY = input[0].length;
		double[][] output = new double[sizeX][sizeY];
		double[][] CromeQuant = getCromeQuant();
		
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = (input[x1][y1] * (CromeQuant[x][y] * Math.pow(2, n)));
					}
				}
			}
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
	
	public static double[] blockToSequence(double[][] input) {
		double[] output = new double[64];
		int[][] zigzag = generateZigzagPattern();	
		for(int i = 0; i < 64; i++){
			output[i] = input[ (zigzag[i][0]) ][ (zigzag[i][1]) ];
		}	
		return output;
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
	
	public static double[][] getYquantTable() {
		double[][] table = new double[8][8];
		
		table[0][0] = 4;
		table[1][0] = 4;
		table[2][0] = 4;
		table[3][0] = 8;
		table[4][0] = 8;
		table[5][0] = 16;
		table[6][0] = 16;
		table[7][0] = 32;
		
		table[0][1] = 4;
		table[1][1] = 4;
		table[2][1] = 4;
		table[3][1] = 8;
		table[4][1] = 8;
		table[5][1] = 16;
		table[6][1] = 16;
		table[7][1] = 32;
		
		table[0][2] = 4;
		table[1][2] = 4;
		table[2][2] = 8;
		table[3][2] = 8;
		table[4][2] = 16;
		table[5][2] = 16;
		table[6][2] = 32;
		table[7][2] = 32;
		
		table[0][3] = 8;
		table[1][3] = 8;
		table[2][3] = 8;
		table[3][3] = 16;
		table[4][3] = 16;
		table[5][3] = 32;
		table[6][3] = 32;
		table[7][3] = 32;
		
		table[0][4] = 8;
		table[1][4] = 8;
		table[2][4] = 16;
		table[3][4] = 16;
		table[4][4] = 32;
		table[5][4] = 32;
		table[6][4] = 32;
		table[7][4] = 32;
		
		table[0][5] = 16;
		table[1][5] = 16;
		table[2][5] = 16;
		table[3][5] = 32;
		table[4][5] = 32;
		table[5][5] = 32;
		table[6][5] = 32;
		table[7][5] = 32;
		
		table[0][6] = 16;
		table[1][6] = 16;
		table[2][6] = 32;
		table[3][6] = 32;
		table[4][6] = 32;
		table[5][6] = 32;
		table[6][6] = 32;
		table[7][6] = 32;
		
		table[0][7] = 32;
		table[1][7] = 32;
		table[2][7] = 32;
		table[3][7] = 32;
		table[4][7] = 32;
		table[5][7] = 32;
		table[6][7] = 32;
		table[7][7] = 32; 
		
		return table;
	}
	
	public static double[][] getCromeQuant() {
		double[][] table = new double[8][8];
		
		table[0][0] = 8;
		table[1][0] = 8;
		table[2][0] = 8;
		table[3][0] = 16;
		table[4][0] = 32;
		table[5][0] = 32;
		table[6][0] = 32;
		table[7][0] = 32;
		
		table[0][1] = 8;
		table[1][1] = 8;
		table[2][1] = 8;
		table[3][1] = 16;
		table[4][1] = 32;
		table[5][1] = 32;
		table[6][1] = 32;
		table[7][1] = 32;
		
		table[0][2] = 8;
		table[1][2] = 8;
		table[2][2] = 16;
		table[3][2] = 32;
		table[4][2] = 32;
		table[5][2] = 32;
		table[6][2] = 32;
		table[7][2] = 32;
		
		table[0][3] = 16;
		table[1][3] = 16;
		table[2][3] = 32;
		table[3][3] = 32;
		table[4][3] = 32;
		table[5][3] = 32;
		table[6][3] = 32;
		table[7][3] = 32;
		
		table[0][4] = 32;
		table[1][4] = 32;
		table[2][4] = 32;
		table[3][4] = 32;
		table[4][4] = 32;
		table[5][4] = 32;
		table[6][4] = 32;
		table[7][4] = 32;
		
		table[0][5] = 32;
		table[1][5] = 32;
		table[2][5] = 32;
		table[3][5] = 32;
		table[4][5] = 32;
		table[5][5] = 32;
		table[6][5] = 32;
		table[7][5] = 32;
		
		table[0][6] = 32;
		table[1][6] = 32;
		table[2][6] = 32;
		table[3][6] = 32;
		table[4][6] = 32;
		table[5][6] = 32;
		table[6][6] = 32;
		table[7][6] = 32;
		
		table[0][7] = 32;
		table[1][7] = 32;
		table[2][7] = 32;
		table[3][7] = 32;
		table[4][7] = 32;
		table[5][7] = 32;
		table[6][7] = 32;
		table[7][7] = 32; 
		
		return table;
	}
	
	public static double[][][] gen16xBlockRed() {
		double[][][] redImg = new double[16][16][3];
		for(int y = 0; y < 16; y++){
			for(int x = 0; x < 16; x++){
				redImg[x][y][0] = 255;
				redImg[x][y][1] = 0;
				redImg[x][y][2] = 0;		
			}
		}
		return redImg;
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
