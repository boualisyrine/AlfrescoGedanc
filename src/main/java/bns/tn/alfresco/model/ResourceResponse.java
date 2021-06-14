package bns.tn.alfresco.model;

import org.springframework.core.io.Resource;

import java.util.List;

public class ResourceResponse {
    private String attachementName;
    private Resource resource;

    public String getAttachementName() {
        return attachementName;
    }

    public void setAttachementName(String attachementName) {
        this.attachementName = attachementName;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
