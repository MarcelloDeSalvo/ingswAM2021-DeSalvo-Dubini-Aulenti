package it.polimi.ingsw.view.gui.customJObject;

import it.polimi.ingsw.view.gui.buttons.ButtonImage;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableWithButtons extends JPanel {

    public TableWithButtons() {
        super();
        this.setLayout(new BorderLayout());
        JTable table = new JTable(new LobbyTable());
        TableCellRenderer tableRenderer;
        tableRenderer = table.getDefaultRenderer(JoinButton.class);
        table.setDefaultRenderer(JoinButton.class, new JTableButtonRenderer(tableRenderer));
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }

}

class LobbyTable extends AbstractTableModel {
    private final String[] columns = {"Lobby", "Owner", "Connected", "Full", "Closed", "Join"};
    private Object[][] rows = new Object[][]{
        {"Lobby1", "Ciccio", "2/4", "yes", "false", new JoinButton("join", "NOME LOBBY")},
        {"Lobby2", "Pippo", "1/4", "yes", "false", new JoinButton("join", "NOME LOBBY") },
    };

    public String getColumnName(int column) {
        return columns[column];
    }
    public int getRowCount() {
        return rows.length;
    }
    public int getColumnCount() {
        return columns.length;
    }
    public Object getValueAt(int row, int column) {
        return rows[row][column];
    }
    public boolean isCellEditable(int row, int column) {
        if (column == 4) {
            return true;
        } else {
            return false;
        }
    }

    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}

class JTableButtonRenderer implements TableCellRenderer {
    private final TableCellRenderer defaultRenderer;

    public JTableButtonRenderer(TableCellRenderer renderer) {
        defaultRenderer = renderer;
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof Component)
            return (Component)value;
        return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

class JoinButton extends ButtonImage{
    private String lobbyName;

    public JoinButton(String text, String lobbyName) {
        super(text,15, true);
        this.lobbyName = lobbyName;
        this.addActionListener( e-> System.out.println(lobbyName)
        );
    }


}
