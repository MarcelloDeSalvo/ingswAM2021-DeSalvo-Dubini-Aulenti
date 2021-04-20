package model.cards;

/**
 * Defines one Card's state <br>
 * ACTIVE: the card (leaderCard) has been activated by a player <br>
 * ON_TOP: the card is on top of a production deck <br>
 * HAND: the card is in a player's hand <br>
 * PURCHASABLE: the card is inside the card grid <br>
 */
public enum Status {
    ACTIVE, ON_TOP, HAND, DISCARDED, PURCHASABLE
}
