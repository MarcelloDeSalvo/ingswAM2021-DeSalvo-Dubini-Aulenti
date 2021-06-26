package it.polimi.ingsw.liteModel;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.Status;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;
import java.util.List;

public class LiteHand {
    private final List<Integer> hand;
    private final List<LeaderCard> leaderCards;

    public LiteHand(List<Integer> hand, List<LeaderCard> leaderCards) {
        this.hand = hand;
        this.leaderCards = new ArrayList<>(leaderCards);

        for (LeaderCard ld: this.leaderCards)
            ld.setStatus(Status.HAND);
    }

    public LiteHand(List<LeaderCard> leaderCards) {
        this.hand = new ArrayList<>();
        this.leaderCards = new ArrayList<>(leaderCards);

        for (LeaderCard ld:  this.leaderCards)
            ld.setStatus(Status.HAND);
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

    public String toString(String player) {
        StringBuilder s = new StringBuilder("\n");

        s.append("These are the Leader Cards in ").append(player).append("'s ").append(Color.ANSI_BLUE.escape()).append("HAND").append(Color.ANSI_RESET.escape()).append(":\n");

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

    public List<Integer> getHand() {
        return hand;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public LeaderCard getSpecificLeaderCard (int requestedID) {
        for (LeaderCard lc: leaderCards) {
            if(lc.getId() == requestedID)
                return lc;
        }
        return null;
    }

    public Status getStatusFromSpecificLeaderCard (int requestedID) {
        for (LeaderCard lc: leaderCards) {
            if(lc.getId() == requestedID)
                return lc.getStatus();
        }

        return null;
    }

    public Integer getLeadersActiveNumber(){
        int activated=0;
        for (Integer in:hand) {
            if (getSpecificLeaderCard(in).getStatus()==Status.ACTIVE)
                activated++;
        }
        return activated;
    }
}
