package de.raindancer.modules.healthbars;

import de.raindancer.core.ui.messages.Messages;
import de.raindancer.core.ui.preference.Preference;
import de.raindancer.core.ui.profile.PlayerSwitch;
import de.raindancer.modules.healthbars.rules.HealthStyle;
import de.raindancer.modules.healthbars.service.HealthBarService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HealthBarsPreferencesTest {

    private final Player player = mock(Player.class);
    private final Messages messages = mock(Messages.class);
    private final HealthBarService bars = mock(HealthBarService.class);
    private final PlayerSwitch wants = mock(PlayerSwitch.class);
    private HealthBarsSettings settings = HealthBarsSettings.DEFAULTS;
    private HealthBarsServices services;

    @BeforeEach
    void wire() {
        services = new HealthBarsServices(null, messages, bars);
        when(bars.wants()).thenReturn(wants);
    }

    @Test
    void theSwitchIsOnTheShownIdAndGoesThroughTheSwitch() {
        Preference shown = HealthBarsPreferences.shown(() -> services, () -> settings);
        when(wants.isOn(player)).thenReturn(true);
        assertThat(shown.id()).isEqualTo("healthbars:shown");
        assertThat(shown.group()).isEqualTo("Health bars");
        assertThat(shown.state(player)).isEqualTo("On");

        shown.choose().accept(player, 1);
        verify(wants).set(player, false);
        verify(messages).send(player, "healthbars.switched-off");
        shown.choose().accept(player, 0);
        verify(wants).set(player, true);
        verify(messages).send(player, "healthbars.switched-on");
    }

    @Test
    void theStyleChoiceIsHeartsAndBarAndSetsTheStyle() {
        Preference style = HealthBarsPreferences.style(() -> services, () -> settings);
        when(bars.style(player)).thenReturn(HealthStyle.BAR);
        when(bars.preview(any(), any())).thenReturn(Component.text("x"));
        assertThat(style.id()).isEqualTo("healthbars:style");
        assertThat(style.options()).containsExactly("Hearts", "Bar");
        assertThat(style.state(player)).isEqualTo("Bar");

        style.choose().accept(player, 0);
        verify(bars).style(player, HealthStyle.HEARTS);
        verify(messages).send(any(Player.class), org.mockito.ArgumentMatchers.eq("healthbars.style"), any(Object[].class));
    }

    @Test
    void notOfferedWhenTheServerShowsNoBarsAtAll() {
        Preference shown = HealthBarsPreferences.shown(() -> services, () -> settings);
        assertThat(shown.shown().test(player)).isTrue();
        settings = new HealthBarsSettings(false, false, true, 32, 15, 8, HealthStyle.HEARTS, 25);
        assertThat(shown.shown().test(player)).isFalse();
    }
}
