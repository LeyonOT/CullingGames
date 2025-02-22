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

    private static List<Action> loadLongActionsFromFile(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName))
                .filter(line -> !line.startsWith("--"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(ActionLoader::tryGetActionByName)
                .filter(Objects::nonNull) //nie bierze pod uwage akcji ktore nie istnieja
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
