package net.iris.ac.utils;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.stream.Collectors;

public class MiscUtils {
    public static boolean getLookingAt(PlayerWrapper player, EntityLiving livingEntity) {
        LocationHolder eye = player.asEntity().getEyeLocation();
        Vector3D toEntity = vectorSubtract(livingEntity.getEyeLocation().asVector(), eye.asVector());
        double dot = vectorDot(toEntity.normalize(), eye.getFacingDirection());

        return dot > 0.99D;
    }

    @Nullable
    public static EntityLiving getTargetEntity(PlayerWrapper player) {
        for (EntityBasic e : getNearbyEntities(player.getLocation(), 10)) {
            if (e instanceof EntityLiving) {
                if (getLookingAt(player, (EntityLiving) e)) {
                    return (EntityLiving) e;
                }
            }
        }
        return null;
    }

    @NonNull
    public static Vector3D vectorSubtract(Vector3D vec1, Vector3D vec2) {
        return new Vector3D(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY(), vec1.getZ() - vec2.getZ());
    }

    public static double vectorDot(Vector3D vec1, Vector3D vec2) {
        return vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY() + vec1.getZ() * vec2.getZ();
    }

    @NonNull
    public static List<EntityBasic> getNearbyEntities(LocationHolder loc, int radius) {
        final double squaredRadius = radius * radius;
        return loc.getWorld().getEntities().stream()
                .filter(e -> e.getLocation().getDistanceSquared(loc) <= radius)
                .collect(Collectors.toList());
    }
}
