import java.util.AbstractMap;
import java.util.List;

public class TeamActions {
    private TeamActions() {

    }

    public static final TeamAction SETUP_CAMP = new TeamAction(
            new String[]{"$team$ try to setup a small camp, but they fail.",
                    "$team1$ set$num1$ up a small camp with some help of $team2$.",
                    "$team1$ set$num1$ up a small camp, while $team2$ gather$num2$ some resources."},
            2,
            new int[]{0, 2, 5},
            new int[]{0, 5, 13},
            new Item[][]{null, null, null},
            new boolean[]{false, false, false},
            List.of(new AbstractMap.SimpleEntry<>(Item.COMPASS, 1), new AbstractMap.SimpleEntry<>(Item.ROPE, 1)),
            new Trait[]{Trait.SMART},
            new Personality[]{Personality.RESOURCEFUL},
            new Personality[]{Personality.IMPULSIVE},
            new int[]{0,0},
            new Item[][]{null, null, {Item.FOOD}},
            new int[]{0,0,0}
    );

}
