import java.util.ArrayList;

/**
 * @author: Yihong Zhou (yzhou8)
 *          Mengwen Li (mli2)
 * Interface for Feature.
 */
public interface Feature {
    /**
     * This function is used to get the return value of a feature.
     * @param boardState
     * @return The calculated return value for the feature.
     */
    public int getFeature(ArrayList<Integer> boardState);

    /**
     * This function is used to get the feature's name.
     * @return
     */
    public String getFeatureName();
}
