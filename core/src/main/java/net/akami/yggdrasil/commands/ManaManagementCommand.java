package net.akami.yggdrasil.commands;

import net.akami.yggdrasil.api.input.UUIDHolder;
import net.akami.yggdrasil.api.mana.ManaContainer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.function.BiConsumer;

public class ManaManagementCommand implements CommandExecutor {

    private List<? extends AbstractYggdrasilPlayer> players;
    private Set<FieldGroup> fields;

    public ManaManagementCommand(List<? extends AbstractYggdrasilPlayer> players) {
        this.players = players;
        this.fields = createFields();
    }

    private Set<FieldGroup> createFields() {
        return new HashSet<>(Arrays.asList(
                new FieldGroup("amount", new HashMap<String, BiConsumer<ManaContainer, Float>>(){{
                    put("set", ManaContainer::setPreciseAmount);
                    put("add", ManaContainer::restore);
                    put("remove", ManaContainer::use);
                }}),
                new FieldGroup("max", new HashMap<String, BiConsumer<ManaContainer, Float>>(){{
                    put("set", ManaContainer::setPreciseMaxMana);
                    put("add", ManaContainer::addMaxMana);
                    put("remove", ManaContainer::removeMaxMana);
                }}),
                new FieldGroup("regen", new HashMap<String, BiConsumer<ManaContainer, Float>>(){{
                    put("set", ManaContainer::setPreciseRegeneration);
                    put("add", ManaContainer::enhanceRegeneration);
                    put("remove", ManaContainer::slowdownRegeneration);
                }})
        ));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

        if(!(src instanceof Player)) {
            return CommandResult.empty();
        }

        Player player = (Player) src;
        AbstractYggdrasilPlayer target = UUIDHolder.getByUUID(players, player.getUniqueId());

        if(target == null) {
            return CommandResult.empty();
        }

        String fieldName = args.<String>getOne("field").get();
        String action = args.<String>getOne("action").get();
        float value = args.<Double>getOne("value").get().floatValue();

        getByName(fieldName).ifPresent(field -> field.execute(action, target.getMana(), value));
        return CommandResult.success();
    }

    public CommandSpec loadSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.string(Text.of("action")),
                        GenericArguments.string(Text.of("field")),
                        GenericArguments.doubleNum(Text.of("value")))
                .executor(this)
                .build();
    }

    private Optional<FieldGroup> getByName(String name) {
        for(FieldGroup group : fields) {
            if(group.fieldName.equals(name)) {
                return Optional.of(group);
            }
        }
        return Optional.empty();
    }

    private static class FieldGroup {

        private String fieldName;
        private Map<String, BiConsumer<ManaContainer, Float>> actions;

        private FieldGroup(String fieldName, Map<String, BiConsumer<ManaContainer, Float>> actions) {
            this.fieldName = fieldName;
            this.actions = actions;
        }

        private void execute(String actionName, ManaContainer target, float value) {
            BiConsumer<ManaContainer, Float> action = actions.get(actionName);
            if(action != null) {
                action.accept(target, value);
            }
        }
    }
}
