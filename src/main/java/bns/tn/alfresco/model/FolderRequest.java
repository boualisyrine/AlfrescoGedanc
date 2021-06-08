package bns.tn.alfresco.model;


import java.util.List;

public class FolderRequest {

	private String action;
	private String path;
	private boolean showHiddenItems;

	private List<FolderManager> data;
	private String name;
	private String newName;
	private List<String> names;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
