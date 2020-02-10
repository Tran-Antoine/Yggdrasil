package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.MagicUser;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class FireElementItem extends ElementItem {

    public FireElementItem(MagicUser user) {
        super(user);
    }

    @Override
    protected ParticleType getParticleType() {
        return ParticleTypes.LAVA;
    }

    @Override
    protected DyeColor getColor() {
        return DyeColors.RED;
    }

    @Override
    protected ElementType getType() {
        return ElementType.FIRE;
    }

    @Override
    protected Text getName() {
        return Text
                .builder()
                .color(TextColors.RED)
                .append(Text.of("Fire"))
                .build();
    }
}
