package model;

public interface ObserverFaithPath {
    /**
     * Update FaithPath so that the right amount of FAITHPOINTs is added to a player position
     * @param faithPoints qty of FAITHPOINTs
     */
    void update(int faithPoints);

    /**
     * Method called when a player overflows Resources in Deposit. It updates the position of every other player by <br>
     * the param
     * @param faithPoints qty of FAITHPOINTs
     */
    void updateEveryOneElse(int faithPoints);


}
