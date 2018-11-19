package br.org.rnp.convertepaste.dao.persistent;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import br.org.rnp.convertepaste.util.Log;

public abstract class GenericJpaDAO<T, I extends Serializable> {
    protected EntityManager entityManager;

    protected void getEntityManager() {
        if (this.entityManager == null) {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("pastebinminerator");
            this.entityManager = factory.createEntityManager();
        }
    }

    public synchronized T save(T entity) throws Exception {
        T saved = null;
        try {
            entityManager.getTransaction().begin();
            saved = entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (PersistenceException e) {
            Log.log(e.toString(), this.getClass().getName());
            try {
                entityManager.getTransaction().rollback();
            } catch (Exception e2) {
                Log.log(e2.toString(), this.getClass().getName());
            }
            throw e;
        } catch (Exception e1) {
            Log.log(e1.toString(), this.getClass().getName());
            throw e1;
        }
        return saved;
    }

    public synchronized void remove(T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            Log.log(ex.toString(), this.getClass().getName());
            entityManager.getTransaction().rollback();
        }
    }

    public T getById(Class<T> classe, I pk) {
        try {
            return entityManager.find(classe, pk);
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll(Class<T> classe) {
        return (List<T>) entityManager.createQuery("select o from " + classe.getSimpleName() + " o", classe.getClass())
                .getResultList();
    }
}