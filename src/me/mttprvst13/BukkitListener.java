package me.mttprvst13;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitListener implements Listener {
	public static fly plugin;
	
	public BukkitListener(fly fly) {
		// TODO Auto-generated constructor stub
		return;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		
		Player player = event.getPlayer();
		event.setJoinMessage(ChatColor.GOLD + "Hey, wasup, " + player.getName() + '!');
	    if(player.hasPermission("fly.willfly")){
			player.setAllowFlight(true);
			player.setFlying(true);
	    }
		
	}
	
}
