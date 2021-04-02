package Model.Player;

import Model.Exceptions.DepositSlotMaxDimExceeded;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private PlayerBoard playerBoard;

    public Player(String nickname) {
        this.nickname = nickname;
        this.playerBoard = new PlayerBoard(3,3);
    }

}
