package net.akami.yggdrasil.api.display;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DisplayGroup implements Group<TextDisplayer, Text> {

    private Map<TextDisplayer, Text> currentSequence = new HashMap<>();
    private boolean start = false;

    public void start(){
        if(start) return;
        start = true;
        Task.builder()
                .name("DisplayGroup")
                .interval(1, TimeUnit.SECONDS)
                .execute(this::autoUpdate)
                .submit(Sponge.getPluginManager().getPlugin("yggdrasil").get());
    }

    @Override
    public void addElement(TextDisplayer textDisplayer, Text element){
        Text toPut = Text.of();
        if(currentSequence.containsKey(textDisplayer)) {
            toPut = currentSequence.get(textDisplayer);
            toPut = toPut.concat(Text.builder().color(TextColors.AQUA).append(Text.of(" > ")).build());
        }
        toPut = toPut.concat(element);
        currentSequence.put(textDisplayer, toPut);

        textDisplayer.displayActionBar(toPut);
    }

    public void clearSequence(SimpleTextDisplayer textDisplayer){
        currentSequence.remove(textDisplayer);
    }

    private void autoUpdate(){
        for(Map.Entry<TextDisplayer, Text> entry : currentSequence.entrySet()){
            Text text = entry.getValue();
            entry.getKey().displayActionBar(text);
        }
    }
}
