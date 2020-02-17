package net.akami.yggdrasil.api.utils;

import com.flowpowered.math.vector.Vector3d;

import java.util.function.BiFunction;
import java.util.function.Function;

public class YggdrasilMath {

    private YggdrasilMath() {}

    public static Vector3d headRotationToDirection(Vector3d headRot) {
        double pitch = Math.toRadians(-headRot.getX());
        double yaw = Math.toRadians(-headRot.getY());
        double x = Math.sin(yaw)*Math.cos(pitch);
        double z = Math.cos(yaw)*Math.cos(pitch);
        double y = Math.sin(pitch);
        return new Vector3d(x, y/1.5, z);
    }

    public static double velocityToFallingDistance(double yVelocity) {
        return Math.max(2.8 * Math.exp(1.298 * Math.abs(yVelocity)) - 5, 0);
    }

    public static BiFunction<Float, Integer, Float> instantCostFunction(Function<Integer, Float> costPerTier) {
        return (time, tier) -> time == 0 ? costPerTier.apply(tier) : 0f;
    }

    public static Function<Integer, Float> standardPolynomialFunction(float initial) {
        return (tier) -> initial + (float) Math.pow(2*tier - 1, 1.3) - 1;
    }

    public static BiFunction<Float, Integer, Float> instantStandardPolynomialFunction(float initial) {
        return instantCostFunction(standardPolynomialFunction(initial));
    }
}
