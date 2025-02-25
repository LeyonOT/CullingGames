import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class TeamActions {
    private TeamActions() {

    }

    public static final TeamAction SETUP_CAMP = new TeamAction(
            "$team1$ setups a camp while $team2$ gathers some resources",
            2
    );

}
