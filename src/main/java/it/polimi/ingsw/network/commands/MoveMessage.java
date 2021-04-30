package it.polimi.ingsw.network.commands;

public class MoveMessage extends Message{
    int x;
    int y;
    String messaggio;

    public MoveMessage(int x, int y, String messaggio) {
        super(Command.MOVE);
        this.x = x;
        this.y = y;
        this.messaggio = messaggio;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    @Override
    public String toString() {
        return "Move{" +
                "x=" + x +
                ", y=" + y +
                ", messaggio='" + messaggio + '\'' +
                '}';
    }
}
