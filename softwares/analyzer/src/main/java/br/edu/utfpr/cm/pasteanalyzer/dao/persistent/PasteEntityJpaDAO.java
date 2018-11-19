package br.edu.utfpr.cm.pasteanalyzer.dao.persistent;

import java.util.List;

import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.PasteEntity;

public class PasteEntityJpaDAO extends GenericJpaDAO<PasteEntity, Long> {
    private static PasteEntityJpaDAO instance;

    public static synchronized PasteEntityJpaDAO getInstance() {
        if (instance == null) {
            instance = new PasteEntityJpaDAO();
        }
        return instance;
    }

    private PasteEntityJpaDAO() {
        getEntityManager();
    }

    public PasteEntity getById(Long id) {
        return super.getById(PasteEntity.class, id);
    }

    public List<PasteEntity> getAll() {
        return (List<PasteEntity>) super.getAll(PasteEntity.class);
    }

    public synchronized PasteEntity save(PasteEntity paste) throws Exception {
        return super.save(paste);
    }
}