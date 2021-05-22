package com.boubou19.admintools;

import net.minecraft.entity.player.EntityPlayer;
import org.json.simple.JSONObject;

public class PlayerCoords{
    public int x, y, z, dim;

    PlayerCoords(JSONObject coords){
        this.x = ((Long) coords.get("x")).intValue();
        this.y = ((Long) coords.get("y")).intValue();
        this.z = ((Long) coords.get("z")).intValue();
        this.dim = ((Long) coords.get("dim")).intValue();
    }

    PlayerCoords(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event){
        this.x = (int) event.player.posX;
        this.y = (int) event.player.posY;
        this.z = (int) event.player.posZ;
        this.dim = event.player.dimension;
    }

    PlayerCoords(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event){
        this.x = (int) event.player.posX;
        this.y = (int) event.player.posY;
        this.z = (int) event.player.posZ;
        this.dim = event.player.dimension;
    }

    public PlayerCoords(EntityPlayer player){
        this.x = (int) player.posX;
        this.y = (int) player.posY;
        this.z = (int) player.posZ;
        this.dim = player.dimension;
    }

    public PlayerCoords( int x, int y, int z, int dim){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    public int[] buildArray(){
        return new int[]{this.x, this.y, this.z, this.dim};
    }

    public boolean sameCoordAs(PlayerCoords other){
        int[] array = this.buildArray();
        int[] otherArray = other.buildArray();
        for (int i=0; i < 3; i++){
            if (array[i] != otherArray[i]){
                return false;
            }
        }
        return true;
    }

    public JSONObject getJsonObject(){
        JSONObject obj = new JSONObject();
        obj.put("x", this.x);
        obj.put("y", this.y);
        obj.put("z", this.z);
        obj.put("dim", this.dim);
        return obj;
    }

    @Override
    public String toString() {
        return " x:" + this.x + " y:" + this.y + " z:" + this.z + " dim:" + this.dim;
    }
}
