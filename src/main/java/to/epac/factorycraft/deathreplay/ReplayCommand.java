package to.epac.factorycraft.deathreplay;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReplayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("DeathReplay.Admin")) {
            sender.sendMessage("§cYou don't have permission to perform this command.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("§cCorrect usage: /DeathReplay <duration/disabledworlds>.");
            return false;
        }

        if (args[0].equalsIgnoreCase("duration")) {
            try {
                int duration = Integer.parseInt(args[1]);

                DeathReplay.duration = duration;
                DeathReplay.getInst().getConfig().set("DeathReplay.Duration", duration);
                DeathReplay.getInst().saveConfig();

                sender.sendMessage("§aDuration has been set to " + duration + "s.");
            } catch (Exception e) {
                sender.sendMessage("§cPlease enter duration in seconds.");
                return false;
            }
        } else if (args[0].equalsIgnoreCase("disabledworlds")) {
            if (args.length == 1) {
                sender.sendMessage("§cCorrect usage: <add/remove> <world>.");
                return false;
            }

            if (args[1].equalsIgnoreCase("add")) {
                if (args.length == 2) {
                    sender.sendMessage("§cPlease enter world to add.");
                    return false;
                }

                DeathReplay.disabledWorlds.add(args[2]);
                DeathReplay.getInst().getConfig().set("DeathReplay.DisabledWorlds", DeathReplay.disabledWorlds);
                DeathReplay.getInst().saveConfig();
                sender.sendMessage("§aAdded " + args[2] + " into disabled worlds.");
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length == 2) {
                    sender.sendMessage("§cPlease enter world to remove.");
                    return false;
                }

                DeathReplay.disabledWorlds.remove(args[2]);
                DeathReplay.getInst().getConfig().set("DeathReplay.DisabledWorlds", DeathReplay.disabledWorlds);
                DeathReplay.getInst().saveConfig();
                sender.sendMessage("§aRemoved " + args[2] + " from disabled worlds.");
            } else {
                sender.sendMessage("§cCorrect usage: <add/remove> <world>.");
                return false;
            }
        } else {
            sender.sendMessage("§cCorrect usage: /DeathReplay <duration/disabledworlds>.");
        }


        return true;
    }
}
