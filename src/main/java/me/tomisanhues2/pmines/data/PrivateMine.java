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
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
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
import java.util.Objects;
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

    public PrivateMine(UUID uuid, UpgradeLevel level) {
        this.uuid = uuid;
        upgradeLevel = level;
        autoReset = null;
        teleportLocation = null;
        isPlayerOnline = true;
        pasteMine();
    }


    public boolean pasteMine() {

        int totalMines = PrivateMines.getInstance().mineManager.getMineCount() + 1;

        File schematic = new File(PrivateMines.getInstance().getDataFolder() + "/schematics/privatemine.schem");

        Clipboard clipboard;


        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            clipboard = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clipboard.paste(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world")), BlockVector3.at(0, 150, totalMines * 180));
        centerMineLocation = new Location(Bukkit.getWorld("private_mine_world"), 0, 149, (totalMines * 180) + 43);
        teleportLocation = new Location(Bukkit.getWorld("private_mine_world"), 0.5, 150, (totalMines * 180) + 0.5);
        //Run a task 1 second after the mine is created to fill it with the correct materials
        Bukkit.getScheduler().runTaskLaterAsynchronously(PrivateMines.getInstance(), this::fillMine, 20);
        PrivateMines.getInstance().mineManager.mineCount++;
        return true;
    }

    public void createRegions() {
        ProtectedRegion mineRegion =
                new ProtectedCuboidRegion(uuid.toString() + "-MINE", BlockVector3.at(centerMineLocation.getBlockX() + 80, centerMineLocation.getBlockY() - 90, centerMineLocation.getBlockZ() - 75),
                        BlockVector3.at(centerMineLocation.getBlockX() - 94, centerMineLocation.getBlockY() + 66, centerMineLocation.getBlockZ() + 97));
        mineRegion.getOwners().addPlayer(uuid);
        mineRegion.setFlag(Flags.ENTRY, StateFlag.State.DENY);
        mineRegion.setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.NON_OWNERS);
        mineRegion.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
        mineRegion.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.ALL);
        mineRegion.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
        mineRegion.setFlag(Flags.BLOCK_PLACE.getRegionGroupFlag(), RegionGroup.ALL);
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).addRegion(mineRegion);


        BlockVector3 min =
                BlockVector3.at(centerMineLocation.getBlockX() + upgradeLevel.getTier(), centerMineLocation.getBlockY() - 67, centerMineLocation.getBlockZ() - upgradeLevel.getTier());
        BlockVector3 max =
                BlockVector3.at(centerMineLocation.getBlockX() - upgradeLevel.getTier(), centerMineLocation.getBlockY(), centerMineLocation.getBlockZ() + upgradeLevel.getTier());

        region = new ProtectedCuboidRegion(uuid.toString(), min, max);
        region.setPriority(10);
        region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
        region.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.OWNERS);
        region.getOwners().addPlayer(uuid);
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).addRegion(region);

        try {
            WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).save();
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }


    }


    public boolean resetMine() {
        fillMine();
        startTasks();
        //teleport the player 1 second after the mine is reset
        Bukkit.getScheduler().runTaskLaterAsynchronously(PrivateMines.getInstance(), () -> Bukkit.getPlayer(uuid).teleport(teleportLocation), 1);
        Bukkit.getPlayer(uuid).setFlying(true);
        return true;
    }

    public void startTasks() {
        if (autoReset != null) {
            return;
        }
        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            isPlayerOnline = true;
            autoReset = Bukkit.getScheduler().runTaskTimerAsynchronously(PrivateMines.getInstance(), () -> {
                if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                    double percentage = getMinedBlockedPercentage() * 100;
                    System.out.println("Percentage: " + percentage);
                    if (percentage >= 50) {
                        resetMine();
                        Bukkit.getPlayer(uuid).sendTitle("§cMine Reset", "§7Your mine has been automatically reset!", 10, 40, 10);
                    }
                } else {
                    isPlayerOnline = false;
                    autoReset.cancel();
                }
            }, 60L, 60L);
        }
    }

    public void stopTask() {
        if (autoReset != null) {
            autoReset.cancel();
        }
        isPlayerOnline = false;
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).removeRegion(uuid.toString());
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).removeRegion(uuid.toString() + "-MINE");
    }

    public boolean fillMine() {
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        final RandomPattern pattern = new RandomPattern();
        int percentage = (int) Math.round(100.0 / upgradeLevel.getMaterials().size());
        for (Material material : upgradeLevel.getMaterials()) {
            //Divide 100 by the amount of materials to get the percentage of each material
            //Make sure to round to the nearest integer
            pattern.add(BukkitAdapter.adapt(material.createBlockData()), percentage);
        }

        Region region = new CuboidRegion(min, max);

        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).fastMode(true).build()) {
            editSession.setBlocks(region, pattern);
            editSession.flushQueue();
        }
        return true;
    }

    public void upgradeMine() {
        if (upgradeLevel.getTier() == 34) {
            Bukkit.getPlayer(uuid).sendMessage("§cYou have reached the maximum upgrade level!");
            return;
        }
        upgradeLevel = ConfigUtils.getUpgradeLevel(upgradeLevel.getTier() + 1);
        BlockVector3 min =
                BlockVector3.at(centerMineLocation.getBlockX() + upgradeLevel.getTier(), centerMineLocation.getBlockY() - 67, centerMineLocation.getBlockZ() - upgradeLevel.getTier());
        BlockVector3 max =
                BlockVector3.at(centerMineLocation.getBlockX() - upgradeLevel.getTier(), centerMineLocation.getBlockY(), centerMineLocation.getBlockZ() + upgradeLevel.getTier());

        region = new ProtectedCuboidRegion(uuid.toString(), min, max);

        resetMine();
    }

    public double getMinedBlockedPercentage() {
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        CuboidRegion region = new CuboidRegion(min, max);

        int totalBlocks = 0;
        int minedBlocks = 0;

        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(BukkitAdapter.adapt(Bukkit.getWorld("private_mine_world"))).fastMode(true).build()) {
            for (BlockVector3 blockVector3 : region) {
                if (editSession.getBlock(blockVector3).getBlockType().getMaterial().isAir()) {
                    minedBlocks++;
                }
                totalBlocks++;
            }
        }
        return (double) minedBlocks / totalBlocks;
    }
}
