package net.akami.yggdrasil.player;

import net.akami.yggdrasil.api.item.InteractiveItem;
import net.akami.yggdrasil.api.life.LifeComponent;
import net.akami.yggdrasil.api.mana.ManaContainer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import net.akami.yggdrasil.api.spell.ElementType;
import net.akami.yggdrasil.api.spell.SpellCaster;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import net.akami.yggdrasil.item.*;
import net.akami.yggdrasil.life.PlayerLifeComponent;
import net.akami.yggdrasil.mana.PlayerManaContainer;
import net.akami.yggdrasil.spell.EarthTowerSpell;
import net.akami.yggdrasil.spell.FireballCaster;
import net.akami.yggdrasil.spell.GravitySpell;
import net.akami.yggdrasil.spell.WindOfFireSpell;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

import java.util.*;

public class YggdrasilPlayer implements AbstractYggdrasilPlayer {

    private UUID id;
    private ManaContainer mana;
    private LifeComponent life;
    private List<ElementType> sequence;
    private List<SpellCaster> spells;
    private List<InteractiveItem> items;

    public YggdrasilPlayer(UUID id) {
        this.id = id;
        this.sequence = new ArrayList<>();
        this.mana = new PlayerManaContainer(100, 4f, this); // this is a regular mana container
        //this.mana = new PlayerManaContainer(1000, 10, this); // for testing
        this.life = new PlayerLifeComponent(3, 50, this);
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
                new WindElementItem(this),
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
        Iterator<Slot> slots = target.getInventory().<Slot>slots().iterator();
        for(InteractiveItem interactiveItem : items) {
            ItemStack item = interactiveItem.matchingItem();
            Optional<Slot> freeSlot = findSlot(slots, item);
            freeSlot.ifPresent((slot) -> slot.set(item));
        }
    }

    private Optional<Slot> findSlot(Iterator<Slot> slots, ItemStack item) {
        while (slots.hasNext()) {
            Slot current = slots.next();
            if (current.canFit(item)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }

    // TODO : Don't hardcode values
    @Override
    public void addDefaultSpells() {

        spells.add(new FireballCaster(this));
        spells.add(new SpellCaster.Builder()
                .withGenerator(WindOfFireSpell::new)
                .withManaUsage(YggdrasilMath.instantStandardPolynomialFunction(120))
                .withSequence(
                        ElementType.FIRE, ElementType.FIRE, ElementType.FIRE,
                        ElementType.EARTH,
                        ElementType.WIND,
                        ElementType.EARTH)
                .build());
        spells.add(new SpellCaster.Builder()
                .withGenerator(EarthTowerSpell::new)
                .withManaUsage(YggdrasilMath.instantStandardPolynomialFunction(40))
                .withSequence(
                        ElementType.EARTH, ElementType.EARTH, ElementType.EARTH,
                        ElementType.WIND)
                .build());
        spells.add(new SpellCaster.Builder()
                .withGenerator(GravitySpell::new)
                .withManaUsage(YggdrasilMath.instantStandardPolynomialFunction(80))
                .withSequence(
                        ElementType.EARTH, ElementType.EARTH,
                        ElementType.WIND, ElementType.WIND)
                .build());
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
}
