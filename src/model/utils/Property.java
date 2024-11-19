package model.utils;

import static controller.InputListener.yesResponse;

public class Property extends Square{
    private int price;
    private int rent;
    private boolean owned;
    private Player owner;
    public Property(int position, String name, int price, int rent){
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.owned = false;
        this.owner = null;
    }
    public String getName(){
        return this.name;
    }
    public boolean isOwned(){
        return this.owned;
    }
    public void beOwnedBy(Player player){
        this.owner = player;
        this.owned = true;
    }
    public void disown(){
        this.owned = false;
        this.owner = null;
    }
    public Player getOwner() {
        return this.owner;
    }
    @Override
    public String effectLine(Player player){
        if(!this.owned){
            return player.getName() + " just landed on the " + this.name + " square! Do you want to buy " + this.name + " for $" + this.price + "?\n" +
                    "(Y to buy /any key to decline)";
        }else if(player.equals(this.owner)){
            return player.getName() + " just landed on the " + this.name + " square, which is the owner of this property!";
        }
        return player.getName() + " just landed on the " + this.name + " square, which is owned by " + this.owner.getName() +
                "! You must pay for its rent : $" + this.rent + ".";
    }
    @Override
    public void affectPlayer(Player player){
        if(!this.owned){
            if(player.getMoney() < this.price){
                System.out.println(player.getName() + " does not have enough money to buy the ownership of " + this.name + "!");
            }else if(!yesResponse()){
                System.out.println(player.getName() + " declined the ownerships of " + this.name + ".");
            }else{
                System.out.println(player.getName() + " just owned " + this.name + " at a price of $" + this.price + ".");
                player.reduceMoney(this.price);
                this.beOwnedBy(player);
            }
        }else if(!player.equals(this.owner)){
            player.reduceMoney(this.rent);
            this.owner.addMoney(this.rent);
            player.checkRetirement();
        }
        //If the property is not owned, there is no action
    }
    @Override
    public String getStatus(){
        String ownershipInformation = "";
        if(this.owned){
            ownershipInformation = "\nOwner : " + this.owner.getName();
        }
        return this.position + ". " + this.name + "\nOwned : " + this.owned + " " + ownershipInformation;
    }
}
