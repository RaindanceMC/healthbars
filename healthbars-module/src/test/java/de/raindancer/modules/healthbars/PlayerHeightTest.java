package de.raindancer.modules.healthbars;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** A player's health line sits under their name, not on their head. */
class PlayerHeightTest {

    @Test
    void aQuarterBlockAboveTheHeadByDefault() {
        assertThat(HealthBarsSettings.DEFAULTS.playerLift()).isEqualTo(0.25);
    }

    @Test
    void keptWithinReason() {
        assertThat(HealthBarsSettings.DEFAULTS.withPlayerHeight(-20).playerLift()).isEqualTo(0.0);
        assertThat(HealthBarsSettings.DEFAULTS.withPlayerHeight(400).playerLift()).isEqualTo(1.0);
    }
}
