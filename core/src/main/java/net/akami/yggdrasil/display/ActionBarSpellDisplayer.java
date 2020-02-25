package net.akami.yggdrasil.display;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;

public class ActionBarSpellDisplayer {
    private Map<UUID, List<Text>> currentSequence = new HashMap<>();
    private boolean start = false;

    public void start(){
        if(start) return;
        start = true;
        Sponge.getScheduler().createTaskBuilder()
                .name("ActionBarSpellDisplayer")
                .intervalTicks(20)
                .execute(this::execute)
                .submit(Sponge.getPluginManager().getPlugin("yggdrasil").get());
    }

    public void addElement(UUID uuid, Text elementType){
        List<Text> elementTypes = new ArrayList<>();
        if(currentSequence.containsKey(uuid)) elementTypes = currentSequence.get(uuid);
        elementTypes.add(elementType);
        currentSequence.put(uuid, elementTypes);
    }

    public void clearDisplay(UUID uuid){
        currentSequence.remove(uuid);
        Sponge.getServer().getPlayer(uuid).ifPresent(player -> player.sendMessage(ChatTypes.ACTION_BAR, Text.of("")));
    }


    private void execute(){
        for(Map.Entry<UUID, List<Text>> entry : currentSequence.entrySet()){
            Optional<Player> optionalPlayer = Sponge.getServer().getPlayer(entry.getKey());
            Text.Builder text = Text.builder();
            text.append(entry.getValue().get(0));
            for(int i = 1 ; i < entry.getValue().size() ; i++){
                text.append(Text.builder().color(TextColors.AQUA).append(Text.of(" > ")).build()).append(entry.getValue().get(i));
            }
            optionalPlayer.ifPresent(player -> player.sendMessage(ChatTypes.ACTION_BAR, text.build()));
        }
    }
}
