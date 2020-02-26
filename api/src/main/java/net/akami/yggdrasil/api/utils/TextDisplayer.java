package net.akami.yggdrasil.api.utils;

import org.spongepowered.api.text.Text;

public interface TextDisplayer {

    void displayActionBar(Text name);
    void addActionBarDisplayElement(Text element);
    void clearActionBarDisplay();

}
