import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 *
 * Which player can connect more live 2-pieces vertically.
 */
public class VerticalOne implements Feature{
    public VerticalOne(){}

    @Override
    public String getFeatureName(){
        return "Vertical connected one";
    }

    @Override
    public int getFeature(ArrayList<Integer> board){
        Helper h = new Helper();
        return h.verticalN(h.arrListtoArr(board), 1);
    }
}
