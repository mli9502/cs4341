import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Which player has more continuous pieces in horizontal lines.
 */
public class HorizontalTwo implements Feature{
    public HorizontalTwo(){}

    @Override
    public String getFeatureName(){
        return "Horizontal connected two + three";
    }

    @Override
    public int getFeature(ArrayList<Integer> board){
        Helper h = new Helper();
        return h.horizontalN(h.arrListtoArr(board), 2) +
                h.horizontalN(h.arrListtoArr(board), 3);
    }
}
