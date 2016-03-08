import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Which player has more continuous pieces in vertical lines and horizontal lines.
 */
public class Combine implements Feature {
    public Combine(){}

    @Override
    public String getFeatureName(){
        return "Horizontal + Vertical";
    }

    @Override
    public int getFeature(ArrayList<Integer> board){
        Helper h = new Helper();
        return h.horizontalN(h.arrListtoArr(board), 2) +
                h.horizontalN(h.arrListtoArr(board), 3) +
                h.verticalN(h.arrListtoArr(board), 2) +
                h.verticalN(h.arrListtoArr(board), 3);
    }
}
