package model.utils;

public class InJailJustVisiting extends Square{
    public InJailJustVisiting(int position, String name){
        super(position, name);
        this.passEffective = true;
    }
    @Override
    public String effectLine(Player player){
        if(player.isJailed()){
            return player.getName() + " has been jailed in the " + this.name + " square! Make your choice.";
        }
        return player.getName() + " just passed the " + this.name + " square! It was a visit.";
    }
    @Override
    public void affectPlayer(Player player){
    }
}
