package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.PlayerFavour;
import it.polimi.ingsw.view.cli.Color;
import java.util.ArrayList;

public class LiteFaithPath {
    private int length;
    private ArrayList<Character> vaticanReports;
    private ArrayList<Integer> victoryPoints;
    private ArrayList<Integer> papalFavours;


    private ArrayList<PlayerFavour> playersFavourList;
    private ArrayList<Integer> positions;
    private ArrayList<String>  nicknames;

    public LiteFaithPath(){
        this.nicknames = new ArrayList<>();
        this.playersFavourList = new ArrayList<>();
        this.positions = new ArrayList<>();
    }

    /**
     * Sets up the positions and playerFavoursList  arrays to the correct length
     */
    public void setUpPositions(ArrayList<String> nicknames) {
        this.nicknames = nicknames;
        for(int i = 0; i < nicknames.size(); i++){
            playersFavourList.add(new PlayerFavour());
            positions.add(0);
        }
    }

    /**
     * Increments the position of the player with the given nickname
     */
    public void incrementPosition(int faithPoints, String nickname) {
        int movingPlayer = nicknames.indexOf(nickname);
        for(int i = 0; i < faithPoints; i++){
            if(positions.get(movingPlayer) != length-1) {
                positions.set(movingPlayer, positions.get(movingPlayer) + 1);
            }
        }
    }

    /**
     * Increments the position of all the players but the given one
     */
    public void incrementOthersPositions(int faithPoints, String nickname) {
        int standingPlayer = nicknames.indexOf(nickname);
        for(int j = 0; j < faithPoints; j++) {
            for (int i = 0; i < nicknames.size(); i++) {
                if (i != standingPlayer && positions.get(i) != length - 1) {
                    positions.set(i, positions.get(i) + 1);
                }
            }
        }
    }

    /**
     * Updates the list of papal favours earned by players given an arraylist in this form: <2,0,0,2> <br>
     * that means that players 1 and 4 received the 2 point favour
     */
    public void incrementPlayerFavours(ArrayList<Integer> favours){
        int c=0;
        for (Integer in:favours) {
            if(in!=0)
                playersFavourList.get(c).addFavour(in);
            c++;
        }
    }

    /**
     * Resets the liteFaithPath for a new Game
     */
    public void reset(ArrayList<String> nicknames){
        this.nicknames=new ArrayList<>();
        this.playersFavourList=new ArrayList<>();
        this.positions=new ArrayList<>();
        setUpPositions(nicknames);
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Color.ANSI_RED.escape()).append("FAITHPATH: \n\n").append(Color.ANSI_RESET.escape());

        int c = 0;

        for(int i=0;i<length;i++){

            if(victoryPoints.get(i) != c){
                c = victoryPoints.get(i);
                stringBuilder.append(c).append(" \t");
            }else{
                stringBuilder.append("  \t");
            }

            switch (vaticanReports.get(i)) {
                case ('E'):
                    stringBuilder.append(Color.ANSI_WHITE.escape());
                    break;

                case('X'):
                    stringBuilder.append(Color.ANSI_YELLOW.escape());
                    break;

                case('P'):
                    stringBuilder.append(Color.ANSI_RED.escape());
                    break;

            }

            stringBuilder.append(" ▓▓ ").append(Color.ANSI_RESET.escape());

            int var=0;
            for (Integer in: positions){
                if(i == in)
                    stringBuilder.append(Color.ANSI_RED.escape()).append(" ✞ ").append(Color.ANSI_RESET.escape()).append(nicknames.get(var)).append("\t");
                var++;
            }
            stringBuilder.append("\n");

        }
        stringBuilder.append("\n");

        stringBuilder.append(Color.ANSI_BLUE.escape()).append("PAPAL FAVOURS: \n").append(Color.ANSI_RESET.escape());
        int nickNum=0;
        for (String nickname:nicknames) {
            stringBuilder.append(nickname).append(" : ");
            for (Integer in:playersFavourList.get(nickNum).getFavours()) {
                stringBuilder.append(in).append("\t");
            }
            stringBuilder.append("\n");
            nickNum++;
        }
        return stringBuilder.toString();
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public Integer getPositions(String name) {
        return positions.get(nicknames.indexOf(name));
    }
}





