package de.raindancer.modules.healthbars.listener;

import de.raindancer.modules.healthbars.service.HealthBarService;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/** A player's hit starts a mob's bar (lava, falls and farms' own damage do not); a death or a quit ends one. */
public final class HealthBarsListener implements Listener {

    private final HealthBarService bars;

    public HealthBarsListener(HealthBarService bars) {
        this.bars = bars;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHurt(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity living && !(living instanceof ArmorStand)
                && byAPlayer(event.getDamager())) {
            bars.hit(living);
        }
    }

    /** The player's own blow, or an arrow, trident or snowball they shot. */
    public static boolean byAPlayer(Entity damager) {
        return damager instanceof Player
                || damager instanceof Projectile shot && shot.getShooter() instanceof Player;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent event) {
        bars.forget(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        bars.forget(event.getPlayer().getUniqueId());
    }
}
