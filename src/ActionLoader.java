import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ActionLoader {
    public static List<Action> loadActions(String simpleActionsFile, String advancedActionsFile) {
        List<Action> actions = new ArrayList<>();

        try {
            actions.addAll(loadActionsFromFile(simpleActionsFile));
            actions.addAll(loadActionsFromFile(advancedActionsFile));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return actions;
    }

    private static List<Action> loadActionsFromFile(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName))
                .filter(line -> !line.startsWith("--"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(ActionLoader::tryGetActionByName)
                .filter(Objects::nonNull) //nie bierze pod uwage akcji ktore nie istnieja
                .collect(Collectors.toList());
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
        ACTION_MAP.put("SIT_AND_THINK", StaticActions.SIT_AND_THINK);
        ACTION_MAP.put("SCAVENGE_CAMPFIRE", StaticActions.SCAVENGE_CAMPFIRE);
        ACTION_MAP.put("MEDITATE", StaticActions.MEDITATE);
        ACTION_MAP.put("RABBIT_STALKER", StaticActions.RABBIT_STALKER);
        ACTION_MAP.put("WATER_GATHER", StaticActions.WATER_GATHER);
        ACTION_MAP.put("BACKPACK_SEARCH", StaticActions.BACKPACK_SEARCH);
        ACTION_MAP.put("SKYSEEING", StaticActions.SKYSEEING);
        ACTION_MAP.put("BUSH_SCAVENGE", StaticActions.BUSH_SCAVENGE);
        ACTION_MAP.put("DANGER_SENSE", StaticActions.DANGER_SENSE);
        ACTION_MAP.put("STONE_THROWING", StaticActions.STONE_THROWING);
        ACTION_MAP.put("CRY", StaticActions.CRY);
        ACTION_MAP.put("PARANOID", StaticActions.PARANOID);
        ACTION_MAP.put("RUN", StaticActions.RUN);
        ACTION_MAP.put("STRETCH", StaticActions.STRETCH);
        ACTION_MAP.put("FIND_BERRIES", StaticActions.FIND_BERRIES);
        ACTION_MAP.put("SING", StaticActions.SING);
        ACTION_MAP.put("FIDGET", StaticActions.FIDGET);
        ACTION_MAP.put("FIND_MAP_1", StaticActions.FIND_MAP_1);
        ACTION_MAP.put("FIND_MAP_2", StaticActions.FIND_MAP_2);
        ACTION_MAP.put("FIND_MAP_3", StaticActions.FIND_MAP_3);
        ACTION_MAP.put("CRAFT_ROPE_1", StaticActions.CRAFT_ROPE_1);
        ACTION_MAP.put("CRAFT_ROPE_2", StaticActions.CRAFT_ROPE_2);
        ACTION_MAP.put("FIND_ROPE_1", StaticActions.FIND_ROPE_1);
        ACTION_MAP.put("FIND_ROPE_2", StaticActions.FIND_ROPE_2);
        ACTION_MAP.put("TREE_CLIMB", StaticActions.TREE_CLIMB);
        ACTION_MAP.put("SHIELD_CRAFT", StaticActions.SHIELD_CRAFT);
        ACTION_MAP.put("MAP_SEARCH_FOOD", StaticActions.MAP_SEARCH_FOOD);
        ACTION_MAP.put("MAP_SEARCH_CHEST", StaticActions.MAP_SEARCH_CHEST);
        ACTION_MAP.put("MAP_SEARCH_RUINS", StaticActions.MAP_SEARCH_RUINS);
        ACTION_MAP.put("MAP_SEARCH_FOOD_2", StaticActions.MAP_SEARCH_FOOD_2);
        ACTION_MAP.put("MAP_SEARCH_GUN", StaticActions.MAP_SEARCH_GUN);

        //Long Actions
        ACTION_MAP.put("LOG_ENCOUNTER", LongActions.LOG_ENCOUNTER);
        ACTION_MAP.put("RIVER_CROSSING", LongActions.RIVER_CROSSING);
        ACTION_MAP.put("FRUIT_TREE_CLIMB", LongActions.FRUIT_TREE_CLIMB);
        ACTION_MAP.put("FIND_TREASURE_1", LongActions.FIND_TREASURE_1);
        ACTION_MAP.put("FIND_TREASURE_2", LongActions.FIND_TREASURE_2);
        ACTION_MAP.put("FIND_TREASURE_3", LongActions.FIND_TREASURE_3);
        }

    private static Action getActionByName(String actionName) {
        Action action = ACTION_MAP.get(actionName);
        if (action == null) {
            throw new IllegalArgumentException("Unknown action: " + actionName);
        }
        return action;
    }
}
