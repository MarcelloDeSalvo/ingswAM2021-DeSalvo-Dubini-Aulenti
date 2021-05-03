package it.polimi.ingsw.view;


public abstract class ClientView implements View, UserInput {
    String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
