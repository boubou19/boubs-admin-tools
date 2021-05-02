package com.boubou19.admintools.integration.oc;

import com.boubou19.admintools.AdminTools;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LuaConverter {

    private static String formatEntry(Map<String, String> entry, String entryName, boolean overall){
        if (!overall){
            String[] s = new String[]{
                    "  [\"",
                    entryName,
                    "\"]={[\"loaded entities\"]=\"",
                    entry.get("loaded entities"),
                    "\", [\"name\"]=\"",
                    entry.get("name"),
                    "\", [\"tick time\"]=\"",
                    entry.get("tick time"),
                    "\", [\"loaded tile entities\"]=\"",
                    entry.get("loaded tile entities"),
                    "\", [\"loaded chunks\"]=\"",
                    entry.get("loaded chunks"),
                    "\"},"};
            return String.join("", Arrays.asList(s)) ;
        }

        String[] s = new String[]{
                "  [\""+entryName,
                "\"]={[\"loaded entities\"]=\"",
                entry.get("loaded entities"),
                "\", [\"loaded dims\"]=\"",
                entry.get("loaded dims"),
                "\", [\"tick time\"]=\"",
                entry.get("tick time"),
                "\", [\"loaded tile entities\"]=\"",
                entry.get("loaded tile entities"),
                "\", [\"loaded chunks\"]=\"",
                entry.get("loaded chunks"),
                "\"}"};
        return String.join("", Arrays.asList(s)) ;
    }

    private static List<String> getLuaTPSTable(Map<String, Map<String, String>> tpsMap){
        String key;
        List<String> table = new ArrayList<>();

        table.add("tps = {");

        for (int i = 1; i < tpsMap.size() - 1; i++){
            key = Integer.toString(i);
            String line = formatEntry(tpsMap.get(key), key, false);
            table.add(line);
        }

        key = "overall";
        String line = formatEntry(tpsMap.get(key), key, true);
        table.add(line);
        table.add("}");
        table.add("return tps");

        return table;
    }

    private static Map<String, String> createMapEntry(String entry, boolean overall){
        String[] line, splitNameTicktime, loadedChunksArray, loadedEntitiesArray, loadedTEArray, loadedDimsArray;
        String name, tickTime, loadedChunks, loadedEntities, loadedTE, loadedDims;
        Map<String, String> tpsEntry = new TreeMap<>();

        line = entry.replace("\n", "").split("\\. ");

        splitNameTicktime = line[0].split(":");
        splitNameTicktime[1] = splitNameTicktime[1].split(" \\/ ")[splitNameTicktime.length - 1].replace(" ms", "");

        name = splitNameTicktime[0];
        tickTime = splitNameTicktime[1];

        loadedChunksArray = line[1].split(": ");
        loadedChunks = loadedChunksArray[loadedChunksArray.length - 1];

        loadedEntitiesArray = line[2].split(": ");
        loadedEntities = loadedEntitiesArray[loadedEntitiesArray.length - 1];

        loadedTEArray = line[3].replace(".", "").split(": ");
        loadedTE = loadedTEArray[loadedTEArray.length - 1];

        tpsEntry.put("tick time", tickTime);
        tpsEntry.put("loaded chunks", loadedChunks);
        tpsEntry.put("loaded entities", loadedEntities);
        tpsEntry.put("loaded tile entities", loadedTE);
        if (!overall){
            tpsEntry.put("name", name);
        }
        else{
            loadedDimsArray = line[line.length - 1].replace(".", "").split(": ");
            loadedDims = loadedDimsArray[loadedDimsArray.length - 1];
            tpsEntry.put("loaded dims" , loadedDims);
        }

        return tpsEntry;
    }

    public static void processTps(List<String> tpsList, List<Path> computers){
        ListIterator<String> iterator = tpsList.listIterator();
        Map<String, Map<String, String>> tpsTable = new TreeMap<>();
        Map<String, String> mapEntry;

        while (iterator.hasNext()){
            iterator.next();
        }

        mapEntry = createMapEntry(iterator.previous(), true);
        tpsTable.put("overall", mapEntry);

        int i = 1;
        while (iterator.hasPrevious()){
            mapEntry = createMapEntry(iterator.previous(), false);
            tpsTable.put(Integer.toString(i), mapEntry);
            i += 1;
        }

        List<String> tps = getLuaTPSTable(tpsTable);
        for (Path computer : computers){
            AdminTools.writeToDedicatedLogFile(Paths.get(computer.toString(), "tps.lua"), tps);
        }
    }
}
