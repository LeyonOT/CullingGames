import java.util.*;

public class Character {
    private String name;
    private String gender;
    private Trait trait;
    private Personality[] personality;
    private int saturation;
    private boolean tired;
    private boolean wounded;
    private List<Item> equipment;
    private int value;
    private Team team;
    private boolean teamActionParticipation;
    private boolean alive;

    private final int maxSlots;
    private int usedSlots;

    public Character(String name, String gender, Trait trait) {
        this.name = name;
        this.gender = gender;
        this.trait = trait;
        this.personality = Personality.getRandom();
        this.saturation = 100;
        this.tired = false;
        this.wounded = false;
        this.equipment = new ArrayList<>();
        this.team = null;
        this.teamActionParticipation = false;
        this.alive = true;
        this.value = 0;

        this.maxSlots = trait == Trait.STRONG ? 5 : 4;
        this.usedSlots = 0;
    }
    public void rest() {
        tired = false;
    } //tylko uzywane w nocy

    public void reduceSaturation(int saturationCost) {
        saturation -= saturationCost;
        if (saturation < 40)
            tired = true;
        else
            tired = false;
        //System.out.println(name + (saturationCost>=0?" loses ":" gains ") + (saturationCost>=0?saturationCost:-saturationCost) + " saturation.");
        System.out.println("(" + (saturationCost>=0?"-":"+") + (saturationCost>=0?saturationCost:-saturationCost) + ") saturation.");
        if (this.saturation <=0) {
            die();
            System.out.println(name + " has died of starvation.");
        }

    }

    public void takeWound() {
        if (!alive)
            return;
        if (!wounded) {
            System.out.println(name + " got wounded!");
            wounded = true;
        }
        else {
            die();
            System.out.println(name + " died from the damage "+getPron(false)+" took!");
        }
    }

    private void die(){
        alive = false;
    }

    public void healWound() {
        this.wounded = false;
    }



    //EQUIPMENT
    private void sortEquipmentByPriority() {
        equipment.sort(Comparator.comparingInt(Item::getPriority).thenComparingInt(Item::getSlotSize));
    }

    public void addItem(Item newItem) {
        boolean wasAdded = false;

        if (usedSlots + newItem.getSlotSize() <= maxSlots) {
            equipment.add(newItem);
            usedSlots += newItem.getSlotSize();
            System.out.println(name + " added " + newItem.getName() + " to "+getPron(true)+" equipment.");
            wasAdded = true;
        } else {
            sortEquipmentByPriority();
            ArrayList<Item> indexesToRemove = new ArrayList<>();
            int sizeCounter = 0;

            for (int i = 0; i < equipment.size(); i++) {
                if (newItem.getPriority() > equipment.get(i).getPriority()) {
                    if (newItem.getSlotSize() <= equipment.get(i).getSlotSize()) {
                        System.out.println(name + " threw away " + equipment.get(i).getName() + " to get " + newItem.getName() + ".");
                        wasAdded = true;
                        equipment.remove(i);
                        usedSlots -= equipment.get(i).getSlotSize();
                        equipment.add(newItem);
                        usedSlots += newItem.getSlotSize();
                        break;
                    } else {
                        indexesToRemove.add(equipment.get(i));
                        sizeCounter += equipment.get(i).getSlotSize();
                        int prioritySum = 0;
                        for (Item item : indexesToRemove) {
                            prioritySum += item.getPriority();
                        }
                        if ((sizeCounter >= newItem.getSlotSize() - (maxSlots - usedSlots)) && prioritySum < newItem.getPriority()) {
                            StringBuilder s = new StringBuilder(name + " threw away ");
                            for (Item item : indexesToRemove) {
                                s.append(item.getName()).append(", ");
                                equipment.remove(item);
                                usedSlots -= item.getSlotSize();
                            }
                            s.deleteCharAt(s.length() - 2);
                            s.append("to get ").append(newItem.getName()).append(".");
                            wasAdded = true;
                            equipment.add(newItem);
                            usedSlots += newItem.getSlotSize();
                            System.out.println(s);
                            break;
                        }
                    }
                } else if (newItem.getPriority() <= equipment.get(i).getPriority()) break;
            }
        }
        sortEquipmentByPriority();
        updateValue();
        if (!wasAdded) {
            System.out.println(name + " could not add " + newItem.getName() + " to "+getPron(true)+" equipment.");
        }
    }

    public boolean removeItem(Item item) {
        if (item == Item.RANDOM) {
            if (equipment.isEmpty()) {
                System.out.println(name + " has no items to remove.");
                return false;
            } else {
                Random random = new Random();
                int randomIndex = random.nextInt(equipment.size());
                Item randomItem = equipment.get(randomIndex);
                equipment.remove(randomItem);
                usedSlots -= randomItem.getSlotSize();
                System.out.println(name + " lost " + randomItem.getName() + " from "+getPron(true)+" equipment.");
                updateValue();
                return true;
            }
        }

        if (equipment.contains(item)) {
            equipment.remove(item);
            usedSlots -= item.getSlotSize();
            System.out.println(name + " removed " + item.getName() + " from "+getPron(true)+" equipment.");
            updateValue();
            return true;
        } else {
            System.out.println(name + " does not have " + item.getName() + " in "+getPron(true)+" equipment.");
            return false;
        }
    }

    public boolean hasItem(Item item) {
        return equipment.contains(item);
    }

    public String showEquipment() {
        StringBuilder s = new StringBuilder(name + "'s Equipment:\n");
        for (Item item : equipment) {
            s.append(" - ").append(item).append("\n");
        }
        s.append("Used Slots: " + usedSlots + "/" + maxSlots);
        return s.toString();
    }

    private void updateValue() {
        value = 0;
        for (Item i:equipment) {
            value += i.getPriority() * 10 + 5 * (4-i.getSlotSize());
        }
    }

    //Team-ups and shit
    public void setTeam(Team team) {
        this.team = team;
    }
    public void setTAP(boolean set) { teamActionParticipation = set; }

    public boolean inTeam() {
        return team != null;
    }



    //GETTERS
    public String getName() {
        return name;
    }
    public int getSaturation() {
        return saturation;
    }
    public boolean isAlive() {
        return alive;
    }
    public boolean isWounded() {
        return wounded;
    }
    public Team getTeam() {
        return team;
    }
    public boolean getTAP() { return teamActionParticipation; }
    public String getGender() {
        return gender;
    }
    public Trait getTrait() {
        return tired?Trait.TIRED:trait;
    }
    public int getValue() {
        return value;
    }
    public boolean hasPersonality(Personality p) {
        for (Personality i : personality) {
            if (i.equals(p))
                return true;
        }
        return false;
    }
    public void printStatus() {
        System.out.println(name + " (" + gender + ") | Sat: " + saturation + " | Trait: " + getTrait() + "\n" +
                "Personality: " + personality[0] + ", " + personality[1] + "\nTeam: " + (inTeam()?team.getName():"none") + "\n" +
                showEquipment());
    }
    public String getPron(boolean b) {
        if (b)
            return gender.equals("Male")?"his":"her";
        return gender.equals("Male")?"he":"she";
    }
}