package br.org.rnp.convertepaste.dao.persistent;

import java.util.List;

import br.org.rnp.convertepaste.entity.jpa.FullPaste;

public class FullPasteJpaDAO extends GenericJpaDAO<FullPaste, Long> {

    private static FullPasteJpaDAO instance;

    public static synchronized FullPasteJpaDAO getInstance() {
        if (instance == null) {
            instance = new FullPasteJpaDAO();
        }
        return instance;
    }

    private FullPasteJpaDAO() {
        getEntityManager();
    }

    public FullPaste getById(Long id) {
        return super.getById(FullPaste.class, id);
    }

    public List<FullPaste> getAll() {
        return (List<FullPaste>) super.getAll(FullPaste.class);
    }

    public synchronized FullPaste save(FullPaste FullPaste) throws Exception {
        return super.save(FullPaste);
    }
}