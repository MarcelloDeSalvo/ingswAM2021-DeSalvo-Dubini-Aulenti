package Model;

import Model.Cards.DevelopmentCard;
import javax.smartcardio.Card;
import java.util.*;

public class Deck{
    private Queue<DevelopmentCard> deck;

    public Deck() {
        this.deck = new LinkedList<DevelopmentCard>();
    }


    public ArrayList<DevelopmentCard> randomInsert(ArrayList<DevelopmentCard> cardsList){
        Collections.shuffle(cardsList);
        for (DevelopmentCard card: cardsList) {
            deck.add(card);
        }
        return cardsList;
    }

    //getterAndSetter
    public Queue<DevelopmentCard> getDeck() {
        return deck;
    }

    public void setDeck(PriorityQueue<DevelopmentCard> deck) {
        this.deck = deck;
    }

}
