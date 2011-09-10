package no.insane.insane.handlers;

import org.bukkit.Location;

public class RedstoneRemoteData {
	private byte face;
	private Location loc;
	
	public void setFace(byte face) {
		this.face = face;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public byte getFace() {
		return this.face;
	}
	
	public Location getLocation() {
		return this.loc;
	}
}