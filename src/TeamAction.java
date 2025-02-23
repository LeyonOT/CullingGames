import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeamAction extends Action {
    protected int minMembers;

    public TeamAction(String description, int minMembers) {
        super(description, -15, null, null, false, false);
        this.minMembers = minMembers;
    }

    public boolean isAccessible(Team team) {
        return team.getActionMembers().size() >= minMembers;
    }

    public void perform(Team team) {
        if (isAccessible(team)) {
            System.out.println();
            System.out.println(replaceVal(description, team));
            for (Character c: team.getActionMembers()) {
                c.reduceSaturation(saturationCost);
            }
        } else { //nie powinno si enigdy polyadic
            System.out.println(team.getName() + " Team can't perform this action");
        }
    }

    protected String replaceVal(String s, Team t) {
        return s.replace("$team1$", getTeamNames(t.getActionMembers().subList(0, t.getActionMembers().size()/2)))
                .replace("$team2$", getTeamNames(t.getActionMembers().subList(t.getActionMembers().size()/2, t.getActionMembers().size())));
    }

    private String getTeamNames(List<Character> lc){
        StringBuilder s = new StringBuilder();

        s.append(lc.get(0).getName());

        if (lc.size() == 1) return s.toString();
        if (lc.size() == 2) {
            s.append(" and ").append(lc.get(1).getName());
            return s.toString();
        }

        for (int i = 1; i < lc.size()-1; i++) {
            s.append(", ").append(lc.get(0).getName());
        }
        s.append(" and ").append(lc.get(lc.size() - 1).getName());
        return s.toString();
    }
}
