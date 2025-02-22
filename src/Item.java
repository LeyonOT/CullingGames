import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item {
    private String name;
    private int slotSize;
    private int priority;
    protected static List<Item> listOfItems = new ArrayList<>();

    public Item(String name, int slotSize, int priority) {
        this.name = name;
        this.slotSize = slotSize;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return name + " (" + slotSize + " slots)";
    }

    public static Item findItem(String name) {
        if (name.toLowerCase().startsWith("random")) {
            return randomItem(Integer.parseInt(name.substring(6)));
        }
        for (Item item : listOfItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        throw new IllegalArgumentException();
    }

    //Declarations
    public static Item RANDOM = new Item("Random", 0, 0); //RANDOM FOR CHARACTER EQUIPMENT
    public static Item TORCH = new Item("Torch", 1, 1);

    static {
        listOfItems.add(TORCH);
    }

    //Implementation TODO
    public static Item COMPASS = new Item("Compass", 1, 1);

    static {
        listOfItems.add(COMPASS);
    }

    //Implementation TODO
    public static Item SLEEPING_BAG = new Item("Sleeping Bag", 2, 1);

    static {
        listOfItems.add(SLEEPING_BAG);
    }

    public static Item LADDER = new Item("Ladder", 2, 3);

    static {
        listOfItems.add(LADDER);
    }

    public static Item ROPE = new Item("Rope", 1, 2);

    static {
        listOfItems.add(ROPE);
    }

    public static Item MAP = new Item("Map", 1, 4);

    static {
        listOfItems.add(MAP);
    }

    public static Item ROCK = new Item("Rock", 1, 0);

    static {
        listOfItems.add(ROCK);
    }

    public static Item SWORD = new Item("Sword", 1, 4);

    static {
        listOfItems.add(SWORD);
    }

    public static Item AXE = new Item("Axe", 2, 3);

    static {
        listOfItems.add(AXE);
    }

    public static Item HAMMER = new Item("Hammer", 1, 2);

    static {
        listOfItems.add(HAMMER);
    }

    public static Item HELMET = new Item("Helmet", 1, 3);

    static {
        listOfItems.add(HELMET);
    }

    public static Item SHIELD = new Item("Shield", 2, 4);

    static {
        listOfItems.add(SHIELD);
    }

    public static Item GUN = new Item("Gun", 1, 5);

    static {
        listOfItems.add(GUN);
    }

    public static Item BOW = new Item("Bow", 2, 3);

    static {
        listOfItems.add(BOW);
    }

    public static Item FOOD = new Item("Food", 1, 4);

    static {
        listOfItems.add(FOOD);
    }


    //RANDOM FOR RANDOM ITEM FROM THE LIST OF SAID PRIORITY
    public static Item randomItem(int maxPriority) {
        Random random = new Random();
        int currentRange = listOfItems.size();
        List<Item> tempItems = new ArrayList<>(listOfItems);

        while (currentRange > 0) {
            int itemIndex = random.nextInt(currentRange);
            Item selectedItem = tempItems.get(itemIndex);

            if (itemIndex <= maxPriority) {
                return selectedItem;
            } else {
                currentRange--;
                tempItems.set(itemIndex, tempItems.get(currentRange));
            }
        }

        return Item.ROCK;
    }
}
