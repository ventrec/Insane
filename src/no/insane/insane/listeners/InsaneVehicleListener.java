package no.insane.insane.listeners;

import org.bukkit.event.vehicle.VehicleListener;
import no.insane.insane.Insane;
	public class InsaneVehicleListener extends VehicleListener {
		
		@SuppressWarnings("unused")
		private Insane plugin;

		public InsaneVehicleListener(Insane instance) {
			this.plugin = instance;
		}
		
	}