package no.insane.insane.listeners;

import no.insane.insane.Insane;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.UserHandler;
import no.insane.insane.handlers.WorldConfigurationHandler;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

	public class InsaneBlockListener extends BlockListener {
		
		private Insane plugin;
		private UserHandler userHandler;

		public InsaneBlockListener(Insane instance) {
			this.plugin = instance;
			this.userHandler = instance.getUserHandler();
		}
		
		public void onBlockForm(BlockFormEvent e) {
			World w = e.getBlock().getWorld();
			
			ConfigurationHandler cfg = this.plugin.getGlobalStateManager();
			WorldConfigurationHandler wcfg = cfg.get(w);
			
			if((!e.isCancelled()) && (e.getNewState().getType() == Material.ICE) && (!wcfg.IceRegen)) {
				e.setCancelled(true);
			} else if((!e.isCancelled()) && (e.getNewState().getType() == Material.SNOW) && (!wcfg.SnowRegen)) {
				e.setCancelled(true);
			}
			
		}
		
		public void onBlockPlace(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			
			// Kreves det tillatelse for bygging og kan brukeren bygge?
			if(!this.userHandler.canBuild(p, p.getWorld())) {
				e.setCancelled(true);
			}
		}
		
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			
			// Kreves det tillatelse for bygging og kan brukeren bygge?
			if(!this.userHandler.canBuild(p, p.getWorld())) {
				e.setCancelled(true);
			}
		}
		
	}