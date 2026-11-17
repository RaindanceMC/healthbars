package de.raindancer.modules.healthbars;

import de.raindancer.core.ui.preference.Preference;
import de.raindancer.modules.healthbars.rules.HealthStyle;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.function.Supplier;

/** What /preferences offers for health bars: the same two things /healthbars switches. */
public final class HealthBarsPreferences {

    private static final String GROUP = "Health bars";

    private HealthBarsPreferences() {
    }

    public static Preference shown(Supplier<HealthBarsServices> services, Supplier<HealthBarsSettings> settings) {
        return Preference.toggle("healthbars:shown", GROUP, "Show health bars",
                        "Shows how hurt a mob is above its head, and health under players' names.",
                        player -> services.get().bars().wants().isOn(player),
                        (player, on) -> {
                            HealthBarsServices live = services.get();
                            live.bars().wants().set(player, on);
                            live.messages().send(player, on ? "healthbars.switched-on" : "healthbars.switched-off");
                        })
                .shownTo(player -> offered(settings.get()));
    }

    public static Preference style(Supplier<HealthBarsServices> services, Supplier<HealthBarsSettings> settings) {
        List<String> options = List.of("Hearts", "Bar");
        return Preference.choice("healthbars:style", GROUP, "Style", "How health is drawn: a heart and the number, or a bar and the percent.",
                        options, player -> services.get().bars().style(player).ordinal(),
                        (player, index) -> {
                            HealthBarsServices live = services.get();
                            live.bars().style(player, HealthStyle.values()[index]);
                            live.messages().send(player, "healthbars.style", "example",
                                    MiniMessage.miniMessage().serialize(live.bars().preview(player, player)));
                        })
                .shownTo(player -> offered(settings.get()));
    }

    private static boolean offered(HealthBarsSettings settings) {
        return settings.players() || settings.mobs();
    }
}
