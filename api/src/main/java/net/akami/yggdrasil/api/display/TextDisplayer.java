package net.akami.yggdrasil.api.display;

import org.spongepowered.api.text.Text;

public interface TextDisplayer {
    void displayActionBar(Text name);
    void clearActionBarDisplay();
}
