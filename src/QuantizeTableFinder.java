import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;


public class QuantizeTableFinder {

	public static int imgOrigSizeX;
	public static int imgOrigSizeY;
	public static int padX;
	public static int padY;
	public static int sizeX;
	public static int sizeY;
	public static int chromaSizeX;
	public static int chromaSizeY;
	public static double[][][] origImgArray;
	public static double[][][] padArrayRGB;
	public static double[][][] arrayYCbCr;
	public static double[][] YarrayDCT;
	public static double[][] CbArrayDCT;
	public static double[][] CrArrayDCT;
	public static double[][] quantTable;
	

	public static void main(String[] args) {
		
		runInit();
		
		double[] q = new double[64];
//		for(int i = 0; i< 64; i++) {
//			q[i] = 4;
//		}
		
//		quantTable = getYquantTableAlt1();
//		
//		System.out.println("");
//		printArray(quantTable);
//		System.out.println("");
//		
//		int z = 0;
//		for(int y = 0; y < 8; y++){ 
//			for(int x = 0; x < 8; x++){
//				 q[z] = quantTable[x][y];
//				z++;
//			}
//		}
//		
//		System.out.println("first q");
//		printArray(q);
//		System.out.println("");
//		
//		long count = 0;
//		long count2 = 0;
//		
//		double test = 0.0;
		
//		while(test < 2048.0) {
//			q[0] *= 2;
//			
//			//shift
//			int i = 0;
//			while(q[i] > 32 && i < 63){
//				q[i] = 4;
//				q[i + 1] *= 2;
//				i++;
//			}
			
//			int n = 0;
//			for(int y = 0; y < 8; y++){ 
//				for(int x = 0; x < 8; x++){
//					quantTable[x][y] = q[n];
//					n++;
//				}
//			}
//			
//			printArray(quantTable);
//			
//		double costY = runCompresionRatioCalculation(0);
//			
//			if(count2 % 1000l == 0l){
//				count++;	
//			}
//			
//			if(count % 100l == 0l){
//			//	System.out.println("\ncostY = " + costY);
//				printArray(quantTable);
//				System.out.println("test = " + test);
//			}
//			
//			
//			if(costY == 133140.0){
//				System.out.println("found it");
//				printArray(quantTable);
//				//break;
//			}	
//			
//			q[0] *= 2;
//			
//			int m = 0;
//			for(int y = 0; y < 8; y++){ 
//				for(int x = 0; x < 8; x++){
//					quantTable[x][y] = q[m];
//					m++;
//				}
//			}
//			
//			printArray(quantTable);
//			
//			costY = runCompresionRatioCalculation(0);
//			
//			if(costY == 133140.0){
//				System.out.println("found it");
//				printArray(quantTable);
//				//break;
//			}	
//			
//			test = 0.0;
//			for(int m = 0; m< 64; m++) {
//				test += q[m];
//			}
			
			
//		}
		
//		System.out.println("finished");
		
	
		runCompresionRatioCalculation(0);	
		//runCompresionRatioCalculation(5);
		
	}
	

	
	
