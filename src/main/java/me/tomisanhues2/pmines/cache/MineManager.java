package me.tomisanhues2.pmines.cache;

import me.tomisanhues2.pmines.data.PrivateMine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MineManager {

    private final HashMap<UUID, PrivateMine> privateMines = new HashMap<>();

    private final MinePersistenceHandler mineDataPersistenceHandler;

    public MineManager(MinePersistenceHandler mineDataPersistenceHandler) {
        this.mineDataPersistenceHandler = mineDataPersistenceHandler;
    }

    public void loadMineData(UUID uuid) {
        PrivateMine privateMine = mineDataPersistenceHandler.loadMineData(uuid);
        privateMines.put(uuid, privateMine);
    }

    public void unloadMineData(UUID uuid) {
        PrivateMine privateMine = privateMines.get(uuid);
        mineDataPersistenceHandler.saveMineData(privateMine);
        privateMines.remove(uuid);
    }

    public PrivateMine createNewMine(PrivateMine privateMine) {

        privateMines.put(privateMine.uuid, privateMine);
        return privateMine;
    }

    public int getMineCount() {
        return privateMines.size();
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
