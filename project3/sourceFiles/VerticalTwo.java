import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Which player has more continuous pieces in vertical lines.
 */
public class VerticalTwo implements Feature{

    public VerticalTwo(){}

    @Override
    public String getFeatureName(){
        return "Vertical connected two + three";
    }

    @Override
    public int getFeature(ArrayList<Integer> board){
        Helper h = new Helper();
        return h.verticalN(h.arrListtoArr(board), 2) +
                h.verticalN(h.arrListtoArr(board), 3);
    }
}
