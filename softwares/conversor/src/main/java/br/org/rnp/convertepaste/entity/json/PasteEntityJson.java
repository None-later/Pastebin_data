package br.org.rnp.convertepaste.entity.json;

import java.io.Serializable;

public class PasteEntityJson implements Serializable, Comparable<PasteEntityJson> {
    private static final long serialVersionUID = 1297482978051084505L;
    private long id;
    private String value;
    private ReasonJson reason;

    public PasteEntityJson() {
        super();
    }

    /**
     * @param paste
     * @param reason
     * @param value
     */
    public PasteEntityJson(PasteJson paste, ReasonJson reason, String value) {
        super();
        // this.paste = paste;
        this.reason = reason;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /*
     * public Paste getPaste() { return paste; }
     * 
     * public void setPaste(Paste paste) { this.paste = paste; }
     */

    /*
     * public Reason getReason() { return reason; }
     * 
     * public void setReason(Reason reason) { this.reason = reason; }
     */

    public ReasonJson getReason() {
        return reason;
    }

    public void setReason(ReasonJson reason) {
        this.reason = reason;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PasteEntityJson)) {
            return false;
        }
        PasteEntityJson other = (PasteEntityJson) obj;
        if (reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!reason.equals(other.reason)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(": ");
        sb.append(value);
        return sb.toString();
    }

    @Override
    public int compareTo(PasteEntityJson o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
        /*
         * int compareReason = this.getReason().compareTo(o.getReason()); if
         * (compareReason != 0) { return compareReason; } int comparePaste =
         * this.getPaste().getKey().compareTo(o.getPaste().getKey()); if (comparePaste
         * != 0) { return comparePaste; }
         */
        return this.getValue().compareTo(o.getValue());
    }
}