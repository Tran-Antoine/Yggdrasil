package net.akami.yggdrasil.display;

import net.akami.yggdrasil.api.utils.TextDisplayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionBarSpellDisplayer {
    private Map<TextDisplayer, List<Text>> currentSequence = new HashMap<>();
    private boolean start = false;

    public void start(){
        if(start) return;
        start = true;
        Sponge.getScheduler().createTaskBuilder()
                .name("ActionBarSpellDisplayer")
                .intervalTicks(20)
                .execute(this::display)
                .submit(Sponge.getPluginManager().getPlugin("yggdrasil").get());
    }

    public void addElement(TextDisplayer textDisplayer, Text element){
        List<Text> elementTypes = new ArrayList<>();
        if(currentSequence.containsKey(textDisplayer)) elementTypes = currentSequence.get(textDisplayer);
        elementTypes.add(element);
        currentSequence.put(textDisplayer, elementTypes);
    }

    public void clearSequence(TextDisplayer textDisplayer){
        currentSequence.remove(textDisplayer);
    }

    private void display(){
        for(Map.Entry<TextDisplayer, List<Text>> entry : currentSequence.entrySet()){
            Text text = Text.joinWith(Text.builder().color(TextColors.AQUA).append(Text.of(" > ")).build(), entry.getValue());
            entry.getKey().displayActionBar(text);
        }
    }
}
