package model.utils;

public abstract class Square implements GAME_CONSTANTS{
    protected String name;
    protected boolean passEffective;
    protected int position;
    public Square(int position, String name){
        this.position = position;
        this.name = name;
    }
    public abstract String effectLine(Player player);
    public abstract void affectPlayer(Player player);
}
