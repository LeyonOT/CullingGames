import java.util.*;

public class Team {
    private List<Character> members;
    private List<Character> bannedMembers;
    private List<Item> sharedEquipment;
    private Map<Character, List<Item>> eqTransactionList;
    private int maxSlots;
    private int usedSlots;
    private String name;
    private static List<String> nameChoices = new ArrayList<>();
    static {
        nameChoices.add("Alpha"); nameChoices.add("Beta"); nameChoices.add("Gamma"); nameChoices.add("Delta");
        nameChoices.add("Epsilon"); nameChoices.add("Zeta"); nameChoices.add("Eta"); nameChoices.add("Theta");
        nameChoices.add("Iota"); nameChoices.add("Kappa"); nameChoices.add("Lambda"); nameChoices.add("Mu");
        nameChoices.add("Nu"); nameChoices.add("Xi"); nameChoices.add("Omicron"); nameChoices.add("Pi");
        nameChoices.add("Rho"); nameChoices.add("Sigma"); nameChoices.add("Tau"); nameChoices.add("Upsilon");
        nameChoices.add("Phi"); nameChoices.add("Chi"); nameChoices.add("Psi"); nameChoices.add("Omega");
    }

    public Team(Character c1, Character c2) {
        this.members = new ArrayList<>();
        this.bannedMembers = new ArrayList<>();
        addMember(c1, false);
        addMember(c2, false);
        this.maxSlots = 3;
        this.sharedEquipment = new ArrayList<>();
        this.eqTransactionList = new HashMap<>();
        this.name = nameChoices.get(new Random().nextInt(nameChoices.size()));
        nameChoices.remove(name);
        System.out.println(c1.getName() + " and " + c2.getName() + " decided to team up!");
    }

    public void addMember(Character character, boolean showMessage) {
        members.add(character);
        character.setTeam(this);
        maxSlots = ((int) (1.5*members.size())) + (members.size()/3);
        if (showMessage) System.out.println(character.getName() + " has joined the " + name + " Team!");
    }

    public void removeMember(Character character, boolean showText) {
        members.remove(character);
        maxSlots = ((int) (1.5*members.size())) + (members.size()/3);
        manageEq();
        if (showText)
            System.out.println(character.getName() + " left the " + name + " Team.");
        if (members.isEmpty())
            nameChoices.add(name);
        character.setTeam(null);
    }

    public boolean manageTeam() { //TRUE jesli Team dalej istnieje
        members.removeIf(c -> !c.isAlive());
        maxSlots = ((int) (1.5*members.size())) + (members.size()/3);
        if (members.size() == 1) removeMember(members.get(0), false);

        for (Map.Entry<Character, List<Item>> transaction : eqTransactionList.entrySet()) {
            transaction.getKey().clearTempEquipment();
        }
        eqTransactionList.clear();

        return !members.isEmpty();
    }


    //EQUIPMENT SHARED
    public void manageEq() {
        if (maxSlots == 0){
            sharedEquipment.clear();
            return;
        }
        if (usedSlots == 0) return;
        sortEquipmentByPriority();
        for (int i = usedSlots; i > maxSlots; i = usedSlots) {
            Item item;
            if (!sharedEquipment.isEmpty()) {
                item = sharedEquipment.getFirst();
                sharedEquipment.removeFirst();
                usedSlots -= item.getSlotSize();
            }

            if (usedSlots < 1) break;
            //System.out.println(sharedEquipment);
        }

    }
    public void addItem(Item newItem) {
        boolean wasAdded = false;

        if (usedSlots + newItem.getSlotSize() <= maxSlots) {
            sharedEquipment.add(newItem);
            usedSlots += newItem.getSlotSize();
            System.out.println(newItem.getName() + " was added to " + name + " Team's shared equipment.");
            wasAdded = true;
        } else {
            sortEquipmentByPriority();
            ArrayList<Item> indexesToRemove = new ArrayList<>();
            int sizeCounter = 0;

            for (int i = 0; i < sharedEquipment.size(); i++) {
                if (newItem.getPriority() > sharedEquipment.get(i).getPriority()) {
                    if (newItem.getSlotSize() <= sharedEquipment.get(i).getSlotSize()) {
                        System.out.println(name + " Team replaced " + sharedEquipment.get(i).getName() + " with " + newItem.getName() + ".");
                        wasAdded = true;
                        usedSlots -= sharedEquipment.get(i).getSlotSize();
                        sharedEquipment.remove(i);
                        sharedEquipment.add(newItem);
                        usedSlots += newItem.getSlotSize();
                        break;
                    } else {
                        indexesToRemove.add(sharedEquipment.get(i));
                        sizeCounter += sharedEquipment.get(i).getSlotSize();
                        int prioritySum = 0;
                        for (Item item : indexesToRemove) {
                            prioritySum += item.getPriority();
                        }
                        if ((sizeCounter >= newItem.getSlotSize() - (maxSlots - usedSlots)) && prioritySum < newItem.getPriority()) {
                            StringBuilder s = new StringBuilder(name + " Team replaced ");
                            for (Item item : indexesToRemove) {
                                s.append(item.getName()).append(", ");
                                sharedEquipment.remove(item);
                                usedSlots -= item.getSlotSize();
                            }
                            s.deleteCharAt(s.length() - 2);
                            s.append("with ").append(newItem.getName()).append(".");
                            wasAdded = true;
                            sharedEquipment.add(newItem);
                            usedSlots += newItem.getSlotSize();
                            System.out.println(s);
                            break;
                        }
                    }
                } else if (newItem.getPriority() <= sharedEquipment.get(i).getPriority()) break;
            }
        }
        sortEquipmentByPriority();
        if (!wasAdded) System.out.println(newItem.getName() + " did not fit in " + name + " Team's shared equipment.");
    }

