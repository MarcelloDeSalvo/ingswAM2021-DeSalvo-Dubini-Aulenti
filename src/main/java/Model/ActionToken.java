package Model;

import Model.Player.Player;

interface Action{
    Boolean doAction(Game g);
}

public class ActionToken {
    private Action action;
    private int randomNumber;
    //Genera un numero a caso che poi serve a mettere in action l'abilità casuale


}
