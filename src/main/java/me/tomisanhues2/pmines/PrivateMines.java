package me.tomisanhues2.pmines;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PrivateMines extends JavaPlugin {

    private static PrivateMines instance;

    {
        instance = this;
    }

    public static PrivateMines getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        PaperCommandManager acf = new PaperCommandManager(this);

    }

}
