package to.epac.factorycraft.deathReplay;

import me.jumper251.replay.replaysystem.Replay;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DeathReplay extends JavaPlugin {

    public static Map<UUID, Replay> replays = new HashMap<>();

    private static DeathReplay inst;

    @Override
    public void onEnable() {
        inst = this;

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DeathHandler(), this);
        pm.registerEvents(new PlayerHandler(), this);
        pm.registerEvents(new ReplaySessionFinishHandler(), this);
        pm.registerEvents(new RespawnHandler(), this);
    }

    @Override
    public void onDisable() {
    }

    public static DeathReplay getInst() {
        return inst;
    }
}
