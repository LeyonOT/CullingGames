import java.util.Random;

public class LongActions {
    private LongActions() {

    }
    public static final LongAction LOG_ENCOUNTER = new LongAction.Builder(
            "$char$ encounters a big log blocking the path.",
            "$char$ easily gets over the log",
            "$char$ struggles and loses a lot of energy trying to get over the log.",
            8
    ).withModifiers(action -> {
        action.addGenderModifier("Male", 1);
        action.addTraitModifier(Trait.STRONG, 3);
        action.addTraitModifier(Trait.LUCKY, 1);
        action.addItemModifier(Item.AXE, 4);
    }).withWinEffects(character -> {
        System.out.println((character.hasItem(Item.AXE)?" by cutting it in half with their Axe.":"."));
        character.reduceSaturation(5);
    }).withLoseEffects(character -> {
        character.reduceSaturation(18);
    }).withCriticalLoss(character -> {
        System.out.println(character.getName() + " catches on a sharp branch while trying to get over the log and wounds their leg.");
        character.reduceSaturation(23);
        character.takeWound();
    }).build();

    public static final LongAction RIVER_CROSSING = new LongAction.Builder(
            "$char$ reaches a river and needs to cross it.",
            "$char$ finds a shallow part of the river and crosses it easily.",
            "$char$ tries to swim across the river but gets tired and struggles.",
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
        System.out.println(character.getName() + " gets caught in a strong current and is swept away.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(43);
    }).build();

    public static final LongAction FRUIT_TREE_CLIMB = new LongAction.Builder(
            "$char$ sees a fruit tree and decides to climb it.",
            "$char$ climbs the tree",
            "$char$ climbs the tree and finds nothing edible.",
            5
    ).withModifiers(action -> {
        action.addTraitModifier(Trait.STRONG, 1);
        action.addTraitModifier(Trait.FAST, 1);
        action.addItemModifier(Item.ROPE, 2);
        action.addItemModifier(Item.LADDER, 4);
    }).withWinEffects(character -> {
        int satReduction = 0;
        if (character.hasItem(Item.LADDER)) {
            satReduction = 15;
            System.out.print(" using a ladder");
        }
        else if (character.hasItem(Item.ROPE)) {
            satReduction = 10;
            System.out.print(" using a rope");
        }
        System.out.print(" and finds");
        if (character.getTrait() == Trait.LUCKY) {
            System.out.println(" two ripe fruits.");
            if (character.hasItem(Item.ROPE)) character.removeItem(Item.ROPE);
            character.addItem(Item.FOOD);
            character.reduceSaturation(-10);
        }
        else {
            System.out.println(" a ripe fruit.");
            if (character.hasItem(Item.ROPE)) character.removeItem(Item.ROPE);
            character.addItem(Item.FOOD);
            character.reduceSaturation(15-satReduction);
        }
    }).withLoseEffects(character -> {
        character.reduceSaturation(15);
    }).withCriticalLoss(character -> {
        System.out.println(character.getName() + " falls from the tree and breaks their arm.");
        character.reduceSaturation(15);
        character.takeWound();
    }).build();

    public static final LongAction FIND_TREASURE_1 = new LongAction.Builder(
            "$char$ finds a hidden treasure chest.",
            "$char$ opens the chest and finds",
            "$char$ opens the chest but it's empty.",
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
                System.out.println(" a sword.");
                character.addItem(Item.SWORD);
                break;
            case 4,5,6,7,8,9:
                System.out.println(" a rope.");
                character.addItem(Item.ROPE);
                break;
            case 1,2,3:
                System.out.println(" a rock.");
                character.addItem(Item.ROCK);
                break;
        }
        character.reduceSaturation(5);
    }).withLoseEffects(character -> {
        character.reduceSaturation(5);
    }).withCriticalLoss(character -> {
        System.out.println(character.getName() + " puts one of their items in it and forgets to take it back.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(0);
    }).build();

    public static final LongAction FIND_TREASURE_2 = new LongAction.Builder(
            "$char$ uses their map to find a hidden treasure chest.",
            "$char$ opens the chest and finds",
            "$char$ opens the chest but it's empty.",
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
                System.out.println(" a shield.");
                character.removeItem(Item.MAP);
                character.addItem(Item.SHIELD);
                break;
            case 4,5,6,7,8,9:
                System.out.println(" a helmet.");
                character.removeItem(Item.MAP);
                character.addItem(Item.HELMET);
                break;
            case 1,2,3:
                System.out.println(" a rock.");
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
        System.out.println(character.getName() + " puts one of their items in it and forgets to take it back.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(0);
    }).build();

    public static final LongAction FIND_TREASURE_3 = new LongAction.Builder(
            "$char$ finds a hidden treasure chest.",
            "$char$ opens the chest and finds",
            "$char$ opens the chest but it's empty.",
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
                System.out.println(" a gun.");
                character.addItem(Item.GUN);
                break;
            case 4,5,6,7,8,9:
                System.out.println(" a compass.");
                character.addItem(Item.COMPASS);
                break;
            case 1,2,3:
                System.out.println(" a rock.");
                character.addItem(Item.ROCK);
                break;
        }
        character.reduceSaturation(5);
    }).withLoseEffects(character -> {
        character.reduceSaturation(5);
    }).withCriticalLoss(character -> {
        System.out.println(character.getName() + " puts one of their items in it and forgets to take it back.");
        character.removeItem(Item.RANDOM);
        character.reduceSaturation(0);
    }).build();


}
