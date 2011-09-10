package no.insane.insane.commands;


import no.insane.insane.Insane;
import no.insane.insane.handlers.ActionID;
import no.insane.insane.handlers.CommandHandler;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.LogHandler;
import no.insane.insane.handlers.UserHandler;
import no.insane.insane.handlers.WorldConfigurationHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class KickCommand extends CommandHandler {
	
	private UserHandler userHandler;
	private LogHandler logHandler;
	private Insane plugin;

	public KickCommand(Insane instance) {
		super(instance);
		this.plugin = instance;
		setStatus(5);
		this.userHandler = instance.getUserHandler();
		this.logHandler = instance.getLogHandler();
	}

	@Override
	public boolean onPlayerCommand(Player p, Command command, String label, String[] args) {
		ConfigurationHandler 		cfg = plugin.getGlobalStateManager();
		WorldConfigurationHandler 	wcfg = cfg.get(p.getWorld());
		if(wcfg.KickCommand) {
			if(args.length >= 1) {
				Player v = plugin.playerMatch(args[0]);
				if(this.userHandler.getUserStatus(p) >= this.userHandler.getUserStatus(v)) {
					if(args.length > 1) {
						String kick = "";
						for(int i = 1; i <= args.length-1; i++) {
							kick += args[i] + " ";
						}
						kick = kick.substring(0, kick.length()-1);
						logHandler.addLine(this.userHandler.getUID(p), this.userHandler.getUID(v), ActionID.KICK, 0, kick);
						v.kickPlayer(kick);
					} else {
						logHandler.addLine(this.userHandler.getUID(p), this.userHandler.getUID(v), ActionID.KICK, 0, "NULL");
						v.kickPlayer("Du ble kicket, ingen grunn oppgitt.");
					}
				} else {
					p.sendMessage(ChatColor.RED + "Du har ikke rett tilgangsnivå til å kicke denne brukeren.");
				}
			}
		}
		return true;
	}
}