package net.akami.yggdrasil.api.utils;

import com.flowpowered.math.vector.Vector3d;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SpaceScanner {

    private Iteration top = null;
    private List<Consumer<List<Double>>> actions;
    private Set<Predicate<List<Double>>> filters;
    private List<Double> shift;

    public SpaceScanner() {
        this.actions = new ArrayList<>();
        this.filters = new HashSet<>();
        this.shift = new ArrayList<>();
    }

    public void queueIteration(double from, double to, double step) {
        if(top == null) {
            top = new Iteration(null, from, to, step);
            return;
        }
        top.stack(from, to, step);
    }

    public void addAction(Consumer<List<Double>> action) {
        actions.add(action);
    }

    public void addFilter(Predicate<List<Double>> condition) {
        filters.add(condition);
    }

    public void shift(List<Double> shift) {
        this.shift = shift;
    }

    public void run() {

        boolean canContinue = true;

        while(canContinue) {
            List<Double> values = new ArrayList<>();
            canContinue = top.nextIteration(values);
            shiftValues(values);
            for(Consumer<List<Double>> action : actions) {
                if(filters
                        .stream()
                        .allMatch(listPredicate -> listPredicate.test(values))) {

                    action.accept(values);
                }
            }
        }
    }

    private void shiftValues(List<Double> values) {
        for(int i = 0; i < shift.size(); i++) {
            values.set(i, values.get(i) + shift.get(i));
        }
    }

    public static List<Double> asShift(Vector3d vec) {
        return Arrays.asList(vec.getX(), vec.getY(), vec.getZ());
    }

    private static class Iteration {

        private double from;
        private double to;
        private double step;
        private double current;

        private Iteration parent;
        private Iteration next;

        public Iteration(Iteration parent, double from, double to, double step) {
            this.parent = parent;
            this.from = from;
            this.to = to;
            this.step = step;
            this.current = from;
        }

        private void stack(double from, double to, double step) {
            if(next == null) {
                this.next = new Iteration(this, from, to, step);
            } else {
                next.stack(from, to, step);
            }
        }

        private void increment() {
            current += step;
        }

        private Iteration onLoopEnded() {
            if(parent != null) {
                parent.increment();
                if(parent.wentOverEnd()) {
                    return parent.onLoopEnded();
                }
                return this;
            }
            return null;
        }

        private void reset() {
            this.current = from;
            if(next != null) {
                next.reset();
            }
        }

        private boolean wentOverEnd() {
            return current > to;
        }

        private boolean nextIteration(List<Double> toFill) {
            toFill.add(current);

            if(next == null) {
                increment();
            }
            if(wentOverEnd()) {
                Iteration root = onLoopEnded();
                if(root == null) { // meaning the retrieved iteration is the head -> looping should be terminated
                    return false;
                }
                root.reset();
                return true;
            }
            if(next != null) return next.nextIteration(toFill);
            return true;
        }
    }

    public static class Builder {

        private SpaceScanner scanner;

        public Builder() {
            this.scanner = new SpaceScanner();
        }

        public SpaceScanner build() {
            return scanner;
        }

        public Builder queueIteration(double from, double to, double step) {
            scanner.queueIteration(from, to, step);
            return this;
        }

        public Builder shift(List<Double> shift) {
            scanner.shift(shift);
            return this;
        }

        public Builder withAction(Consumer<List<Double>> action) {
            scanner.addAction(action);
            return this;
        }

        public Builder withFilter(Predicate<List<Double>> condition) {
            scanner.addFilter(condition);
            return this;
        }
    }
}
