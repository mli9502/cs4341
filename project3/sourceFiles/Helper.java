import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Helper class includes helper functions used.
 */
public class Helper {
    /**
     * Function used to convert boar arraylist to board array.
     * @param board
     * @return
     */
    public static int[][] arrListtoArr(ArrayList<Integer> board){
        int rtnArr[][] = new int[6][7];
        // col
        for(int i = 0; i < 7; i ++){
            // row
            for(int j = 5; j >= 0; j --){
                rtnArr[j][i] = board.get(i * 6 + 5 - j);
            }
        }
        System.out.println("********************************************");
        for(int i = 0; i < 6; i ++){
            for(int j = 0; j < 7; j ++){
                System.out.print(rtnArr[i][j]);
            }
            System.out.println();
        }
        System.out.println("********************************************");
        return rtnArr;
    }

    /**
     *
     * @param board
     * @param n: number of connected pieces would like to find.
     * @return returns the difference of n connected pieces, which can be added to n + 1 vertically on the next move,
     *          between player 1 and player 2.
     */
    public static int verticalN(int[][] board, int n){
        if(n > 4){
            return 0;
        }
        if(n == 1){
            int fpCnt = 0;
            int spCnt = 0;
            // col
            for(int i = 0; i < 7; i ++){
                // row
                for(int j = 0; j < 6; j ++){
                    if(board[j][i] != 0){
                        if(j < 2){
                            break;
                        }
                        if(board[j][i] == 1){
                            // System.out.println("i is " + i + " j is " + j);
                            fpCnt ++;
                            break;
                        }
                        else{
                            // System.out.println("i is " + i + " j is " + j);
                            spCnt ++;
                            break;
                        }
                    }
                }
            }
            System.out.println("fpCnt is " + fpCnt + " spCnt is " + spCnt);
            return fpCnt - spCnt;
        }
        int fpCnt = 0;
        int spCnt = 0;
        // col
        for(int i = 0; i < 7; i ++){
            // row
            for(int j = 5; j >= 0; j --){
                if(j <= n){
                    break;
                }
                if(board[j][i] == 1){
                    // Check for 3 connected player 1 piece.
                    int k;
                    for(k = 0; k < n; k ++){
                        // System.out.println("i is " + i + " j is " + j + " k is " + k);
                        if(board[j - k][i] != 1){
                            break;
                        }
                    }
                    // System.out.println("After k loop, k is " + k);
                    if(k == n){
                        // If there are 3 connected player 1 piece, check whether it is a valid move.
                        if(board[j - n][i] == 0){
                            // System.out.println("Increase fpCnt");
                            fpCnt ++;
                        }
                        j -= n;
                        continue;
                    }
                }
                if(board[j][i] == 2){
                    // Check for 3 connected player 1 piece.
                    int k;
                    for(k = 0; k < n; k ++){
                        // System.out.println("i is " + i + " j is " + j + " k is " + k);
                        if(board[j - k][i] != 2){
                            break;
                        }
                    }
                    // System.out.println("After k loop, k is " + k);
                    if(k == n){
                        // If there are 3 connected player 1 piece, check whether it is a valid move.
                        if(board[j - n][i] == 0){
                            // System.out.println("Increase spCnt");
                            spCnt ++;
                        }
                        j -= n;
                        continue;
                    }
                }
            }
        }
        // System.out.println("fpCnt is " + fpCnt + " spCnt is " + spCnt);
        return fpCnt - spCnt;
    }

    /**
     *
     * @param board
     * @return returns the difference of n connected pieces, which can be added to a live n + 1 horizontally on the next move,
     *          between player 1 and player 2.
     */
    public static int horizontalN(int[][] board, int n){
        int p1Cnt = 0;
        int p2Cnt = 0;
        // row
        for(int i = 5; i >= 0; i --){
            // col
            for(int j = 0; j < 7; j ++){
                if(j >= 7 - n){
                    break;
                }
                int oneCnt = 0;
                int twoCnt = 0;
                int zeroCnt = 0;
                int zeroPos = 0;
                for(int k = 0; k < n + 1; k ++){
                    if(board[i][j + k] == 1){
                        oneCnt ++;
                    }else if(board[i][j + k] == 2){
                        twoCnt ++;
                    }else{
                        zeroCnt ++;
                        zeroPos = j + k;
                    }
                }
                if((oneCnt == n && zeroCnt == 1) ||
                        (twoCnt == n && zeroCnt == 1)){
                    /**
                     * Handles the condition like the following:
                     * 0000000
                     * 0000000
                     * 0000000
                     * 0000000
                     * 0021010
                     * 0121020
                     */
                    if((i < 5 && board[i + 1][zeroPos] != 0) ||
                            i == 5){
                        /**
                         * Handles the condition like the following:
                         * 0000000
                         * 0020000
                         * 0010000
                         * 0020000
                         * 0010000
                         * 0112200
                         */
                        if(oneCnt == n) {
                            if(j == 0){
                                // System.out.println("j is 0");
                                if(board[i][j + n + 1] == 0){
                                    // System.out.println("Increase p1Cnt");
                                    p1Cnt ++;
                                    if(zeroPos != j){
                                        j = zeroPos;
                                    }
                                }
                            }else if(j + n == 6){
                                // System.out.println("j + 2 == 6");
                                if(board[i][j - 1] == 0){
                                    p1Cnt ++;
                                    if(zeroPos != j){
                                        j = zeroPos;
                                    }
                                }
                            }else{
                                // System.out.println("else");
                                if(board[i][j - 1] == 0 || board[i][j + n + 1] == 0){
                                    p1Cnt ++;
                                    if(zeroPos != j){
                                        j = zeroPos;
                                    }
                                }
                            }
                        }else{
                            if(j == 0){
                                if(board[i][j + n + 1] == 0){
                                    p2Cnt ++;
                                    if(zeroPos != j){
                                        j = zeroPos;
                                    }
                                }
                            }else if(j + n == 6){
                                if(board[i][j - 1] == 0){
                                    p2Cnt ++;
                                    if(zeroPos != j){
                                        j = zeroPos;
                                    }
                                }
                            }else{
                                // System.out.println("j is " + j);
                                if(board[i][j - 1] == 0 || board[i][j + n + 1] == 0){
                                    // System.out.println("increase");
                                    p2Cnt ++;
                                    if(zeroPos != j){
                                        j = zeroPos;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // System.out.println("p1Cnt is " + p1Cnt + " p2Cnt is " + p2Cnt);
        return p1Cnt - p2Cnt;
    }
}
