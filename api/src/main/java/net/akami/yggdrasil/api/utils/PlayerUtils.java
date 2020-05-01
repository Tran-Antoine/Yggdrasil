package net.akami.yggdrasil.api.utils;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class PlayerUtils {

    public static Optional<Entity> getNearestPlayer(Player self) {

        World world = self.getWorld();
        Entity currentNearest = null;
        double currentMinDistance = Double.MAX_VALUE;

        for(Entity entity : world.getEntities(entity -> entity.getType() == EntityTypes.PIG)) {

            if(entity.equals(self)) continue;

            double distance = self.getPosition().distance(entity.getLocation().getPosition());
            if(distance < currentMinDistance) {
                currentMinDistance = distance;
                currentNearest = entity;
            }
        }

        return Optional.ofNullable(currentNearest);
    }
}
