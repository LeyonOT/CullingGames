import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Team {
    private List<Character> members;
    private List<Item> sharedEquipment;
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
        addMember(c1, false);
        addMember(c2, false);
        this.sharedEquipment = new ArrayList<>();
        this.name = nameChoices.get(new Random().nextInt(nameChoices.size()));
        nameChoices.remove(name);
        System.out.println(c1.getName() + " and " + c2.getName() + " decided to team up!");
    }

    public void addMember(Character character, boolean showMessage) {
        members.add(character);
        character.setTeam(this);
        if (showMessage)
        System.out.println(character.getName() + " has joined the " + name + " Team!");
    }

    public void removeMember(Character character, boolean showText) {
        members.remove(character);
        if (showText)
            System.out.println(character.getName() + " left the " + name + " Team.");
        if (members.isEmpty())
            nameChoices.add(name);
        character.setTeam(null);
    }

    public boolean manageTeam() { //TRUE jesli Team dalej istnieje
        members.removeIf(c -> !c.isAlive());
        if (members.size() == 1) removeMember(members.get(0), false);
        return !members.isEmpty();
    }


    //Placeholdery
    public List<Character> getMembers() {
        return members;
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

    public void addEquipment(Item item) {
        sharedEquipment.add(item);
    }

    public void removeEquipment(Item item) {
        sharedEquipment.remove(item);
    }


    //zamiast tego moze byc ze postac bierze ze wspolnego ekwipunku jak jest modyfikator przy long actions
    public void shareResources() {
        for (Character member : members) {
            if (!sharedEquipment.isEmpty()) {
                Item item = sharedEquipment.get(0);
                member.addItem(item);
                removeEquipment(item);
                System.out.println(member.getName() + " received " + item.getName() + " from team resources.");
            }
        }
    }


    //Na razie tylko na dwie osoby, potem moze ze zdradza caly team
    public void handleBetrayal() {
        if (members.size() < 2) return;

        Random rand = new Random();
        int betrayerIndex = rand.nextInt(members.size());
        int victimIndex = (betrayerIndex + 1) % members.size();

        Character betrayer = members.get(betrayerIndex);
        Character victim = members.get(victimIndex);

        System.out.println(betrayer.getName() + " betrayed " + victim.getName() + "!");

        victim.takeWound();
        if (!victim.isAlive()) {
            removeMember(victim, true);
            System.out.println(victim.getName() + " got wounded due to the betrayal.");
        }
    }


}