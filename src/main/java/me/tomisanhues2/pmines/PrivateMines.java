package me.tomisanhues2.pmines;

import co.aikar.commands.PaperCommandManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.tomisanhues2.pmines.cache.MineDataPersistenceHandler;
import me.tomisanhues2.pmines.cache.MineManager;
import me.tomisanhues2.pmines.cache.MinePersistenceHandler;
import me.tomisanhues2.pmines.commands.AdminCommands;
import me.tomisanhues2.pmines.commands.PlayerCommands;
import me.tomisanhues2.pmines.data.PrivateMine;
import me.tomisanhues2.pmines.data.UpgradeLevel;
import me.tomisanhues2.pmines.events.PlayerEvents;
import me.tomisanhues2.pmines.utils.ConfigUtils;
import me.tomisanhues2.pmines.utils.PlaceholderUtils;
import me.tomisanhues2.pmines.utils.VoidGenerator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PrivateMines extends JavaPlugin {

    private static PrivateMines instance;

    public MineManager mineManager;

    MineDataPersistenceHandler mineDataPersistenceHandler;

    {
        instance = this;
    }

    public static PrivateMines getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        createEmptyMineWorld();
        ConfigUtils configUtils = new ConfigUtils();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderUtils(this).register();
        }

        ConfigurationSerialization.registerClass(UpgradeLevel.class);

        mineDataPersistenceHandler = new MineDataPersistenceHandler(this);
        mineManager = new MineManager(mineDataPersistenceHandler);

        //getServer().getPluginManager().registerEvent(new MineEvents(this), this);

        PaperCommandManager acf = new PaperCommandManager(this);
        acf.registerCommand(new AdminCommands());
        acf.registerCommand(new PlayerCommands());

        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        //Run a task 5 seconds later to make sure the world is loaded
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            mineManager.saveAllMines();
        }, 600L, 600L);
    }

    @Override
    public void onDisable() {
        for (PrivateMine mine : mineManager.getMines()) {
            mineDataPersistenceHandler.saveMineData(mine);
        }
//        RegionManager container = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world")));
//        Map<String, ProtectedRegion> regions = container.getRegions();
//        for (String regionName : regions.keySet()) {
//            container.removeRegion(regionName);
//        }
//        try {
//            container.save();
//        } catch (StorageException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void createEmptyMineWorld() {
        //Check if the world already exists
        WorldCreator worldCreator = new WorldCreator("private_mine_world");
        worldCreator.generator(new VoidGenerator());
        worldCreator.createWorld();

        //Run a task 1 second later to make sure the world is loaded
        Bukkit.getScheduler().runTaskLater(this, () -> {
            World world = Bukkit.getWorld("private_mine_world");
            assert world != null;
            world.setAutoSave(false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        }, 20);
    }

}
