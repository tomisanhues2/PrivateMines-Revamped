package me.tomisanhues2.pmines.utils;

import me.tomisanhues2.pmines.PrivateMines;
import me.tomisanhues2.pmines.data.PrivateMine;

import java.util.UUID;

public class MineUtils {

    private static final PrivateMines plugin = PrivateMines.getInstance();

    public static PrivateMine createNewMine(UUID uuid) {
        PrivateMine privateMine = new PrivateMine(uuid);
        privateMine.pasteMine();

        plugin.mineManager.createNewMine(privateMine);
        return privateMine;
    }
}
