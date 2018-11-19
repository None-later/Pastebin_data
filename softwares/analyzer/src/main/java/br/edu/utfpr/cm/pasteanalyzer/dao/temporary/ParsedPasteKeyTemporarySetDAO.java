package br.edu.utfpr.cm.pasteanalyzer.dao.temporary;

public class ParsedPasteKeyTemporarySetDAO extends PasteKeyTemporarySetDAO {

    private static ParsedPasteKeyTemporarySetDAO instance;

    private ParsedPasteKeyTemporarySetDAO() {
        super();
    }

    public static synchronized ParsedPasteKeyTemporarySetDAO getInstance() {
        if (instance == null) {
            instance = new ParsedPasteKeyTemporarySetDAO();
        }
        return instance;
    }
}