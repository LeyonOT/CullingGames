public class StaticActions {
    //---------- IDLE ----------
    public static final Action SIT_AND_THINK = new Action(
            "$char$ sits and thinks about life.",
            1,
            null,
            null,
            false,
            false);
    public static final Action SCAVENGE_CAMPFIRE = new Action(
            "$char$ scavenges through the ashes of a burnt campfire, hoping to find leftover supplies.",
            2,
            null,
            null,
            false,
            false);
    public static final Action MEDITATE = new Action(
            "$char$ takes a moment to meditate, reflecting on survival strategies.",
            -2,
            null,
            null,
            false,
            false);
    public static final Action RABBIT_STALKER = new Action(
            "$char$ sneaks up on a group of wild rabbits, trying to blend in with the surroundings to observe their behavior.",
            2,
            null,
            null,
            false,
            false);
    public static final Action WATER_GATHER = new Action(
            "$char$ drinks some rainwater, getting hydration for the day.",
            -8,
            null,
            null,
            false,
            false);
    public static final Action BACKPACK_SEARCH = new Action(
            "$char$ digs through an old backpack left behind, searching for useful items.",
            0,
            null,
            null,
            false,
            false);
    public static final Action SKYSEEING = new Action(
            "$char$ takes a moment to observe the sky, noting the weather patterns to predict rain or storms.",
            1,
            null,
            null,
            false,
            false);
    public static final Action BUSH_SCAVENGE = new Action(
            "$char$ rummages through the underbrush, hoping to find edible berries or roots.",
            3,
            null,
            null,
            false,
            false);
    public static final Action DANGER_SENSE = new Action(
            "$char$ listens intently for distant sounds of danger, trying to gauge the location of other competitors.",
            2,
            null,
            null,
            false,
            false);
    public static final Action STONE_THROWING = new Action(
            "$char$ finds a smooth stone and spends some time practicing throwing it into a nearby pond.",
            1,
            null,
            null,
            false,
            false);
    public static final Action CRY = new Action(
            "$char$ breaks down crying.",
            8,
            null,
            null,
            false,
            false);
    public static final Action PARANOID = new Action(
            "$char$ thinks someone is following them and hides behind some bushes.",
            1,
            null,
            null,
            false,
            false);
    public static final Action RUN = new Action(
            "$char$ takes a jog around the field but finds nothing.",
            10,
            null,
            null,
            false,
            false);

    public static final Action STRETCH = new Action(
            "$char$ stretches their muscles to keep them in shape.",
            5,
            null,
            null,
            false,
            false);

    public static final Action FIND_BERRIES = new Action(
            "$char$ finds some berries and eats them.",
            -10,
            null,
            null,
            false,
            false);

    public static final Action SING = new Action(
            "$char$ sings a song to keep their spirits up.",
            2,
            null,
            null,
            false,
            false);

    public static final Action FIDGET = new Action(
            "$char$ fidgets nervously due to anxiety.",
            1,
            null,
            null,
            false,
            false);


    //---------- FIND ----------
    public static final Action FIND_MAP_1 = new Action(
            "$char$ stumbles upon a Map hidden in some rubble.",
            1,
            null,
            Item.MAP,
            false,
            false);
    public static final Action FIND_MAP_2 = new Action(
            "$char$ picks up an old, dusty book from the ground and finds a Map between its pages.",
            1,
            null,
            Item.MAP,
            false,
            false);
    public static final Action FIND_MAP_3 = new Action(
            "$char$ draws a Map based on a dream he had some time ago.",
            1,
            null,
            Item.MAP,
            false,
            false);
    public static final Action FIND_ROPE_1 = new Action(
            "$char$ stumbles upon a Rope hidden in some rubble.",
            2,
            null,
            Item.ROPE,
            false,
            false);
    public static final Action FIND_ROPE_2 = new Action(
            "$char$ finds an abandoned building and decides to take a Rope off of it's entrance.",
            2,
            null,
            Item.ROPE,
            false,
            false);



    //---------- CRAFT ----------
    public static final Action CRAFT_ROPE_1 = new Action(
            "$char$ rips off their torn clothes to make a Rope with.",
            5,
            null,
            Item.ROPE,
            false,
            false);
    public static final Action CRAFT_ROPE_2 = new Action(
            "$char$ finds some sturdy grass and picks it to make a Rope with.",
            5,
            null,
            Item.ROPE,
            false,
            false);
    public static final Action SHIELD_CRAFT = new Action(
            "$char$ uses their insane genius, few loose wooden boards they found and a Rope to craft a shield.",
            4,
            Item.ROPE,
            Item.SHIELD,
            true,
            false);



    //---------- USE ROPE ----------
    public static final Action TREE_CLIMB = new Action(
            "$char$ uses a makeshift rope to climb a tree, scanning the area for potential dangers.",
            4,
            Item.ROPE,
            null,
            false,
            false);



    //---------- USE MAP ----------
    public static final Action MAP_SEARCH_FOOD = new Action(
            "$char$ uses their Map to find a camp with some leftovers.",
            4,
            Item.MAP,
            Item.FOOD,
            true,
            false);
    public static final Action MAP_SEARCH_CHEST = new Action(
            "$char$ uses their Map to find a chest buried under some sand and mud.",
            7,
            Item.MAP,
            Item.randomItem(4),
            true,
            false);
    public static final Action MAP_SEARCH_RUINS = new Action(
            "$char$ uses their Map to find lost ruins with some loot in them.",
            8,
            Item.MAP,
            Item.randomItem(4),
            true,
            false);
    public static final Action MAP_SEARCH_FOOD_2 = new Action(
            "$char$ uses their Map to find what looks like an abandoned altar with some offerings still on it.",
            4,
            Item.MAP,
            Item.FOOD,
            true,
            true);
    public static final Action MAP_SEARCH_GUN = new Action(
            "$char$ uses their Map to find a Gun buried in ground under an old tree.",
            3,
            Item.MAP,
            Item.GUN,
            true,
            false);
}
