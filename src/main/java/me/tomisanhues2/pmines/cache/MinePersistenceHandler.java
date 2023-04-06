package me.tomisanhues2.pmines.cache;

import me.tomisanhues2.pmines.data.PrivateMine;

import java.util.UUID;

public interface MinePersistenceHandler {

    void saveMineData(PrivateMine phone);

    PrivateMine loadMineData(UUID phoneUUID);
}
