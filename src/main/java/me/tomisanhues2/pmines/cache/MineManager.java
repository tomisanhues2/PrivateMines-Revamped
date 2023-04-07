package me.tomisanhues2.pmines.cache;

import me.tomisanhues2.pmines.data.PrivateMine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MineManager {

    private final HashMap<UUID, PrivateMine> privateMines = new HashMap<>();
    public int mineCount = 1;

    private final MinePersistenceHandler mineDataPersistenceHandler;

    public MineManager(MinePersistenceHandler mineDataPersistenceHandler) {
        this.mineDataPersistenceHandler = mineDataPersistenceHandler;
        loadAllMineData();
    }

    private void addMine(PrivateMine privateMine) {
        if (privateMines.containsKey(privateMine.uuid)) {
            return;
        }
        privateMines.put(privateMine.uuid, privateMine);
        privateMine.createRegions();
        privateMine.fillMine();
        privateMine.startTasks();
    }

    public void loadMineData(UUID uuid) {
        PrivateMine privateMine = mineDataPersistenceHandler.loadMineData(uuid);
        if (privateMine == null) {
            return;
        }
        addMine(privateMine);
        return;
    }

    public void unloadMineData(UUID uuid) {
        PrivateMine privateMine = privateMines.get(uuid);
        mineDataPersistenceHandler.saveMineData(privateMine);
        privateMines.remove(uuid);
    }

    public void loadAllMineData() {
        File mineDataFolder = new File("mineData");
        File[] mineDataFiles = mineDataFolder.listFiles();
        if (mineDataFiles == null) {
            return;
        }
        for (File mineDataFile : mineDataFiles) {
            if (mineDataFile.isFile()) {
                String fileName = mineDataFile.getName();
                if (fileName.endsWith(".yml")) {
                    String uuidString = fileName.substring(0, fileName.length() - 4);
                    UUID uuid = UUID.fromString(uuidString);
                    loadMineData(uuid);
                }
            }
        }
    }

    public void saveAllMines() {
        for (PrivateMine privateMine : privateMines.values()) {
            mineDataPersistenceHandler.saveMineData(privateMine);
        }
    }

    public PrivateMine createNewMine(PrivateMine privateMine) {

        addMine(privateMine);
        return privateMine;
    }

    public int getMineCount() {
        return mineCount;
    }

    public PrivateMine getMine(UUID uuid) {
        return privateMines.get(uuid);
    }

    public List<PrivateMine> getMines() {
        return new ArrayList<>(privateMines.values());
    }

    public HashMap<UUID, PrivateMine> getMineMap() {
        return privateMines;
    }

}
