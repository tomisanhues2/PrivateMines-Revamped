package me.tomisanhues2.pmines.cache;

import me.tomisanhues2.pmines.PrivateMines;
import me.tomisanhues2.pmines.data.PrivateMine;
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
        File file = new File(plugin.getDataFolder(), privateMine.uuid.toString() + ".yml");
        mineDataWrite = YamlConfiguration.loadConfiguration(file);

        //TODO set all the fields
        try {
            mineDataWrite.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PrivateMine loadMineData(UUID phoneUUID) {
        try {
            File phoneDataFile = new File(plugin.getDataFolder(), "mineData/" + phoneUUID + ".yml");
            if (!phoneDataFile.exists()) {
                System.out.println("Failed to load mine data for mine with UUID " + phoneUUID);
                return null;
            }
            mineDataRead = YamlConfiguration.loadConfiguration(phoneDataFile);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load mine data for mine with UUID " + phoneUUID);
            return null;
        }
    }
}
