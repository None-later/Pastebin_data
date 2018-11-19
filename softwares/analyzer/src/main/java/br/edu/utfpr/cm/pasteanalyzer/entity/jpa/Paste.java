package br.edu.utfpr.cm.pasteanalyzer.entity.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

/**
 * Entity that represents a paste from www.pastebin.com
 * 
 * @author felipevr
 */
@Entity(name = "paste")
public class Paste implements Serializable {
    private static final long serialVersionUID = 1478536282556538276L;
    @Expose(serialize = false)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Expose
    @Column(nullable = false, unique = true, length = 8)
    private String key;
    private int hits;
    @Expose
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date pasteDate;
    @Expose
    @Column(length = 255)
    private String title;
    @Expose
    @Column(length = 100)
    private String author;
    @Expose
    @Column(length = 30)
    private String format;
    private int visibility;
    @Expose
    @Column(length = 3, nullable = false)
    private String espireDate;
    @Expose
    @Column(nullable = false)
    private String text;
    @Expose(serialize = false)
    @ManyToMany
    @JoinTable(name = "paste_reason", joinColumns = { @JoinColumn(name = "paste_id") }, inverseJoinColumns = {
            @JoinColumn(name = "reason_id") })
    @Column(nullable = true)
    private Set<Reason> reasons;

    // @OneToMany(mappedBy = "paste", targetEntity = PasteEntity.class, fetch =
    // FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose(serialize = false)
    @ManyToMany
    @JoinTable(name = "paste_entity", joinColumns = { @JoinColumn(name = "paste_id") }, inverseJoinColumns = {
            @JoinColumn(name = "entity_id") })
    @Column(nullable = true)
    private Set<PasteEntity> entities;

    private boolean analyzed;
    @Expose
    private boolean relevant;
    @Expose
    private double relevancy;

    // @JsonInclude()
    @Expose
    @Transient
    private List<String> jSonReasons;

    public Paste() {
        super();
        reasons = new TreeSet<Reason>();
        jSonReasons = new ArrayList<String>();
        entities = new TreeSet<PasteEntity>();
    }

    /**
     * @return the id
     */
    public long getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the hits
     */
    public int getHits() {
        return this.hits;
    }

    /**
     * @param hits
     *            the hits to set
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    /**
     * @return the pasteDate
     */
    public Date getPasteDate() {
        return this.pasteDate;
    }

    /**
     * @param pasteDate
     *            the pasteDate to set
     */
    public void setPasteDate(Date pasteDate) {
        this.pasteDate = pasteDate;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the visibility
     */
    public int getVisibility() {
        return this.visibility;
    }

    /**
     * @param visibility
     *            the visibility to set
     */
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the espireDate
     */
    public String getEspireDate() {
        return this.espireDate;
    }

    /**
     * @param espireDate
     *            the espireDate to set
     */
    public void setEspireDate(String espireDate) {
        this.espireDate = espireDate;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the acceptedBy
     */
    public Set<Reason> getReasons() {
        return this.reasons;
    }

    /**
     * @param reasons
     *            the acceptedBy to set
     */
    public void setReasons(Set<Reason> reasons) {
        if (reasons == null) {
            return;
        }
        this.reasons = reasons;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<PasteEntity> getEntities() {
        return entities;
    }

    public void setEntities(Set<PasteEntity> entities) {
        this.entities = entities;
    }

    /**
     * @return the analyzed
     */
    public synchronized boolean isAnalyzed() {
        return this.analyzed;
    }

    /**
     * @param analyzed
     *            the analyzed to set
     */
    public synchronized void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }

    /**
     * @return the relevant
     */
    public boolean isRelevant() {
        return relevant;
    }

    /**
     * @param relevant
     *            the relevant to set
     */
    public void setRelevant(boolean relevant) {
        this.relevant = relevant;
    }

    /**
     * @return the relevancy
     */
    public synchronized double getRelevancy() {
        return this.relevancy;
    }

    /**
     * @param relevancy
     *            the relevancy to set
     */
    public synchronized void setRelevancy(double relevancy) {
        this.relevancy = relevancy;
    }

    public void addRelevancy(Double mod) {
        this.relevancy += mod;
    }

    public List<String> getjSonReasons() {
        return this.jSonReasons;
    }

    public void setjSonReasons(List<String> jSonReasons) {
        this.jSonReasons = jSonReasons;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (analyzed ? 1231 : 1237);
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((entities == null) ? 0 : entities.hashCode());
        result = prime * result + ((espireDate == null) ? 0 : espireDate.hashCode());
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + hits;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((pasteDate == null) ? 0 : pasteDate.hashCode());
        result = prime * result + ((reasons == null) ? 0 : reasons.hashCode());
        long temp;
        temp = Double.doubleToLongBits(relevancy);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + visibility;
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
        if (!(obj instanceof Paste)) {
            return false;
        }
        Paste other = (Paste) obj;
        if (analyzed != other.analyzed) {
            return false;
        }
        if (author == null) {
            if (other.author != null) {
                return false;
            }
        } else if (!author.equals(other.author)) {
            return false;
        }

        if (entities == null) {
            if (other.entities != null) {
                return false;
            }
        } else if (!entities.equals(other.entities)) {
            return false;
        }
        if (espireDate == null) {
            if (other.espireDate != null) {
                return false;
            }
        } else if (!espireDate.equals(other.espireDate)) {
            return false;
        }
        if (format == null) {
            if (other.format != null) {
                return false;
            }
        } else if (!format.equals(other.format)) {
            return false;
        }
        if (hits != other.hits) {
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
        if (pasteDate == null) {
            if (other.pasteDate != null) {
                return false;
            }
        } else if (!pasteDate.equals(other.pasteDate)) {
            return false;
        }
        if (reasons == null) {
            if (other.reasons != null) {
                return false;
            }
        } else if (!reasons.equals(other.reasons)) {
            return false;
        }
        if (Double.doubleToLongBits(relevancy) != Double.doubleToLongBits(other.relevancy)) {
            return false;
        }
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (visibility != other.visibility) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Paste [id=");
        builder.append(this.id);
        builder.append(", key=");
        builder.append(this.key);
        builder.append(", hits=");
        builder.append(this.hits);
        builder.append(", pasteDate=");
        builder.append(this.pasteDate);
        builder.append(", title=");
        builder.append(this.title);
        builder.append(", format=");
        builder.append(this.format);
        builder.append(", visibility=");
        builder.append(this.visibility);
        builder.append(", espireDate=");
        builder.append(this.espireDate);
        builder.append(", text=");
        builder.append(this.text);
        builder.append(", acceptedBy=");
        builder.append(this.reasons);
        builder.append(", getId()=");
        builder.append(this.getId());
        builder.append(", getKey()=");
        builder.append(this.getKey());
        builder.append(", getHits()=");
        builder.append(this.getHits());
        builder.append(", getPasteDate()=");
        builder.append(this.getPasteDate());
        builder.append(", getTitle()=");
        builder.append(this.getTitle());
        builder.append(", getFormat()=");
        builder.append(this.getFormat());
        builder.append(", getVisibility()=");
        builder.append(this.getVisibility());
        builder.append(", getEspireDate()=");
        builder.append(this.getEspireDate());
        builder.append(", getText()=");
        builder.append(this.getText());
        builder.append(", getAcceptedBy()=");
        builder.append(this.getReasons());
        builder.append(", hashCode()=");
        builder.append(this.hashCode());
        builder.append(", getClass()=");
        builder.append(this.getClass());
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }

}