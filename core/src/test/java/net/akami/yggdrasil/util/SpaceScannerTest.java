package net.akami.yggdrasil.util;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.utils.SpaceScanner;
import org.junit.Test;

public class SpaceScannerTest {

    @Test
    public void print_values_from_triple_iteration_test() {

        Vector3d center = new Vector3d(3, 9, 12);
        SpaceScanner scanner = new SpaceScanner.Builder()
                .queueIteration(7, 10, 1)
                .queueIteration(0, 3, 1.5)
                .queueIteration(2, 8, 10)
                .shift(SpaceScanner.asShift(center))
                .withAction(System.out::println)
                .build();
        scanner.run();
    }
}

