package de.raindancer.modules.healthbars.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Bars bucketed by world and square cell, so a viewer is only compared with the bars near them. Built per round, never kept. */
public final class BarGrid<T> {

    private record Cell(UUID world, int x, int z) {
    }

    private final double size;
    private final Map<Cell, List<T>> cells = new HashMap<>();

    public BarGrid(double cellSize) {
        this.size = Math.max(1, cellSize);
    }

    public void add(UUID world, double x, double z, T item) {
        cells.computeIfAbsent(cell(world, x, z), key -> new ArrayList<>()).add(item);
    }

    /** Every item in a cell the square of half-width {@code range} around the point touches: a superset of those within range. */
    public List<T> near(UUID world, double x, double z, double range) {
        int fromX = (int) Math.floor((x - range) / size);
        int toX = (int) Math.floor((x + range) / size);
        int fromZ = (int) Math.floor((z - range) / size);
        int toZ = (int) Math.floor((z + range) / size);
        List<T> found = new ArrayList<>();
        for (int cx = fromX; cx <= toX; cx++) {
            for (int cz = fromZ; cz <= toZ; cz++) {
                List<T> here = cells.get(new Cell(world, cx, cz));
                if (here != null) {
                    found.addAll(here);
                }
            }
        }
        return found;
    }

    private Cell cell(UUID world, double x, double z) {
        return new Cell(world, (int) Math.floor(x / size), (int) Math.floor(z / size));
    }
}
