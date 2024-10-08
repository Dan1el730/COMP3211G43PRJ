package model.utils;

public class FreeParking extends Square{
    public FreeParking(int position, String name){
        super(position,name);
        this.passEffective = true;
    }
    @Override
    public String effectLine(Player player){
        return player.getName() + " just passed the " + this.name + " square! Nothing happened.";
    }
    @Override
    public void affectPlayer(Player player){
        //Free parking has no effect!
    }
}
