package br.org.rnp.convertepaste.dao.temporary;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Keeps between <code>minimumCacheLength</code> and
 * <code>maximumCacheLength</code> paste's keys. These keys will not be
 * persisted.
 * 
 * @author Felipe
 */
public abstract class PasteKeyTemporarySetDAO {
    private static int maximumCacheLength = 1500;
    private static int minimumCacheLength = 600;
    private Set<String> keys;

    protected PasteKeyTemporarySetDAO() {
        super();
        this.keys = new LinkedHashSet<String>(maximumCacheLength);
    }

    public synchronized boolean add(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Key must not be null or empty");
        }

        boolean added = this.keys.add(s);

        if (added) {
            this.clearOldElements();
        }

        return added;
    }

    private void clearOldElements() {
        if (this.keys.size() > PasteKeyTemporarySetDAO.maximumCacheLength) {
            Set<String> oldKeys = new LinkedHashSet<String>();
            int i = 0;
            Iterator<String> iterator = this.keys.iterator();
            while (iterator.hasNext() && i++ <= PasteKeyTemporarySetDAO.minimumCacheLength) {
                oldKeys.add(iterator.next());
            }
            this.removeAll(oldKeys);
        }
    }

    private synchronized void removeAll(Set<String> oldKeys) {
        this.keys.removeAll(oldKeys);
    }

    public synchronized boolean remove(String s) {
        return this.keys.remove(s);
    }

    public boolean contains(String s) {
        return this.keys.contains(s);
    }

    /**
     * @return the cacheLength
     */
    public static synchronized int getMaximumCacheLength() {
        return maximumCacheLength;
    }

    /**
     * @param cacheLength
     *            the cacheLength to set
     */
    public static synchronized void setMaximumCacheLength(int cacheLength) {
        PasteKeyTemporarySetDAO.maximumCacheLength = cacheLength;
    }

    /**
     * @return the minimumCacheLength
     */
    public static synchronized int getMinimumCacheLength() {
        return minimumCacheLength;
    }

    /**
     * @param minimumCacheLength
     *            the minimumCacheLength to set
     */
    public static synchronized void setMinimumCacheLength(int minimumCacheLength) {
        PasteKeyTemporarySetDAO.minimumCacheLength = minimumCacheLength;
    }
}