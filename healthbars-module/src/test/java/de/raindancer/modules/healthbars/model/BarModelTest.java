package de.raindancer.modules.healthbars.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BarModelTest {

    private final UUID world = UUID.randomUUID();

    @Test
    @DisplayName("the grid hands back bars near the viewer, not those across the map or in another world")
    void gridFindsOnlyNearBars() {
        BarGrid<String> grid = new BarGrid<>(32);
        grid.add(world, 10, 10, "near");
        grid.add(world, 40, 10, "next cell, in range");
        grid.add(world, 500, 500, "far");
        grid.add(UUID.randomUUID(), 10, 10, "other world");
        assertThat(grid.near(world, 0, 0, 32)).containsExactlyInAnyOrder("near", "next cell, in range");
        assertThat(grid.near(world, -1000, -1000, 32)).isEmpty();
    }

    @Test
    @DisplayName("negative coordinates land in the right cells")
    void negativeCells() {
        BarGrid<String> grid = new BarGrid<>(16);
        grid.add(world, -1, -1, "a");
        assertThat(grid.near(world, -5, -5, 8)).containsExactly("a");
    }

    @Test
    @DisplayName("a wearer standing still is not moved; a step or another world is")
    void spot() {
        Spot spot = new Spot(world, 1, 64, 1);
        assertThat(spot.sameAs(world, 1.001, 64, 1)).isTrue();
        assertThat(spot.sameAs(world, 1.2, 64, 1)).isFalse();
        assertThat(spot.sameAs(world, 1, 64.5, 1)).isFalse();
        assertThat(spot.sameAs(UUID.randomUUID(), 1, 64, 1)).isFalse();
    }

    @Test
    @DisplayName("an area takes only so many bars; a freed place goes to the next mob")
    void cap() {
        MobBarCap cap = new MobBarCap();
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        UUID c = UUID.randomUUID();
        assertThat(cap.claim(a, "w:0:0", 2)).isTrue();
        assertThat(cap.claim(a, "w:0:0", 2)).as("the same mob again").isTrue();
        assertThat(cap.claim(b, "w:0:0", 2)).isTrue();
        assertThat(cap.claim(c, "w:0:0", 2)).isFalse();
        assertThat(cap.claim(c, "w:1:0", 2)).as("another area").isTrue();
        cap.release(a);
        assertThat(cap.claim(c, "w:0:0", 2)).as("c moves into the freed place").isTrue();
        assertThat(cap.in("w:1:0")).as("and leaves its old area").isZero();
        assertThat(cap.in("w:0:0")).isEqualTo(2);
    }
}
