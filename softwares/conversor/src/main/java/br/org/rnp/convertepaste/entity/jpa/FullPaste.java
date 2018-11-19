package br.org.rnp.convertepaste.entity.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity implementation class for Entity: paste
 *
 */
@Entity(name = "fullpaste")
public class FullPaste implements Serializable {
    private static final long serialVersionUID = 1715751548261343937L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false, unique = true, length = 10)
    private String key;
    @Column(nullable = false)
    private boolean analized;

    public FullPaste() {
        super();
    }

    public FullPaste(String text, String key, boolean analized) {
        super();
        this.text = text;
        this.key = key;
        this.analized = analized;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAnalized() {
        return analized;
    }

    public void setAnalized(boolean analized) {
        this.analized = analized;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (analized ? 1231 : 1237);
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        if (!(obj instanceof FullPaste)) {
            return false;
        }
        FullPaste other = (FullPaste) obj;
        if (analized != other.analized) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id).append(": ").append(id).append(", ");
        sb.append("key: ").append(key).append(", analyzed: ").append(analized).append("\n");
        sb.append(text);
        return sb.toString();
    }
}