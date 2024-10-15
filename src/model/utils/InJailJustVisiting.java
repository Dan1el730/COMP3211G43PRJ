package model.utils;

import static controller.InputListener.receivedResponse;
import static controller.InputListener.yesResponse;

public class InJailJustVisiting extends Square{
    private Dice jailDice;
    public InJailJustVisiting(int position, String name){
        super(position, name);
        this.jailDice = new Dice();
        this.passEffective = true;
    }
    @Override
    public String effectLine(Player player){
        if(player.isJailed()){
            return player.getName() + " has been jailed in the " + this.name + " square! Make your choice.";
        } else if(player.getPosition() == this.position){
            return player.getName() + " just landed the " + this.name + " square! It was a visit.";
        } else{
            return player.getName() + " just passed the " + this.name + " square! It was a visit.";
        }
    }
    @Override
    public void affectPlayer(Player player){
        boolean yielded = false;
        if(player.isJailed()){
            if(player.getJailCounter() < 2 && player.getMoney() >= JAIL_PENALTY){

                System.out.println("You now have $" + player.getMoney() + ".");
                System.out.println("An offer to pay $" + JAIL_PENALTY + " fine instead of betting two same faces, yield? (Y/ any key)");
                if(yesResponse()){
                    player.reduceMoney(JAIL_PENALTY);
                    player.unjail();
                    player.useTurn(jailDice);
                    yielded = true;
                }
            }
            if(!yielded){
                System.out.println("Attempt to throw first dice. (Press any key)");
                receivedResponse();
                int firstFace = jailDice.getNewFace();
                System.out.println("The face was " + firstFace + "\n");
                System.out.println("Attempt to throw second dice. (Press any key)");
                receivedResponse();
                int secondFace = jailDice.getNewFace();
                System.out.println("The face was " + secondFace + "\n");
                if(firstFace == secondFace){
                    System.out.println("The faces are same, you succeeded in getting out of jail.");
                    player.unjail();
                    player.useTurn(firstFace+secondFace);
                }else{
                    System.out.println("The faces are not same, you are still jailed for " + player.getJailCounter() + " turns.");
                    player.passJailTurn();
                }
                if(player.getJailCounter() == -1){
                    System.out.println("You are forced to pay a fine of $" + JAIL_PENALTY + " to get out of jail and move this turn according to two dices you threw.");
                    player.reduceMoney(JAIL_PENALTY);
                    player.unjail();
                    player.checkRetirement();
                    if(!player.isRetired()){
                        player.useTurn(firstFace+secondFace);
                    }
                }
            }
        }
        //does not have any effect if player is not jailed
    }
}
