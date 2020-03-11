package net.akami.yggdrasil.api.display;

import org.spongepowered.api.text.Text;

public interface SimpleTextDisplayer extends TextDisplayer {

    Group<TextDisplayer, Text> getDisplayGroup();

    default void addActionBarDisplayElement(Text element) {
        getDisplayGroup().addElement(this, element);
    }
}
