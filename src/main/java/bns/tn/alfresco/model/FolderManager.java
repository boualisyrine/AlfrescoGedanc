package bns.tn.alfresco.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;



public class FolderManager {
    private String id;
	private String name;
	private int size;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private Date dateModified;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private Date dateCreated;
	private boolean hasChild;
	private Boolean isFile;
	private String type;
	private String filterPath;
	@JsonProperty(value = "_fm_created")
    private Date fmCreated;
	@JsonProperty("_fm_modified")
	private Date fmModified;
	@JsonProperty("_fm_iconClass")
	private String fmIconClass;
    @JsonProperty("_fm_id")
    private String fmId;
    @JsonProperty("_fm_icon")
    private String fmIcon;

    @JsonProperty("_fm_htmlAttr")
    private Object fmHtmlAttr;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(Boolean file) {
        isFile = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilterPath() {
        return filterPath;
    }

    public void setFilterPath(String filterPath) {
        this.filterPath = filterPath;
    }

    public Boolean getFile() {
        return isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
    }

    public Date getFmCreated() {
        return fmCreated;
    }

    public void setFmCreated(Date fmCreated) {
        this.fmCreated = fmCreated;
    }

    public Date getFmModified() {
        return fmModified;
    }

    public void setFmModified(Date fmModified) {
        this.fmModified = fmModified;
    }

    public String getFmIconClass() {
        return fmIconClass;
    }

    public void setFmIconClass(String fmIconClass) {
        this.fmIconClass = fmIconClass;
    }

    public String getFmId() {
        return fmId;
    }

    public void setFmId(String fmId) {
        this.fmId = fmId;
    }

    public String getFmIcon() {
        return fmIcon;
    }

    public void setFmIcon(String fmIcon) {
        this.fmIcon = fmIcon;
    }

    public Object getFmHtmlAttr() {
        return fmHtmlAttr;
    }

    public void setFmHtmlAttr(Object fmHtmlAttr) {
        this.fmHtmlAttr = fmHtmlAttr;
    }
}
