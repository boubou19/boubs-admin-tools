package com.boubou19.admintools;

import com.google.common.collect.Lists;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Path;
import java.util.List;


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

    // credit to Namikon from AMDI for this method
    public static List<String> getMatchedPlayers(Path offlineDataPath, String actualPrefix){
            File saveFolder = new File(offlineDataPath.toAbsolutePath().toString());
            File[] files = saveFolder.listFiles( new FilenameFilter(){
                @Override
                public boolean accept( File dir, String name )
                {
                    return name.startsWith( actualPrefix );
                }
            } );

            List<String> result = Lists.newArrayList();

            for( File f : files )
            {
                String name = f.getName();
                result.add( name.substring(0, name.length() - 5 ) );
            }
            return result;
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

