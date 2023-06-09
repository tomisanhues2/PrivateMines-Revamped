package me.tomisanhues2.pmines.cache;

import me.tomisanhues2.pmines.PrivateMines;
import me.tomisanhues2.pmines.data.PrivateMine;
import me.tomisanhues2.pmines.data.UpgradeLevel;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MineDataPersistenceHandler implements MinePersistenceHandler {

    private final PrivateMines plugin;

    private FileConfiguration mineDataRead;
    private FileConfiguration mineDataWrite;

    public MineDataPersistenceHandler(PrivateMines plugin) {
        this.plugin = plugin;

    }

    @Override
    public void saveMineData(PrivateMine privateMine) {
        File file = new File(plugin.getDataFolder(),"mineData/" + privateMine.uuid.toString() + ".yml");
        mineDataWrite = YamlConfiguration.loadConfiguration(file);
        mineDataWrite.set("uuid", privateMine.uuid.toString());
        mineDataWrite.set("upgradeLevel", privateMine.upgradeLevel);
        mineDataWrite.set("teleportlocation", privateMine.teleportLocation);
        mineDataWrite.set("centerMinelocation",privateMine.centerMineLocation);
        try {
            mineDataWrite.save(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PrivateMine loadMineData(UUID mineUUID) {
        try {
            File phoneDataFile = new File(plugin.getDataFolder(), "mineData/" + mineUUID + ".yml");
            if (!phoneDataFile.exists()) {
                System.out.println("Failed to load mine data for mine with UUID " + mineUUID);
                return null;
            }
            mineDataRead = YamlConfiguration.loadConfiguration(phoneDataFile);
            UUID uuid = UUID.fromString(mineDataRead.getString("uuid"));
            UpgradeLevel upgradeLevel = (UpgradeLevel) mineDataRead.get("upgradeLevel");
            Location teleportLocation = (Location) mineDataRead.get("teleportLocation");
            Location centerMineLocation = (Location) mineDataRead.get("centerMineLocation");
            return new PrivateMine(uuid, upgradeLevel, teleportLocation, centerMineLocation);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load mine data for mine with UUID " + mineUUID);
            return null;
        }
    }
}
