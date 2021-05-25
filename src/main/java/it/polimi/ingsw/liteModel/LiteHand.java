package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;

public class LiteHand {
    private final ArrayList<Integer> hand;
    private final ArrayList<LeaderCard> leaderCards;

    public LiteHand(ArrayList<Integer> hand, ArrayList<LeaderCard> leaderCards) {
        this.hand = hand;
        this.leaderCards = new ArrayList<>(leaderCards);

        for (Integer id : hand) {
            leaderCards.add(leaderCards.get(id-1));
        }
    }

    public LiteHand(ArrayList<LeaderCard> leaderCards) {
        this.hand=new ArrayList<>();
        this.leaderCards = new ArrayList<>(leaderCards);
    }


    public void discardFromHand(Integer id) throws NullPointerException, IllegalArgumentException{
        hand.remove(id);
    }

    public void activateLeader(int id){
         leaderCards.get(id-1).setStatus(Status.ACTIVE);
    }

    public void addLeader(int id){
        hand.add(id);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\n");

        s.append("These the Leader Cards in your ").append(Color.ANSI_BLUE.escape()).append("HAND").append(Color.ANSI_RESET.escape()).append(":\n");

        for (int id: hand) {
            s.append("-------------------------------------------------------------------\\");
            s.append("\n");
            s.append(leaderCards.get(id-1).toString());
            s.append("\n");
            s.append("-------------------------------------------------------------------/\n\n");
        }

        if(hand.isEmpty())
            s.append("\n").append(Color.ANSI_RED.escape()).append("EMPTY").append(Color.ANSI_RESET.escape());

        return s.toString();
    }
}
