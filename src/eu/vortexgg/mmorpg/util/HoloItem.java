package eu.vortexgg.mmorpg.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class HoloItem {

    public static ArmorStand createHoloItem(Material material, Location loc) {
	ArmorStand armorStand = (ArmorStand)loc.getWorld().spawn(loc, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setHelmet(new ItemStack(material));
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setSmall(true);
        return armorStand;
    }

    public static ArmorStand createHoloItem(ItemStack item, Location loc) {
	ArmorStand armorStand = (ArmorStand)loc.getWorld().spawn(loc, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setHelmet(item);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setSmall(true);
        return armorStand;
    }

}
