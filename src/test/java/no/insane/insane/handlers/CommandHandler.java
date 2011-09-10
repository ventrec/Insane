package no.insane.insane.handlers;

import no.insane.insane.Insane;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandHandler implements CommandExecutor {

	protected final Insane plugin;
	private ChatColor errorChatColor;
	private ChatColor okChatColor;
	private ChatColor varChatColor;
	private ChatColor defaultChatColor;
	private ChatColor commandChatColor;
	private ChatColor argChatColor;
	private int status;
	protected UserHandler userHandler;
	
	public CommandHandler(Insane instance) {
		this.plugin = instance;
		this.userHandler = instance.getUserHandler();
		this.setErrorChatColor(ChatColor.RED);
		this.setOkChatColor(ChatColor.GREEN);
		this.setVarChatColor(ChatColor.WHITE);
		this.setDefaultChatColor(ChatColor.DARK_GREEN);
		this.setCommandChatColor(ChatColor.GOLD);
		this.setArgChatColor(ChatColor.GRAY);
		this.setStatus(15);
	}

	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (CommandHandler.isPlayer(sender)) {
			Player player = (Player) sender;
			if (this.userHandler.getUserStatus(player) >= getStatus() || player.isOp()) {
				return onPlayerCommand(player, command, label, args);
			}
		}
		return true;
	}
	public abstract boolean onPlayerCommand(Player player, Command command,
			String label, String[] args);

	public static boolean isPlayer(CommandSender sender) {
		if(sender instanceof Player)
					return true;
		return false;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return this.status;
	}

	public void setErrorChatColor(ChatColor errorChatColor) {
		this.errorChatColor = errorChatColor;
	}

	public ChatColor getErrorChatColor() {
		return this.errorChatColor;
	}

	public void setOkChatColor(ChatColor okChatColor) {
		this.okChatColor = okChatColor;
	}

	public ChatColor getOkChatColor() {
		return this.okChatColor;
	}
	public Insane getInstance() {
		return this.plugin;
	}

	public void setVarChatColor(ChatColor varChatColor) {
		this.varChatColor = varChatColor;
	}

	public ChatColor getVarChatColor() {
		return this.varChatColor;
	}

	public void setDefaultChatColor(ChatColor defaultChatColor) {
		this.defaultChatColor = defaultChatColor;
	}

	public ChatColor getDefaultChatColor() {
		return this.defaultChatColor;
	}

	public void setCommandChatColor(ChatColor commandChatColor) {
		this.commandChatColor = commandChatColor;
	}

	public ChatColor getCommandChatColor() {
		return this.commandChatColor;
	}

	public void setArgChatColor(ChatColor argChatColor) {
		this.argChatColor = argChatColor;
	}

	public ChatColor getArgChatColor() {
		return this.argChatColor;
	}

}
