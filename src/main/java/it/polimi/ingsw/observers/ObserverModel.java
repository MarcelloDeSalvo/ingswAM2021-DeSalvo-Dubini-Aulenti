package it.polimi.ingsw.observers;

import it.polimi.ingsw.model.player.deposit.Deposit;
import it.polimi.ingsw.observers.gameListeners.CardGridListener;
import it.polimi.ingsw.observers.gameListeners.FaithPathListener;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public interface ObserverModel extends FaithPathListener, CardGridListener {
    void printHello();
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
     * Prints the players' turn order
     */
    void printOrder(ArrayList<String> randomOrder);

    void printLobby(ArrayList<String> lobbiesInfos);
    void printDeposit(Deposit deposit, String depositInfo);
    void printHand(ArrayList<Integer> leaderIDs, String nickname);
    void printItsYourTurn(String nickname);

    void askForResources(String nickname, int qty);
    void askForLeaderCardID(String nickname);

    void notifyBoughtCard(String nickname);
    void notifyGameSetup(ArrayList<Integer> cardGridIDs, ArrayList<String> nicknames);
    void notifyLeaderDiscarded(int id, String nickname);
    void notifyLeaderActivated(int id, String nickname);
    void notifyLastTurn();
    void notifyWinner(ArrayList<String> winners);

}
