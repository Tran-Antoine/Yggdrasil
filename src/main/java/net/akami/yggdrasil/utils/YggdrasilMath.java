package net.akami.yggdrasil.utils;

import com.flowpowered.math.vector.Vector3d;

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

    public static Function<Float, Float> instantCostFunction(float cost) {
        return (time) -> time == 0 ? cost : 0;
    }
}
