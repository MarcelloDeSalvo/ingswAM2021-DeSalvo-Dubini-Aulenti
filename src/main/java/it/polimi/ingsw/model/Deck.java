package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.util.*;

public class Deck{
    private LinkedList<DevelopmentCard> deck;

    public Deck() {
        this.deck = new LinkedList<>();
    }


    public ArrayList<DevelopmentCard> randomInsert(ArrayList<DevelopmentCard> cardsList){
        Collections.shuffle(cardsList);
        for (DevelopmentCard card: cardsList) {
            deck.push(card);
        }
        return cardsList;
    }

    //GETTER AND SETTER-------------------------------------------------------------------------------------------------
    public LinkedList<DevelopmentCard> getDeck() {
        return deck;
    }

    public void setDeck(LinkedList<DevelopmentCard> deck) {
        this.deck = deck;
    }
    //------------------------------------------------------------------------------------------------------------------


    //JAVA--------------------------------------------------------------------------------------------------------------
    public String toStringAll() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }

    public String toStringOnTop() {
        return deck.getFirst().toString();
    }
    //------------------------------------------------------------------------------------------------------------------

}
