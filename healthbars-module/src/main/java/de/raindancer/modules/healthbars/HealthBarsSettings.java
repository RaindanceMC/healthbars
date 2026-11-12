package de.raindancer.modules.healthbars;

import de.raindancer.core.data.settings.Describe;
import de.raindancer.core.data.settings.In;
import de.raindancer.core.data.settings.Key;
import de.raindancer.core.data.settings.Range;
import de.raindancer.core.data.settings.Settings;
import de.raindancer.core.data.settings.Title;
import de.raindancer.core.data.settings.Topic;
import de.raindancer.modules.healthbars.rules.HealthStyle;
import org.bukkit.Material;

/** What an owner decides about health bars. The schema of the file and of the /settings page. */
@Settings(id = "healthbars", topics = {
        @Topic(path = "healthbars", title = "Health bars", icon = Material.RED_DYE),
})
public record HealthBarsSettings(

        @In("healthbars") @Title("Health under players' names")
        @Key("players")
        boolean players,

        @In("healthbars") @Title("Health over hurt mobs")
        @Describe("A mob's health shows while it is hurt and for a while after its last hit — not over every "
                + "animal in sight.")
        @Key("mobs")
        boolean mobs,

        @In("healthbars") @Title("On for somebody who never chose")
        @Describe("Whether a player who never typed /healthbars sees them. Everybody can switch them for "
                + "themselves; who has chosen keeps what they chose.")
        @Key("on-by-default")
        boolean onByDefault,

        @In("healthbars") @Title("Seen from, blocks") @Range(min = 4, max = 64)
        @Key("range")
        int range,

        @In("healthbars") @Title("A mob's bar stays, seconds after a hit") @Range(min = 1, max = 300)
        @Key("mob-seconds")
        int mobSeconds,

        @In("healthbars") @Title("Mob bars per chunk") @Range(min = 1, max = 64)
        @Describe("At most this many hit mobs wear a bar in one chunk at a time — a mob farm does not fill the "
                + "world with floating text.")
        @Key("mob-cap")
        int mobCap,

        @In("healthbars") @Title("Style for somebody who never chose")
        @Describe("HEARTS: a heart and the health. BAR: a line and the percent. Each player picks their own "
                + "with /healthbars style.")
        @Key("default-style")
        HealthStyle defaultStyle,

        @In("healthbars") @Title("Height under players' names, hundredths of a block") @Range(min = 0, max = 100)
        @Describe("How far above a player's head their health line floats. 0 sits on the head; around 25 is "
                + "right under the name. Changes at once.")
        @Key("player-height")
        int playerHeight) {

    public static final HealthBarsSettings DEFAULTS = new HealthBarsSettings(true, true, true, 32, 15, 8, HealthStyle.HEARTS,
            25);

    /** Blocks above a player's head, clamped. */
    public double playerLift() {
        return Math.max(0, Math.min(100, playerHeight)) / 100.0;
    }

    public HealthBarsSettings withPlayerHeight(int hundredths) {
        return new HealthBarsSettings(players, mobs, onByDefault, range, mobSeconds, mobCap, defaultStyle, hundredths);
    }
}
