import java.util.*;

public class TeamAction extends Action {
    protected int minMembers;
    private int maleMod, femaleMod;
    private Map<Trait, Integer> traitModifiers;
    private Map<Item, Integer> itemModifiers;
    private Map<Integer, Map<Item, Integer>> highestItemModifiers;

    public TeamAction(String description, int minMembers) {
        super(description, 0, null, null, false, false);
        this.minMembers = minMembers;
    }

    public boolean isAccessible(Team team) {
        return team.getActionMembers().size() >= minMembers;
    }

    public void perform(Team team) {
        if (isAccessible(team)) {

            System.out.println(replaceVal(description, team));

            for (Character c: team.getActionMembers()) {
                c.reduceSaturation(saturationCost);
            }
            System.out.println();
        } else { //nie powinno si enigdy polyadic
            System.out.println(team.getName() + " Team can't perform this action");
        }
    }

    protected String replaceVal(String s, Team t) {
        return s.replace("$team1$", getTeamNames(t.getActionMembers().subList(0, t.getActionMembers().size()/2)))
                .replace("$team2$", getTeamNames(t.getActionMembers().subList(t.getActionMembers().size()/2, t.getActionMembers().size())))
                .replace("$num1$", (t.getActionMembers().size()>3)?"":"s")
                .replace("$num2$", (t.getActionMembers().size()>2)?"":"s");
    }

    @FunctionalInterface
    public interface ModifierApplier {
        void apply(TeamAction action);
    }

    @FunctionalInterface
    public interface EffectApplier {
        void apply(Character character);
    }

    public static class Builder {
        private String description;
        private int minMembers;
        private TeamAction.ModifierApplier modifierApplier;

        public Builder(String description, int minMembers) {
            this.description = description;
            this.minMembers = minMembers;
            this.modifierApplier = action -> {};
        }

        public TeamAction build() {
            TeamAction action = new TeamAction(description, minMembers);
            modifierApplier.apply(action);
            return action;
        }
    }

    public void addTraitModifier(Trait trait, int modifier) {
        traitModifiers.put(trait, modifier);
    }

    public void addItemModifier(Item item, int modifier) {
        itemModifiers.put(item, modifier);
    }
    public void addHighestItemModifier(Item item, int modifier, int groupId) {
        highestItemModifiers.computeIfAbsent(groupId, k -> new HashMap<>()).put(item, modifier);
    }

    public void addGenderModifier(String gender, int modifier) {
        if (gender.equals("Male"))
            maleMod = modifier;
        else
            femaleMod = modifier;
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
