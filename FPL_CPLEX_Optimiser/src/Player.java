import java.util.Comparator;

public class Player implements Comparable<Player>, Comparator<Player> {

    String name, team, pos,secondName;
    int wage, points;
    float cba;

    public Player(String name, String team, String pos, int wage, int points){
        this.name = name;
        this.team = team;
        this.wage = wage;
        this.points = points;
        this.pos = pos;
        cba = (float)points/wage;
    }

    public Player(String name, String pos, int wage, int points){
        this.name = name;
        this.team = "null";
        this.wage = wage;
        this.points = points;
        this.pos = pos;
        cba = (float)points/wage;
    }

    public void setSecondName(String s){
        secondName = s;
    }

    @Override
    public int compareTo(Player p) {
        return Double.compare(p.cba, this.cba);
    }

    @Override
    public int compare(Player o1, Player o2) {
        return o1.name.compareTo(o2.name);
    }
}
