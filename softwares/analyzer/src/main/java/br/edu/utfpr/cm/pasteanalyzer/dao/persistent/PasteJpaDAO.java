package br.edu.utfpr.cm.pasteanalyzer.dao.persistent;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Paste;

public class PasteJpaDAO extends GenericJpaDAO<Paste, Long> {

    private static PasteJpaDAO instance;

    public static synchronized PasteJpaDAO getInstance() {
        if (instance == null) {
            instance = new PasteJpaDAO();
        }
        return instance;
    }

    private PasteJpaDAO() {
        getEntityManager();
    }

    public Paste getById(Long id) {
        return super.getById(Paste.class, id);
    }

    public Paste getByKey(final String key) {
        try {
            Query query = entityManager.createQuery("select p from paste p where key = :key", Paste.class);
            query.setParameter("key", key);
            return (Paste) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Paste> getAll() {
        return (List<Paste>) super.getAll(Paste.class);
    }

    public synchronized Paste save(Paste paste) throws Exception {
        return super.save(paste);
    }

    /**
     * @param key
     *            a paste valid key
     * @return trueif there isa paste with thiskey, but false.
     */
    public boolean containsPasteWithKey(String key) {
        return this.getByKey(key) != null;
    }
}