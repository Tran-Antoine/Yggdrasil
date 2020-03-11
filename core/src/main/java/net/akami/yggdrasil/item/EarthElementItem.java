package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.MagicUser;
import net.akami.yggdrasil.api.display.SimpleTextDisplayer;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EarthElementItem extends ElementItem {

    public EarthElementItem(MagicUser user, SimpleTextDisplayer textDisplayer) {
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

    @Override
    protected Text getSymbol() {
        return Text
                .builder()
                .color(TextColors.BLACK)
                //.append(Text.of("\t\\u23DA"))
                .append(Text.of("E"))
                .build();
    }
}
