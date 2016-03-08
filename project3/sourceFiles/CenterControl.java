import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 * Which player has more pieces in the center columns.
 */
public class CenterControl implements Feature {

    public CenterControl(){}

    @Override
    public String getFeatureName(){
        return "CenterControl";
    }

    @Override
    public int getFeature(ArrayList<Integer> board){
        Helper h = new Helper();
        int[][] boardArr = h.arrListtoArr(board);
        int p1Cnt = 0, p2Cnt = 0;
        // Row.
        for(int i = 0; i < 6; i ++){
            // Column.
            for(int j = 2; j < 5; j ++){
                if(boardArr[i][j] == 1){
                    p1Cnt ++;
                }else if(boardArr[i][j] == 2){
                    p2Cnt ++;
                }
            }
        }
        return p1Cnt - p2Cnt;
    }
}
