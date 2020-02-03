package net.akami.yggdrasil.api.spell;

public class SpellCastResult {

    private SpellCaster caster;
    private int tierChosen;

    public SpellCastResult(SpellCaster caster, int tierChosen) {
        this.caster = caster;
        this.tierChosen = tierChosen;
    }

    public SpellCaster getCaster() {
        return caster;
    }

    public int getChosenTier() {
        return tierChosen;
    }
}
