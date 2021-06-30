package it.polimi.ingsw.view.gui.customJObject;

import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.commands.Command;
import it.polimi.ingsw.network.commands.ManageDepositMessage;
import it.polimi.ingsw.network.commands.SendContainer;
import it.polimi.ingsw.network.commands.SwitchDepositMessage;
import it.polimi.ingsw.view.gui.Gui;

import javax.swing.*;

public class DepositMenu extends JMenu {

    private final ResourceType selectedResource;
    private final Gui gui;
    private final int source;

    public DepositMenu(Gui gui, String s, ResourceType selectedResource, Command command, int source) {
        super(s);
        this.gui = gui;
        this.selectedResource = selectedResource;
        this.source = source;

        int itemSize = gui.getMyLiteDeposit().getDeposits().size();
        JMenuDeposit item;

        for (int i = 0; i<itemSize; i++){
            if (!gui.getMyLiteDeposit().isLeaderType(i)){
                item = new JMenuDeposit("Deposit " +(i+1), i+1, command);
            }else {
                item = new JMenuDeposit("Leader Storage " + gui.getMyLiteDeposit().getType(i).deColored(), i+1, command);
            }

            this.add(item);
        }
    }


    private class JMenuDeposit extends JMenuItem{
        private final int id;

        public JMenuDeposit(String text, int id, Command command) {
            super(text);
            this.id = id;
            this.addActionListener( e->{
                if(selectedResource == ResourceType.BLANK)
                    return;
                if (command==Command.SEND_DEPOSIT_ID)
                    gui.send(new SendContainer(Command.SEND_DEPOSIT_ID, new ResourceContainer(selectedResource,1), "DEPOSIT", id, gui.getNickname()));
                if (command==Command.SWITCH_DEPOSIT)
                    gui.send(new SwitchDepositMessage(source, id, gui.getNickname()));
                if (command==Command.MANAGE_DEPOSIT)
                    gui.send(new ManageDepositMessage(1, source, id, gui.getNickname()));
            });
        }

        public int getId() {
            return id;
        }
    }

}
