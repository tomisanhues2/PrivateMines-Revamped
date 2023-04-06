package me.tomisanhues2.pmines;

import co.aikar.commands.PaperCommandManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.tomisanhues2.pmines.cache.MineDataPersistenceHandler;
import me.tomisanhues2.pmines.cache.MineManager;
import me.tomisanhues2.pmines.cache.MinePersistenceHandler;
import me.tomisanhues2.pmines.commands.AdminCommands;
import me.tomisanhues2.pmines.commands.PlayerCommands;
import me.tomisanhues2.pmines.data.PrivateMine;
import me.tomisanhues2.pmines.utils.ConfigUtils;
import me.tomisanhues2.pmines.utils.VoidGenerator;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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

        mineDataPersistenceHandler = new MineDataPersistenceHandler(this);
        mineManager = new MineManager(mineDataPersistenceHandler);

        //getServer().getPluginManager().registerEvent(new MineEvents(this), this);

        PaperCommandManager acf = new PaperCommandManager(this);
        acf.registerCommand(new AdminCommands());
        acf.registerCommand(new PlayerCommands());

    }

    @Override
    public void onDisable() {
        for (PrivateMine mine : mineManager.getMines()) {
            mineDataPersistenceHandler.saveMineData(mine);
        }
    }

    private void createEmptyMineWorld() {
        //Check if the world already exists
        if (getServer().getWorld("private_mine_world") != null) {
            Bukkit.unloadWorld("private_mine_world", false);
            try {
                FileUtils.deleteDirectory(new File("private_mine_world"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        WorldCreator worldCreator = new WorldCreator("private_mine_world");
        worldCreator.generator(new VoidGenerator());
        worldCreator.createWorld();

        //Run a task 1 second later to make sure the world is loaded
        Bukkit.getScheduler().runTaskLater(this, () -> {
            ProtectedRegion global = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).getRegion("__GLOBAL__");
            }, 20);
    }

}
