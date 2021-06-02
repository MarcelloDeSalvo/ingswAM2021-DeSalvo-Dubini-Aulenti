package it.polimi.ingsw.observers;


import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.observers.gameListeners.*;

import java.util.ArrayList;
import java.util.List;

public interface ObserverModel extends FaithPathListener, CardGridListener, VaultListener, DepositListener, ProductionListener {

    /**
     * Simple reply from the server
     */
    void printHello();

    /**
     * Says goodbye
     */
    void printQuit(String nickname);

    /**
     * Multi-player Server-Side: Sends a generic reply to all the players in game <br>
     * Multi-player Client-Side: prints a generic reply received by the server <br><br>
     * Single-player: Prints a generic reply received by the controller
     * @param payload is the generic reply
     */
    void printReply(String payload);

    /**
     * Multi-player Server-Side: Sends a generic reply to a specific player in game <br>
     * Multi-player Client-Side: prints a generic reply received by the server <br><br>
     * Single-player: Prints a generic reply received by the Controller
     * @param payload is the generic reply
     * @param nickname is the nick of the player that needs to be notified
     */
    void printReply_uni(String payload, String nickname);

    /**
     * Multi-player Server-Side: Sends a generic reply to all the players in game except the one<br>
     * Multi-player Client-Side: prints a generic reply received by the server <br><br>
     * Single-player: Prints a generic reply received by the Controller
     * @param payload is the generic reply
     * @param nickname is the nick of the player that needs to be excluded from the notify
     */
    void printReply_everyOneElse(String payload, String nickname);

    /**
     * Notifies a player that it's his turn
     */
    void printItsYourTurn(String nickname);

    /**
     * Asks the player a certain number of resources
     */
    void askForResources(String nickname, int qty);

    /**
     * Asks the player that he should send a leaderID
     */
    void askForLeaderCardID(String nickname);

    /**
     * Asks the player where does he want to put the resources after the selection from the market
     */
    void askForMarketDestination(ArrayList<ResourceContainer> containers, String nickname);

    /**
     * Sends a message containing the setup information for the game
     * @param cardGridIDs the initial iDs of the card grid
     * @param nicknames the players' nicknames
     * @param marketSetUp the starting marbles' positions
     */
    void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames, ArrayList<ResourceContainer> marketSetUp);

    /**
     * Notifies the players that the turn phase is started
     */
    void notifyGameIsStarted();

    /**
     * Sends players the initial ids of the cards in their hands
     * @param leaderIDs the card iDs
     * @param nickname the player
     */
    void notifyCardsInHand(ArrayList<Integer> leaderIDs, String nickname);

    /**
     * Notifies a player that he correctly bought a card
     */
    void notifyBuyOk(String nickname, int slotID, int cardID);

    /**
     * Notifies a player that he could not buy a card
     * @param error is the error for which he couldn't buy the card
     */
    void notifyBuyError(String error);

    /**
     * Notifies a player that he chose a usable slot
     */
    void notifyBuySlotOk(String mex);

    /**
     * Notifies a player that the leader card has been discarded correctly
     * @param id the leader card id
     */
    void notifyLeaderDiscarded(int id, String nickname);

    /**
     * Notifies a player that the leader card has been activated correctly
     * @param id the leader card id
     */
    void notifyLeaderActivated(int id, String nickname);

    /**
     * Notifies a player that the production has been activated correctly
     */
    void notifyProductionOk(String senderNick);

    /**
     * Notifies a player that the deposit has changed correctly after the manage deposit command
     */
    void notifyMoveOk(String senderNick);

    /**
     * Notifies all players the new information about the marbles' positions
     */
    void notifyMarketUpdate(String selection, int selected);

    /**
     * Notifies all players that they are on the final turn
     */
    void notifyLastTurn();

    /**
     * Notifies all players who won
     */
    void notifyWinner(ArrayList<String> winners);

    /**
     * Notifies all players the scoreboard
     * @param playersTotalVictoryPoints the players' points
     */
    void notifyScores(List<Integer> playersTotalVictoryPoints,ArrayList<String> nicknames);

    /**
     * Notifies all players that the game is over
     */
    void notifyGameEnded();

}
