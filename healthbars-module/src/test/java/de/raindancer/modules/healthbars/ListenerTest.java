package de.raindancer.modules.healthbars;

import de.raindancer.modules.healthbars.listener.HealthBarsListener;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListenerTest {

    @Test
    @DisplayName("a mob gets a bar from a player's blow or shot, not from other mobs or the world")
    void onlyPlayersStartBars() {
        Arrow shotByPlayer = mock(Arrow.class);
        when(shotByPlayer.getShooter()).thenReturn(mock(Player.class));
        Arrow shotByDispenser = mock(Arrow.class);
        assertThat(HealthBarsListener.byAPlayer(mock(Player.class))).isTrue();
        assertThat(HealthBarsListener.byAPlayer(shotByPlayer)).isTrue();
        assertThat(HealthBarsListener.byAPlayer(shotByDispenser)).isFalse();
        assertThat(HealthBarsListener.byAPlayer(mock(Zombie.class))).isFalse();
    }
}
