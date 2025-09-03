package to.epac.factorycraft.deathreplay;

import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.api.ReplaySessionFinishEvent;
import me.jumper251.replay.replaysystem.Replay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReplaySessionFinishHandler implements Listener {

    @EventHandler
    public void onReplayFinished(ReplaySessionFinishEvent event) {
        Player player = event.getPlayer();
        Replay oldReplay = event.getReplay();

        DeathReplay.spectatorRunnables.computeIfPresent(player.getUniqueId(), (uuid, bukkitRunnable) -> {
            bukkitRunnable.cancel();
            return bukkitRunnable;
        });
        Replay replay = ReplayAPI.getInstance().recordReplay(player.getUniqueId().toString(), player);
        DeathReplay.replays.put(player.getUniqueId(), replay);
    }
}
