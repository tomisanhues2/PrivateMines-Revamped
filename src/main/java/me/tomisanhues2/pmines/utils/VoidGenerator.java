package me.tomisanhues2.pmines.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new BlockPopulator[0]);
    }

    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    public int xyzToByte(int x, int y, int z) {
        return (x * 16 + z) * 128 + y;
    }

    public byte[] generate(World world, Random rand, int chunkX, int chunkZ) {
        byte[] result = new byte[32768];
        if (chunkX == 0 && chunkZ == 0)
            result[xyzToByte(0, 64, 0)] = (byte) Material.BEDROCK.getId();
        return result;
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.0D, 66.0D, 0.0D);
    }
}
