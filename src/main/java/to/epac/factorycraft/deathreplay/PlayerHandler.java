package to.epac.factorycraft.deathreplay;

import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Replay replay = ReplayAPI.getInstance().recordReplay(player.getUniqueId() + "", player);
        DeathReplay.replays.put(player.getUniqueId(), replay);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ReplayAPI.getInstance().stopReplay(player.getUniqueId() + "", false);
        DeathReplay.replays.remove(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if (ReplayHelper.replaySessions.containsKey(p.getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        // If world change, restart recording
        if (DeathReplay.replays.containsKey(player.getUniqueId())) {
            ReplayAPI.getInstance().stopReplay(player.getUniqueId() + "", false);

            Replay replay = ReplayAPI.getInstance().recordReplay(player.getUniqueId() + "", player);
            DeathReplay.replays.put(player.getUniqueId(), replay);
        }
    }
}
