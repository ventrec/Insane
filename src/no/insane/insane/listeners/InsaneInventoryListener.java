package no.insane.insane.listeners;

import no.insane.insane.Insane;
import org.bukkit.event.inventory.InventoryListener;

	public class InsaneInventoryListener extends InventoryListener {
		
		@SuppressWarnings("unused")
		private Insane plugin;

		public InsaneInventoryListener(Insane instance) {
			this.plugin = instance;
		}
		
	}