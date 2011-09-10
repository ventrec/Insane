package no.insane.insane.handlers;

import org.bukkit.Location;

public class PlayerData {

	private int status;
	private int gid;
	private int uid;
	private Location Wand1 = null;
	private Location Wand2 = null;
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public void setGID(int gid) {
		this.gid = gid;
	}
	
	public int getGID() {
		return this.gid;
	}
	
	public void setUID(int uid) {
		this.uid = uid;
	}
	
	public int getUID() {
		return this.uid;
	}
		
	public void setWand1(Location l) {
		this.Wand1 = l;
	}
	
	public Location getWand1() {
		return this.Wand1;
	}
	
	public void setWand2(Location l) {
		this.Wand2 = l;
	}
	
	public Location getWand2() {
		return this.Wand2;
	}

}
