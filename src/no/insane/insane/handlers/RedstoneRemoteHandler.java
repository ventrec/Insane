package no.insane.insane.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import no.insane.insane.Insane;

public class RedstoneRemoteHandler {
	
	private HashMap<String, ArrayList<RedstoneRemoteData>> recievers;
	
	public RedstoneRemoteHandler(Insane instance) {
		this.recievers = new HashMap<String, ArrayList<RedstoneRemoteData>>();
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setReciever(Block b) {
		
		Sign sign = (Sign) b.getState();
		String name = sign.getLine(1);
		
		byte face = sign.getData().getData();
		Location loc = new Location(sign.getWorld(), sign.getX(), sign.getY(), sign.getZ());
		
		RedstoneRemoteData rrd = new RedstoneRemoteData();

		rrd.setFace(face);
		rrd.setLocation(loc);
		
		if(this.recievers.containsKey(name)) {
			ArrayList<RedstoneRemoteData> recvs = new ArrayList<RedstoneRemoteData>();
			recvs = this.recievers.get(name);
			Iterator itr = recvs.iterator();
			boolean add = true;
			while(itr.hasNext()) {
				RedstoneRemoteData irrd = (RedstoneRemoteData) itr.next();
				if(rrd.getLocation() == irrd.getLocation()) {
					add = false;
				}
			}
			if(add) {
				recvs.add(rrd);
				this.recievers.put(name, recvs);
			}

		} else {
			ArrayList<RedstoneRemoteData> recvs = new ArrayList();
			recvs.add(rrd);
			this.recievers.put(name, recvs);
		}
		
	}
	
	public HashMap<String, ArrayList<RedstoneRemoteData>> getRecievers() {
		return this.recievers;
	}
}