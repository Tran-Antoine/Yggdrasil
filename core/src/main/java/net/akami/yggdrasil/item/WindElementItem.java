package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.MagicUser;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;

public class WindElementItem extends ElementItem {

    public WindElementItem(MagicUser user) {
        super(user);
    }

    @Override
    protected ElementType getType() {
        return ElementType.WIND;
    }

    @Override
    protected ParticleType getParticleType() {
        return ParticleTypes.CLOUD;
    }

    @Override
    protected DyeColor getColor() {
        return DyeColors.GRAY;
    }
}
