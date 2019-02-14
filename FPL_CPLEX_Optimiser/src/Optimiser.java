import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.ArrayList;

public class Optimiser {

    ArrayList<Player> players;
    int budget, numGK, numDef, numMid, numAtt, teamSize;
    int points_tot;

    public Optimiser(ArrayList<Player> players, String formation, int budget){
        this.players = players;
        String[] formationSplit = formation.split("-");
        numGK = Integer.parseInt(formationSplit[0]);
        numDef = Integer.parseInt(formationSplit[1]);
        numMid = Integer.parseInt(formationSplit[2]);
        numAtt = Integer.parseInt(formationSplit[3]);
        teamSize = numGK+numMid+numDef+numAtt;
        this.budget = budget;
    }

    public void solve() throws IloException {

        IloCplex cplex = new IloCplex();

        int numberOfPlayers = players.size();

        int[] points = new int[numberOfPlayers];
        int[] wages = new int[numberOfPlayers];
        String[] names = new String[numberOfPlayers];
        IloNumVar[] io_CPLEX = new IloNumVar[numberOfPlayers];
        IloNumExpr[] wage_count = new IloNumExpr[numberOfPlayers];
        IloNumExpr[] gk = new IloNumExpr[numberOfPlayers];
        IloNumExpr[] def = new IloNumExpr[numberOfPlayers];
        IloNumExpr[] mid = new IloNumExpr[numberOfPlayers];
        IloNumExpr[] att = new IloNumExpr[numberOfPlayers];

        int tot = 0;

        for (int i = 0; i < points.length; i++) {
            points[i] = players.get(i).points;
            wages[i] = players.get(i).wage;
            names[i] = players.get(i).name;
            io_CPLEX[i] = cplex.intVar(0, 1);
            wage_count[i] = cplex.prod(io_CPLEX[i], wages[i]);
            gk[i] = cplex.prod(io_CPLEX[i], getPos(players.get(i), "GKP"));
            def[i] = cplex.prod(io_CPLEX[i], getPos(players.get(i), "DEF"));
            mid[i] = cplex.prod(io_CPLEX[i], getPos(players.get(i), "MID"));
            att[i] = cplex.prod(io_CPLEX[i], getPos(players.get(i), "FWD"));
            tot = tot + points[i];
        }

        IloNumExpr objective = cplex.numExpr();

        for (int i = 0; i < numberOfPlayers; i++) {

            //Sum of points*i.o
            objective = cplex.sum(objective, cplex.prod(points[i], io_CPLEX[i]));

        }

        /*
        Adds Constraints: -
        Total wage under given budget
        Team size equals given term
        The correct players are picked from each position
         */

        cplex.addLe(cplex.sum(wage_count), budget);
        cplex.addEq(teamSize, cplex.sum(io_CPLEX));
        cplex.addEq(numGK, cplex.sum(gk));
        cplex.addEq(numDef, cplex.sum(def));
        cplex.addEq(numMid, cplex.sum(mid));
        cplex.addEq(numAtt, cplex.sum(att));

        cplex.addMaximize(objective);

        if (cplex.solve()) {
            System.out.println("---------------------------");
            System.out.println("Solution status: " + cplex.getStatus());
            System.out.println("Maximum Points = " + cplex.getObjValue());
            points_tot = 0;
            int wage_tot = 0;
            System.out.println("---------------------------");
            for (int i = 0; i < numberOfPlayers; i++) {
                if (cplex.getValue(io_CPLEX[i]) == 1.0) {
                    System.out.println(names[i] + " " + wages[i] + " " + points[i]);
                    points_tot = points_tot + points[i];
                    wage_tot = wage_tot + wages[i];
                }
            }
            System.out.println("---------------------------");
            System.out.println("Total Points = " + points_tot);
            System.out.println("Total Wages = " + wage_tot);
        } else {
            System.out.println("---------------------------");
            System.out.println("No Solution Found");
        }


    }

    private double getPos(Player player, String pos) {
        if (player.pos.equals(pos)){
            return 1.0;
        } else {
            return 0.0;
        }
    }


}
