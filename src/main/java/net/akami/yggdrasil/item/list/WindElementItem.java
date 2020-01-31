package net.akami.yggdrasil.item.list;

import net.akami.yggdrasil.spell.ElementType;
import net.akami.yggdrasil.spell.MagicUser;
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
