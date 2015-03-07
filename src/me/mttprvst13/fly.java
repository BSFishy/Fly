package me.mttprvst13;

import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class fly extends JavaPlugin {
	
	public final BukkitListener b1 = new BukkitListener(this);
	public boolean[] isfly;
	protected Logger log;
	public boolean retur = true;
	private int pl = 0;
	public int maxplayers; 

	@SuppressWarnings("static-access")
	@Override
	public void onEnable() {
		this.log = this.getLogger();
		this.maxplayers = this.getServer().getMaxPlayers();
		this.isfly = new boolean[this.maxplayers];
		this.isfly[1] = false;
		
		Updater updater = new Updater(this, 88761, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
		
		Updater.UpdateResult result = updater.getResult();
		if (updater.getResult() == result.UPDATE_AVAILABLE) {
		    this.log.info("New version available! " + updater.getLatestName());
		    this.log.info("Here is a direct link to it: " + updater.getLatestFileLink() );
		}
		getLogger().info("Enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("fly") && sender instanceof Player) {

			Player player = (Player) sender;
			
			if (!player.hasPermission("mfly.canfly")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to fly." +
						" If you think this is an error, report it to an administrator.");
				return retur;
			}

			if(args.length >= 1){
				
				Player p = this.getServer().getPlayer(args[0]);
				
				if(p == null){
					
					player.sendMessage(ChatColor.RED + args[0] + " is not online.");
					
					return this.retur;
					
				}
				
			}
			
			boolean value = this.format(sender, args);

			if (value == true) {
				player.setAllowFlight(false);
				this.isfly[this.pl] = false;
			} else {
				player.setAllowFlight(true);
				player.setFlying(true);
				this.isfly[this.pl] = true;
			}

			return retur;

		}else if(cmd.getName().equalsIgnoreCase("fly")){
			
			if(this.getServer().getOnlinePlayers().size() == 0){
				
				sender.sendMessage(ChatColor.RED + "There are no players online.");
				
				return this.retur;
				
			}
			
			if(args.length >= 1){
				
				Player player = this.getServer().getPlayer(args[0]);
				
				if(player == null){
					
					sender.sendMessage(ChatColor.RED + "That player is not online");
					
					return this.retur;
					
				}
				
				int id = this.getId(args[0]);
				
				// this.log.info(player.getName());
				
				int len = args.length;
				
				if(args.length >= 2){
					
					if(args[1].equalsIgnoreCase("on")){
						
						player.setAllowFlight(true);
						player.setFlying(true);
						
						String msg = ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
								+ player.getName() + " by an admin/the console.";
						
						player.sendMessage(msg);
						sender.sendMessage(msg);
						this.isfly[id] = true;
						
					}else if(args[1].equalsIgnoreCase("off")){
						
						player.setAllowFlight(false);
						
						String msg = ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
								+ player.getName() + " by and admin/the console.";
						
						player.sendMessage(msg);
						sender.sendMessage(msg);
						this.isfly[id] = false;
						
					}else{
						
						sender.sendMessage(ChatColor.RED + args[1] + " is an invalid argument. Use on or off.");
						
					}
					
				}else{
					
					if(!this.isfly[id]){
						

						player.setAllowFlight(true);
						player.setFlying(true);
						
						String msg = ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
								+ player.getName() + " by an admin/the console.";
						
						player.sendMessage(msg);
						sender.sendMessage(msg);
						this.isfly[id] = true;

						
					}else{
						
						player.setAllowFlight(false);
						
						String msg = ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
								+ player.getName() + " by an admin/the console.";
						
						player.sendMessage(msg);
						sender.sendMessage(msg);
						this.isfly[id] = false;

						
					}
					
				}
				
				return this.retur;
				
			}
			
			sender.sendMessage(ChatColor.RED + "You must be a player to run that command.");
			
			return retur;
			
		}

		return retur;
	}
	
	public boolean format(CommandSender sender, String[] args){
		
		if(args.length >= 1){
			
			if(args.length == 1){
					
					this.getId(args[0]);
					Player p = this.getServer().getPlayer(args[0]);
					
					String msg = ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Fly mode " + ChatColor.AQUA + "toggled" + ChatColor.BLUE + " for "
							+ p.getName() + " by " + sender.getName();
					
					sender.sendMessage(msg);
					p.sendMessage(msg);

					
					if(this.isfly[this.pl] == true){
						
						this.isfly[this.pl] = true;
						
						return true;
						
					}else{
						
						this.isfly[this.pl] = false;
						
						return false;
						
					}
				
			}else{
				
				this.getId(args[0]);

				Player p = this.getServer().getPlayer(args[0]);
				
				if(args[1].equalsIgnoreCase("on")){
					
					String msg = ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
							+ p.getName() + " by " + sender.getName() + ".";
					
					p.sendMessage(msg);
					sender.sendMessage(msg);
					
					this.isfly[this.pl] = false;
					
					return false;
					
				}else{
					
					String msg = ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
							+ p.getName() + " by " + sender.getName() + ".";
					
					p.sendMessage(msg);
					sender.sendMessage(msg);
					
					this.isfly[this.pl] = true;
					
					return true;
					
				}
				
			}
			
		}else{
			
			this.getId(sender.getName());
			
			if(this.isfly[this.pl] == true){
				
				sender.sendMessage(ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
						+ sender.getName() + " by you.");
				
				this.isfly[this.pl] = true;
				
				return true;
				
			}else{
				
				sender.sendMessage(ChatColor.RED + "[mtt's Fly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
						+ sender.getName() + " by you.");
				
				this.isfly[this.pl] = false;
				
				return false;
				
			}
			
			
			
		}
		
	}
	
	public int getId(String sender){
		
		int i = 0;
		this.pl = 0;
		for(Player p : this.getServer().getOnlinePlayers()){
			
			if(i == 0){
				
				i++;
				
				continue;
				
			}
			
			this.log.info("Interval " + i + ": Player " + p.getName());
			
			if(p.getName() == sender){
				
				this.pl = i;
				
				break;
				
			}
			
			i++;
			
		}
		
		if(this.pl == 2){
			
			this.pl = (this.isfly.length + 1);
			
			this.log.info("Fly length: " + this.isfly.length);
		 	
		}
		
		//if(!this.isfly[this.pl]){
			
		//	this.isfly[this.pl] = true;
			
		//}
		
		return this.pl;
		
	}

}
