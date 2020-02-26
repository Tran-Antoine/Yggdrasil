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
import net.akami.yggdrasil.api.utils.TextDisplayer;
import net.akami.yggdrasil.display.ActionBarSpellDisplayer;
import net.akami.yggdrasil.item.*;
import net.akami.yggdrasil.life.PlayerLifeComponent;
import net.akami.yggdrasil.mana.PlayerManaContainer;
import net.akami.yggdrasil.spell.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.*;

public class YggdrasilPlayer implements AbstractYggdrasilPlayer, TextDisplayer {

    private UUID id;
    private ManaContainer mana;
    private LifeComponent life;
    private List<ElementType> sequence;
    private List<SpellType> excludedTypes;
    private List<SpellCaster> spells;
    private List<InteractiveItem> items;
    private ExcludedSpellHandler spellHandler;
    private ActionBarSpellDisplayer actionBarSpellDisplayer;

    public YggdrasilPlayer(UUID id, ExcludedSpellHandler spellHandler, ActionBarSpellDisplayer actionBarSpellDisplayer) {
        this.id = id;
        this.spellHandler = spellHandler;
        this.sequence = new ArrayList<>();
        this.mana = new PlayerManaContainer(100, 4f, this);
        this.life = new PlayerLifeComponent(3, 50, this);
        this.excludedTypes = new ArrayList<>();
        this.spells = new ArrayList<>();
        this.items = new ArrayList<>();
        this.actionBarSpellDisplayer = actionBarSpellDisplayer;
        addDefaultItems();
        addDefaultSpells();
    }

    @Override
    public void addDefaultItems() {
        items.addAll(Arrays.asList(
                new AdvancedMovementItem(this),
                new InstantHealItem(this),

                new SpellTriggerItem(this, this),
                new FireElementItem(this, this),
                new AirElementItem(this, this),
                new EarthElementItem(this, this),
                new WaterElementItem(this, this)));
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

    @Override
    public void displayActionBar(Text text) {
        Sponge.getServer().getPlayer(this.id).ifPresent(player -> player.sendMessage(ChatTypes.ACTION_BAR, text));
    }

    @Override
    public void addActionBarDisplayElement(Text element) {
        actionBarSpellDisplayer.addElement(this, element);
    }

    @Override
    public void clearActionBarDisplay() {
        actionBarSpellDisplayer.clearSequence(this);
        Sponge.getServer().getPlayer(this.id).ifPresent(player -> player.sendMessage(ChatTypes.ACTION_BAR, Text.of("")));
    }
}