	public static void runInit() {
		File file = new File("Ducky.ppm");
		//int quality = 0;
		Image origImg;
		origImg = new Image(file.getAbsolutePath());
		//origImg.display(file.getName() + "-original image");
		
		// Get and store original size
		imgOrigSizeX = origImg.getW();
		imgOrigSizeY = origImg.getH();
		padX = 0;
		if(imgOrigSizeX % 8 > 0)
			padX = 8 - imgOrigSizeX % 8;
		padY = 0;
		if(imgOrigSizeY % 8 > 0)
			padY = 8 - imgOrigSizeY % 8;
		
		sizeX = imgOrigSizeX + padX;
		sizeY = imgOrigSizeY + padY;
		
		// Calculate size of Cb and Cr subsample image arrays, greater than (image size/2) and divisible by 8
		chromaSizeX = (sizeX / 2);
		if(chromaSizeX % 8 > 0)
			chromaSizeX += 8 - chromaSizeX % 8;
		
		chromaSizeY = (sizeY / 2);
		
		if(chromaSizeY % 8 > 0)
			chromaSizeY += 8 - chromaSizeY % 8;
		
		System.out.println("imgOrigSizeX = " + origImg.getW());
		System.out.println("imgOrigSizeY = " + origImg.getH());
		System.out.println("padX = " + padX);
		System.out.println("padY = " + padY);
		System.out.println("sizeX = " + sizeX);
		System.out.println("sizeY = " + sizeY);
		System.out.println("chromaSizeX = " + chromaSizeX);
		System.out.println("chromaSizeY = " + chromaSizeY);
		
		// Convert original image to an Array
		origImgArray = imageRGBtoDoubleArray(origImg);
		
		// Pad Array size to multiple of 8
		padArrayRGB = padImageArray(origImgArray, padX, padY);
		
		arrayYCbCr = new double[sizeX][sizeY][3];
		for(int y = 0; y < sizeY; y++){
			for(int x = 0; x < sizeX; x++){	
				//ycbcr[][][3] = convert(rgb[][][3])
				arrayYCbCr[x][y] = matrixRGBtoYCbCR(padArrayRGB[x][y]);
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
					
		// Perform the DCT for Y image, Cb image, and Cr image 
		YarrayDCT = runDCTconvertion(arrayY);
		CbArrayDCT = runDCTconvertion(subSampleCb);
		CrArrayDCT = runDCTconvertion(subSampleCr);
		
		
	}
	
		
	public static double runCompresionRatioCalculation(int quality){
	
		double[][] quantizedY = new double [sizeX][sizeY];
		double[][] quantizedCb = new double [chromaSizeX][chromaSizeY];
		double[][] quantizedCr = new double [chromaSizeX][chromaSizeY];
		
	
		// Quantize image components
		quantizedY = quantizeLuma(YarrayDCT, quality);
		quantizedCb = quantizeChroma(CbArrayDCT, quality);
		quantizedCr = quantizeChroma(CrArrayDCT, quality);
		
		
		// Calculate component compression costs
		double totalCost = 0;
		double countY = 0;
		double countCb = 0;
		double countCr = 0;
		double bitCostY = 0;
		double bitCostCb = 0;
		double bitCostCr = 0;
		double origCost = 0;
		
		//System.out.println("\nFor a quantization level n = " + quality);
		
		origCost = imgOrigSizeX * imgOrigSizeY * 24;
		//System.out.println("The original image cost, (S), is " + origCost);
		
		//loop through all the image 8x8 blocks and quantize the values
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				double[][] block = new double[8][8];
				for(int y = 0; y < 8; y++){
					for(int x = 0; x < 8; x++){
						block[x][y] = quantizedY[xx + x][yy + y];
					}
				}
		
				System.out.println("sizeX = " + sizeX )	;																//System.out.println("block = ");
			printArray(block);
				// Convert from 2D block to 1D sequence (zigzag)
				double[] sequence = blockToSequenceInvert(block);
				// Perform a run length encode of 1D sequence and count number of sequence pairs for the AC component
				bitCostY += (9 - quality);  // Adds the DC for that block
				countY = runLengthEncode(sequence); 
				bitCostY += (15 - quality) * countY;
			}
		}
		//System.out.println("The Y values cost is " + bitCostY + " bits.");
		

//		for(int xx = 0; xx < chromaSizeX; xx += 8){
//			for(int yy = 0; yy < chromaSizeY; yy += 8){
//				
//				double[][] block = new double[8][8];
//				for(int y = 0; y < 8; y++){
//					for(int x = 0; x < 8; x++){
//						block[x][y] = quantizedCb[xx + x][yy + y];
//					}
//				}
//				// Convert from 2D block to 1D sequence (zigzag)
//				double[] sequence = blockToSequenceInvert(block);
//				// Perform a run length encode of 1D sequence and count number of sequence pairs
//				bitCostCb += (8 - quality);
//				countCb = runLengthEncode(sequence);
//				bitCostCb += (14 - quality) * countCb;
//			}
//		}
//		//System.out.println("The Cb values cost is " + bitCostCb + " bits.");
//		
//
//		for(int xx = 0; xx < chromaSizeX; xx += 8){
//			for(int yy = 0; yy < chromaSizeY; yy += 8){
//				double[][] block = new double[8][8];
//				for(int y = 0; y < 8; y++){
//					for(int x = 0; x < 8; x++){
//						block[x][y] = quantizedCr[xx + x][yy + y];
//					}
//				}
//				// Convert from 2D block to 1D sequence (zigzag)
//				double[] sequence = blockToSequenceInvert(block);
//				// Perform a run length encode of 1D sequence and count number of sequence pairs
//				bitCostCr += (8 - quality);
//				countCr = runLengthEncode(sequence);
//				bitCostCr += (14 - quality) * countCr;
//			}
//		}
		//System.out.println("The Cr values cost is " + bitCostCr + " bits.");
		
		// Calculate total compression costs
		totalCost = bitCostY + bitCostCb + bitCostCr;
		//System.out.println("The total compressed image cost, (D), is " + totalCost + " bits.");
		
		//System.out.println("The compression ratio, (S/D), is " + origCost/totalCost);
		
		
		return bitCostY;
			
	}
	
	
	
