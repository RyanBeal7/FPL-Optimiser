import ilog.concert.IloException;

import java.util.ArrayList;

public class Test {

    public Test(){
        System.out.println("------------- TEST -------------");
        if (doTest()){
            System.out.println("---------------------");
            System.out.println("Test Passed");
        } else {
            System.out.println("---------------------");
            System.out.println("Test Failed");
        }

    }

    public boolean doTest(){
        ArrayList<Player> players = createTestSet();
        Optimiser o = new Optimiser(players, "2-5-5-3", 1000);
        try {
            o.solve();
        } catch (IloException e) {
            e.printStackTrace();
        }
        if (o.points_tot==15000){
            return true;
        }else {
            return false;
        }
    }

    public ArrayList<Player> createTestSet(){
        ArrayList<Player> players = new ArrayList<Player>();
        Player gk1 = new Player("GKP 1", "GKP", 10, 1000);
        Player gk2 = new Player("GKP 2", "GKP", 10, 1000);
        Player gk3 = new Player("BAD GK", "GKP", 100, 1);
        Player def1 = new Player("DEF 1", "DEF", 10, 1000);
        Player def2 = new Player("DEF 2", "DEF", 10, 1000);
        Player def3 = new Player("DEF 3", "DEF", 10, 1000);
        Player def4 = new Player("DEF 4", "DEF", 10, 1000);
        Player def5 = new Player("DEF 5", "DEF", 10, 1000);
        Player def6 = new Player("BAD DEF", "DEF", 1, 100);
        Player mid1 = new Player("MID 1", "MID", 10, 1000);
        Player mid2 = new Player("MID 2", "MID", 10, 1000);
        Player mid3 = new Player("MID 3", "MID", 10, 1000);
        Player mid4 = new Player("MID 4", "MID", 10, 1000);
        Player mid5 = new Player("MID 5", "MID", 10, 1000);
        Player mid6 = new Player("BAD MID", "MID", 1, 100);
        Player for1 = new Player("FOR 1", "FWD", 10, 1000);
        Player for2 = new Player("FOR 2", "FWD", 10, 1000);
        Player for3 = new Player("FOR 3", "FWD", 10, 1000);
        Player for4 = new Player("BAD FOR", "FWD", 1, 100);
        players.add(gk1);
        players.add(gk2);
        players.add(gk3);
        players.add(def1);
        players.add(def2);
        players.add(def3);
        players.add(def4);
        players.add(def5);
        players.add(def6);
        players.add(mid1);
        players.add(mid2);
        players.add(mid3);
        players.add(mid4);
        players.add(mid5);
        players.add(mid6);
        players.add(for1);
        players.add(for2);
        players.add(for3);
        players.add(for4);
        return players;
    }


}
