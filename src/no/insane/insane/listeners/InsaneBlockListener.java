package no.insane.insane.listeners;

import no.insane.insane.Insane;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

	public class InsaneBlockListener extends BlockListener {
		
		@SuppressWarnings("unused")
		private Insane plugin;

		public InsaneBlockListener(Insane instance) {
			this.plugin = instance;
		}
		
		public void onBlockForm(BlockFormEvent e) {
			
			if(!e.isCancelled() && e.getNewState().getType() == Material.ICE) {
				e.setCancelled(true);
			} else if(!e.isCancelled() && e.getNewState().getType() == Material.SNOW) {
				e.setCancelled(true);
			}
			
		}
		
		public void onBlockPlace(BlockPlaceEvent e) {
			
		}
		
	}