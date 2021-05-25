package bns.tn.alfresco.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.chemistry.opencmis.client.api.Folder;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    @JsonProperty(value = "value")
    private String id;
    @JsonProperty(value = "text")
    private String name;
    private String path;
    @JsonProperty(value = "children")
    private List<Tree> childrens = new ArrayList<>();


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tree> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Tree> childrens) {
        this.childrens = childrens;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
