
public class RunLengthEncodeDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		int[][] zigzag = generateZigzagPattern();
		
	
		double[][] block = getCromeQuant();
		
		printArray(block, 8);
		
		double[] sequence = blockToSequence(block);
		
		for(int i = 0; i < 64; i++){
			System.out.print(sequence[i] + "  ");
		}
		int count = runLengthEncode(sequence);
		System.out.println("\npair count = " + count);
			
		
	}
	
	public static int runLengthEncode(double[] inSequence){
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
			output[i] = input[zigzag[i][0]][zigzag[i][1]];
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
	
	public static double[][] getCromeQuant() {
		double[][] tab = new double[8][8];
		
		tab[0][0] = 8;
		tab[1][0] = 8;
		tab[2][0] = 8;
		tab[3][0] = 16;
		tab[4][0] = 32;
		tab[5][0] = 32;
		tab[6][0] = 32;
		tab[7][0] = 32;
		
		tab[0][1] = 8;
		tab[1][1] = 8;
		tab[2][1] = 8;
		tab[3][1] = 16;
		tab[4][1] = 32;
		tab[5][1] = 32;
		tab[6][1] = 32;
		tab[7][1] = 32;
		
		tab[0][2] = 8;
		tab[1][2] = 8;
		tab[2][2] = 16;
		tab[3][2] = 32;
		tab[4][2] = 32;
		tab[5][2] = 32;
		tab[6][2] = 32;
		tab[7][2] = 32;
		
		tab[0][3] = 16;
		tab[1][3] = 16;
		tab[2][3] = 32;
		tab[3][3] = 32;
		tab[4][3] = 32;
		tab[5][3] = 32;
		tab[6][3] = 32;
		tab[7][3] = 32;
		
		tab[0][4] = 32;
		tab[1][4] = 32;
		tab[2][4] = 32;
		tab[3][4] = 32;
		tab[4][4] = 32;
		tab[5][4] = 32;
		tab[6][4] = 32;
		tab[7][4] = 32;
		
		tab[0][5] = 32;
		tab[1][5] = 32;
		tab[2][5] = 32;
		tab[3][5] = 32;
		tab[4][5] = 32;
		tab[5][5] = 32;
		tab[6][5] = 32;
		tab[7][5] = 32;
		
		tab[0][6] = 32;
		tab[1][6] = 32;
		tab[2][6] = 32;
		tab[3][6] = 32;
		tab[4][6] = 32;
		tab[5][6] = 32;
		tab[6][6] = 32;
		tab[7][6] = 32;
		
		tab[0][7] = 32;
		tab[1][7] = 32;
		tab[2][7] = 32;
		tab[3][7] = 32;
		tab[4][7] = 32;
		tab[5][7] = 32;
		tab[6][7] = 32;
		tab[7][7] = 32; 
		
		return tab;
	}
}
