package model.utils;

public class IncomeTax extends Square{
    public IncomeTax(int position, String name) {
        super(position, name);
        this.passEffective = false;
    }
    @Override
    public String effectLine(Player player){
        return player.getName() + " just landed on the " + this.name + " square! $" + calculateTax(player) + " income collected.";
    }
    @Override
    public void affectPlayer(Player player){
        player.reduceMoney(calculateTax(player));
    }
    private int calculateTax(Player player){
        return ((int)Math.floor(player.getMoney()*INCOME_TAX_RATE/10.0) * 10);
    }
}
