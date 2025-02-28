import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ActionLoader {
    public static List<Action> loadActions(String simpleActionsFile, String advancedActionsFile, String moddedActionsFile) {
        List<Action> actions = new ArrayList<>();

        try {
            actions.addAll(loadActionsFromFile(simpleActionsFile));
            actions.addAll(loadLongActionsFromFile(advancedActionsFile));
            actions.addAll(loadActionsFromFile(moddedActionsFile));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return actions;
    }
    public static List<TeamAction> loadActions(String teamActionsFile) {
        List<TeamAction> actions = new ArrayList<>();

        try {
            actions.addAll(loadTeamActionsFromFile(teamActionsFile));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return actions;
    }

    private static List<Action> loadLongActionsFromFile(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName))
                .filter(line -> !line.startsWith("--"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(ActionLoader::tryGetActionByName)
                .filter(Objects::nonNull) //nie bierze pod uwage akcji ktore nie istnieja
                .collect(Collectors.toList());
    }
    private static List<TeamAction> loadTeamActionsFromFile(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName))
                .filter(line -> !line.startsWith("--"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(ActionLoader::createTeamAction)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    private static List<Action> loadActionsFromFile(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName))
                .filter(line -> !line.startsWith("--"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(ActionLoader::createAction)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Action createAction(String line) {
        String[] parts = line.split("\"");
        if (parts.length < 2) {
            System.out.println("Warning: Invalid action format: " + line);
            return null;
        }

        String description = parts[1].trim();
        String arguments = (parts.length > 2) ? parts[2].trim() : "";
        String[] argumentsSplit = arguments.split("\\s+");

        if (argumentsSplit.length != 5) {
            System.out.println("Warning: Invalid arguments for action: " + line);
            return null;
        }
        try {
            int saturationCost = Integer.parseInt(argumentsSplit[0]);
            Item requiredItem = assignItem(argumentsSplit[1]);
            Item rewardItem = assignItem(argumentsSplit[2]);
            boolean consumedItem = Boolean.parseBoolean(argumentsSplit[3]);
            boolean applyWound = Boolean.parseBoolean(argumentsSplit[4]);

            return new Action(description, saturationCost, requiredItem, rewardItem, consumedItem, applyWound);
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Invalid argument format in action: " + line);
            return null;
        }
    }

    private static Item assignItem(String itemName) {
        if (itemName.equals("null")) {
            return null;
        }
        try {
            return Item.findItem(itemName);
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Item '" + itemName + "' from file not loaded because it does not exist.");
            return null;
        }
    }



    //TEAM ACTION CREATION ---------------------


    private static TeamAction createTeamAction(String line) {
        String[] parts = line.split("\\|"); // Splittin with |
        if (parts.length < 13) {
            System.out.println("Warning: Invalid TeamAction format: " + line);
            return null;
        }

        try {
            String[] descriptions = parts[0].split("~"); //Splittind descriptions with ~
            int minMembers = Integer.parseInt(parts[1]);
            int[] diff = Arrays.stream(parts[2].split(",")).mapToInt(Integer::parseInt).toArray();
            int[] satGain = Arrays.stream(parts[3].split(",")).mapToInt(Integer::parseInt).toArray();
            Item[][] itemRequired = assignItemArrays(parts[4]);
            boolean[] itemUsed = assignBool(parts[5]);
            List<AbstractMap.SimpleEntry<Item, Integer>> itemMods = assignItemMods(parts[6]);
            Trait[] traitMods = assignTraits(parts[7]);
            Personality[] personalityPlusMods = assignPersonalities(parts[8]);
            Personality[] personalityMinusMods = assignPersonalities(parts[9]);
            int[] genderMods = Arrays.stream(parts[10].split(",")).mapToInt(Integer::parseInt).toArray();
            Item[][] itemRewards = assignItemArrays(parts[11]);
            int[] wounds = Arrays.stream(parts[12].split(",")).mapToInt(Integer::parseInt).toArray();

            return new TeamAction(descriptions, minMembers, diff, satGain, itemRequired, itemUsed, itemMods, traitMods, personalityPlusMods, personalityMinusMods, genderMods, itemRewards, wounds);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Warning: Invalid argument format in TeamAction: " + line);
            return null;
        }
    }
    private static Item[][] assignItemArrays(String itemsStr) {
        if (itemsStr.equals("null")) {
            return new Item[0][0];
        }

        String[] itemArrays = itemsStr.split(";");
        Item[][] result = new Item[itemArrays.length][];

        for (int i = 0; i < itemArrays.length; i++) {
            if (itemArrays[i].equals("null")) {
                result[i] = null;
            } else {
                String[] items = itemArrays[i].split(",");
                result[i] = new Item[items.length];
                for (int j = 0; j < items.length; j++) {
                    result[i][j] = assignItem(items[j]);
                }
            }
        }
        return result;
    }

    private static boolean[] assignBool(String part) {
        if (part == null || part.isEmpty()) {
            return new boolean[0];
        }
        String[] boolStrings = part.split(",");
        boolean[] boolArray = new boolean[boolStrings.length];
        for (int i = 0; i < boolStrings.length; i++) {
            boolArray[i] = Boolean.parseBoolean(boolStrings[i].trim());
        }
        return boolArray;
    }


    private static List<AbstractMap.SimpleEntry<Item, Integer>> assignItemMods(String modsStr) {
        if (modsStr.equals("null")) {
            return Collections.emptyList();
        }

        String[] modPairs = modsStr.split(";");
        List<AbstractMap.SimpleEntry<Item, Integer>> result = new ArrayList<>();

        for (String pair : modPairs) {
            String[] parts = pair.split(",");
            if (parts.length == 2) {
                result.add(new AbstractMap.SimpleEntry<>(assignItem(parts[0]), Integer.parseInt(parts[1])));
            }
        }
        return result;
    }

    private static Trait[] assignTraits(String traitsStr) {
        if (traitsStr.equals("null")) {
            return new Trait[0];
        }

        String[] traitNames = traitsStr.split(",");
        Trait[] result = new Trait[traitNames.length];
        for (int i = 0; i < traitNames.length; i++) {
            result[i] = Trait.valueOf(traitNames[i]);
        }
        return result;
    }

    private static Personality[] assignPersonalities(String personalitiesStr) {
        if (personalitiesStr.equals("null")) {
            return new Personality[0];
        }

        String[] personalityNames = personalitiesStr.split(",");
        Personality[] result = new Personality[personalityNames.length];
        for (int i = 0; i < personalityNames.length; i++) {
            result[i] = Personality.valueOf(personalityNames[i]);
        }
        return result;
    }


    //-----------------------------------


    private static Action tryGetActionByName(String actionName) {
        try {
            return getActionByName(actionName);
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Action '" + actionName + "' from file not loaded because it does not exist.");
            return null;
        }
    }

    private static final Map<String, Action> ACTION_MAP = new HashMap<>();
    static {
        //Long Actions
        ACTION_MAP.put("LOG_ENCOUNTER", LongActions.LOG_ENCOUNTER);
        ACTION_MAP.put("RIVER_CROSSING", LongActions.RIVER_CROSSING);
        ACTION_MAP.put("FRUIT_TREE_CLIMB", LongActions.FRUIT_TREE_CLIMB);
        ACTION_MAP.put("FIND_TREASURE_1", LongActions.FIND_TREASURE_1);
        ACTION_MAP.put("FIND_TREASURE_2", LongActions.FIND_TREASURE_2);
        ACTION_MAP.put("FIND_TREASURE_3", LongActions.FIND_TREASURE_3);
        ACTION_MAP.put("BEAR_ATTACK", LongActions.BEAR_ATTACK);
    }

    private static Action getActionByName(String actionName) {
        Action action = ACTION_MAP.get(actionName);
        if (action == null) {
            throw new IllegalArgumentException("Unknown action: " + actionName);
        }
        return action;
    }
}
