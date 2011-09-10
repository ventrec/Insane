package no.insane.insane.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import no.insane.insane.Insane;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class RedstoneRemoteHandler {
	
	private HashMap<String, ArrayList<RedstoneRemoteData>> recievers;
	
	public RedstoneRemoteHandler(Insane instance) {
		this.recievers = new HashMap<String, ArrayList<RedstoneRemoteData>>();
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void setReciever(Block b, String name) {
		
		Sign sign = (Sign) b.getState();
		
		byte face = sign.getData().getData();
		
		RedstoneRemoteData rrd = new RedstoneRemoteData();

		rrd.setFace(face);
		rrd.setLocation(b.getLocation());
		
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
				Insane.log.info("La til ny reciever på eksisterende frekvens");
			}

		} else {
			ArrayList<RedstoneRemoteData> recvs = new ArrayList<RedstoneRemoteData>();
			recvs.add(rrd);
			this.recievers.put(name, recvs);
			Insane.log.info("La til ny reciever på ny frekvens");
		}
		
	}
	
	public void removeReciever(String name, RedstoneRemoteData rrd) {
		this.recievers.get(name).remove(rrd);
	}
	
	public boolean hasReciever(String name) {
		if(this.recievers.containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<RedstoneRemoteData> getRecievers(String name) {
		return this.recievers.get(name);
	}
	public HashMap<String, ArrayList<RedstoneRemoteData>> getRecieverHash() {
		return this.recievers;
	}
}