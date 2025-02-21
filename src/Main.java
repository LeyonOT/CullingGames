import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Action> actionList = ActionLoader.loadActions("simple_actions.txt", "advanced_actions.txt");

        Game gameManager = new Game(actionList);

        gameManager.setupCharacters();
        gameManager.startGame();
    }
}
