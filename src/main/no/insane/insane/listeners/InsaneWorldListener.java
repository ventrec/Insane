package no.insane.insane.listeners;

import org.bukkit.event.world.WorldListener;
import no.insane.insane.Insane;

	public class InsaneWorldListener extends WorldListener {
		
		@SuppressWarnings("unused")
		private Insane plugin;

		public InsaneWorldListener(Insane instance) {
			this.plugin = instance;
		}
		
	}