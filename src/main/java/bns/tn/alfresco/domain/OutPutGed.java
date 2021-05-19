package bns.tn.alfresco.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class OutPutGed implements Serializable {
//    @JsonManagedReference
    private String urlFile;
	private String nameFile;
	private String idDoc;
	private String pathFolder;
    private String contentType;
    private String titreFile;
    private String descripFile;
    private String typeFile;
    private LocalDate dateModif;

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public String getContentType() { return contentType; }
//
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getTitreFile() {
        return titreFile;
    }

    public void setTitreFile(String titreFile) {
        this.titreFile = titreFile;
    }

    public String getDescripFile() {
        return descripFile;
    }

    public void setDescripFile(String descripFile) {
        this.descripFile = descripFile;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }

    public LocalDate getDateModif() {
        return dateModif;
    }

    public void setDateModif(LocalDate dateModif) {
        this.dateModif = dateModif;
    }

    @Override
    public String toString() {
        return "OutPutGed{" +
            "urlFile='" + urlFile + '\'' +
            ", nameFile='" + nameFile + '\'' +
            ", idDoc='" + idDoc + '\'' +
            ", pathFolder='" + pathFolder + '\'' +
            ", titreFile='" + titreFile + '\'' +
            ", descripFile='" + descripFile + '\'' +
            ", typeFile='" + typeFile + '\'' +
            ", dateModif='" + dateModif + '\'' +

            // ", contentType='" + contentType + '\'' +
            "}";
    }

//    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder("OutPutGed{");
//        sb.append("urlFile='").append(urlFile).append('\'');
//        sb.append(", nameFile='").append(nameFile).append('\'');
//        sb.append(", idDoc='").append(idDoc).append('\'');
//        sb.append(", pathFolder='").append(pathFolder).append('\'');
//        sb.append(", contentType='").append(contentType).append('\'');
//        sb.append('}');
//        return sb.toString();
//    }
}
