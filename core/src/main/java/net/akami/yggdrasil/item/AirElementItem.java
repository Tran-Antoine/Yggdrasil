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

public class AirElementItem extends ElementItem {

    public AirElementItem(MagicUser user, SimpleTextDisplayer textDisplayer) {
        super(user, textDisplayer);
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
        return Text.builder()
                .color(TextColors.GRAY)
                .append(Text.of("Air"))
                .build();
    }

    @Override
    protected Text getSymbol() {
        return Text.builder()
                .color(TextColors.GRAY)
                //.append(Text.of("\uD83C\uDF00"))
                .append(Text.of("A"))
                .build();
    }
}
