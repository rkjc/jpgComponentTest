/*
 **************************************************************************
 *
 *   Forward Discrete Cosine Transform (FDCT)
 *
 **************************************************************************
 */
import java.io.*;

public class DCT_2 {

//  public static void main(String[] args) {
//    final int N = 8;  // Block size
//    int     nrows, ncols, m, n, x, y, u, v, img[][];
//    double  in[][], dct[][], sum, au, av;
//    double  n1=Math.sqrt(1.0/N), n2=Math.sqrt(2.0/N);
//
//    if (args.length != 4) {
//      System.out.println("Usage: Dct <nrows> <ncols> <in_img> <dct_file>");
//      System.exit(0);
//    }
//    nrows = Integer.parseInt(args[0]);
//    ncols = Integer.parseInt(args[1]);
//    if (nrows%N!=0 || ncols%N!=0) {
//      System.out.println("Nrows and ncols should be multiples of 8");
//      System.exit(0);
//    }
//    img = new int[nrows][ncols];
//    ArrayIO.readByteArray(args[2], img, nrows, ncols);
//    in = new double[nrows][ncols];
//    dct = new double[nrows][ncols];
//
//    // For each NxN block[m,n]
//    for (m=0; m<nrows; m+=N) {
//      for (n=0; n<ncols; n+=N) {
//
//        // For each pixel[u,v] in block[m,n]
//        for (u=m; u<m+N; u++) {
//          au = (u==m)? n1: n2;
//          for (v=n; v<n+N; v++) {
//            av = (v==n)? n1: n2;
//
//            // Sum up all pixels in the block
//            for (x=m, sum=0; x<m+N; x++) {
//              for (y=n; y<n+N; y++) {
//                in[x][y] = img[x][y] - 128.0;  // Subtract by 128
//                sum += in[x][y] * Math.cos((2*(x-m)+1)*(u-m)*Math.PI/(2*N)) *
//                                  Math.cos((2*(y-n)+1)*(v-n)*Math.PI/(2*N));
//              }
//            }
//	    dct[u][v] = au * av * sum;
//
//          } // for v
//        } // for u
//
//      }  // for n
//    }  // for m
//
//    ArrayIO.writeDoubleArray(args[3], dct, nrows, ncols);
//  }

}  // End class Dct

