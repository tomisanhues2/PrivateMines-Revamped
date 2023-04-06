package me.tomisanhues2.pmines.data;

import com.fastasyncworldedit.core.configuration.Settings;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.tomisanhues2.pmines.PrivateMines;
import me.tomisanhues2.pmines.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class PrivateMine {

    public UUID uuid;

    public UpgradeLevel upgradeLevel;

    public BukkitTask autoReset;

    public Location teleportLocation;

    public Location centerMineLocation;

    public boolean isPlayerOnline;

    public ProtectedRegion region;

    public PrivateMine(UUID uuid) {
        this.uuid = uuid;
        upgradeLevel = ConfigUtils.getUpgradeLevel(1);
        autoReset = null;
        teleportLocation = null;
        isPlayerOnline = true;
    }


    public boolean pasteMine() {

        int totalMines = PrivateMines.getInstance().mineManager.getMineCount() + 1;

        File schematic = new File("plugins/PrivateMines/schematics/privatemine.schem");

        Clipboard clipboard;


        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            clipboard = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clipboard.paste(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world")), BlockVector3.at(0, 150, totalMines * 180));
        System.out.println("Pasted mine at " + totalMines * 180);
        teleportLocation = new Location(Bukkit.getWorld("private_mine_world"), 0.5, 150, (totalMines * 180) + 0.5);
        centerMineLocation = new Location(Bukkit.getWorld("private_mine_world"), 0, 149, (totalMines * 180) + 43);
        BlockVector3 min =
                BlockVector3.at(centerMineLocation.getBlockX() + 1, centerMineLocation.getBlockY() - 67, centerMineLocation.getBlockZ() - 1);
        BlockVector3 max =
                BlockVector3.at(centerMineLocation.getBlockX() - 1, centerMineLocation.getBlockY(), centerMineLocation.getBlockZ() + 1);
        region = new ProtectedCuboidRegion(uuid.toString(), min, max);
        region.setFlag(Flags.NOTIFY_ENTER, true);
        System.out.println(region);
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).addRegion(region);
        return true;
    }

    public boolean resetMine() {
        fillMine();
        return true;
    }

    private boolean fillMine() {
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        final RandomPattern pattern = new RandomPattern();

        for (Material material : upgradeLevel.getMaterials()) {
            pattern.add(BukkitAdapter.adapt(material.createBlockData()), (double) 100 / upgradeLevel.getMaterials().size());
        }

        Region region = new CuboidRegion(min, max);

        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).fastMode(true).build()) {
            editSession.setBlocks(region, pattern);
            editSession.flushQueue();
        }
        return true;
    }
}
