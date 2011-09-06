package no.insane.insane.listeners;

import no.insane.insane.Insane;
import no.insane.insane.handlers.UserHandler;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

	public class InsanePlayerListener extends PlayerListener {
		
		private Insane plugin;
		private UserHandler userHandler;

		public InsanePlayerListener(Insane instance) {
			this.plugin = instance;
			this.userHandler = instance.getUserHandler();
		}

		public void onPlayerJoin(PlayerJoinEvent e) {
			this.userHandler.login(e.getPlayer());
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
		
	}