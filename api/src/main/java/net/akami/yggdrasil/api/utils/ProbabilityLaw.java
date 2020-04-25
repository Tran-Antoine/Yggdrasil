package net.akami.yggdrasil.api.utils;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

public class ProbabilityLaw<T> {

    public static final float GUARANTEED = -1;

    private final Map<T, Float> events;
    private final Random random;
    private float totalWeight;

    public ProbabilityLaw() {
        this.events = new HashMap<>();
        this.random = new Random();
        this.totalWeight = 0;
    }

    public static <T> ProbabilityLaw<T> of(List<T> elements, Function<T, Float> func) {
        ProbabilityLaw<T> law = new ProbabilityLaw<>();
        for(T element : elements) {
            law.add(element, func.apply(element));
        }
        return law;
    }

    public void add(T element, float weight) {
        // 0 weight elements are not taken into account
        if(weight == 0) return;

        events.put(element, weight);
        totalWeight += weight;
    }

    public void clear() {
        events.clear();
        totalWeight = 0;
    }

    public void remove(T element) {
        totalWeight -= events.get(element);
        events.remove(element);
    }

    public Optional<T> draw() {
        if(events.size() == 0) {
            return Optional.empty();
        }

        float current = 0;
        float randValue = random.nextFloat() * totalWeight;
        T previous = null;
        for(Entry<T, Float> event : events.entrySet()) {
            float value = event.getValue();
            T key = event.getKey();
            if(value == -1) {
                return Optional.of(key);
            }
            if(current > randValue) {
                return Optional.ofNullable(previous);
            }
            previous = key;
            current += value;
        }
        return Optional.ofNullable(previous);
    }
}
