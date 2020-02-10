package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.MagicUser;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class AirElementItem extends ElementItem {

    public AirElementItem(MagicUser user) {
        super(user);
    }

    @Override
    protected ElementType getType() {
        return ElementType.AIR;
    }

    @Override
    protected ParticleType getParticleType() {
        return ParticleTypes.CLOUD;
    }

    @Override
    protected DyeColor getColor() {
        return DyeColors.GRAY;
    }

    @Override
    protected Text getName() {
        return Text
                .builder()
                .color(TextColors.GRAY)
                .append(Text.of("Air"))
                .build();
    }
}
