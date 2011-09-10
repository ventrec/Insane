package no.insane.insane.listeners;

import java.util.ArrayList;
import java.util.Iterator;

import no.insane.insane.Insane;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.RedstoneRemoteData;
import no.insane.insane.handlers.RedstoneRemoteHandler;
import no.insane.insane.handlers.UserHandler;
import no.insane.insane.handlers.WorldConfigurationHandler;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

	public class InsaneBlockListener extends BlockListener {
		
		private Insane plugin;
		private UserHandler userHandler;
		private RedstoneRemoteHandler rrh;

		public InsaneBlockListener(Insane instance) {
			this.plugin = instance;
			this.userHandler = instance.getUserHandler();
			this.rrh = instance.getRedstoneRemoteHandler();
		}
		
		public void onBlockForm(BlockFormEvent e) {
			World w = e.getBlock().getWorld();
			
			ConfigurationHandler cfg = this.plugin.getGlobalStateManager();
			WorldConfigurationHandler wcfg = cfg.get(w);
			
			if((!e.isCancelled()) && (e.getNewState().getType() == Material.ICE) && (!wcfg.IceRegen)) {
				e.setCancelled(true);
			} else if((!e.isCancelled()) && (e.getNewState().getType() == Material.SNOW) && (!wcfg.SnowRegen)) {
				e.setCancelled(true);
			}
			
		}
		
		public void onSignChange(SignChangeEvent e) {
			if(e.getLine(0).equalsIgnoreCase("mottaker")) {
				if(e.getLine(1).length() > 0) {
					this.rrh.setReciever(e.getBlock(), e.getLine(1));
				}
			}
		}
		@SuppressWarnings("rawtypes")
		public void onBlockRedstoneChange(BlockRedstoneEvent event) {
            if (!(event.getBlock().getState() instanceof Sign)) {
                    return;
            }
            Block block = event.getBlock();
            Sign sign = (Sign) event.getBlock().getState();
            if(sign.getLine(0).equalsIgnoreCase("sender")) {
            	if(sign.getLine(1).length() > 0) {
            		String name = sign.getLine(1);
            		if(this.rrh.hasReciever(name)) {
            			ArrayList<RedstoneRemoteData> mottakere = this.rrh.getRecievers(name);
            			Iterator itr = mottakere.iterator();
            			RedstoneRemoteData rrrd = null;
            			boolean remove = false;
            			while(itr.hasNext()) {
            				RedstoneRemoteData rrd = (RedstoneRemoteData) itr.next();
            				Block b = this.plugin.getServer().getWorld(block.getWorld().getName()).getBlockAt(rrd.getLocation());
            				if((b.getType() == Material.REDSTONE_TORCH_ON) || (b.getType() == Material.SIGN_POST)) {
	            				if((block.isBlockPowered()) || (block.isBlockIndirectlyPowered())) {
	            					b.setTypeIdAndData(Material.REDSTONE_TORCH_ON.getId(), rrd.getFace(), true);
	            				} else {
	            					b.setTypeIdAndData(Material.SIGN_POST.getId(), rrd.getFace(), true);	
	            				}
            				} else {
            					rrrd = rrd;
            					remove = true;            					
            				}
            			}
            			if(remove) {
            				this.rrh.removeReciever(name, rrrd);
            			}
            		}
            	} else {
            		return;
            	}
            } else {
            	return;
            }
		}
		public void onBlockPlace(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			
			// Kreves det tillatelse for bygging og kan brukeren bygge?
			if(!this.userHandler.canBuild(p, p.getWorld())) {
				e.setCancelled(true);
			}
		}
		
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			// Kreves det tillatelse for bygging og kan brukeren bygge?
			if(!this.userHandler.canBuild(p, p.getWorld())) {
				e.setCancelled(true);
			}
			
		}
		
	}