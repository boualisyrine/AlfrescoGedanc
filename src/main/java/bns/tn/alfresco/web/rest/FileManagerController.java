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
    public FolderResponse folderManager(@RequestBody FolderRequest folderRequest) {

        if (folderRequest.getAction().equals("read")) {
            return cmisUtilsGed.getAllFolder(folderRequest.getPath());
        } else if (folderRequest.getAction().equals("create")) {
            return cmisUtilsGed.createFolder(folderRequest.getPath(), folderRequest.getName());

        } else if (folderRequest.getAction().equals("delete")) {
            return cmisUtilsGed.deleteFolderOrFile(folderRequest.getPath(), folderRequest.getNames());
        } else if (folderRequest.getAction().equals("rename")) {
            return cmisUtilsGed.renameFileOrFolder(folderRequest.getPath(), folderRequest.getName(),
                folderRequest.getNewName());
        } else if (folderRequest.getAction().equals("search")) {

            return cmisUtilsGed.search(folderRequest.getPath(), folderRequest.getSearchString());
        } else if (folderRequest.getAction().equals("details")) {
            return cmisUtilsGed.detailFolderOrFile(folderRequest.getPath(), folderRequest.getNames());
        } else if (folderRequest.getAction().equals("upload")) {

        } else if (folderRequest.getAction().equals("download")) {

        }


        return null;
    }
}
