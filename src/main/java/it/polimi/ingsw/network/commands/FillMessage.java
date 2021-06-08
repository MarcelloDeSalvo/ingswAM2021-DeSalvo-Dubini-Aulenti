package it.polimi.ingsw.network.commands;

public class FillMessage extends Message {
    int slotID;
    int qmi;
    int qmo;

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
