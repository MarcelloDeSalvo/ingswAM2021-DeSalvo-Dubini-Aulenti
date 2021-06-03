package it.polimi.ingsw.model;

import it.polimi.ingsw.observers.ObservableModel;
import it.polimi.ingsw.observers.ObserverModel;

import java.util.ArrayList;
import java.util.Collections;

public class Lorenzo implements ObservableEndGame, ObservableModel {
    private ArrayList<ActionToken> actionTokens;
    private final ArrayList<ObserverEndGame> observersEndGame;

    public Lorenzo() {
        this.observersEndGame = new ArrayList<>();
    }

    public Lorenzo(ArrayList<ActionToken> actionTokens) {
        this.actionTokens = actionTokens;
        this.observersEndGame = new ArrayList<>();
        Collections.shuffle(this.actionTokens);
    }

    public Lorenzo(ArrayList<ActionToken> actionTokens, boolean test) {
        this.actionTokens = actionTokens;
        this.observersEndGame = new ArrayList<>();
        //Collections.shuffle(this.actionTokens);
    }

    //LORENZO'S METHODS-------------------------------------------------------------------------------------------------
    public void shuffleActionTokens() {
        Collections.shuffle(actionTokens);
    }

    /**
     * Activates the actions of the first ActionToken (the one on top) <br>
     * then moves the "used" token in the back of the ArrayList
     * @param g game
     */
    public void pickAction(Game g) {
        for (Action action : actionTokens.get(0).getActions()) {
            action.doAction(g);
        }

        actionTokens.add(actionTokens.get(0));
        actionTokens.remove(0);
    }
    //------------------------------------------------------------------------------------------------------------------


    //OBSERVER METHODS--------------------------------------------------------------------------------------------------
    @Override
    public void notifyEndGame() {
        for (ObserverEndGame observer : this.observersEndGame) {
            observer.updateEndGame();
        }

    }

    @Override
    public void addObserver(ObserverEndGame observerEndGame) {
        observersEndGame.add(observerEndGame);
    }

    @Override
    public void removeObserver(ObserverEndGame observerEndGame) {
        observersEndGame.remove(observerEndGame);
    }
    //------------------------------------------------------------------------------------------------------------------


    @Override
    public void addView(ObserverModel view) {
        for (ActionToken actionToken: actionTokens)
            actionToken.addActionTokenListener(view);
    }

    //GETTERS-----------------------------------------------------------------------------------------------------------
    public ArrayList<ActionToken> getActionTokens() {
        return actionTokens;
    }

    public ArrayList<ObserverEndGame> getObserversEndGame() {
        return observersEndGame;
    }
    //------------------------------------------------------------------------------------------------------------------
}

/*
                                       _ . - = - . _
                                   . "  \  \   /  /  " .
                                 ,  \                 /  .
                               . \   _,.--~=~"~=~--.._   / .
                              ;  _.-"  / \ !   ! / \  "-._  .
                             / ,"     / ,` .---. `, \     ". \              LORENZO'S WATCHING YOU
                            /.'   `~  |   /:::::\   |  ~`   '.\
                            \`.  `~   |   \:::::/   | ~`  ~ .'/
                             \ `.  `~ \ `, `~~~' ,` /   ~`.' /       HE KNOWS WHERE YOU'RE HIDING YOUR RESOURCES
                              .  "-._  \ / !   ! \ /  _.-"  .
                               ./    "=~~.._  _..~~=`"    \.
                                 ,/         ""          \,
                                   . _/             \_ .
                                      " - ./. .\. - "
 */
