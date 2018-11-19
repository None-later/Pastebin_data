package br.org.rnp.convertepaste.entity.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: entity
 *
 */
@Entity(name = "entity")
public class PasteEntity implements Serializable, Comparable<PasteEntity> {
    private static final long serialVersionUID = 1297482978051084505L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;
    @Column(nullable = false)
    private String value;
    private boolean correct;

    // @ManyToOne
    // @JoinColumn(name = "paste")
    // private Paste paste;
    @ManyToOne
    @JoinColumn(name = "reason")
    private Reason reason;

    public PasteEntity() {
        super();
    }

    /**
     * @param paste
     * @param reason
     * @param value
     */
    public PasteEntity(Paste paste, Reason reason, String value) {
        super();
        // this.paste = paste;
        this.reason = reason;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    /**
     * @return the correct
     */
    public boolean isCorrect() {
        return correct;
    }

    /**
     * @param correct
     *            the correct to set
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (correct ? 1231 : 1237);
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PasteEntity)) {
            return false;
        }
        PasteEntity other = (PasteEntity) obj;
        if (correct != other.correct) {
            return false;
        }
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
    public int compareTo(PasteEntity o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
        int compareReason = this.getReason().compareTo(o.getReason());
        if (compareReason != 0) {
            return compareReason;
        }

        return this.getValue().compareTo(o.getValue());
    }
}