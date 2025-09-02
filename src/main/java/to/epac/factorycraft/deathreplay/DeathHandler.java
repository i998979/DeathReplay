package to.epac.factorycraft.deathreplay;

import me.jumper251.replay.api.ReplayAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathHandler implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!DeathReplay.replays.containsKey(player.getUniqueId())) return;

        ReplayAPI.getInstance().stopReplay(player.getUniqueId() + "", true);

        // If player dies in disabled world, remove replay instantly as we don't need to replay it
        if (DeathReplay.disabledWorlds.contains(player.getWorld().getName())) {
            DeathReplay.replays.remove(player.getUniqueId());
        }
    }
}
