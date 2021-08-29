package eu.vortexgg.mmorpg.util;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;

public class BukkitUtil {

    public static List<LivingEntity> getNearbyEntities(Entity entity, double range) {
	return entity.getNearbyEntities(range, range, range).stream().filter(en -> en instanceof LivingEntity).map(en -> (LivingEntity) en).collect(Collectors.toList());
    }

    public static LivingEntity rayTraceEntity(LivingEntity bukkitEntity, int range) {
	Location eye = bukkitEntity.getEyeLocation();
	BlockIterator iterator = new BlockIterator(bukkitEntity.getWorld(), eye.toVector(), eye.getDirection(), 0.0D, range);
	Chunk chunk = null;
	Entity[] entities = null;
	while (iterator.hasNext()) {
	    Location l = iterator.next().getLocation();
	    if (chunk != l.getChunk()) {
		chunk = l.getChunk();
		entities = chunk.getEntities();
	    }
	    if (entities != null) {
		for (int i = 0; i < entities.length; i++) {
		    Entity entity = entities[i];
		    if (entity != bukkitEntity && l.getWorld() == entity.getLocation().getWorld()
			    && entity instanceof LivingEntity && !entity.isInvulnerable() && !entity.isDead()
			    && l.distance(entity.getLocation()) < 1.25)
			return (LivingEntity) entity;
		}
	    }
	}
	return null;
    }
}
