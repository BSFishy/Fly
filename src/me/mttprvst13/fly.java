package me.mttprvst13;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

import me.mttprvst13.Updater.UpdateType;

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
	File config = null;
	public int[] timeoutVar;
	public long sleepTime = 1000;

	@SuppressWarnings("static-access")
	@Override
	public void onEnable() {
		
		// Config stuff
		this.saveDefaultConfig();
		
		// Initualize variables
		this.log = this.getLogger();
		this.maxplayers = this.getServer().getMaxPlayers();
		this.isfly = new boolean[this.maxplayers];
		this.isfly[1] = false;
		this.timeoutVar = new int[this.maxplayers];
		this.timeoutVar[1] = 1;
		
		// Check to see what to do with an update
		UpdateType update = this.getConfig().getString("downloadUpdate").equalsIgnoreCase("true") ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD;
		
		// Make an instance of the updater
		Updater updater = new Updater(this, 88761, this.getFile(), update, false);
		
		// Get the result of the updater
		Updater.UpdateResult result = updater.getResult();
		if (updater.getResult() == result.UPDATE_AVAILABLE) {
			// If there is an update tell everyone
		    this.log.info("New version available! " + updater.getLatestName());
		    this.log.info("Here is a direct link to it: " + updater.getLatestFileLink() );
		}
		
		//timeout
		this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
    			int i = 0;
    			for(int p : timeoutVar){
    				
    				if(i == 0){
    					
    					i++;
    					continue;
    					
    				}
    				
    				if(p > 0){
    					
    					timeoutVar[i]--;
    					
    				}
    				
    				i++;
    				
    			}
    			
            }
        }, 0, 20);
		
		
		
		getLogger().info("Enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		// If the player did /fly
		if (cmd.getName().equalsIgnoreCase("fly") && sender instanceof Player) {

			// Get the player instance
			Player player = (Player) sender;
			
			// If the player doesn't have the permission
			if (!player.hasPermission("mfly.canfly")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to fly." +
						" If you think this is an error, report it to an administrator.");
				return retur;
			}
			
			// Timeout
			int timeout = this.getTimeout(player.getName());
			if(timeoutVar[timeout] > 0){
				
				player.sendMessage(ChatColor.RED + "You cannot run that command at this time, you must wait " + Integer.toString(timeoutVar[timeout]) + " seconds.");
				
				return this.retur;
				
			}

			// Check to see if the the args[0] player is online
			if(args.length >= 1 && !(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))){
				
				Player p = this.getServer().getPlayer(args[0]);
				
				if(p == null){
					
					player.sendMessage(ChatColor.RED + args[0] + " is not online.");
					
					return this.retur;
					
				}
				
			}
			
			// Get what to do
			boolean value = this.format(sender, args);
			
			timeoutVar[timeout] = Integer.parseInt(this.getConfig().getString("timeout"));

			// Change player fly mode
			if (value == true) {
				player.setAllowFlight(false);
				this.isfly[this.pl] = false;
			} else {
				player.setAllowFlight(true);
				// player.setFlying(true);
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
				
				int len = args.length;
				
				if(args.length >= 2){
					
					if(args[1].equalsIgnoreCase("on")){
						
						player.setAllowFlight(true);
						player.setFlying(true);
						
						String msg = ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
								+ player.getName() + " by an admin/the console.";
						
						player.sendMessage(msg);
						sender.sendMessage(msg);
						this.isfly[id] = true;
						
					}else if(args[1].equalsIgnoreCase("off")){
						
						player.setAllowFlight(false);
						
						String msg = ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
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
						
						String msg = ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
								+ player.getName() + " by an admin/the console.";
						
						player.sendMessage(msg);
						sender.sendMessage(msg);
						this.isfly[id] = true;

						
					}else{
						
						player.setAllowFlight(false);
						
						String msg = ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
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
		
		if(cmd.getName().equalsIgnoreCase("flyspeed")){
			
			if(args.length < 1){
				
				sender.sendMessage(ChatColor.RED + "This command requires arguments.");
				
				return retur;
				
			}
			
			if((this.getServer().getOnlinePlayers().size() <= 1 && args.length == 2) || !(sender instanceof Player)){
				
				sender.sendMessage(ChatColor.RED + "There are no players online execpt for you.");
				
				return retur;
				
			}
			
			float speed = 0.1f;
			
			if(args.length == 1){
				
				if(!(sender instanceof Player)){
					
					sender.sendMessage(ChatColor.RED + "Please specify the user.");
					
					return retur;
					
				}
				
				switch(args[0]){
				
				case "1":
					speed = 0.1f;
					break;
				case "2":
					speed = 0.2f;
					break;
				case "3":
					speed = 0.3f;
					break;
				case "4":
					speed = 0.4f;
					break;
				case "5":
					speed = 0.5f;
					break;
				case "6":
					speed = 0.6f;
					break;
				case "7":
					speed = 0.6f;
					break;
				case "8":
					speed = 0.7f;
					break;
				case "9":
					speed = 0.8f;
					break;
				case "10":
					speed = 1f;
					break;
				default:
					sender.sendMessage(ChatColor.RED + "You must enter a speed from 1 to 10.");
					return retur;
				}
				
				Player player = (Player) sender;
				
				player.setFlySpeed(speed);
				player.sendMessage(ChatColor.RED + "[mttsFly] " + ChatColor.BLUE + "Set your fly speed to " + ChatColor.AQUA + args[0] + ChatColor.BLUE + " by you.");
				
				return retur;
				
			}else{
				
				switch(args[0]){
				
				case "1":
					speed = 0.1f;
					break;
				case "2":
					speed = 0.2f;
					break;
				case "3":
					speed = 0.3f;
					break;
				case "4":
					speed = 0.4f;
					break;
				case "5":
					speed = 0.5f;
					break;
				case "6":
					speed = 0.6f;
					break;
				case "7":
					speed = 0.6f;
					break;
				case "8":
					speed = 0.7f;
					break;
				case "9":
					speed = 0.8f;
					break;
				case "10":
					speed = 1f;
					break;
				default:
					sender.sendMessage(ChatColor.RED + "You must enter a speed from 1 to 10.");
					return retur;
				}
				
				String msg = ChatColor.RED + "[mttsFly] " + ChatColor.BLUE + "Set your fly speed to " + ChatColor.AQUA + args[0] + ChatColor.BLUE + " by you.";
				
				Player player = this.getServer().getPlayer(args[1]);
				
				player.setFlySpeed(speed);
				sender.sendMessage(msg);
				player.sendMessage(msg);
				
				return retur;
				
			}
			
		}
		
		if(cmd.getName().equalsIgnoreCase("flying")){
			
			if(args.length < 1){
				
				sender.sendMessage(ChatColor.RED + "That command requires arguments.");
				
				return retur;
				
			}
			
			this.getId(args[0]);
			
			log.info(Boolean.toString(isfly[pl]));
			
			if(isfly[pl]){
				
				sender.sendMessage(ChatColor.RED + "[mttsFly] " + ChatColor.AQUA + args[0] + ChatColor.BLUE + " is flying.");
				return retur;
				
			}else{
				
				sender.sendMessage(ChatColor.RED + "[mttsFly] " + ChatColor.AQUA + args[0] + ChatColor.BLUE + " is " + ChatColor.RED + "not" + ChatColor.BLUE + " flying.");
				return retur;
				
			}
			
		}

		return retur;
	}
	
	public boolean format(CommandSender sender, String[] args){
		
		if(args.length >= 1){
			
			if(args.length == 1){
				
				this.getId(sender.getName());
				
				if(args[0].equalsIgnoreCase("on")){
					
					sender.sendMessage(ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
							+ sender.getName() + " by you.");
					
					this.isfly[this.pl] = false;
					
					return false;
					
				}else if(args[0].equalsIgnoreCase("off")){
					
					sender.sendMessage(ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
							+ sender.getName() + " by you.");
					
					this.isfly[this.pl] = true;
					
					return true;
					
				}else{
					
					this.getId(args[0]);
					Player p = this.getServer().getPlayer(args[0]);
					
					String msg = ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Fly mode " + ChatColor.AQUA + "toggled" + ChatColor.BLUE + " for "
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
					
				}
				
			}else{
				
				this.getId(args[0]);

				Player p = this.getServer().getPlayer(args[0]);
				
				if(args[1].equalsIgnoreCase("on")){
					
					String msg = ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
							+ p.getName() + " by " + sender.getName() + ".";
					
					p.sendMessage(msg);
					sender.sendMessage(msg);
					
					this.isfly[this.pl] = false;
					
					return false;
					
				}else{
					
					String msg = ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
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
				
				sender.sendMessage(ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_RED + "false" + ChatColor.BLUE + " for "
						+ sender.getName() + " by you.");
				
				this.isfly[this.pl] = true;
				
				return true;
				
			}else{
				
				sender.sendMessage(ChatColor.RED + "[mttsFly]" + ChatColor.BLUE +"Set fly mode to " + ChatColor.DARK_GREEN + "true" + ChatColor.BLUE + " for "
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
			
			if(p.getName() == sender){
				
				this.pl = i;
				
				break;
				
			}
			
			i++;
			
		}
		
		if(this.pl == 0){
			
			
			this.pl = (i + 1);
		 	
		}
		
		//if(!this.isfly[this.pl]){
			
		//	this.isfly[this.pl] = true;
			
		//}
		
		return this.pl;
		
	}
	
	public int getTimeout(String p){
		
		int i = 0;
		for(Player player : this.getServer().getOnlinePlayers()){
			
			if(i == 0){
				
				i++;
				
				continue;
				
			}
			
			if(player.getName() == p){
								
				break;
				
			}
			
			i++;
			
		}
		
		return i;
		
	}

}
