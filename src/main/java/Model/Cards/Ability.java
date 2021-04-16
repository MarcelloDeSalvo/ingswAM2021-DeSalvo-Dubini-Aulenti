package Model.Cards;
import Model.Player.PlayerBoard_AbilityAccess;

public interface Ability {
    /**
     * Calls the Leader's ability with a Strategy pattern
     * @param playerBoard is the current Player's playerBoard
     * @return true if the ability can be done and finishes without errors
     */
     boolean useAbility(PlayerBoard_AbilityAccess playerBoard);

}
