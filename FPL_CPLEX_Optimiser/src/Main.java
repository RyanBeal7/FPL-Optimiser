import ilog.concert.IloException;

public class Main {

    public static void main(String[] args) {

        System.out.println("-------- CPLEX EXAMPLE -------- ");

        DataReader dr = new DataReader();
        dr.getInjuries();

        //Optimiser fplMain = new Optimiser(dr.getPlayers(), "1-3-4-3", 820);
        //Optimiser fplBench = new Optimiser(dr.getPlayers(), "1-1-1-1", 180);
        Optimiser fplAll = new Optimiser(dr.getPlayers(), "2-5-5-3", 1000);

        try {
            //fplMain.solve();
            //fplBench.solve();
            fplAll.solve();
        } catch (IloException e) {
            e.printStackTrace();
        }

    }

}
