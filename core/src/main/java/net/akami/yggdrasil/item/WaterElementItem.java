package net.akami.yggdrasil.item;

import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.MagicUser;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
    protected int getParticleQuantity() {
        return 150;
    }

    @Override
    protected DyeColor getColor() {
        return DyeColors.BLUE;
    }

    @Override
    protected Text getName() {
        return Text
                .builder()
                .color(TextColors.DARK_BLUE)
                .append(Text.of("Water"))
                .build();
    }
}
