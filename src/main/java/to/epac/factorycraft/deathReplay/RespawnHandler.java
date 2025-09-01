package to.epac.factorycraft.deathReplay;

import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnHandler implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Player watcher = event.getPlayer();

        if (!DeathReplay.replays.containsKey(player.getUniqueId())) return;

        Replay replay = DeathReplay.replays.get(player.getUniqueId());
        int duration = replay.getReplayInfo().getDuration();

        ReplayAPI.getInstance().playReplay(player.getUniqueId() + "", player);

        Bukkit.getScheduler().runTaskLaterAsynchronously(DeathReplay.getInst(), () -> {
            do {
                Bukkit.getScheduler().runTask(DeathReplay.getInst(), () -> {
                    ReplayAPI.getInstance().jumpToReplayTime(player, (int) (duration / 20.0 - 5));
                });
            } while (!ReplayHelper.replaySessions.containsKey(watcher.getName()));
        }, 1);
    }
}
