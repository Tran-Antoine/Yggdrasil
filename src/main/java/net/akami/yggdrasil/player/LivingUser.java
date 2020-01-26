package net.akami.yggdrasil.player;

public interface LivingUser extends UUIDHolder, LivingEntity {

    LifeComponent getLife();
}
