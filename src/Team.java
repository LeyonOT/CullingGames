import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Team {
    private List<Character> members;
    private List<Item> sharedEquipment;

    public Team() {
        this.members = new ArrayList<>();
        this.sharedEquipment = new ArrayList<>();
    }

    public void addMember(Character character) {
        members.add(character);
        character.setTeam(this);
        //Moze tutaj SOUT?
    }

    public void removeMember(Character character) {
        members.remove(character);
        character.setTeam(null);
        //Moze tutaj SOUT?
    }


    //Placeholdery
    public List<Character> getMembers() {
        return members;
    }

    public List<Item> getSharedEquipment() {
        return sharedEquipment;
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
            removeMember(victim);
            System.out.println(victim.getName() + " got wounded due to the betrayal.");
        }
    }
}