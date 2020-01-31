package net.akami.yggdrasil.item.list;

import net.akami.yggdrasil.spell.ElementType;
import net.akami.yggdrasil.spell.MagicUser;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;

public class WaterElementItem extends ElementItem {

    public WaterElementItem(MagicUser user) {
        super(user);
    }

    @Override
    protected ElementType getType() {
        return ElementType.WATER;
    }

    @Override
    protected ParticleType getParticleType() {
        return ParticleTypes.WATER_SPLASH;
    }

    @Override
    protected int getQuantity() {
        return 150;
    }

    @Override
    protected DyeColor getColor() {
        return DyeColors.BLUE;
    }
}
