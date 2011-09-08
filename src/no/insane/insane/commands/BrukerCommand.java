package no.insane.insane.commands;


import no.insane.insane.Insane;
import no.insane.insane.handlers.CommandHandler;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.UserHandler;
import no.insane.insane.handlers.WorldConfigurationHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class BrukerCommand extends CommandHandler {
	
	private UserHandler userHandler;

	public BrukerCommand(Insane instance) {
		super(instance);
		setStatus(0);
		this.userHandler = instance.getUserHandler();
	}

	@Override
	public boolean onPlayerCommand(Player p, Command command, String label, String[] args) {
		ConfigurationHandler 		cfg = plugin.getGlobalStateManager();
		WorldConfigurationHandler 	wcfg = cfg.get(p.getWorld());
		if(wcfg.BrukerCommand) {
			p.sendMessage(ChatColor.WHITE + "------------ Brukerinformasjon ------------");
			p.sendMessage(ChatColor.WHITE + "ID: " + ChatColor.GREEN + this.userHandler.getUID(p));
			p.sendMessage(ChatColor.WHITE + "Brukernavn: " + ChatColor.GREEN + p.getName());
			p.sendMessage(ChatColor.WHITE + "Status: " + ChatColor.GREEN + this.userHandler.getStatus(p));			
		}
		return true;
	}
}