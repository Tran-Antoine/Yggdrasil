package net.akami.yggdrasil.api.mana;


import net.akami.yggdrasil.api.spell.MagicUser;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ManaDrainTask implements Consumer<Task> {

    private static final int CALLS_PER_SECOND = 10;

    private float time = 1;
    private MagicUser user;
    private Function<Float, Float> costFunc;
    private Runnable onCancelled;
    private Supplier<Boolean> cancelPredicate;

    public ManaDrainTask(MagicUser user, Function<Float, Float> costFunc) {
        this.user = user;
        this.costFunc = costFunc;
    }

    @Override
    public void accept(Task task) {
        float cost = costFunc.apply(time++) / CALLS_PER_SECOND;
        ManaContainer mana = user.getMana();

        if(!mana.hasEnoughMana(cost) || cost == 0
                || (cancelPredicate != null && cancelPredicate.get())) {
            task.cancel();
            if(onCancelled != null) onCancelled.run();
            return;
        }
        mana.use(cost);
    }

    public void setOnCancelled(Runnable onCancelled) {
        this.onCancelled = onCancelled;
    }

    public void setCancelPredicate(Supplier<Boolean> cancelPredicate) {
        this.cancelPredicate = cancelPredicate;
    }
}
