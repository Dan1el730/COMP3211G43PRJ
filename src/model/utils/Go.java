package model.utils;

public class Go extends Square{
    public Go(int position, String name) {
        super(position,name);
        this.passEffective = true;
    }
    @Override
    public String effectLine(Player player){
        return player.getName() + " just passed the " + this.name + " square! $1500 salary received.";
    }
    @Override
    public void affectPlayer(Player player){
        player.addMoney(GO_INCOME);
    }
    @Override
    public String getStatus(){
        return this.position + ". " + this.name;
    }
}
