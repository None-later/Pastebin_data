package br.edu.utfpr.cm.pasteanalyzer.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.utfpr.cm.pasteanalyzer.dao.persistent.FullPasteJpaDAO;
import br.edu.utfpr.cm.pasteanalyzer.dao.persistent.PasteEntityJpaDAO;
import br.edu.utfpr.cm.pasteanalyzer.dao.persistent.PasteJpaDAO;
import br.edu.utfpr.cm.pasteanalyzer.dao.persistent.ReasonJpaDAO;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.FullPaste;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Paste;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.PasteEntity;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Reason;

public class ViewPaste {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FullPaste fullPaste;
    Scanner input;

    public ViewPaste(FullPaste fullPaste, Scanner input) {
        super();
        this.fullPaste = fullPaste;
        this.input = input;
    }

    public void start() {
        System.out.println("\n\t***\n");
        Paste paste = gson.fromJson(fullPaste.getText(), Paste.class);
        System.out.println("FullPaste id: " + fullPaste.getId() + ", Paste key: " + paste.getKey());
        System.out.println("Paste Text:");
        showText(paste.getText());
        Relevancy relevancy = isRightOrRelevant(paste.getKey(), Relevancy.FALSE);
        boolean r = sumarizeRelevancy(relevancy);
        paste.setRelevant(r);
        System.out.println((paste.isRelevant() ? "Sim" : "Não"));
        persisteReasons(paste.getReasons());
        persisteEntities(paste);
        Paste pPaste = persistePaste(paste);
        if (pPaste != null) {
            FullPasteJpaDAO.getInstance().remove(fullPaste);
        }
    }

    /**
     * @param relevancy
     * @return true of TRUE or TRUETOALL, false otherwise
     */
    private boolean sumarizeRelevancy(Relevancy relevancy) {
        return relevancy == Relevancy.FALSE || relevancy == Relevancy.FALSETOALL ? false : true;
    }

    private boolean appliesToAll(Relevancy relevancy) {
        if (relevancy == Relevancy.FALSETOALL || relevancy == Relevancy.TRUETOALL) {
            return true;
        }
        return false;
    }

    private Paste persistePaste(Paste paste) {
        Paste pPaste = null;
        try {
            pPaste = PasteJpaDAO.getInstance().save(paste);
            System.out.println("Paste  identificado por " + pPaste.getKey() + " salvo com o ID:" + pPaste.getId());
        } catch (Exception e) {
            System.out.println("Erro persistindo paste: " + paste.getKey() + ":\n" + e.toString());
        }
        return pPaste;
    }

    private void persisteEntities(Paste paste) {
        ArrayList<PasteEntity> entities = new ArrayList<PasteEntity>(paste.getEntities());
        entities.sort(null);
        Reason lastReason = new Reason();
        Relevancy relevancy;
        boolean correct = false;
        boolean toAll = false;
        Set<PasteEntity> addEntities = new HashSet<PasteEntity>();
        for (PasteEntity entity : entities) {
            if (!lastReason.equals(entity.getReason())) {
                lastReason = entity.getReason();
                toAll = false;
            }
            System.out.println("\n\t[ " + lastReason.getReason().substring(lastReason.getReason().indexOf("_")+1) + " ]:");
            showText(entity.getValue());
            if (!toAll) {
                relevancy = isRightOrRelevant(entity.getReason().getReason(), Relevancy.FALSE);
                toAll = appliesToAll(relevancy);
                correct = sumarizeRelevancy(relevancy);
            }
            System.out.println(correct ? "sim" : "não");
            entity.setCorrect(correct);
            Reason reason = getReason(entity.getReason());
            if (reason == null) {
                System.out.println("Erro tentando recuperar reason: " + entity.getReason().getReason());
            } else {
                entity.setReason(reason);
                PasteEntity pEntity = saveEntity(entity);
                if (pEntity != null) {
                    addEntities.add(pEntity);
                }
            }
            if (correct) {
                entity.getReason().addCorrects();
            } else {
                entity.getReason().addIncorrects();
            }
            updateReason(reason);
        }
        paste.getEntities().clear();
        paste.getEntities().addAll(addEntities);
    }

    private PasteEntity saveEntity(PasteEntity entity) {
        try {
            return PasteEntityJpaDAO.getInstance().save(entity);
        } catch (Exception e) {
            System.out.println("Erro salvando pasteEntity:\n" + e.toString());
        }
        return null;
    }

    private void updateReason(Reason reason) {
        try {
            ReasonJpaDAO.getInstance().save(reason);
        } catch (Exception e) {
            System.out.println("Erro atualizando reason: " + reason.getReason() + ":\n" + e.toString());
        }
    }

    private void persisteReasons(Set<Reason> reasons) {
        Set<Reason> addReasons = new HashSet<Reason>();
        Iterator<Reason> iterator = reasons.iterator();
        while (iterator.hasNext()) {
            Reason reason = iterator.next();
            Reason pReason = getReason(reason);
            if (pReason != null) {
                addReasons.add(pReason);
            } else {
                System.out.println("Erro carregando/salvando reason: " + reason.getReason());
            }
        }

        reasons.clear();
        reasons.addAll(addReasons);
    }

    /**
     * @param reason
     * @return
     */
    private Reason getReason(Reason reason) {
        try {
            Reason pReason = ReasonJpaDAO.getInstance().getByReason(reason.getReason());
            if (pReason == null) {
                pReason = ReasonJpaDAO.getInstance().save(reason);
            }
            return pReason;
        } catch (Exception e) {
            System.out.println("Erro salvando/carregando Reason: " + reason.getReason() + ":\n" + e.toString());
        }
        return null;
    }

    private Relevancy isRightOrRelevant(String name, Relevancy def) {
        String defs = (def == Relevancy.FALSE ? "n"
                : def == Relevancy.FALSETOALL ? "nt" : def == Relevancy.TRUE ? "s" : "st");
        System.out.println("A entidade identificada por " + name
                + " é relevante ou está correta? [(s]im/[n]ão st/nt - s/n para todos]) [default: " + defs + "]");
        String in = input.nextLine();
        if (in.equals("s")) {
            return Relevancy.TRUE;
        } else if (in.equals("n")) {
            return Relevancy.FALSE;
        } else if (in.equals("st")) {
            return Relevancy.TRUETOALL;
        } else if (in.equals("nt")) {
            return Relevancy.FALSETOALL;
        }
        return def;
    }

    private void showText(String text) {
        String[] lines = text.split("\n");
        int count = 0;
        while (count < lines.length) {
            System.out.println(lines[count]);
            count++;
            if (count % 20 == 0) {
                System.out.println("\n[c]ontinuar, [t]erminar.");
                String in = input.nextLine();
                if (in.contains("t")) {
                    break;
                }
            }
        }
    }
}