    public Item removeItem(Item item, boolean showText) {
        if (item == Item.RANDOM) {
            if (sharedEquipment.isEmpty()) {
                if(showText) System.out.println(name + " Team has no items to remove.");
                return null;
            } else {
                Random random = new Random();
                int randomIndex = random.nextInt(sharedEquipment.size());
                Item randomItem = sharedEquipment.get(randomIndex);
                sharedEquipment.remove(randomItem);
                usedSlots -= randomItem.getSlotSize();
                if(showText) System.out.println(name + " Team lost " + randomItem.getName() + " from their equipment.");
                return randomItem;
            }
        }

        if (sharedEquipment.contains(item)) {
            sharedEquipment.remove(item);
            usedSlots -= item.getSlotSize();
            if(showText) System.out.println(name + " Team removed " + item.getName() + " from their equipment.");
            return item;
        } else {
            if(showText) System.out.println(name + " Team does not have " + item.getName() + " in their equipment.");
            return null;
        }
    }

    public boolean hasItem(Item item) {
        int sharedItems = 0;
        int accessibleItems = 0;
        for (Map.Entry<Character, List<Item>> transaction : eqTransactionList.entrySet()) {
            for (Item i:transaction.getValue()) {
                if (i.equals(item)) sharedItems++;
            }
        }
        for (Item i:sharedEquipment) {
            if (i.equals(item)) accessibleItems++;
        }
        return accessibleItems > sharedItems;
    }
    public void addEqTransaction(Character character, Item item) {
        List<Item> temp = new ArrayList<>();
        if (eqTransactionList.containsKey(character)) {
            temp = eqTransactionList.get(character);
            temp.add(item);
        } else {
            temp.add(item);
        }
        eqTransactionList.put(character,temp);
    }

    private void sortEquipmentByPriority() {
        sharedEquipment.sort(Comparator.comparingInt(Item::getPriority).reversed());
    }

    //Na razie tylko na dwie osoby, potem moze ze zdradza caly team

    public void handleBetrayal() {
        if (members.size() < 2) return;

        int betrayerIndex = new Random().nextInt(members.size());
        int victimIndex = (betrayerIndex + 1 + new Random().nextInt(members.size()-1)) % members.size();

        Character betrayer = members.get(betrayerIndex);
        Character victim = members.get(victimIndex);

        boolean isTreacherous = betrayer.hasPersonality(Personality.TREACHEROUS);
        boolean isTrustworthy = betrayer.hasPersonality(Personality.TRUSTWORTHY);
        boolean isAggressive = betrayer.hasPersonality(Personality.AGGRESSIVE);

        double modifier = (isTreacherous?0.07:0) - (isTrustworthy?0.04:0) + (isAggressive?0.03:0);
        double base = 0.05;

        double r = new Random().nextDouble();
        //System.out.println("Betrayer:" + betrayer.getName() + " | " + (r-modifier) + " | " + base);
        if (r - modifier > base) return;

        System.out.print(betrayer.getName() + " betrayed the " + name + " Team and");
        if (new Random().nextInt(2) == 1){//KRADZIEZ
                Item stolenItem = removeItem(Item.RANDOM, false);
                removeMember(betrayer, false);
                bannedMembers.add(betrayer);
                if (stolenItem != null) {
                    System.out.println(" stole " + stolenItem + "!");
                    betrayer.addItem(stolenItem);
                } else System.out.println(" ran away!");
        }
        else {
            removeMember(betrayer, false);
            bannedMembers.add(betrayer);
            System.out.println(" attacked " + victim.getName() + "!");
            victim.takeWound();
        }
        manageTeam();
    }


    //GETTERY
    public List<Character> getMembers() {
        return members;
    }
    public List<Character> getBannedMembers() {
        return bannedMembers;
    }
    public Character getValueMember(boolean max, boolean action) {
        List<Character> charList = action?getActionMembers():members;
        Character mostValue = charList.get(0);
        Character leastValue = charList.get(0);
        for (Character c : charList) {
            if (c.getValue() > mostValue.getValue()) mostValue = c;
            if (c.getValue() < leastValue.getValue()) leastValue = c;
        }
        return max?mostValue:leastValue;
    }
    public List<Character> getActionMembers() {
        List<Character> memberList = new ArrayList<>();
        for (Character c: members) {
            if (c.getTAP()) memberList.add(c);
        }
        //System.out.println(name + " participating members: " + memberList.size());
        return memberList;
    }

    public List<Item> getSharedEquipment() {
        return sharedEquipment;
    }

    public String getName() {
        return name;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public int getUsedSlots() {
        return usedSlots;
    }
}