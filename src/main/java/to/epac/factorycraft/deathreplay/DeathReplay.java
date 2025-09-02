package to.epac.factorycraft.deathreplay;

import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class DeathReplay extends JavaPlugin {

    public static Map<UUID, Replay> replays = new HashMap<>();

    private static DeathReplay inst;

    public static List<String> disabledWorlds = new ArrayList<>();

    @Override
    public void onEnable() {
        inst = this;

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DeathHandler(), this);
        pm.registerEvents(new PlayerHandler(), this);
        pm.registerEvents(new ReplaySessionFinishHandler(), this);
        pm.registerEvents(new RespawnHandler(), this);

        saveDefaultConfig();
        disabledWorlds = getConfig().getStringList("DeathReplay.DisabledWorlds");

        for (Player player : Bukkit.getOnlinePlayers()) {
            ReplayAPI.getInstance().stopReplay(player.getUniqueId() + "", false);

            Replay replay = ReplayAPI.getInstance().recordReplay(player.getUniqueId() + "", player);
            DeathReplay.replays.put(player.getUniqueId(), replay);
        }
    }

    @Override
    public void onDisable() {
    }

    public static DeathReplay getInst() {
        return inst;
    }
}
