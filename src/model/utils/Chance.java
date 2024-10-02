package model.utils;

import java.util.Random;

public class Chance extends Square {
    private int amountToChange;
    private final Random rng;
    public Chance(int position, String name){
        super(position,name);
        this.passEffective = false;
        this.amountToChange = 0;
        this.rng = new Random();
    }
    @Override
    public String effectLine(Player player){
        if(this.amountToChange > 0){
            return player.getName() + " just landed on the " + this.name + " square!";
        }
        return player.getName() + " just landed on the " + this.name + " square!";
    }
    @Override
    public void affectPlayer(Player player){
        this.amountToChange = drawChange();
        if(this.amountToChange > 0){
            System.out.println("$" + this.amountToChange + " amount gained for " + player.getName() + ".");
            player.addMoney(this.amountToChange);
        }else{
            System.out.println("$" + Math.abs(this.amountToChange) + " amount lost for " + player.getName() + ".");
            player.reduceMoney(Math.abs(this.amountToChange));
        }
    }
    private int drawChange() {
        int index = rng.nextInt(CHANCE_MONEY_CHANGES.length);
        return CHANCE_MONEY_CHANGES[index];
    }
}
