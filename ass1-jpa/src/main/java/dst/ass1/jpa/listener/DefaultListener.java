package dst.ass1.jpa.listener;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultListener {

    private static AtomicInteger loaded = new AtomicInteger(0);
    private static AtomicInteger updated = new AtomicInteger(0);
    private static AtomicInteger removed = new AtomicInteger(0);
    private static AtomicInteger persisted = new AtomicInteger(0);
    private static AtomicLong persistTime = new AtomicLong(0);

    private static final Map<Integer, Long> persist = new HashMap<>();

    @PrePersist
    void onPrePersist(Object o) {
        persist.put(o.hashCode(), System.currentTimeMillis());
    }
    @PostPersist
    void onPostPersist(Object o ) {
        persisted.incrementAndGet();

        persistTime.addAndGet(System.currentTimeMillis() - persist.remove(o.hashCode()));
    }
    @PostLoad
    void onPostLoad(Object o) {
        loaded.incrementAndGet();
    }
    @PostUpdate
    void onPostUpdate(Object o) {
        updated.incrementAndGet();
    }
    @PostRemove
    void onPostRemove(Object o) {
        removed.incrementAndGet();
    }


    public static int getLoadOperations() {
        return loaded.intValue();
    }

    public static int getUpdateOperations() {
        return updated.intValue();
    }

    public static int getRemoveOperations() {
        return removed.intValue();
    }

    public static int getPersistOperations() {
        return persisted.intValue();
    }

    public static long getOverallTimeToPersist() {
        return persistTime.longValue();
    }

    public static double getAverageTimeToPersist() {
        return (persisted.intValue() == 0) ? 0l : (persistTime.longValue() / persisted.intValue());
    }

    public static synchronized void clear() {
        loaded.set(0);
        updated.set(0);
        removed.set(0);
        persisted.set(0);

        persistTime.set(0);
    }
}
