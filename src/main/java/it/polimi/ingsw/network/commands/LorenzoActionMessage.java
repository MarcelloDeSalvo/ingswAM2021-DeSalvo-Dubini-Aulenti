package it.polimi.ingsw.network.commands;

import it.polimi.ingsw.model.cards.Colour;

public class LorenzoActionMessage extends Message{
    private final int actionID;
    private final Colour colour;

    /**
     * Message from the server. Used to notify that Lorenzo performed an Action
     */
    public LorenzoActionMessage(int actionID, Colour colour, Target target) {
        super(new MessageBuilder().setTarget(target).setCommand(Command.NOTIFY_LORENZO_ACTION));
        this.actionID = actionID;
        this.colour = colour;
    }

    public int getActionID() {
        return actionID;
    }

    public Colour getColour() {
        return colour;
    }
}
