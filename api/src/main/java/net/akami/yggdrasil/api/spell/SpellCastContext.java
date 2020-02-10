package net.akami.yggdrasil.api.spell;

public class SpellCastContext {

    private SpellCaster caster;
    private int tierChosen;
    private boolean requiresLocation;

    public SpellCastContext(SpellCaster caster, int tierChosen, boolean requiresLocation) {
        this.caster = caster;
        this.tierChosen = tierChosen;
        this.requiresLocation = requiresLocation;
    }

    public SpellCaster getCaster() {
        return caster;
    }

    public int getChosenTier() {
        return tierChosen;
    }

    public boolean requiresLocation() {
        return requiresLocation;
    }
}
