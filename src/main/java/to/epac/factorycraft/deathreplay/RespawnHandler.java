package to.epac.factorycraft.deathreplay;

import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnHandler implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Player watcher = event.getPlayer();

        if (!DeathReplay.replays.containsKey(player.getUniqueId())) {
            Replay replay = ReplayAPI.getInstance().recordReplay(player.getUniqueId() + "", player);
            DeathReplay.replays.put(player.getUniqueId(), replay);

            return;
        }

        Replay replay = DeathReplay.replays.get(player.getUniqueId());
        int duration = replay.getReplayInfo().getDuration();

        ReplayAPI.getInstance().playReplay(player.getUniqueId() + "", player);

        Bukkit.getScheduler().runTaskLaterAsynchronously(DeathReplay.getInst(), () -> {
            do {
                Bukkit.getScheduler().runTaskLater(DeathReplay.getInst(), () -> {
                    ReplayAPI.getInstance().jumpToReplayTime(player, (int) (duration / 20.0 - 5));

                    Replayer replayer = ReplayHelper.replaySessions.get(player.getName());
                    int uuid = replayer.getNPCList().entrySet().stream().filter(
                                    entry -> entry.getKey().equals(player.getName()))
                            .map(entry1 -> entry1.getValue().getId()).findFirst().orElse(-1);

                    Bukkit.getScheduler().runTaskLater(DeathReplay.getInst(), () -> {
                        player.setGameMode(GameMode.SPECTATOR);
                        if (uuid != -1)
                            replayer.getSession().getPacketListener().setCamera(player, uuid, 3);
                    }, 20);
                }, 1);
            } while (!ReplayHelper.replaySessions.containsKey(watcher.getName()));
        }, 1);
    }
}
