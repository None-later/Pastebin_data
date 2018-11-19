package br.edu.utfpr.cm.pasteanalyzer.dao.persistent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.FullPaste;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Paste;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Reason;
import br.edu.utfpr.cm.pasteanalyzer.util.Util;
import br.edu.utfpr.cm.pasteanalyzer.view.FilterType;

public class FullPasteJpaDAO extends GenericJpaDAO<FullPaste, Long> {
    private static FullPasteJpaDAO instance;
    StringBuffer ids;
    StringBuffer addedIds;
    private boolean filtering;

    public static synchronized FullPasteJpaDAO getInstance() {
        if (instance == null) {
            instance = new FullPasteJpaDAO();
        }
        return instance;
    }

    private FullPasteJpaDAO() {
        ids = new StringBuffer();
        addedIds = new StringBuffer();
        getEntityManager();
    }

    /**
     * @return the keys
     */
    public StringBuffer getIds() {
        return ids;
    }

    /**
     * @param ids
     *            the keys to set
     */
    public void setIds(StringBuffer ids) {
        this.ids = ids;
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

    public List<FullPaste> getAmountFiltered(int amount, FilterType type, ArrayList<String> filter) {
        boolean keysIsNotEmpty = ids.length() > 0;
        boolean addedKeysIsNotEmpty = addedIds.length() > 0;
        String queryWhere = (keysIsNotEmpty || addedKeysIsNotEmpty || filtering ? "where " : "");
        String notInKey = (keysIsNotEmpty || addedKeysIsNotEmpty ? "id not in (" : "");
        String keysText = (keysIsNotEmpty ? ids.toString() : "");
        String addedKeysText = (addedKeysIsNotEmpty ? (keysIsNotEmpty ? ", " : "") + addedIds.toString() : "");
        String queryFilteredKeys = notInKey + keysText + addedKeysText + (!notInKey.isEmpty() ? ") " : "");
        String queryContainsReasonText = (!queryFilteredKeys.isEmpty() ? "and " : "")
                + createContainsQuery(type, filter);
        String queryText = "select p from fullpaste p " + queryWhere + queryFilteredKeys + queryContainsReasonText
                + "order by random()";
        System.out.println("#query: " + queryText);
        TypedQuery<FullPaste> query = entityManager.createQuery(queryText, FullPaste.class);
        query.setMaxResults(amount);
        return (List<FullPaste>) query.getResultList();
    }

    private String createContainsQuery(FilterType type, ArrayList<String> filters) {
        String query = "";
        boolean first = true;
        for (String s : filters) {
            if (!first) {
                if (type == FilterType.NONE) {
                    query += "and ";
                } else {
                    query += "or ";
                }
            } else {
                first = false;
            }
            if (type == FilterType.NONE) {
                query += "not ";
            }
            query += "text like '%" + s + "%' ";
        }
        return query;
    }

    public List<FullPaste> getAmount(int amount, FilterType type, ArrayList<String> filter) {
        if (type == FilterType.NO_FILTER) {
            this.filtering = false;
            return getAmount(amount);
        } else if (type == FilterType.ONLY || type == FilterType.NONE || type == FilterType.ANY) {
            this.filtering = true;
            System.out.println("#preparando pastes filtrados...");
            return getFiltered(amount, type, filter);
        }
        return null;
    }

    private List<FullPaste> getAmount(int amount) {
        String queryText = "select p from fullpaste p order by random()";
        TypedQuery<FullPaste> query = entityManager.createQuery(queryText, FullPaste.class);
        query.setMaxResults(amount);
        return (List<FullPaste>) query.getResultList();
    }

    private List<FullPaste> getFiltered(int amount, FilterType type, ArrayList<String> filter) {
        ArrayList<FullPaste> filtered = new ArrayList<FullPaste>();
        List<FullPaste> rawList = null;
        while (filtered.size() < amount) {
            rawList = getAmountFiltered(amount, type, filter);
            if (rawList == null || rawList.isEmpty()) {
                break;
            }
            for (FullPaste fp : rawList) {
                if (isValid(fp, type, filter)) {
                    filtered.add(fp);
                    if (filtered.size() == amount) {
                        break;
                    }
                    long  addId = fp.getId();
                    if (addId==0) {
                        continue;
                    }
                    if (addedIds.length() > 0) {
                        addedIds.append(", ");
                    }
                    addedIds.append("'").append(addId).append("'");
                } else {
                    long id = fp.getId();
                    if (id == 0) {
                        System.out.println("***Error: fullpaste " + fp.getId() + " não possui key válida.");
                        continue;
                    }
                    if (ids.length() > 0) {
                        ids.append(", ");
                    }
                    ids.append("'").append(id).append("'");
                }
            }
            System.out.println("#Pastes filtrados: " + filtered.size());
        }
        return filtered;
    }

    private boolean isValid(FullPaste fullPaste, FilterType type, ArrayList<String> filter) {
        Paste paste = Util.gson.fromJson(fullPaste.getText(), Paste.class);
        Set<Reason> reasons = paste.getReasons();
        if (reasons.isEmpty()) {
            if (type == FilterType.ANY || type == FilterType.ONLY) {
                return false;
            } else if (type == FilterType.NONE) {
                return true;
            }
        }
        Iterator<Reason> iterator = reasons.iterator();
        Reason reason;
        while (iterator.hasNext()) {
            reason = iterator.next();
            boolean filterContains = filter.contains(reason.getReason());
            if ((type == FilterType.ONLY && !filterContains) || (type == FilterType.NONE && filterContains)) {
                return false;
            } else if (type == FilterType.ANY && filterContains) {
                return true;
            }
        }

        if (type == FilterType.ANY) {
            return false;
        } else {
            return true;
        }
    }
}