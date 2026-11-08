package de.raindancer.modules.healthbars.model;

import java.util.UUID;

/** Where a bar was last put, to tell whether its wearer moved at all. */
public record Spot(UUID world, double x, double y, double z) {

    private static final double STILL = 0.01;

    public boolean sameAs(UUID otherWorld, double ox, double oy, double oz) {
        return world.equals(otherWorld) && Math.abs(x - ox) < STILL && Math.abs(y - oy) < STILL
                && Math.abs(z - oz) < STILL;
    }
}
