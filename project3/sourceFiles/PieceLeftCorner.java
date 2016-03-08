import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Which player occupies the left corner of the board.
 */
public class PieceLeftCorner implements Feature {

    public PieceLeftCorner(){}

    @Override
    public int getFeature(ArrayList<Integer> boardState) {
        int rtn = boardState.get(0);
        return rtn;
    }

    @Override
    public String getFeatureName(){
        return "PieceLeftCorner";
    }
}
