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
        this.leaderCards = new ArrayList<>();

        for (Integer id : hand) {
            leaderCards.add(leaderCards.get(id-1));
        }
    }

    public void discardFromHand(Integer id) throws NullPointerException, IllegalArgumentException{
        hand.remove(id);
    }

    public void activateLeader(int id){
         leaderCards.get(id-1).setStatus(Status.ACTIVE);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\n");

        s.append("These the Leader Cards in your ").append(Color.ANSI_CYAN.escape()).append("HAND").append(Color.ANSI_RESET.escape()).append(":\n");

        for (int id: hand) {
            s.append("-------------------------------------------------------------------\\");
            s.append("\n");
            s.append(leaderCards.get(id - 1).toString());
            s.append("\n");
            s.append("-------------------------------------------------------------------/\n\n");
        }

        return s.toString();
    }
}
