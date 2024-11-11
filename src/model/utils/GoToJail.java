package model.utils;

public class GoToJail extends Square{
    private final int inJailJustVisitingPosition;
    public GoToJail(int position, String name, int ijjvIndex){
        super(position,name);
        this.passEffective = false;
        this.inJailJustVisitingPosition = ijjvIndex + 1;
    }
    @Override
    public String effectLine(Player player){
        return player.getName() + " just landed on the " + this.name + " square! " + player.getName() + " is now In Jail.";
    }
    @Override
    public void affectPlayer(Player player){
        player.jail();
        player.setPosition(inJailJustVisitingPosition);
    }
    @Override
    public String getStatus(){
        return this.position + ". " + this.name;
    }
}
