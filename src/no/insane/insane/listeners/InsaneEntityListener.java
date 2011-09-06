package no.insane.insane.listeners;

import no.insane.insane.Insane;
import org.bukkit.event.entity.EntityListener;

	public class InsaneEntityListener extends EntityListener {

		@SuppressWarnings("unused")
		private Insane plugin;

		public InsaneEntityListener(Insane instance) {
			this.plugin = instance;
		}
		
	}