package foodapp.com.meltingpot;

/**
 * Created by kellytan on 9/6/15.
 */
public class MatchTool {

    private static MatchTool instance = new MatchTool();

    private MatchTool() {
    }

    public static MatchTool getInstance() {
        return instance;
    }
}
