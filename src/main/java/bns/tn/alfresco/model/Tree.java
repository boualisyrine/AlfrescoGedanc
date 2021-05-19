package bns.tn.alfresco.model;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private String parent;
    private String name;
    private List<Tree> childrens = new ArrayList<>();

    public void  addChildren(Tree child) {

        childrens.add(child);
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
}
