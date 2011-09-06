package no.insane.insane.listeners;

import no.insane.insane.Insane;
import org.bukkit.event.weather.WeatherListener;

	public class InsaneWeatherListener extends WeatherListener {
		
		@SuppressWarnings("unused")
		private Insane plugin;

		public InsaneWeatherListener(Insane instance) {
			this.plugin = instance;
		}
		
	}