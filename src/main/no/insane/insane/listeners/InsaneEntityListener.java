package no.insane.insane.listeners;

import no.insane.insane.Insane;
import no.insane.insane.handlers.ConfigurationHandler;
import no.insane.insane.handlers.WorldConfigurationHandler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class InsaneEntityListener extends EntityListener {

	private final Insane plugin;

	public InsaneEntityListener(Insane instance) {
		this.plugin = instance;
	}

	public void onEntityDamage(EntityDamageEvent event) {
		World world = event.getEntity().getWorld();
		Entity attacker = null;
		Entity defender = event.getEntity();
		DamageCause type = event.getCause();

		ConfigurationHandler cfg = this.plugin.getGlobalStateManager();
		WorldConfigurationHandler wcfg = cfg.get(world);
		if ((!wcfg.Damage) || (!wcfg.PVPDamage) || (!wcfg.CreatureDamage)) {
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent) event;
				attacker = subEvent.getDamager();
				if ((defender instanceof Player) && (!wcfg.PVPDamage) && (attacker instanceof Player) || (defender instanceof Player) && (attacker instanceof Creature || attacker instanceof Monster) && (!wcfg.CreatureDamage)) {
					event.setCancelled(true);
					return;
				}
			}
		}

		if (type == DamageCause.DROWNING) {
			if ((!wcfg.Damage) || (!wcfg.DrowningDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.CONTACT) {
			if ((!wcfg.Damage) || (!wcfg.ContactDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.ENTITY_EXPLOSION) {
			if (!wcfg.Damage || (attacker instanceof Creature) && (!wcfg.CreatureDamage)) {
				event.setCancelled(true);
			} else if ((!wcfg.Damage)	|| !(attacker instanceof LivingEntity) && (!wcfg.TNTDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.FALL) {
			if ((!wcfg.Damage) || (!wcfg.FallDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.SUFFOCATION) {
			if ((!wcfg.Damage) || (!wcfg.BlockDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.FIRE) {
			if ((!wcfg.Damage) || (!wcfg.FireDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.FIRE_TICK) {
			if ((!wcfg.Damage) || (!wcfg.FireDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.LAVA) {
			if ((!wcfg.Damage) || (!wcfg.LavaDamage)) {
				event.setCancelled(true);
			}
		} else if (type == DamageCause.VOID) {
			if ((!wcfg.Damage) || (!wcfg.VoidDamage)) {
				event.setCancelled(true);
				if(event.getEntity() instanceof Player) {
					Player player = (Player)event.getEntity();
					if (wcfg.VoidTeleport) {
						player.teleport(event.getEntity().getWorld().getSpawnLocation());
					}
				}
			}
		}

		if (attacker instanceof Player) {

		}

		if (defender instanceof Player) {

		}

		if (defender instanceof Player) {
			if (!event.isCancelled() && !wcfg.Damage) {
				event.setCancelled(true);
			}
		}

	}
	
	public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {

	}

	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

	}

	public void onEntityExplode(EntityExplodeEvent event) {
		Location l = event.getLocation();
		Entity e = event.getEntity();
		World world = l.getWorld();

		ConfigurationHandler cfg = this.plugin.getGlobalStateManager();
		WorldConfigurationHandler wcfg = cfg.get(world);

		if (e instanceof LivingEntity) {
			if ((e instanceof Creeper) && (!wcfg.CreeperBlockDamage)) {
				event.setCancelled(true);
			}
		} else {
			if (!wcfg.TNTBlockDamage) {
				event.setCancelled(true);
			}
		}

	}

	public void onCreatureSpawn(CreatureSpawnEvent event) {
		CreatureType type = event.getCreatureType();
		World world = event.getLocation().getWorld();
		ConfigurationHandler cfg = this.plugin.getGlobalStateManager();
		WorldConfigurationHandler wcfg = cfg.get(world);

		if (type == CreatureType.CHICKEN && (!wcfg.Chickens)) {
			event.setCancelled(true);
		} else if (type == CreatureType.COW && (!wcfg.Cows)) {
			event.setCancelled(true);
		} else if (type == CreatureType.PIG && (!wcfg.Pigs)) {
			event.setCancelled(true);
		} else if (type == CreatureType.SHEEP && (!wcfg.Sheeps)) {
			event.setCancelled(true);
		} else if (type == CreatureType.SQUID && (!wcfg.Squids)) {
			event.setCancelled(true);
		} else if (type == CreatureType.CREEPER && (!wcfg.Creepers)) {
			event.setCancelled(true);
		} else if (type == CreatureType.SKELETON && (!wcfg.Skeletons)) {
			event.setCancelled(true);
		} else if (type == CreatureType.SLIME && (!wcfg.Slimes)) {
			event.setCancelled(true);
		} else if (type == CreatureType.ZOMBIE && (!wcfg.Zombies)) {
			event.setCancelled(true);
		} else if (type == CreatureType.SPIDER && (!wcfg.Spiders)) {
			event.setCancelled(true);
		} else if (type == CreatureType.WOLF && (!wcfg.Wolves)) {
			event.setCancelled(true);
		} else if (type == CreatureType.GHAST && (!wcfg.Ghasts)) {
			event.setCancelled(true);
		} else if (type == CreatureType.PIG_ZOMBIE && (!wcfg.PigZombies)) {
			event.setCancelled(true);
		}

	}
}