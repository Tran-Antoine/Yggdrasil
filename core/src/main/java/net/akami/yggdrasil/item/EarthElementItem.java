package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.utils.TextDisplayer;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EarthElementItem extends ElementItem {

    public EarthElementItem(MagicUser user, TextDisplayer textDisplayer) {
        super(user, textDisplayer);
    }

    @Override
    protected ParticleType getParticleType() {
        return ParticleTypes.BLOCK_CRACK;
    }

    @Override
    protected ElementType getType() {
        return ElementType.EARTH;
    }

    @Override
    protected DyeColor getColor() {
        return DyeColors.BROWN;
    }

    @Override
    protected Text getName() {
        return Text
                .builder()
                .color(TextColors.BLACK)
                .append(Text.of("Earth"))
                .build();
    }
}
