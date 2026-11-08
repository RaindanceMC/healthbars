package de.raindancer.modules.healthbars.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** At most N hit mobs wear a bar per area, so a farm full of hurt mobs does not fill the world with displays. */
public final class MobBarCap {

    private final Map<UUID, String> areaOf = new HashMap<>();
    private final Map<String, Integer> count = new HashMap<>();

    /** True when the mob already has a bar or may get one now. */
    public synchronized boolean claim(UUID mob, String area, int most) {
        String mine = areaOf.get(mob);
        if (mine != null) {
            if (mine.equals(area)) {
                return true;
            }
            release(mob);
        }
        if (count.getOrDefault(area, 0) >= most) {
            return false;
        }
        areaOf.put(mob, area);
        count.merge(area, 1, Integer::sum);
        return true;
    }

    public synchronized void release(UUID mob) {
        String area = areaOf.remove(mob);
        if (area != null) {
            count.merge(area, -1, Integer::sum);
            count.remove(area, 0);
        }
    }

    public synchronized void clear() {
        areaOf.clear();
        count.clear();
    }

    public synchronized int in(String area) {
        return count.getOrDefault(area, 0);
    }
}
