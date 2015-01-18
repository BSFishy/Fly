package me.mttprvst13;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class fly extends JavaPlugin {
	
	public final BukkitListener b1 = new BukkitListener(this);

	@Override
	public void onEnable() {
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(b1, this);
		getServer().getPluginManager().addPermission(new permissions().canFly);
		getServer().getPluginManager().addPermission(new permissions().canSayHey);
		getServer().getPluginManager().addPermission(new permissions().willFly);
		getLogger().info("[mtt's Fly] has been enabled.");
	}

	@Override
	public void onDisable() {
		getServer().getPluginManager().removePermission(new permissions().canFly);
		getServer().getPluginManager().removePermission(new permissions().canSayHey);
		getServer().getPluginManager().removePermission(new permissions().willFly);
		getLogger().info("[mtt's Fly] has been disabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("hey") && sender instanceof Player) {

			Player player = (Player) sender;
			
			if (!player.hasPermission("fly.sayhey")) {
				player.sendMessage("§4You do not have permission to say hey." +
						" If you think this is an error, report it to an administrator.");
				return false;
			}else{
				((Player) sender).chat("Hey, " + sender.hasPermission("fly"));
			}

			String user = (args.length >= 1) ? args[0] : player.getName();

			player.chat("§l§o§3Hello, " + user + ", welcome onto the server!");

			return true;

		}
		
		if (cmd.getName().equalsIgnoreCase("fly") && sender instanceof Player) {

			Player player = (Player) sender;
			
			if (!player.hasPermission("fly.canfly")) {
				player.sendMessage("§4You do not have permission to fly." +
						" If you think this is an error, report it to an administrator.");
				return false;
			}

			boolean value = (player.isFlying()) ? false : true;

			if (value == false) {
				player.setAllowFlight(false);
				player.sendMessage("§4[mtt's Fly]§r§2Set fly mode to §3false§r§2 for "
						+ player.getName());
			} else {
				player.setAllowFlight(true);
				player.setFlying(true);
				player.sendMessage("§4[mtt's Fly]§r§2Set fly mode to §1true§r§2 for "
						+ player.getName());
			}

			return true;

		}

		return false;
	}

}
