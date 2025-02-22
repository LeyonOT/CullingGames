import java.util.*;

public class Game {
    private List<Character> characters;
    private List<Team> teams;
    private List<Action> actions;
    private int dayCount;
    private Scanner scanner;
    private Map<Action, Integer> actionRecency;

    public Game(List<Action> actions) {
        this.characters = new ArrayList<>();
        this.dayCount = 0;
        this.scanner = new Scanner(System.in);
        this.teams = new ArrayList<>();
        this.actions = actions;
        this.actionRecency = new HashMap<>();

        for (Action action : actions) {
            actionRecency.put(action, 100);
        }
    }

    public void setupCharacters() {
        int numCharacters = 0;
        System.out.print("'c' to start game, 'q' to quickplay: ");
        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("")) {
                System.out.println("Starting game setup...");
                break;
            } else if (input.equalsIgnoreCase("q")) {
                System.out.println("Quickplaying game setup...");
                Character character = new Character("Tom", "Male", Trait.STRONG);
                characters.add(character);
                character = new Character("Maks", "Male", Trait.SMART);
                characters.add(character);
                character = new Character("Julka", "Female", Trait.LUCKY);
                characters.add(character);
                character = new Character("Adi", "Male", Trait.FAST);
                characters.add(character);
                System.out.println("All characters have been set up. The game is ready to begin!");
                return;
            } else {
                System.out.println("Invalid input. Please enter 'c' to start game or 'q' to quickplay: ");
            }
        }

        System.out.print("Enter the number of characters (4-8): ");
        while (true) {
            if (scanner.hasNextInt()) {
                numCharacters = scanner.nextInt();
                scanner.nextLine();

                if (numCharacters >= 4 && numCharacters <= 8) break;
                else System.out.println("Invalid number. Please enter a correct number of characters");

            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }

        for (int i = 1; i <= numCharacters; i++) {
            System.out.println("Setting up character " + i + ":");

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter gender (Male/Female): ");
            String gender = scanner.nextLine();

            Trait trait = null;
            System.out.print("Choose a trait (Strong, Smart, Fast, Charismatic, Lucky): ");
            while (trait == null) {
                String traitInput = scanner.nextLine().toLowerCase();

                switch (traitInput) {
                    case "strong", "st":
                        trait = Trait.STRONG;
                        break;
                    case "smart", "sm":
                        trait = Trait.SMART;
                        break;
                    case "fast", "f":
                        trait = Trait.FAST;
                        break;
                    case "charismatic", "c", "ch":
                        trait = Trait.CHARISMATIC;
                        break;
                    case "lucky", "l":
                        trait = Trait.LUCKY;
                        break;
                    default:
                        System.out.println("Invalid trait. Please choose from Strong, Smart, Fast, Charismatic, Lucky.");
                        break;
                }
            }

            Character character = new Character(name, "Male", trait);
            characters.add(character);

            System.out.println("Character created: " + name + " (" + gender + ", " + trait + ")");
        }

        System.out.println("All characters have been set up. The game is ready to begin!");
    }

    public void formTeam(Character char1, Character char2) {
        Team newTeam = new Team();
        newTeam.addMember(char1);
        newTeam.addMember(char2);
        teams.add(newTeam);
        System.out.println(char1.getName() + " and " + char2.getName() + " have formed a team!");
    }

    public void manageTeams() {
        for (Team team : teams) {
            if (team.getMembers().size() > 1) {
                if (new Random().nextDouble() < 0.1) {
                    team.handleBetrayal();
                } else {
                    team.shareResources();
                }
            }
        }
    }

    public void runGame() {
        while (characters.size() > 1) {
            dayCount++;
            System.out.println("------------------- Day " + dayCount + " -------------------");
            playDayCycle();
            manageTeams();
            //playNightCycle();
            removeDeadCharacters();
            while (true) {
                System.out.println("Enter command ('c' or enter to continue, 'h' for help):");

                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();

                    if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("")) {
                        break;
                    } else if (input.equalsIgnoreCase("i")) {
                        for (Character c : characters) {
                            c.printStatus();
                            System.out.println();
                        }
                    } else if (input.equalsIgnoreCase("h")) {
                        System.out.println("Available commands:");
                        System.out.println("'c': Continue to the next day.");
                        System.out.println("'i': Display character information.");
                        System.out.println("'h': Display this help message.");
                    } else {
                        System.out.println("Invalid command. Please try again.");
                    }
                } else {
                    System.out.println("No proper input received. Please enter a command.");
                }
            }
        }
        declareWinner();
    }


    //ACTIONS


    public void assignRandomAction(Character character) {
        Random random = new Random();
        int currentRange = actions.size();
        int actionMercy = 0;
        List<Action> tempAction = new ArrayList<>(actions);

        while (currentRange > 0) {
            int actionIndex = random.nextInt(currentRange);
            Action selectedAction = tempAction.get(actionIndex);

            if (selectedAction.isAccessible(character)) {
                int recency = actionRecency.get(selectedAction);
                if (actionMercy > characters.size()){
                    selectedAction.perform(character);
                    actionRecency.put(selectedAction, Math.max(1, recency / 3));
                    updateRecency(selectedAction);
                    return;
                }

                if (random.nextInt(100) < recency) {
                    selectedAction.perform(character);
                    actionRecency.put(selectedAction, Math.max(1, recency / 3));
                    updateRecency(selectedAction);
                    return;
                } else {
                    actionRecency.put(selectedAction, Math.min(100, recency * 2));
                    currentRange--;
                    actionMercy++;
                    tempAction.set(actionIndex, tempAction.get(currentRange));
                }
            } else {
                //replacing the action
                currentRange--;
                tempAction.set(actionIndex, tempAction.get(currentRange));
            }
        }
        System.out.println(character.getName() + " could not find an accessible action.");
    }
    private void updateRecency(Action selectedAction) {
        for (Map.Entry<Action, Integer> entry : actionRecency.entrySet()) {
            if(entry.getKey() != selectedAction){
                actionRecency.put(entry.getKey(), Math.min(100, entry.getValue() + 5));
            }
        }
    }

    private void playDayCycle() {
        Random random = new Random();
        for (Character character : characters) {
            if (character.isAlive()) {
                if (character.getSaturation() < 30 && character.hasItem(Item.FOOD)) {
                    eatFood(character, random);
                } else if (character.isWounded() && character.hasItem(Item.ROPE) && random.nextDouble() < 0.3) {
                    stabilizeWound(character);
                } else {
                    assignRandomAction(character);
                }
            }
        }
    }

    //moze byc uzyte o cyklu nocy w przyszlosci
    private void playNightCycle() {
        for (Character character : characters) {
            if (character.isAlive()) {
                if (character.getTrait().equals(Trait.TIRED)) {
                    character.rest();  //skipuje jedna akcje w nocy jesli jest zmeczony
                } else {
                    assignRandomAction(character);
                }
            }
        }
    }


    //PLACEHOLDER TEAMY
    public void performTeamAction(Team team) {
        for (Character member : team.getMembers()) {
        }
    }




    private void removeDeadCharacters() {
        characters.removeIf(character -> !character.isAlive());
    }

    private void declareWinner() {
        if (characters.size() == 1) {
            System.out.println("The winner is " + characters.get(0).getName());
        } else {
            System.out.println("No survivors.");
        }
    }

    //SPECIAL ACTIONS
    private void stabilizeWound(Character character) {
        System.out.println(character.getName() + " uses a Rope to stabilize "+character.getPron(true)+" wound.");
        character.healWound();
        character.removeItem(Item.ROPE);
        character.reduceSaturation(10);
    }

    private void eatFood(Character character, Random random) {
        int saturationRestored = random.nextInt(16) + 20;
        System.out.println(character.getName() + " takes a break to eat some Food");
        character.reduceSaturation(-saturationRestored);
        character.removeItem(Item.FOOD);
    }

    //GETTERS
    public Map<Action, Integer> getActionRecency() {
        return actionRecency;
    }
}
