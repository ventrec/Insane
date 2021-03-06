package no.insane.insane.listeners;

import no.insane.insane.Insane;
import no.insane.insane.handlers.BlockProtect;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.UserHandler;
import no.insane.insane.handlers.WorldConfigurationHandler;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

	public class InsaneBlockListener extends BlockListener {
		
		private Insane plugin;
		private UserHandler userHandler;
		private BlockProtect blockProtect;

		public InsaneBlockListener(Insane instance) {
			this.plugin = instance;
			this.userHandler = instance.getUserHandler();
			this.blockProtect = instance.getBlockProtectHandler();
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
		
		public void onSignChange(SignChangeEvent e) {
			
		}

		public void onBlockPlace(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			Block b = e.getBlock();
			// Kreves det tillatelse for bygging og kan brukeren bygge?
			if(!this.userHandler.canBuild(p, p.getWorld())) {
				e.setCancelled(true);
			} else {
				if(!this.blockProtect.add(p, b)) {
					e.setBuild(false);
					e.setCancelled(true);
				}
			}
		}
		
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			Block b = e.getBlock();
			// Kreves det tillatelse for bygging og kan brukeren bygge?
			if(!this.userHandler.canBuild(p, p.getWorld())) {
				e.setCancelled(true);
			} else {
				if(!this.blockProtect.delete(p, b)) {
					e.setCancelled(true);
				}
			}
			
		}
		
	}