package eu.vortexgg.mmorpg.util;

import org.bukkit.util.Vector;

public class VectorUtil {
    public static Vector verRotate(Vector paramVector, double paramDouble) {
	paramDouble = Math.toRadians(paramDouble);
	final double cos = Math.cos(paramDouble), sin = Math.sin(paramDouble), y = paramVector.getY() * cos - paramVector.getZ() * sin, z = paramVector.getY() * sin + paramVector.getZ() * cos;
	return paramVector.setY(y).setZ(z);
    }

    public static Vector horRotate(Vector paramVector, double paramDouble) {
	paramDouble = Math.toRadians(paramDouble);
	final double cos = Math.cos(paramDouble), sin = Math.sin(paramDouble), x = paramVector.getX() * cos + paramVector.getZ() * sin, z = paramVector.getX() * -sin + paramVector.getZ() * cos;
	return paramVector.setX(x).setZ(z);
    }
}