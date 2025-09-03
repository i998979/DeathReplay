package to.epac.factorycraft.deathreplay;

import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class DeathReplay extends JavaPlugin {

    private static DeathReplay inst;

    public static List<String> disabledWorlds;
    public static int duration;

    public static Map<UUID, Replay> replays = new HashMap<>();
    public static Map<UUID, BukkitRunnable> spectatorRunnables = new HashMap<>();

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 27156);
        inst = this;

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DeathHandler(), this);
        pm.registerEvents(new PlayerHandler(), this);
        pm.registerEvents(new ReplaySessionFinishHandler(), this);
        pm.registerEvents(new RespawnHandler(), this);

        getCommand("DeathReplay").setExecutor(new ReplayCommand());

        saveDefaultConfig();
        disabledWorlds = getConfig().getStringList("DeathReplay.DisabledWorlds");
        duration = getConfig().getInt("DeathReplay.Duration", 5);

        for (Player player : Bukkit.getOnlinePlayers()) {
            ReplayAPI.getInstance().stopReplay(player.getUniqueId().toString(), false);

            Replay replay = ReplayAPI.getInstance().recordReplay(player.getUniqueId().toString(), player);
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
