package Model;

import Model.Cards.DevelopmentCard;

import javax.smartcardio.Card;
import java.util.LinkedList;
import java.util.Queue;

public class Deck{
    private Queue<DevelopmentCard> deck;

    public Deck() {
        this.deck = new LinkedList<DevelopmentCard>();
    }


    //getterAndSetter
    public Queue<DevelopmentCard> getDeck() {
        return deck;
    }

    public void setDeck(Queue<DevelopmentCard> deck) {
        this.deck = deck;
    }
}
