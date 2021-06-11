package it.polimi.ingsw.network.commands;

public class FillMessage extends Message {
    private final int slotID;
    private final int qmi;
    private final int qmo;

    /**
     * Message from the server. Used to notify the player to start filling the "Question Marks" for a production
     * @param slotID selected Production Slot
     * @param qmi amount of "Question Marks" on input
     * @param qmo amount of "Question Marks" on output
     */
    public FillMessage(int slotID, int qmi, int qmo, String senderNick) {
        super(new MessageBuilder().setCommand(Command.START_FILL).setNickname(senderNick));
        this.slotID = slotID;
        this.qmi = qmi;
        this.qmo = qmo;
    }

    public int getSlotID() {
        return slotID;
    }

    public int getQmi() {
        return qmi;
    }

    public int getQmo() {
        return qmo;
    }
}
