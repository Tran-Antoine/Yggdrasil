package net.akami.yggdrasil.player;

import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.life.LifeComponent;
import net.akami.yggdrasil.api.mana.ManaContainer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.ExcludedSpellHandler;
import net.akami.yggdrasil.api.spell.SpellCaster;
import net.akami.yggdrasil.api.spell.SpellCaster.SpellType;
import net.akami.yggdrasil.api.utils.ItemUtils;
import net.akami.yggdrasil.item.*;
import net.akami.yggdrasil.life.PlayerLifeComponent;
import net.akami.yggdrasil.mana.PlayerManaContainer;
import net.akami.yggdrasil.spell.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

public class YggdrasilPlayer implements AbstractYggdrasilPlayer {

    private final UUID id;
    private final ManaContainer mana;
    private final LifeComponent life;
    private final List<ElementType> sequence;
    private final List<SpellType> excludedTypes;
    private final List<SpellCaster> spells;
    private final List<InteractiveItem> items;
    private final ExcludedSpellHandler spellHandler;

    public YggdrasilPlayer(UUID id, ExcludedSpellHandler spellHandler) {
        this.id = id;
        this.spellHandler = spellHandler;
        this.sequence = new ArrayList<>();
        this.mana = new PlayerManaContainer(100, 4f, this);
        this.life = new PlayerLifeComponent(3, 50, this);
        this.excludedTypes = new ArrayList<>();
        this.spells = new ArrayList<>();
        this.items = new ArrayList<>();
        addDefaultItems();
        addDefaultSpells();
    }

    @Override
    public void addDefaultItems() {
        items.addAll(Arrays.asList(
                new AdvancedMovementItem(this),
                new InstantHealItem(this),

                new SpellTriggerItem(this),
                new FireElementItem(this),
                new AirElementItem(this),
                new EarthElementItem(this),
                new WaterElementItem(this)));
        addItemsToInventory();
    }

    private void addItemsToInventory() {
        Optional<Player> optPlayer = Sponge.getServer().getPlayer(id);
        optPlayer.ifPresent(this::fill);
    }

    private void fill(Player target) {
        target.getInventory().clear();
        for(InteractiveItem item : items) {
            ItemUtils.fitItemInInventory(target, item);
        }
    }

    @Override
    public void addDefaultSpells() {
        spells.addAll(Arrays.asList(
                new FireballCaster(this),
                new PhoenixArrowCaster(),
                new IncendiaCaster(),
                new CounterVelocityCaster(this),
                new WaterPrisonCaster()
        ));
    }

    @Override
    public UUID getUUID() {
        return id;
    }

    @Override
    public LifeComponent getLife() {
        return life;
    }

    @Override
    public ManaContainer getMana() {
        return mana;
    }

    @Override
    public List<SpellCaster> getSpells() {
        return spells;
    }

    @Override
    public List<InteractiveItem> getItems() {
        return items;
    }

    @Override
    public List<ElementType> currentSequence() {
        return sequence;
    }

    @Override
    public List<SpellType> currentlyExcludedTypes() {
        return excludedTypes;
    }

    @Override
    public ExcludedSpellHandler getExclusionHandler() {
        return spellHandler;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof YggdrasilPlayer && id.equals(((YggdrasilPlayer) obj).id);
    }

}
