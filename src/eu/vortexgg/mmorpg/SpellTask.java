package eu.vortexgg.mmorpg;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

import eu.vortexgg.mmorpg.util.BukkitUtil;
import eu.vortexgg.mmorpg.util.HoloItem;
import eu.vortexgg.mmorpg.util.ParticleEffect;
import eu.vortexgg.mmorpg.util.VectorUtil;
import io.netty.util.internal.ThreadLocalRandom;

public class SpellTask extends JavaPlugin implements Listener {

    public static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final ItemStack MAGMA = new ItemStack(Material.MAGMA);
    private static final ItemStack BLAZE_ROD = new ItemStack(Material.BLAZE_ROD);
    private static final ParticleEffect.BlockData MAGMA_BLOCK_DATA = new ParticleEffect.BlockData(Material.MAGMA, (byte)0);
    
    @Override
    public void onEnable() {
	getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
	if (e.hasItem() && e.getAction().name().contains("RIGHT")) {
	    ItemStack item = e.getItem();
	    if (item.getType() == Material.STICK) {
		Player p = e.getPlayer();
		
		LivingEntity en = BukkitUtil.rayTraceEntity(p, 16);
		if(en == null)
		    return;
		
		Location eye = p.getEyeLocation(), start = eye, target = en.getLocation();
	
		Vector vector = target.clone().add(0, 1.5, 0).subtract(eye).toVector().normalize().multiply(0.5);
		
		start.add(vector);

		final float pitch = start.getPitch() + 90, yaw = start.getYaw();
		
		new BukkitRunnable() {	

		    int ticks = 0;
		    List<ArmorStand> spices = Lists.newArrayList();

		    @Override
		    public void run() {
			if(ticks++ == 28 || start.add(vector).getBlock().getType() != Material.AIR) {
			    ParticleEffect.EXPLOSION_LARGE.display(0.0F, 0.0F, 0.0F, 0.0F, 1, start, 100.0D);
			    spices.forEach(ArmorStand::remove);
			    spices = null;
			    ticks = 0;
			    cancel();
			    return;
			}

			final double mod = -ticks * 0.2F, cos = Math.cos(mod), sin = Math.sin(mod);
			final Vector right = new Vector(sin * 0.5f, 0, cos * 0.5f), left = new Vector(sin * -0.5f, 0, cos * -0.5f);

			ParticleEffect.FLAME.display(0, 0, 0, 0, 1, start.clone().add(VectorUtil.horRotate(VectorUtil.verRotate(right, pitch), -yaw)), 25);
			ParticleEffect.FLAME.display(0, 0, 0, 0, 1, start.clone().add(VectorUtil.horRotate(VectorUtil.verRotate(left, pitch), -yaw)), 25);
			ParticleEffect.FLAME.display(0, 0, 0, 0, 1, start.clone().add(VectorUtil.verRotate(VectorUtil.horRotate(right, pitch), -yaw)), 25);
			ParticleEffect.FLAME.display(0, 0, 0, 0, 1, start.clone().add(VectorUtil.verRotate(VectorUtil.horRotate(left, pitch), -yaw)), 25);
			ParticleEffect.BLOCK_CRACK.display(MAGMA_BLOCK_DATA, 0, 0, 0, 0.1F, 5, start, 25);
			
			if(ticks % 3 == 0) {
			    ArmorStand rod = HoloItem.createHoloItem(BLAZE_ROD, start.clone().add(random.nextDouble(-0.25, 0.25), random.nextDouble(-1.5, 0.15), random.nextDouble(-0.25, 0.25)));
			    rod.setHeadPose(new EulerAngle(random.nextDouble(180), random.nextDouble(180), random.nextDouble(180)));
			    
			    ArmorStand magma = HoloItem.createHoloItem(MAGMA, start.clone().add(random.nextDouble(-0.25, 0.25), random.nextDouble(-1.5, 0.15), random.nextDouble(-0.25, 0.25)));
			    magma.setHeadPose(new EulerAngle(random.nextDouble(180), random.nextDouble(180), random.nextDouble(180)));
			    
			    for(LivingEntity entity : BukkitUtil.getNearbyEntities(magma, 1.5)) {
				if(entity.isDead() || entity.isInvulnerable())
				    continue;
				entity.damage(5.0, p);
				entity.setFireTicks(40);
			    }
			    
			    spices.add(rod);
			    spices.add(magma);
			}
			
		    }
		    
		}.runTaskTimer(this, 0, 1);

	    }
	}
    }
    
}
