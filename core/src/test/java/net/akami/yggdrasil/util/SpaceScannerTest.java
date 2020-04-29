package net.akami.yggdrasil.util;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.utils.SpaceScanner;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        combineDimensions(Flux.just(1, 2, 3), Flux.just(1, 2, 3), Flux.just(1, 2, 3));

    }

    public <R> void combineDimensions(Flux<R>... layers) {
        Flux<List<R>> combined = Flux.from(Mono.just(new ArrayList<>()));

        for (Flux<R> additionalLayer : layers) {
            combined = combined.flatMap(list -> {
                Flux<List<R>> map = additionalLayer.map(x ->
                {
                    List<R> values = new ArrayList<>(list);
                    values.add(x);
                    return values;
                });
                return map;
            });
            System.out.println(combined.toStream().collect(Collectors.toList()));
            System.out.println("...");
        }
    }

    class MyTest {
        List<String> list = Arrays.asList("a", "b", "c");
        int n;

        MyTest(int n) {
            this.n = n;
        }

        public List<String> getList() {
            return list;
        }

        @Override
        public String toString() {
            return "" + n;
        }
    }
}
