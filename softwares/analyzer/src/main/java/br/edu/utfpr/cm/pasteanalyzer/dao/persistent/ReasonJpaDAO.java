package br.edu.utfpr.cm.pasteanalyzer.dao.persistent;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Reason;

public class ReasonJpaDAO extends GenericJpaDAO<Reason, Long> {
    private static ReasonJpaDAO instance;

    public static synchronized ReasonJpaDAO getInstance() {
        if (instance == null) {
            instance = new ReasonJpaDAO();
        }
        return instance;
    }

    private ReasonJpaDAO() {
        getEntityManager();
    }

    public Reason getById(Long id) {
        return super.getById(Reason.class, id);
    }

    public List<Reason> getAll() {
        return (List<Reason>) super.getAll(Reason.class);
    }

    public Reason getByReason(String reason) throws Exception {
        try {
            Query query = entityManager.createQuery("select p from reason p where reason = :reason", Reason.class);
            query.setParameter("reason", reason);
            return (Reason) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw e;
        }
    }
}