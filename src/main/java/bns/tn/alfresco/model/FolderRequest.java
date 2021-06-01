package bns.tn.alfresco.model;


import java.util.List;

public class FolderRequest {

	private String action;
	private String path;
	private boolean showHiddenItems;
	private List<FolderManager> data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isShowHiddenItems() {
        return showHiddenItems;
    }

    public void setShowHiddenItems(boolean showHiddenItems) {
        this.showHiddenItems = showHiddenItems;
    }

    public List<FolderManager> getData() {
        return data;
    }

    public void setData(List<FolderManager> data) {
        this.data = data;
    }
}
