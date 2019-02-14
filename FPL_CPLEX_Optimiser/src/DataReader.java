import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DataReader {

    ArrayList<String> injuredList;

    public DataReader(){
        injuredList = getInjuries();
    }

    public ArrayList<Player> getPlayersCSV(String csvFile)
    {

        ArrayList<Player> players = new ArrayList<Player>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        boolean firstLine = true;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                if (!firstLine) {
                    // use comma as separator
                    String[] lineSplit = line.split(cvsSplitBy);
                    Player p = new Player(lineSplit[0], lineSplit[1], lineSplit[2], Integer.parseInt(lineSplit[3]), Integer.parseInt(lineSplit[lineSplit.length-1]));
                    players.add(p);
                }
                firstLine = false;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return players;
    }

    public ArrayList<Player> getPlayers() {

        String sURL = "https://fantasy.premierleague.com/drf/elements/"; //just a string
        ArrayList<Player> players = new ArrayList<Player>();

        // Connect to the URL using java's native library
        URL url = null;
        JSONObject json = null;
        try {
            url = new URL(sURL);
            URLConnection request = url.openConnection();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            //System.out.println(root);
            Data[] data = new Gson().fromJson(root.toString(), Data[].class);
            for (int i = 0; i < data.length; i++) {
                //System.out.println(data[i].getSecond_name() + " " + data[i].getChance_of_playing_next_round());
                Player p = getPlayer(data[i]);
                p.setSecondName(data[i].second_name);
                if (!injured(p)) {
                    players.add(p);
                } else {
                    System.out.println(data[i].second_name);
                }
            }
            } catch(Exception e){
                e.printStackTrace();
            }
            return players;
    }

    public Player getPlayer(Data d){
        String name = d.getFirst_name() + " " + d.getSecond_name();
        String pos = "null";
        int pos_int = d.getElement_type();
        if (pos_int==1){
            pos = "GKP";
        } else if (pos_int==2){
            pos = "DEF";
        } else if (pos_int==3){
            pos = "MID";
        } else if (pos_int==4){
            pos = "FWD";
        }
        int wage = d.getNow_cost();
        int points = d.getTotal_points();
        return new Player(name, pos, wage, points);
    }

    //Descriptions at https://github.com/JakePrice86/fpl-data-convert

    class Data {
        private  String first_name;
        private String second_name;
        private Long id;
        private int now_cost;
        private int total_points;
        private int element_type;
        private int chance_of_playing_next_round;


        public String getFirst_name() { return first_name; }
        public String getSecond_name() { return second_name; }
        public Long getId() { return id; }
        public int getNow_cost() { return now_cost; }
        public int getTotal_points() { return total_points; }
        public int getElement_type() { return element_type; }
        public int getChance_of_playing_next_round() { return chance_of_playing_next_round; }





        public void setFirst_name(String first_name) { this.first_name = first_name; }
        public void setSecond_name(String second_name) { this.second_name = second_name; }
        public void setId(Long id) { this.id = id; }
        public void setNow_cost(int now_cost) { this.now_cost = now_cost; }
        public void setTotal_points(int total_points) { this.total_points = total_points; }
        public void setElement_type(int element_type) { this.element_type = element_type; }
        public void setChance_of_playing_next_round(int chance_of_playing_this_round) { this.chance_of_playing_next_round = chance_of_playing_next_round; }




        public String toString() {
            return String.format(id + ": "+ first_name + " " + second_name + " = "+ element_type );
        }
    }

    public ArrayList<String> getInjuries(){
        ArrayList<String> injuredNames = new ArrayList<String>();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.fantasyfootballscout.co.uk/fantasy-football-injuries/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element table = doc.select("table").get(0); //select the first table.
        Elements rows = table.select("tr");

        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");

            //System.out.println(row.text());

            String name = cols.get(0).text().replace("(","").replace(")","");
            String[] nameSplit = name.split(" ");
            injuredNames.add(nameSplit[0]);

        }
        return injuredNames;
    }

    public boolean injured(Player p){

        boolean injured=false;

        if (p.secondName.contains("Otto")){
            injured =true;
        }

        for (String s : injuredList){
            if(p.secondName.equals(s)){
                injured = true;
            }
        }
        return injured;
    }

}
