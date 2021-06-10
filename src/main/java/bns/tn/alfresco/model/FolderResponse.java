package bns.tn.alfresco.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FolderResponse {
	@JsonProperty(value = "cwd")
	private FolderManager folder;
	@JsonProperty(value = "files")
	private List<FolderManager> files;
    @JsonProperty(value = "details")
	private Details details;


    public FolderManager getFolder() {
        return folder;
    }

    public void setFolder(FolderManager folder) {
        this.folder = folder;
    }

    public List<FolderManager> getFiles() {
        return files;
    }

    public void setFiles(List<FolderManager> files) {
        this.files = files;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }
}
