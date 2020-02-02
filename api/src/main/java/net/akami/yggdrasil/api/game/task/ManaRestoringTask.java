package net.akami.yggdrasil.api.game.task;

import net.akami.yggdrasil.api.mana.ManaHolder;

import java.util.List;

public class ManaRestoringTask implements Runnable {

    private float deltaTime;
    private List<? extends ManaHolder> manaUsers;

    // Delta time is the delay in seconds between every run() call
    public ManaRestoringTask(List<? extends ManaHolder> manaUsers, float deltaTime) {
        this.manaUsers = manaUsers;
        this.deltaTime = deltaTime;
    }

    @Override
    public void run() {
        for(ManaHolder user : manaUsers) {
            user.getMana().autoRestore(deltaTime);
        }
    }
}
