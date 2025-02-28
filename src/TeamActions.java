import java.util.AbstractMap;
import java.util.List;

public class TeamActions {
    private TeamActions() {

    }

    /*public static final TeamAction SETUP_CAMP = new TeamAction(
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
    );*/

    //JULKA +1 ; KUBA +2 ; LINA +1 ; POCHODNIA + 1; LUCKY + 1
    //6

    /*public static final TeamAction EXPLORE_CAVE = new TeamAction(
            new String[]{"$team$ attempt to explore a cave, but find nothing.",
                    "$team1$ find$num1$ a cave, and $team2$ decide to quickly explore it.",
                    "$team$ delve deep into a cave, discovering a hidden stash of items."},
            3,
            new int[]{0, 5, 8},
            new int[]{-1, -6, -15},
            new Item[][]{null, null, {Item.TORCH}},
            new boolean[]{false, false, true},
            List.of(new AbstractMap.SimpleEntry<>(Item.TORCH, 1), new AbstractMap.SimpleEntry<>(Item.ROPE, 2)),
            new Trait[]{Trait.LUCKY},
            new Personality[]{Personality.BRAVE},
            new Personality[]{Personality.CAUTIOUS},
            new int[]{1, 0},
            new Item[][]{null, {Item.HERBS}, {Item.FOOD, Item.AXE, Item.HERBS}},
            new int[]{0, 0, 0}
    );*/

    //NOT USED ANYWHERE -- ONLY WORKS AS A CREATOR
    /*
    descriptions {"action a","action b","action c"} +
    diff {1,2,3} +
    satGain {-5,5,10} +

    itemRequired {null,null,{Item.AXE, Item.LADDER}} +
    itemUsed {false, false, false} +

    itemMods { {1, gun}, {1, sword}, {2, shield}, {2, helmet} }
    traitMods { STRONG, FAST}
    personalityPlusMods { RESOURCEFUL }
    personalityMinusMods { IMPULSIVE }
    genderMods {1,0} +

    itemRewards {null,{Item.FOOD},{Item.ROPE, Item.FOOD}} +
    wounds {1,0,0} +
     */

    //""~""~""|0|0,0,0|0,0,0|null;null;null|false,false,false|ROCK,1;ROCK,1|SMART|RESOURCEFUL|IMPULSIVE|0,0|null;null;null|0,0,0

    //3+ Min member only for BIG VALUE ACTIONS
    //negative  numbers in item mod for item removal after the actiom

    public static final TeamAction TEST = new TeamAction(
            new String[]{"$team$ attempt to scout the area, but get lost.",
                    "$team$ manage to scout a small area and find some Food.",
                    "$team1$ successfully scout$num1$ a large area and $team2$ collect found loot."},
            2, // Minimum members (usually 2)
            new int[]{0, 3, 5}, // Difficulties
            new int[]{0, 2, 5}, // Saturation gain
            new Item[][]{null, null, {Item.MAP}}, // Required items
            new boolean[]{false, false, false}, // Items used
            List.of(new AbstractMap.SimpleEntry<>(Item.COMPASS, 1), new AbstractMap.SimpleEntry<>(Item.MAP, 2)), //Item modifiers
            new Trait[]{Trait.LUCKY, Trait.SMART}, // Trait modifiers
            new Personality[]{Personality.TREACHEROUS}, // Positive personality modifiers
            new Personality[]{}, // Negative personality modifiers
            new int[]{0, 1}, // Gender modifiers
            new Item[][]{null, {Item.FOOD}, {Item.FOOD}}, // Rewards
            new int[]{0, 0, 0} // Wounds
    );

}
