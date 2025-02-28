import java.util.Random;

public enum Personality {
    AGGRESSIVE(1),  //FIGHTS
    PEACEFUL(1),    //FIGHTS
    TRUSTWORTHY(2),    //TEAM ACTIONS
    TREACHEROUS(2),   //TEAM ACTIONS
    SOCIABLE(3),     //TEAMS
    RECLUSIVE(3),    //TEAMS
    RESOURCEFUL(4),  //TODO
    IMPULSIVE(4),    //TODO
    BRAVE(5),        //TEAM ACTIONS
    CAUTIOUS(5);     //TEAM ACTIONS
    private final int group;
    Personality(int group) {
        this.group=group;
    }
    public int getGroup() {
        return group;
    }
    public static Personality[] getRandom() {
        Random random = new Random();
        Personality[] result = new Personality[2];

        for (int i = 0; i < 2; i++) {
            Personality randomTrait;
            do {
                randomTrait = values()[random.nextInt(values().length)];
            } while (i == 1 && randomTrait.getGroup() == result[0].getGroup());
            result[i] = randomTrait;
        }

        return result;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
