import java.util.*;

public class TeamAction extends Action {
    protected int minMembers;
    private List<Integer> highestItemCheckList;
    private String[] descriptions;
    private int[] satGain, diff, genderMods, wounds;
    private Item[][] itemRequired, itemRewards;
    private boolean[] itemUsed;
    private List<AbstractMap.SimpleEntry<Item, Integer>> itemMods;
    private Trait[] traitMods;
    private Personality[] personalityPlusMods, personalityMinusMods;

    /*
    descriptions {"action a","action b","action c"} +
    diff {1,2,3} +
    satGain {-5,5,10} +

    itemRequired {null,null,{Item.AXE, Item.LADDER}} +
    itemUsed {false} +

    itemMods { {1, gun}, {1, sword}, {2, shield}, {2, helmet} }
    traitMods { STRONG, FAST}
    personalityPlusMods { RESOURCEFUL }
    personalityMinusMods { IMPULSIVE }
    genderMods {1,0} +

    itemRewards {null,{Item.FOOD},{Item.ROPE, Item.FOOD}} +
    wounds {1,0,0} +
     */


    public TeamAction(String[] descriptions, int minMembers,
                      int[] diff, int[] satGain,
                      Item[][] itemRequired,
                      boolean[] itemUsed,
                      List<AbstractMap.SimpleEntry<Item, Integer>> itemMods,
                      Trait[] traitMods,
                      Personality[] personalityPlusMods, Personality[] personalityMinusMods,
                      int[] genderMods,
                      Item[][] itemRewards,
                      int[] wounds
                      ) {
        super();
        this.minMembers = minMembers;
        this.descriptions = descriptions;
        this.diff = diff;
        this.satGain = satGain;
        this.itemRequired = itemRequired;
        this.itemUsed = itemUsed;
        this.itemMods = itemMods;
        this.traitMods = traitMods;
        this.personalityPlusMods = personalityPlusMods;
        this.personalityMinusMods = personalityMinusMods;
        this.genderMods = genderMods;
        this.itemRewards = itemRewards;
        this.wounds = wounds;
        this.highestItemCheckList = new ArrayList<>();
    }

    public boolean isAccessible(Team team) {
        return team.getActionMembers().size() >= minMembers;
    }


    public void perform(Team team) {
        if (isAccessible(team)) {
            List<Character> actionMembers = team.getActionMembers();
            int base =  actionMembers.size();
            int modifier = 0;
            List<Integer> highestItemCheckList = new ArrayList<>();
            for (Character c : actionMembers) {
                modifier += c.getGender().equals("Male") ? genderMods[0] : genderMods[1];
                for (Trait t : traitMods) {
                    if (c.getTrait().equals(t)) {
                        modifier++;
                        break;
                    }
                }
                for (Personality p : personalityPlusMods) {
                    modifier += c.hasPersonality(p) ? 1 : 0;
                }
                for (Personality p : personalityMinusMods) {
                    modifier -= c.hasPersonality(p) ? 1 : 0;
                }
                for (AbstractMap.SimpleEntry<Item, Integer> se : itemMods) {
                    if (highestItemCheckList.contains(se.getValue())) continue;
                    if (c.hasItem(se.getKey())) {
                        modifier++;
                        highestItemCheckList.add(se.getValue());
                    }
                }
            }

            List<Integer> indexesOfMembers = new ArrayList<>();
            List<Item> itemsToRemove = new ArrayList<>();

            int i;
            for (i = diff.length-1; i >0 ; i--) {
                if ((base+modifier) < diff[i]) continue; //przejscie przez difficulties

                if (itemRequired[i] != null) {
                    int itemsPossessed = 0;
                    for (Item ireq : itemRequired[i]) {  //sprawdzenie itemow w danym diff
                        for (Character c : actionMembers) {
                            if (c.hasItem(ireq)) {
                                itemsToRemove.add(ireq);
                                indexesOfMembers.add(actionMembers.indexOf(c));
                                itemsPossessed++;
                                break;
                            }
                        }
                    }
                    if (itemsPossessed < itemRequired[i].length) {
                        indexesOfMembers.clear();
                        itemsToRemove.clear();
                        continue;
                    }
                }
                break;
            }

            System.out.println(replaceVal(descriptions[i], actionMembers));  //show description

            if (itemUsed[i]){                                                //remove items if they were used
                for (Item itx : itemsToRemove) {
                    for (int ix : indexesOfMembers) {
                        if (actionMembers.get(ix).removeItem(itx))
                            break;
                    }
                }
            }
            if (i>0) {
                for (AbstractMap.SimpleEntry<Item, Integer> se : itemMods) {
                    if (se.getValue() < 0 && highestItemCheckList.contains(se.getValue())) {
                        for (Character c : actionMembers) {
                            c.removeItem(se.getKey());
                        }
                    }
                }
            }

            if (itemRewards[i] != null) {                                   //reward items
                for (Item irew : itemRewards[i]) {
                    Character minValCharacter = team.getValueMember(false, true);
                    minValCharacter.addItem(irew);
                }
            }

            for (int j = wounds[i]; j > 0 ; j--) {                          //take wounds
                actionMembers.get(new Random().nextInt(actionMembers.size())).takeWound();
            }
            if (wounds[i]<0){
                System.out.println(getTeamNames(actionMembers) + " heal all their wounds!");
                for (Character c:actionMembers) {
                    c.healWound();
                }
            }
            actionMembers.removeIf(c -> !c.isAlive());
            for (Character c:actionMembers) {
                c.setActionLock(true);
            }

            System.out.println(getTeamNames(actionMembers) + " " + (satGain[i]>=0?"gained ":"lost ") + Math.abs(satGain[i]) + " saturation.");
            for (Character c:actionMembers) {
                c.setActionLock(false);
            }

            System.out.println();
        } else { //nie powinno si enigdy polyadic
            System.out.println(team.getName() + " Team can't perform this action");
        }
    }

    protected String replaceVal(String s, List<Character> t) {
        return s.replace("$team1$", getTeamNames(t.subList(0, t.size()/2)))
                .replace("$team2$", getTeamNames(t.subList(t.size()/2, t.size())))
                .replace("$team$", getTeamNames(t))
                .replace("$num1$", (t.size()>3)?"":"s")
                .replace("$num2$", (t.size()>2)?"":"s");
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
