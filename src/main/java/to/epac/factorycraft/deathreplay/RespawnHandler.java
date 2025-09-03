package to.epac.factorycraft.deathreplay;

import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnHandler implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Player watcher = event.getPlayer();

        if (!DeathReplay.replays.containsKey(player.getUniqueId())) {
            Replay replay = ReplayAPI.getInstance().recordReplay(player.getUniqueId().toString(), player);
            DeathReplay.replays.put(player.getUniqueId(), replay);

            return;
        }

        Replay replay = DeathReplay.replays.get(player.getUniqueId());
        int duration = replay.getReplayInfo().getDuration();

        if (ReplaySaver.exists(player.getUniqueId().toString()) && !ReplayHelper.replaySessions.containsKey(watcher.getName())) {
            ReplaySaver.load(player.getUniqueId().toString(), replay1 -> {
                // Force extend to 5s if replay isn't 5s long
                replay1.getData().setDuration(Math.max(DeathReplay.duration * 20, replay1.getData().getDuration()));
                replay1.play(watcher);
            });
        }

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                // If replay has started
                if (ReplayHelper.replaySessions.containsKey(watcher.getName())) {
                    Bukkit.getScheduler().runTaskLater(DeathReplay.getInst(), () -> {
                        // Jump to last 5s
                        ReplayAPI.getInstance().jumpToReplayTime(player, (int) (duration / 20.0 - DeathReplay.duration));

                        BukkitRunnable spectatorRunnable = new BukkitRunnable() {
                            @Override
                            public void run() {
                                Replayer replayer = ReplayHelper.replaySessions.get(player.getName());
                                if (replayer == null || replayer.getNPCList() == null) return;

                                // Find player NPC
                                int entityId = replayer.getNPCList().entrySet().stream().filter(
                                                entry -> entry.getKey().equals(player.getName()))
                                        .map(entry1 -> entry1.getValue().getId()).findFirst().orElse(-1);

                                // Make player spectate himself constantly
                                player.setGameMode(GameMode.SPECTATOR);
                                if (entityId != -1)
                                    replayer.getSession().getPacketListener().setCamera(player, entityId, 3);
                            }
                        };
                        spectatorRunnable.runTaskTimer(DeathReplay.getInst(), 0, 1);
                        DeathReplay.spectatorRunnables.put(player.getUniqueId(), spectatorRunnable);
                    }, 1);

                    cancel();
                }
            }
        };
        bukkitRunnable.runTaskTimer(DeathReplay.getInst(), 0, 1);
    }
}
