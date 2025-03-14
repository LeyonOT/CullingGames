import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LongActions {
    private LongActions() {

    }
    public static final LongAction LOG_ENCOUNTER = new LongAction.Builder(
            "$char$ encounters a big log blocking the path.",
            "$pronoun1$ easily gets over the log",
            "$pronoun1$ struggles and loses a lot of energy trying to get over the log.",
            8
    ).withModifiers(action -> {
        action.addGenderModifier("Male", 1);
        action.addTraitModifier(Trait.STRONG, 3);
        action.addTraitModifier(Trait.LUCKY, 1);
        action.addItemModifier(Item.AXE, 4);
    }).withWinEffects(character -> {
        System.out.println((character.hasItem(Item.AXE)?" by cutting it in half with "+character.getPron(true)+" Axe.":"."));
        character.reduceSaturation(5);
    }).withLoseEffects(character -> {
        character.reduceSaturation(18);
    }).withCriticalLoss(character -> {
        String s = character.getPron(false);
        s = s.toUpperCase().charAt(0) + s.substring(1);
        System.out.println(s + " catches on a sharp branch while trying to get over the log and wounds "+character.getPron(true)+" leg.");
        character.takeWound();
        character.reduceSaturation(23);
        System.out.println();
    }).build();

    public static final LongAction RIVER_CROSSING = new LongAction.Builder(
            "$char$ reaches a river and needs to cross it.",
            "$pronoun1$ finds a shallow part of the river and crosses it easily.",
            "$pronoun1$ tries to swim across the river but gets tired and struggles.",
            6
    ).withModifiers(action -> {
        action.addTraitModifier(Trait.FAST, 2);
        action.addTraitModifier(Trait.LUCKY, 1);
    }).withWinEffects(character -> {
        System.out.println();
        character.reduceSaturation(4);
    }).withLoseEffects(character -> {
        character.reduceSaturation(21);
    }).withCriticalLoss(character -> {
        String s = character.getPron(false);
        s = s.toUpperCase().charAt(0) + s.substring(1);
        System.out.println(s + " gets caught in a strong current and is swept away.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(43);
        System.out.println();
    }).build();

    public static final LongAction FRUIT_TREE_CLIMB = new LongAction.Builder(
            "$char$ sees a fruit tree and decides to climb it.",
            "$pronoun1$ climbs the tree",
            "$pronoun1$ climbs the tree and finds nothing edible.",
            5
    ).withModifiers(action -> {
        action.addTraitModifier(Trait.STRONG, 1);
        action.addTraitModifier(Trait.FAST, 1);
        action.addItemModifier(Item.ROPE, 2);
        action.addItemModifier(Item.LADDER, 4);
    }).withWinEffects(character -> {
        int satReduction = 0;
        boolean hasLadder = character.hasItem(Item.LADDER);
        boolean hasRope = character.hasItem(Item.ROPE);
        if (hasLadder) {
            satReduction = 15;
            System.out.print(" using a Ladder");
        }
        else if (hasRope) {
            satReduction = 10;
            System.out.print(" using a Rope");
        }
        System.out.print(" and finds");
        if (character.getTrait() == Trait.LUCKY) {
            System.out.println(" two ripe fruits.");
            if (!hasLadder && hasRope) character.removeItem(Item.ROPE);
            character.addItem(Item.FOOD);
            character.reduceSaturation(-10);
        }
        else {
            System.out.println(" a ripe fruit.");
            if (!hasLadder && hasRope) character.removeItem(Item.ROPE);
            character.addItem(Item.FOOD);
            character.reduceSaturation(15-satReduction);
        }
    }).withLoseEffects(character -> {
        character.reduceSaturation(15);
    }).withCriticalLoss(character -> {
        String s = character.getPron(false);
        s = s.toUpperCase().charAt(0) + s.substring(1);
        System.out.println(s + " falls from the tree and breaks "+character.getPron(true)+" arm.");
        character.takeWound();
        character.reduceSaturation(15);
        System.out.println();
    }).build();

    public static final LongAction FIND_TREASURE_1 = new LongAction.Builder(
            "$char$ finds a hidden treasure chest.",
            "$pronoun1$ opens the chest and finds",
            "$pronoun1$ opens the chest but it's empty.",
            6
    ).withModifiers(action -> {
        action.addTraitModifier(Trait.LUCKY, 3);
    }).withWinEffects(character -> {
        Random random = new Random();
        int reward = random.nextInt(10) + 1;
        reward = character.getTrait().equals(Trait.LUCKY) ? reward+2 : reward;
        reward = Math.min(10, reward);
        switch (reward) {
            case 10:
                System.out.println(" a Sword.");
                character.addItem(Item.SWORD);
                break;
            case 4,5,6,7,8,9:
                System.out.println(" a Rope.");
                character.addItem(Item.ROPE);
                break;
            case 1,2,3:
                System.out.println(" a Rock.");
                character.addItem(Item.ROCK);
                break;
        }
        character.reduceSaturation(5);
    }).withLoseEffects(character -> {
        character.reduceSaturation(5);
    }).withCriticalLoss(character -> {
        String s = character.getPron(false);
        s = s.toUpperCase().charAt(0) + s.substring(1);
        System.out.println(s + " puts one of "+character.getPron(true)+" items in it and forgets to take it back.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(0);
        System.out.println();
    }).build();

    public static final LongAction FIND_TREASURE_2 = new LongAction.Builder(
            "$char$ uses $pronoun2$ Map to find a hidden treasure chest.",
            "$pronoun1$ opens the chest and finds",
            "$pronoun1$ opens the chest but it's empty.",
            6
    ).withRequiredItem(
            Item.MAP
    ).withModifiers(action -> {
        action.addTraitModifier(Trait.LUCKY, 3);
    }).withWinEffects(character -> {
        Random random = new Random();
        int reward = random.nextInt(10) + 1;
        reward = character.getTrait().equals(Trait.LUCKY) ? reward+2 : reward;
        reward = Math.min(10, reward);
        switch (reward) {
            case 10:
                System.out.println(" a Shield.");
                character.removeItem(Item.MAP);
                character.addItem(Item.SHIELD);
                break;
            case 4,5,6,7,8,9:
                System.out.println(" a Helmet.");
                character.removeItem(Item.MAP);
                character.addItem(Item.HELMET);
                break;
            case 1,2,3:
                System.out.println(" a Rock.");
                character.removeItem(Item.MAP);
                character.addItem(Item.ROCK);
                break;
        }
        character.reduceSaturation(5);
    }).withLoseEffects(character -> {
        character.removeItem(Item.MAP);
        character.reduceSaturation(5);
    }).withCriticalLoss(character -> {
        character.removeItem(Item.MAP);
        String s = character.getPron(false);
        s = s.toUpperCase().charAt(0) + s.substring(1);
        System.out.println(s + " puts one of "+character.getPron(true)+" items in it and forgets to take it back.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(0);
        System.out.println();
    }).build();

    public static final LongAction FIND_TREASURE_3 = new LongAction.Builder(
            "$char$ finds a hidden treasure chest.",
            "$pronoun1$ opens the chest and finds",
            "$pronoun1$ opens the chest but it's empty.",
            6
    ).withModifiers(action -> {
        action.addTraitModifier(Trait.LUCKY, 3);
    }).withWinEffects(character -> {
        Random random = new Random();
        int reward = random.nextInt(10) + 1;
        reward = character.getTrait().equals(Trait.LUCKY) ? reward+2 : reward;
        reward = Math.min(10, reward);
        switch (reward) {
            case 10:
                System.out.println(" a Gun.");
                character.addItem(Item.GUN);
                break;
            case 4,5,6,7,8,9:
                System.out.println(" a Compass.");
                character.addItem(Item.COMPASS);
                break;
            case 1,2,3:
                System.out.println(" a Rock.");
                character.addItem(Item.ROCK);
                break;
        }
        character.reduceSaturation(5);
    }).withLoseEffects(character -> {
        character.reduceSaturation(5);
    }).withCriticalLoss(character -> {
        String s = character.getPron(false);
        s = s.toUpperCase().charAt(0) + s.substring(1);
        System.out.println(s + " puts one of "+character.getPron(true)+" items in it and forgets to take it back.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(0);
        System.out.println();
    }).build();

    public static final LongAction BEAR_ATTACK = new LongAction.Builder(
            "$char$ gets suddenly attacked by a bear.",
            "$pronoun1$ wins without severe injuries by",
            "$pronoun1$ somehow escapes after long struggle and few wounds.",
            10
    ).withModifiers(action -> {
        action.addTraitModifier(Trait.STRONG, 1);
        action.addTraitModifier(Trait.FAST, 1);
        action.addTraitModifier(Trait.SMART, 1);

        action.addHighestItemModifier(Item.GUN, 3, 1);
        action.addHighestItemModifier(Item.SWORD, 2, 1);
        action.addHighestItemModifier(Item.AXE, 1, 1);

        action.addItemModifier(Item.SHIELD, 2);
        action.addItemModifier(Item.HELMET, 1);
        action.addItemModifier(Item.TORCH, 1);
    }).withWinEffects(character -> {
        Map<Item, String> winDescriptions = new HashMap<>();
        winDescriptions.put(Item.GUN, " shooting the bear with a Gun");
        winDescriptions.put(Item.SWORD, " slaying the bear with a Sword");
        winDescriptions.put(Item.AXE, " fighting off the bear with an Axe");

        boolean itemUsed = false;
        for (Map.Entry<Item, String> entry : winDescriptions.entrySet()) {
            if (character.hasItem(entry.getKey())) {
                System.out.print(entry.getValue());
                itemUsed = true;
                break;
            }
        }

        if (!itemUsed) System.out.print(" defeating the bear with "+character.getPron(true)+" bare hands");

        if (character.hasItem(Item.SHIELD)) {
            System.out.println(", but "+character.getPron(true)+" Shield gets broken in the process.");
            character.removeItem(Item.SHIELD);
        } else System.out.println(".");

        character.addItem(Item.FOOD);
        character.addItem(Item.FOOD);
        character.reduceSaturation(18);
    }).withLoseEffects(character -> {
        character.takeWound();
        character.reduceSaturation(31);
    }).withCriticalLoss(character -> {
        String s = character.getPron(false);
        s = s.toUpperCase().charAt(0) + s.substring(1);
        System.out.println(s + " gets mauled to death by the bear before "+character.getPron(false)+" can even react.");
        character.takeWound();
        character.takeWound();
        System.out.println();
    }).build();


}
