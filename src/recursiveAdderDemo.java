import java.util.ArrayList;

public class recursiveAdderDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int[] arr = new int[16];
		int n = 0;

		incrementArray(arr, n);

	}

	

	public static void incrementArray(int[] arr, int n) {
		if (n > 14)
			return;
		if (arr[n] < 3) {
			arr[n]++;
			printArray(arr);
			incrementArray(arr, n + 1);
		} else {
			arr[n] = 0;
			// arr[n + 1]++;
			// incrementArray(arr, n);
			incrementArray(arr, n + 1);
			printArray(arr);
		}

	}

	public static void printArray(int[] F) {
		int X = F.length;
		for (int x = 0; x < X; x++) {
			// System.out.format("%.1d", F[u][v]);
			System.out.print(F[x] + "  ");
			System.out.print("\t");
		}
		System.out.println("");
	}
}
