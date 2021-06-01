package bns.tn.alfresco.web.rest;

import bns.tn.alfresco.config.CmisUtilsGed;
import bns.tn.alfresco.model.FolderManager;
import bns.tn.alfresco.model.FolderRequest;
import bns.tn.alfresco.model.FolderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/fileManager")
@CrossOrigin("*")
public class FileManagerController {

    @Autowired
    private CmisUtilsGed cmisUtilsGed;
    @PostMapping
    public FolderResponse  folderManager(@RequestBody FolderRequest folderRequest) {

        return cmisUtilsGed.getAllFolder(folderRequest.getPath());
    }
}
