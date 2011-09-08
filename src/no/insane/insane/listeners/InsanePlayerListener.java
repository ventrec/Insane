package no.insane.insane.listeners;

import no.insane.insane.Insane;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.UserHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

	public class InsanePlayerListener extends PlayerListener {
		
		private Insane plugin;
		private UserHandler userHandler;
		


		public InsanePlayerListener(Insane instance) {
			this.plugin = instance;
			this.userHandler = instance.getUserHandler();
		}
		
		public void onPlayerLogin(PlayerLoginEvent e) {
			Player p = e.getPlayer();
			this.userHandler.login(p);
			if(!ConfigurationHandler.ispublic) {
				if (this.userHandler.getStatus(p) < 5) {
					e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Serveren er ikke public.");	
				}
			}
		}
		
		public void onPlayerJoin(PlayerJoinEvent e) {
			e.setJoinMessage(e.getPlayer().getName() + ChatColor.GREEN + " logget på.");
		}
		
		public void onPlayerQuit(PlayerQuitEvent e) {
			this.userHandler.logout(e.getPlayer());
			e.setQuitMessage(e.getPlayer().getName() + ChatColor.RED + " logget av.");
		}
		
		public void onPlayerChat(PlayerChatEvent e) {
			e.setCancelled(true);
			this.plugin.getServer().broadcastMessage(this.userHandler.getChatName(e.getPlayer()) + " " + e.getMessage());
		}
		
		public void onPlayerKick(PlayerKickEvent e) {
			if (e.getReason().equals("You moved too quickly :( (Hacking?)")) {
				e.setCancelled(true);
				return;
			}
			if ((e.getReason().toLowerCase().contains("flying")) || (e.getReason().toLowerCase().contains("floating"))) {
		        e.setCancelled(true);
		    }
			e.setLeaveMessage(null);
		}
		
	}