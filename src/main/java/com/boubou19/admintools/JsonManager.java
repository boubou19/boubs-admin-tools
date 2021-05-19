package com.boubou19.admintools;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;


public class JsonManager {
    public static void main(String[] args) {
        {
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader("test.json"))
            {
                //Read JSON file
                JSONObject obj = (JSONObject) jsonParser.parse(reader);
                for (Object key : obj.keySet()){
                    PlayerCoords pc = new PlayerCoords((JSONObject) obj.get(key));
                    System.out.println(pc);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public static PlayerCoords readOfflinePlayerCoords(Path playerFile){
        JSONParser jsonParser = new JSONParser();
        PlayerCoords playerCoords = null;
        try {
            FileReader reader = new FileReader(playerFile.toAbsolutePath().toFile());
            //Read JSON file
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            playerCoords = new PlayerCoords(obj);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return playerCoords;
    }

    public static boolean writeOfflinePlayerCoords(Path path, PlayerCoords playerCoords){
        boolean success = false;
        try (FileWriter file = new FileWriter(path.toAbsolutePath().toString())) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(playerCoords.getJsonObject().toJSONString());
            file.flush();
            success = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}

class PlayerCoords{
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
