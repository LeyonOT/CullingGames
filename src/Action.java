import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Action {
    protected String description;
    protected int saturationCost;
    protected Item requiredItem,rewardItem;
    protected boolean consumedItem, applyWound;

    public Action(String description, int saturationCost, Item requiredItem, Item rewardItem, boolean consumedItem, boolean applyWound) {
        this.description = description;
        this.saturationCost = saturationCost;
        this.requiredItem = requiredItem;
        this.consumedItem = consumedItem;
        this.applyWound = applyWound;
        this.rewardItem = rewardItem;
    }
    public boolean isAccessible(Character character) {
        if (requiredItem == null) return true;
        return character.hasItem(requiredItem);
    }
    public void perform(Character character) {
        if (isAccessible(character)) {
            System.out.println();
            System.out.println(replaceVal(description, character));
            applyEffects(character);
            System.out.println();
        } else { //nie powinno si enigdy pojawic
            System.out.println(character.getName() + " can't perform this action due to lack of required item.");
        }
    }
    protected void applyEffects(Character character) {
        character.reduceSaturation(saturationCost);
        if (consumedItem) character.removeItem(requiredItem);
        if (rewardItem != null) character.addItem(rewardItem);
        if (applyWound) character.takeWound();
    }

    protected String replaceVal(String s, Character c) {
        return s.replace("$char$", c.getName())
                .replace("$pronoun2$", c.getPron(true))
                .replace("$pronoun1$", c.getPron(false));
    }
}

class LongAction extends Action {
    private int baseDifficulty;
    private String loseDesc, winDesc;
    private int maleMod, femaleMod;
    private final EffectApplier winEffectApplier;
    private final EffectApplier loseEffectApplier;
    private final EffectApplier criticalLoss;
    private Map<Trait, Integer> traitModifiers;
    private Map<Item, Integer> itemModifiers;
    private Map<Integer, Map<Item, Integer>> highestItemModifiers;

    public LongAction(String description, String winDesc, String loseDesc, int baseDifficulty, Item requiredItem, EffectApplier winEffectApplier, EffectApplier loseEffectApplier, EffectApplier criticalLoss) {
        super(description, 0, requiredItem, null, false, false);
        this.baseDifficulty = baseDifficulty;
        this.winDesc = winDesc;
        this.loseDesc = loseDesc;

        this.winEffectApplier = winEffectApplier;
        this.loseEffectApplier = loseEffectApplier;
        this.criticalLoss = criticalLoss;

        this.traitModifiers = new HashMap<>();
        this.itemModifiers = new HashMap<>();
        this.highestItemModifiers = new HashMap<>();
        this.maleMod = 0;
        this.femaleMod = 0;
    }

    @FunctionalInterface
    public interface ModifierApplier {
        void apply(LongAction action);
    }

    @FunctionalInterface
    public interface EffectApplier {
        void apply(Character character);
    }

    public static class Builder {
        private String description;
        private String winDesc;
        private String loseDesc;
        private int baseDifficulty;
        private Item requiredItem;
        private ModifierApplier modifierApplier;
        private EffectApplier winEffectApplier;
        private EffectApplier loseEffectApplier;
        private EffectApplier criticalLoss;

        public Builder(String description, String winDesc, String loseDesc, int baseDifficulty) {
            this.description = description;
            this.winDesc = winDesc;
            this.loseDesc = loseDesc;
            this.baseDifficulty = baseDifficulty;
            this.modifierApplier = action -> {};
            this.winEffectApplier = character -> {};
            this.loseEffectApplier = character -> {};
            this.criticalLoss = character -> {};
        }

        public Builder withRequiredItem(Item item) {
            this.requiredItem = item;
            return this;
        }

        public Builder withModifiers(ModifierApplier applier) {
            this.modifierApplier = applier;
            return this;
        }

        public Builder withWinEffects(EffectApplier applier) {
            this.winEffectApplier = applier;
            return this;
        }

        public Builder withLoseEffects(EffectApplier applier) {
            this.loseEffectApplier = applier;
            return this;
        }
        public Builder withCriticalLoss(EffectApplier applier) {
            this.criticalLoss = applier;
            return this;
        }

        public LongAction build() {
            LongAction action = new LongAction(description, winDesc, loseDesc, baseDifficulty, requiredItem, winEffectApplier, loseEffectApplier, criticalLoss);
            modifierApplier.apply(action);
            return action;
        }
    }
    @Override
    public void perform(Character character) {
        if (!isAccessible(character)) { //nie powinno sie nigdy pojawic
            System.out.println(character.getName() + " can't perform this action due to lack of required item.");
            return;
        }

        int roll = new Random().nextInt(13);
        int modifier = calculateModifier(character);
        int result = roll + modifier;

        System.out.println();
        System.out.println(replaceVal(description, character));
        System.out.println("Roll: " + roll + ", Modifier: " + modifier + ", Total: " + result + " | Difficulty: " + baseDifficulty);

        if (roll == 0) {
            System.out.println("Critical failure!");
            criticalLoss.apply(character);
            return;
        }

        if (result >= baseDifficulty) {
            System.out.print(replaceVal(winDesc, character));
            winEffectApplier.apply(character);
        } else {
            System.out.println(replaceVal(loseDesc, character));
            loseEffectApplier.apply(character);
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



    private int calculateModifier(Character character) {
        int modifier = 0;

        for (Map.Entry<Trait, Integer> entry : traitModifiers.entrySet()) {
            if (character.getTrait() == entry.getKey()) {
                modifier += entry.getValue();
            }
        }

        for (Map.Entry<Item, Integer> entry : itemModifiers.entrySet()) {
            if (character.hasItem(entry.getKey())) {
                modifier += entry.getValue();
            }
        }

        //Z grupy itemow o danym ID dodawany jest najwiekszy modyfikator z przedmiotu, ktory posiada postac
        for (Map<Item, Integer> group : highestItemModifiers.values()) {
            int highestItemModifier = 0;
            for (Map.Entry<Item, Integer> entry : group.entrySet()) {
                if (character.hasItem(entry.getKey())) {
                    highestItemModifier = Math.max(-12, entry.getValue());
                }
            }
            modifier += highestItemModifier;
        }

        if (character.getGender().equals("Male"))
            modifier += maleMod;
        else
            modifier += femaleMod;

        return modifier;
    }
}