	public static double[][][] imageRGBtoDoubleArray(Image img) {
		int imgX = img.getW();
		int imgY = img.getH();
		double[][][] imgArray = new double[imgX][imgY][3];
		int[] rgb = new int[3];
		for(int y = 0; y < imgY; y++){
			for(int x = 0; x < imgX; x++){
				img.getPixel(x, y, rgb);
				imgArray[x][y][0] = rgb[0];
				imgArray[x][y][1] = rgb[1];
				imgArray[x][y][2] = rgb[2];
			}
		}
		return imgArray;
	}
	
	public static Image arrayRGBtoImage (double[][][] imgArray) {
		int lenX = imgArray.length;
		int lenY = imgArray[0].length;
		Image retImg = new Image(lenX, lenY);
		for(int y = 0; y < lenY; y++){
			for(int x = 0; x < lenX; x++){
				int R = (int)Math.min(Math.max((Math.round(imgArray[x][y][0])), 0.0),  255);
				int G = (int)Math.min(Math.max((Math.round(imgArray[x][y][1])), 0.0),  255);
				int B = (int)Math.min(Math.max((Math.round(imgArray[x][y][2])), 0.0),  255);
				int[] rgb = {R, G, B};
				retImg.setPixel(x, y, rgb);
			}
		}
		return retImg;
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
		
		Y = Y - 128.0;
		Cb = Cb - 0.5;
		Cr = Cr - 0.5;
		
		Y = Math.max(Math.min(Y, 127.0), -128.0);
		Cb = Math.max(Math.min(Cb, 127.0), -128.0);
		Cr = Math.max(Math.min(Cr, 127.0), -128.0);
	
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
		//double[][] YquantTable = getYquantTableAlt1();  // debug
		double[][] YquantTable = getYquantTable();
		//printArray(YquantTable);
		for(int xx = 0; xx < sizeX; xx += 8){
			for(int yy = 0; yy < sizeY; yy += 8){
				
				for(int x1 = xx; x1 < xx + 8; x1++){
					x = x1 - xx;
					for(int y1 = yy; y1 < yy + 8; y1++){
						y = y1 - yy;

						output[x1][y1] = Math.round(input[x1][y1] / (YquantTable[x][y] * Math.pow(2, n)) );
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
		//double preValue = inSequence[0];   //breaks the code - make redblack work
		int cc = 1;
		int i = 0;
		for(i = 2; i < 64; i++){
		//for(i = 1; i < 64; i++){  //breaks the code - make redblack work
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
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
		}	
		return output;
	}
	
	public static double[] blockToSequenceInvert(double[][] input) {
		double[] output = new double[64];
		int[][] zigzag = generateZigzagPatternInvert();	
		for(int i = 0; i < 64; i++){
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
		}	
		return output;
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
				//System.out.format("%.1d", F[u][v]);
				System.out.print(F[x][y] + "  ");
				System.out.print("\t");
			}
			System.out.println("");
		}	
		System.out.println("");
	}
	
	public static int[][] generateZigzagPatternInvert() {
		int[][] zigzag = new  int[64][2]; 
		
		zigzag[0][0] = 0;  zigzag[0][1] = 0;
		
		zigzag[1][0] = 0;  zigzag[1][1] = 1;
		zigzag[2][0] = 1;  zigzag[2][1] = 0;
		
		zigzag[3][0] = 2;  zigzag[3][1] = 0;		
		zigzag[4][0] = 1;  zigzag[4][1] = 1;
		zigzag[5][0] = 0;  zigzag[5][1] = 2;
		
		zigzag[6][0] = 0;  zigzag[6][1] = 3;	
		zigzag[7][0] = 1;  zigzag[7][1] = 2;
		zigzag[8][0] = 2;  zigzag[8][1] = 1;
		zigzag[9][0] = 3;  zigzag[9][1] = 0;
		
		zigzag[10][0] = 4;  zigzag[10][1] = 0;		
		zigzag[11][0] = 3;  zigzag[11][1] = 1;
		zigzag[12][0] = 2;  zigzag[12][1] = 2;
		zigzag[13][0] = 1;  zigzag[13][1] = 3;
		zigzag[14][0] = 0;  zigzag[14][1] = 4;
		
		zigzag[15][0] = 0;  zigzag[15][1] = 5;		
		zigzag[16][0] = 1;  zigzag[16][1] = 4;
		zigzag[17][0] = 2;  zigzag[17][1] = 3;
		zigzag[18][0] = 3;  zigzag[18][1] = 2;
		zigzag[19][0] = 4;  zigzag[19][1] = 1;
		zigzag[20][0] = 5;  zigzag[20][1] = 0;
		
		zigzag[21][0] = 6;  zigzag[21][1] = 0;		
		zigzag[22][0] = 5;  zigzag[22][1] = 1;
		zigzag[23][0] = 4;  zigzag[23][1] = 2;
		zigzag[24][0] = 3;  zigzag[24][1] = 3;
		zigzag[25][0] = 2;  zigzag[25][1] = 4;
		zigzag[26][0] = 1;  zigzag[26][1] = 5;
		zigzag[27][0] = 0;  zigzag[27][1] = 6;
		
		zigzag[28][0] = 0;  zigzag[28][1] = 7;		
		zigzag[29][0] = 1;  zigzag[29][1] = 6;
		zigzag[30][0] = 2;  zigzag[30][1] = 5;
		zigzag[31][0] = 3;  zigzag[31][1] = 4;
		zigzag[32][0] = 4;  zigzag[32][1] = 3;
		zigzag[33][0] = 5;  zigzag[33][1] = 2;
		zigzag[34][0] = 6;  zigzag[34][1] = 1;
		zigzag[35][0] = 7;  zigzag[35][1] = 0;
		
		zigzag[36][0] = 7;  zigzag[36][1] = 1;
		zigzag[37][0] = 6;  zigzag[37][1] = 2;
		zigzag[38][0] = 5;  zigzag[38][1] = 3;
		zigzag[39][0] = 4;  zigzag[39][1] = 4;
		zigzag[40][0] = 3;  zigzag[40][1] = 5;
		zigzag[41][0] = 2;  zigzag[41][1] = 6;
		zigzag[42][0] = 1;  zigzag[42][1] = 7;
		
		zigzag[43][0] = 2;  zigzag[43][1] = 7;
		zigzag[44][0] = 3;  zigzag[44][1] = 6;
		zigzag[45][0] = 4;  zigzag[45][1] = 5;
		zigzag[46][0] = 5;  zigzag[46][1] = 4;
		zigzag[47][0] = 6;  zigzag[47][1] = 3;
		zigzag[48][0] = 7;  zigzag[48][1] = 2;
		
		zigzag[49][0] = 7;  zigzag[49][1] = 3;
		zigzag[50][0] = 6;  zigzag[50][1] = 4;
		zigzag[51][0] = 5;  zigzag[51][1] = 5;
		zigzag[52][0] = 4;  zigzag[52][1] = 6;
		zigzag[53][0] = 3;  zigzag[53][1] = 7;
		
		zigzag[54][0] = 4;  zigzag[54][1] = 7;
		zigzag[55][0] = 5;  zigzag[55][1] = 6;
		zigzag[56][0] = 6;  zigzag[56][1] = 5;
		zigzag[57][0] = 7;  zigzag[57][1] = 4;
		
		zigzag[58][0] = 7;  zigzag[58][1] = 5;
		zigzag[59][0] = 6;  zigzag[59][1] = 6;
		zigzag[60][0] = 5;  zigzag[60][1] = 7;
		
		zigzag[61][0] = 6;  zigzag[61][1] = 7;
		zigzag[62][0] = 7;  zigzag[62][1] = 6;
		
		zigzag[63][0] = 7;  zigzag[63][1] = 7;
		
		return zigzag;
	}

	public static int[][] generateZigzagPattern() {
		int[][] zigzag = new  int[64][2]; //x = 0; y = 1
		zigzag[0][0] = 0;
		zigzag[0][1] = 0;
		zigzag[1][0] = 1;
		zigzag[1][1] = 0;
		
		int x = 1;
		int y = 0;
		int index = 1;	
		
		while(true){
			while(x != 0){
				index++;
				x--;
				y++;
				zigzag[index][0] = x;
				zigzag[index][1] = y;
			}
			if(x == 0 && y == 7)
				break;
			index++;
			y++;
			zigzag[index][0] = x;
			zigzag[index][1] = y;
	
			while(y != 0 ){
				index++;
				x++;
				y--;
				zigzag[index][0] = x;
				zigzag[index][1] = y;
			}
			
			index++;
			x++;
			zigzag[index][0] = x;
			zigzag[index][1] = y;
	
		}
			
			index++;
			x++;
			zigzag[index][0] = x;
			zigzag[index][1] = y;
			
		while(true){
			while(x != 7){
				index++;
				x++;
				y--;
				zigzag[index][0] = x;
				zigzag[index][1] = y;
			}
			index++;
			y++;
			zigzag[index][0] = x;
			zigzag[index][1] = y;
			
			while(y != 7){
				index++;
				x--;
				y++;
				zigzag[index][0] = x;
				zigzag[index][1] = y;
			}
			index++;
			x++;
			zigzag[index][0] = x;
			zigzag[index][1] = y;
			
			if(x == 7 && y == 7)
				break;
		}
		return zigzag;
	}
	
	
//	public static double[][] getYquantTable() {
//		return quantTable;
//	}
	
	
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
	
	public static double[][] getYquantTableAlt1() {
		double[][] table = new double[8][8];
		
		table[0][0] = 2;
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
	
	 public void writeDataFile(String fileName)
	  // wrrite the image data in img to a PPM file
	  {
//		FileOutputStream fos = null;
//		PrintWriter dos = null;
//
//		try{
//			fos = new FileOutputStream(fileName);
//			dos = new PrintWriter(fos);
//
//			System.out.println("Writing the Image buffer into "+fileName+"...");
//
//			// write header
//			dos.print("P6"+"\n");
//			dos.print("#CS451"+"\n");
//			dos.print(width + " "+height +"\n");
//			dos.print(255+"\n");
//			dos.flush();
//
//			// write data
//			int x, y;
//			byte[] rgb = new byte[3];
//			for(y=0;y<height;y++)
//			{
//				for(x=0;x<width;x++)
//				{
//					getPixel(x, y, rgb);
//					fos.write(rgb[0]);
//					fos.write(rgb[1]);
//					fos.write(rgb[2]);
//
//				}
//				fos.flush();
//			}
//			dos.close();
//			fos.close();
//
//			System.out.println("Wrote into "+fileName+" Successfully.");
//
//		} // try
//		catch(Exception e)
//		{
//			System.err.println(e.getMessage());
//		}
	  }

}
