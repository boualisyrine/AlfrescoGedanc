package bns.tn.alfresco.web.rest;

import bns.tn.alfresco.config.CmisUtilsGed;
import bns.tn.alfresco.domain.OutPutGed;
import bns.tn.alfresco.model.Tree;
import bns.tn.alfresco.web.rest.vm.Champs;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.apache.chemistry.opencmis.client.api.Session;
//import javax.mail.Session;
import java.io.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;



@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf8")

public class AlfrescoRessource {

    private final Logger log = LoggerFactory.getLogger(AlfrescoRessource.class);
    private static Session session;

    @GetMapping("/getfolprinc")
    public List<String> getFilePrinc() {
        List<String> list2 = new ArrayList<>();
        try {
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder file = cmisUtils.connect();
            Folder homes = cmisUtils.getFolderByName(file, "DIGITAL_HOME");
            String name = homes.getName();
            list2.add(name);
            return list2;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/getfoldern1")
    public List<String> getFolder() {
        List<OutPutGed> l = new ArrayList<OutPutGed>();
        try {
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder entrepot = cmisUtils.connect();
            Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");

            List<String> foldersNames = cmisUtils.listFolder(3, home);
            return foldersNames;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/getfilen1")
    public List<OutPutGed> getFile() {
        List<OutPutGed> l = new ArrayList<OutPutGed>();
        List<String> doc = new ArrayList<>();
        try {
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder entrepot = cmisUtils.connect();
            Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
            List<OutPutGed> files = cmisUtils.getListFile(home);

            //String titre = files.get(1).getTitreFile();
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/getfilepath")
    public List<String> getFilePath() {
        List<OutPutGed> l = new ArrayList<OutPutGed>();
        List<String> doc = new ArrayList<>();
        try {
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder entrepot = cmisUtils.connect();
            Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
            List<OutPutGed> files = cmisUtils.getListFile(home);
            files.forEach(fil -> {
                doc.add(fil.getNameFile());
            });
            return doc;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

////////////////////////*//////////////////////////
   @PostMapping("/getFolder")
   public  List<String> getFolder(@RequestBody List<String> paths) {
       CmisUtilsGed cmisUtils = new CmisUtilsGed();
       Folder entrepot = cmisUtils.connect();
       String path = "DIGITAL_HOME";
       for(String st: paths) {
           path += '/' + st;
       }
       List<String> arrayss = new ArrayList<>();
       Folder home = cmisUtils.getFolderByName(entrepot, path);
       if(home !=null) {
           for (Iterator<CmisObject> it = home.getChildren().iterator(); it.hasNext(); ) {
               CmisObject o = it.next();

               arrayss.add(o.getName());
           }
       }
        return arrayss;
   }





    @GetMapping("/folderTree")
    public  Tree getFolder1() {
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder entrepot = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");


        bns.tn.alfresco.model.Tree tree = new Tree();
        tree.setId(home.getId());
        tree.setPath(home.getPath());
        tree.setName("DIGITAL_HOME");

        tree.setChildrens(cmisUtils.getChildrens(home));
       return tree;
    }

    @GetMapping("/foldn2/{mot}")
    public List<String> searchFile(@PathVariable(name = "mot") String mot) {
        log.info("mot   " + mot);

        List<OutPutGed> l = new ArrayList<OutPutGed>();
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder file = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(file, "DIGITAL_HOME");
        Folder homes = cmisUtils.getFolderByName(home, "/" + mot);
        List<String> foldersName = cmisUtils.listFolder(2, homes);
        return foldersName;
    }




///////////////
    @GetMapping("/filen2/{mot}")
    public List<OutPutGed> getFilePath(@PathVariable(name = "mot") String mot) {
        List<OutPutGed> l = new ArrayList<OutPutGed>();
        List<String> doc = new ArrayList<>();
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder file = cmisUtils.connect();

        Folder home = cmisUtils.getFolderByName(file, "DIGITAL_HOME");
        OutPutGed test = cmisUtils.getFileByName(home, "test");
        Folder homes = cmisUtils.getFolderByName(home, "/" + mot);
        List<OutPutGed> files = cmisUtils.getListFile(homes);
        return files;
    }
    @GetMapping("/foldn3/{mot}/{mot2}")
    public List<String> searchFiles(@PathVariable(name = "mot") String mot, @PathVariable(name = "mot2") String mot2) {
        List<OutPutGed> l = new ArrayList<OutPutGed>();
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder file = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(file, "DIGITAL_HOME");
        Folder homes = cmisUtils.getFolderByName(home, "/" + mot);
        Folder home3 = cmisUtils.getFolderByName(homes, "/" + mot2);
        List<String> foldersName = cmisUtils.listFolder(2, home3);
        return foldersName;
    }

    @GetMapping("/filen3/{mot}/{mot2}")
    public List<OutPutGed> getFilePaths(@PathVariable(name = "mot") String mot, @PathVariable(name = "mot2") String mot2) {
        List<OutPutGed> l = new ArrayList<OutPutGed>();
        List<String> doc = new ArrayList<>();
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder file = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(file, "DIGITAL_HOME");
        Folder homes = cmisUtils.getFolderByName(home, "/" + mot);
        Folder home3 = cmisUtils.getFolderByName(homes, "/" + mot2);
        List<OutPutGed> files = cmisUtils.getListFile(home3);
        return files;
    }
    @GetMapping("/searchFol/{mot}")
    public String searchFolder(@PathVariable(name = "mot") String mot) {

        List<OutPutGed> l = new ArrayList<OutPutGed>();
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder file = cmisUtils.connect();

        Folder home = cmisUtils.getFolderByName(file, "DIGITAL_HOME");

        Folder homes = cmisUtils.getFolderByName(home, "/" + mot);
        String name = homes.getPath();

        return homes.getName();

    }


    @PostMapping("/download")
    @ResponseBody
    public HttpEntity<byte[]> downloadPiecesJointes(@RequestBody List<OutPutGed> att) {
        log.info("attachement{} " + att);
        byte[] bytesArray = null;
        try {
            String path = "/" + att.get(0).getPathFolder() + "/" + att.get(0).getNameFile();  // creating file absolute path
            log.info("path is {}", path);
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            cmisUtils.connect();
            Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
            String ch = doc.getContentStreamMimeType();  // get document content strema

//            String ch2 = doc.getPropertyValue("cmis:title");
            att.get(0).setContentType(ch);
            InputStream stream = doc.getContentStream().getStream();  // get document content strema


//            String title = doc.getPropertyValue("cmis:title");
//            String description = doc.getPropertyValue("cmis:description");

            bytesArray = IOUtils.toByteArray(stream);  // converting inputstream to byte
            HttpHeaders header = new HttpHeaders();
            if (bytesArray != null) {
                header.setContentType(MediaType.parseMediaType(att.get(0).getContentType()));
                header.set("Content-Disposition", "inline");
                header.setContentLength(bytesArray.length);
            } else {
                log.info("bytes is null");
            }
//            log.info("bytesArray{} " ,bytesArray);
            return new HttpEntity<byte[]>(bytesArray, header);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @PostMapping("/type")
//    @ResponseBody
    public String type(@RequestBody List<OutPutGed> att) {
        log.info("attachement{} " + att);
        String path = "/" + att.get(0).getPathFolder() + "/" + att.get(0).getNameFile();  // creating file absolute path
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        cmisUtils.connect();
        Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
        String ch = doc.getContentStreamMimeType();  // get document content strema
        att.get(0).setContentType(ch);
        return ch;
    }


    @GetMapping("/recherche/{rech}")
    @ResponseBody
    public List<OutPutGed> rechercheFile(@PathVariable(name = "rech") String rech) {
        List<OutPutGed> rslt = new ArrayList<OutPutGed>();

        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder entrepot = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
        List<OutPutGed> files = cmisUtils.getListFile(home);
        List<String> folders= cmisUtils.listFolder(2, home);

        files.forEach(fil -> {

            fil.setPathFolder("DIGITAL_HOME");

            String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path

            Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
            String titled = doc.getPropertyValue("cm:title");
            String description = doc.getPropertyValue("cmis:description");
            String ch = doc.getContentStreamMimeType();  // get document content strema
            LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
            fil.setTitreFile(titled);
            fil.setDescripFile(description);
            fil.setTypeFile(ch);
            fil.setDateModif(de);

            if(doc.getName().contains(rech)) {
                rslt.add(fil);
            }
        });
        for (String fil : folders) {
            Folder homes = cmisUtils.getFolderByName(home, "/" + fil);
            List<OutPutGed> filesn2 = cmisUtils.getListFile(homes);
            filesn2.forEach(fil2 -> {

                fil2.setPathFolder("DIGITAL_HOME" + "/" + fil);
                String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                String titled = doc.getPropertyValue("cm:title");
                String description = doc.getPropertyValue("cmis:description");
                String ch = doc.getContentStreamMimeType();  // get document content strema
                LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
                fil2.setTitreFile(titled);
                fil2.setDescripFile(description);
                fil2.setTypeFile(ch);
                fil2.setDateModif(de);
                if (doc.getName().contains(rech)) {
                    rslt.add(fil2);
                }
            });
        }
        for (String fil : folders) {
            Folder homes = cmisUtils.getFolderByName(home, "/" + fil);
            List<String> foldersn2= cmisUtils.listFolder(2, homes);
            List<OutPutGed> files2 = cmisUtils.getListFile(homes);
            foldersn2.forEach(fil2 -> {

                Folder homesf = cmisUtils.getFolderByName(homes, "/" + fil2);

                List<OutPutGed> fileed = cmisUtils.getListFile(homesf);

                fileed.forEach(fil3 -> {
                    fil3.setPathFolder("DIGITAL_HOME" + "/" + fil +"/" + fil2);
                    String path = "/" + fil3.getPathFolder() + "/" + fil3.getNameFile();  // creating file absolute path

                    Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                    String titled = doc.getPropertyValue("cm:title");
                    String description = doc.getPropertyValue("cmis:description");
                    String ch = doc.getContentStreamMimeType();  // get document content strema
                    LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
                    fil3.setTitreFile(titled);
                    fil3.setDescripFile(description);
                    fil3.setTypeFile(ch);
                    fil3.setDateModif(de);
                    if (doc.getName().contains(rech)) {
                        rslt.add(fil3);
                    }
                });
            });
        }
        log.info(" new methode " + rslt);
        return rslt;

    }


    @GetMapping("/getfiletype/{type}")
    public List<OutPutGed> getFile(@PathVariable(name = "type") String type) {
        List<OutPutGed> rslt = new ArrayList<OutPutGed>();

            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder entrepot = cmisUtils.connect();
            Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
            List<OutPutGed> files = cmisUtils.getListFile(home);
        List<String> folders= cmisUtils.listFolder(2, home);

            files.forEach(fil -> {
                fil.setPathFolder("DIGITAL_HOME");
                String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                String docs = cmisUtils.readTheContentsOfTheDocument(doc);  // get document  by its path
log.info("docs / " +docs );
                String ch = doc.getContentStreamMimeType();  // get document content strema
                Date de = doc.getLastModificationDate().getTime();

                String ext;
                if ((ch.equals("application/pdf")) || (ch.equals("text/plain")) || (ch.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                    ext = "Texte";
                } else if ((ch.equals("image/jpeg")) || (ch.equals("image/png")) || (ch.equals("image/svg")) || (ch.equals("image/gif")) || (ch.equals("image/jpg"))) {
                    ext = "Image";
                } else {
                    ext = "Vidéo";
                }

                if (type.equals(ext)) {
                    rslt.add(fil);
                }
            });
        for (String fil : folders) {
            Folder homes = cmisUtils.getFolderByName(home,"/"+ fil);
            List<OutPutGed> file2 = cmisUtils.getListFile(homes);
            file2.forEach(fil2->{

                fil2.setPathFolder("DIGITAL_HOME"+"/"+fil);
                String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path

                String ch = doc.getContentStreamMimeType();  // get document content strema
                Date de = doc.getLastModificationDate().getTime();
                String ext;
                if ((ch.equals("application/pdf")) || (ch.equals("text/plain")) || (ch.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                    ext = "Texte";
                } else if ((ch.equals("image/jpeg")) || (ch.equals("image/png")) || (ch.equals("image/svg")) || (ch.equals("image/gif")) || (ch.equals("image/jpg"))) {
                    ext = "Image";
                } else {
                    ext = "Vidéo";
                }

                if (type.equals(ext)) {
                    rslt.add(fil2);
                }
            });
        }
            return rslt;

    }


    @GetMapping("/getfileDate/{deb}/{fin}")
    public List<OutPutGed> getFileDate(@PathVariable(name = "deb") LocalDate deb, @PathVariable(name = "fin") LocalDate fin) {
        List<OutPutGed> rslt = new ArrayList<OutPutGed>();

        try {
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder entrepot = cmisUtils.connect();
            Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
            List<OutPutGed> files = cmisUtils.getListFile(home);
            List<String> folders= cmisUtils.listFolder(2, home);
            files.forEach(fil -> {
                fil.setPathFolder("DIGITAL_HOME");
                String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path
                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();


                if ((de.isAfter(deb))&&(de.isBefore(fin))) {
                    log.info("    y/n     " + ((de.isAfter(deb))&&(de.isBefore(fin))));
                    rslt.add(fil);
                }
            });
            for (String fil : folders) {
                Folder homes = cmisUtils.getFolderByName(home,"/"+ fil);
                List<OutPutGed> file2 = cmisUtils.getListFile(homes);

                file2.forEach(fil2->{
                    fil2.setPathFolder("DIGITAL_HOME"+"/"+fil);
                    String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

                    Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                    LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();


                    if ((de.isAfter(deb))&&(de.isBefore(fin))) {
                        rslt.add(fil2);
                    }
                });
            }
            return rslt;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @GetMapping("/getfiledesc/{desc}")
    public List<OutPutGed> getFileDesc(@PathVariable(name = "desc") String desc) {

            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder entrepot = cmisUtils.connect();
            Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
            List<OutPutGed> files = cmisUtils.getListFile(home);
            List<OutPutGed> rslt = new ArrayList<OutPutGed>();
        List<String> folders= cmisUtils.listFolder(2, home);

            files.forEach(fil -> {

                fil.setPathFolder("DIGITAL_HOME");
                String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                String description = doc.getPropertyValue("cmis:description");
                if(description != null && !description.isEmpty()) {

                fil.setDescripFile(description);

                    if (description.contains(desc)) {
                        rslt.add(fil);
                    }
                }
            });
        for (String fil : folders) {
            Folder homes = cmisUtils.getFolderByName(home,"/"+ fil);
            List<OutPutGed> file2 = cmisUtils.getListFile(homes);

            file2.forEach(fil2->{

                fil2.setPathFolder("DIGITAL_HOME"+"/"+fil);
                String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path


                String description = doc.getPropertyValue("cmis:description");
                if(description != null && !description.isEmpty()) {
                    fil2.setDescripFile(description);

                    if (description.contains(desc)) {
                        rslt.add(fil2);
                    }
                }
            });
        }

            return rslt;

    }


    @GetMapping("/getfiletitle/{title}")
    public List<OutPutGed> getFileTilte(@PathVariable(name = "title") String title) {

        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder entrepot = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
        List<OutPutGed> files = cmisUtils.getListFile(home);
        List<OutPutGed> rslt = new ArrayList<OutPutGed>();
        List<String> folders= cmisUtils.listFolder(2, home);

        files.forEach(fil -> {

            fil.setPathFolder("DIGITAL_HOME");
            String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path

            Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
            String titled = doc.getPropertyValue("cm:title");
            if(titled != null && !titled.isEmpty()) {

                fil.setTitreFile(titled);

                if (titled.contains(title)) {
                    rslt.add(fil);
                }
            }
        });
        for (String fil : folders) {

            Folder homes = cmisUtils.getFolderByName(home,"/"+ fil);

            List<OutPutGed> file2 = cmisUtils.getListFile(homes);

            file2.forEach(fil2->{

                fil2.setPathFolder("DIGITAL_HOME"+"/"+fil);
                String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path


                String titled = doc.getPropertyValue("cm:title");
                if(titled != null && !titled.isEmpty()) {
                    fil2.setTitreFile(titled);

                    if (titled.contains(title)) {
                        rslt.add(fil2);
                    }
                }
            });
        }

        return rslt;

    }


    @GetMapping("/getfilemot/{mot}")
    public List<OutPutGed> getFileMot(@PathVariable(name = "mot") String mot) {
        List<OutPutGed> rslt = new ArrayList<OutPutGed>();

        try {
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            Folder entrepot = cmisUtils.connect();
            Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
            List<OutPutGed> files = cmisUtils.getListFile(home);
            List<String> folders= cmisUtils.listFolder(2, home);
            files.forEach(fil -> {
                fil.setPathFolder("DIGITAL_HOME");
                String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                String content= cmisUtils.readTheContentsOfTheDocument(doc);

                log.info("------ " + (content.contains(mot)));
                if (content.contains(mot)) {
                    rslt.add(fil);
                }
            });
            for (String fil : folders) {
                Folder homes = cmisUtils.getFolderByName(home,"/"+ fil);
                List<OutPutGed> file2 = cmisUtils.getListFile(homes);
                file2.forEach(fil2->{

                    fil2.setPathFolder("DIGITAL_HOME"+"/"+fil);
                    String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

                    Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                    String content= cmisUtils.readTheContentsOfTheDocument(doc);
                    log.info("------ " + (content.contains(mot)));
                    if (content.contains(mot)) {
                        rslt.add(fil2);
                    }
                });
            }
            log.info("////**///"+rslt );
            return rslt;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

//    @GetMapping("/recherchen1/{M0}/{M}/{M1}/{M2}/{M3}/{M4}/{M5}")
////    @ResponseBody
////    public List<OutPutGed> rechercheAvancen1(@PathVariable(name = "M0" , required = false) String M0,
////                                           @PathVariable(name = "M" , required = false) String M,
////                                           @PathVariable(name = "M1" , required = false) String M1,
////                                           @PathVariable(name = "M2" , required = false) String M2,
////                                           @PathVariable(name = "M3" , required = false) String M3,
////                                           @PathVariable(name = "M4" , required = false) LocalDate M4,
////                                           @PathVariable(name = "M5" , required = false) LocalDate M5) {
@PostMapping("/recherchefin")
@ResponseBody
public List<OutPutGed> rechercheAvancen1(@RequestBody Champs champ) {
    log.info("mot  "+ champ.toString());
    log.info("M0 "+ champ.getM0());
    log.info("M "+ champ.getM());
    log.info("M1 "+ champ.getM1());
    log.info("M2 "+ champ.getM2());
    log.info("M3 "+ champ.getM3());
    log.info("M4 "+ champ.getM4());
    log.info("M5 "+ champ.getM5());

    List<OutPutGed> rslt = new ArrayList<OutPutGed>();
        List<OutPutGed> rsltc = new ArrayList<OutPutGed>();
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder entrepot = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
        List<String> folders= cmisUtils.listFolder(2, home);
        List<OutPutGed> files = cmisUtils.getListFile(home);

    log.info("" + files);
    for (OutPutGed fil : files) {
        fil.setPathFolder("DIGITAL_HOME");

        String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path

        Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
        String titled = doc.getPropertyValue("cm:title");
        String description = doc.getPropertyValue("cmis:description");
        String ch = doc.getContentStreamMimeType();  // get document content strema
        LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
        String content = cmisUtils.readTheContentsOfTheDocument(doc);

        String ext;
        if ((ch.equals("application/pdf")) || (ch.equals("text/plain")) || (ch.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            ext = "Texte";
        } else if ((ch.equals("image/jpeg")) || (ch.equals("image/png")) || (ch.equals("image/svg")) || (ch.equals("image/gif")) || (ch.equals("image/jpg"))) {
            ext = "Image";
        } else {
            ext = "Vidéo";
        }
        fil.setTitreFile(titled);
        fil.setDescripFile(description);
        fil.setTypeFile(ch);
        fil.setDateModif(de);

        if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null) && (champ.getM() != null) && (doc.getName() != null) && (doc.getName().contains(champ.getM())) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
            rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM() == null) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
            rslt.add(fil);
        } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (champ.getM4() == null) && (champ.getM5() == null)) {
            rslt.add(fil);
       } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3()!=null)&& (champ.getM3() == null) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
        rslt.add(fil);
       } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
        rslt.add(fil);
       } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
       rslt.add(fil);
       } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
        rslt.add(fil);
        } else if (champ.getM0() == null && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
       rslt.add(fil);
        } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
       rslt.add(fil);
        }
    }
    for (String fil : folders) {
        Folder homes = cmisUtils.getFolderByName(home, "/" + fil);
        List<OutPutGed> filesn2 = cmisUtils.getListFile(homes);
        filesn2.forEach(fil2 -> {

            fil2.setPathFolder("DIGITAL_HOME" + "/" + fil);
            String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

            Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
            String titled = doc.getPropertyValue("cm:title");
            String description = doc.getPropertyValue("cmis:description");
            String ch = doc.getContentStreamMimeType();  // get document content strema
            LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
            String content = cmisUtils.readTheContentsOfTheDocument(doc);

            String ext;
            if ((ch.equals("application/pdf")) || (ch.equals("text/plain")) || (ch.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                ext = "Texte";
            } else if ((ch.equals("image/jpeg")) || (ch.equals("image/png")) || (ch.equals("image/svg")) || (ch.equals("image/gif")) || (ch.equals("image/jpg"))) {
                ext = "Image";
            } else {
                ext = "Vidéo";
            }
            fil2.setTitreFile(titled);
            fil2.setDescripFile(description);
            fil2.setTypeFile(ch);
            fil2.setDateModif(de);
            if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null) && (champ.getM() != null) && (doc.getName() != null) && (doc.getName().contains(champ.getM())) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM() == null) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                rslt.add(fil2);
            } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3()!=null)&& (champ.getM3() == null) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if (champ.getM0() == null && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                rslt.add(fil2);
            }
        });
    }
    for (String fil : folders) {
        Folder homes = cmisUtils.getFolderByName(home, "/" + fil);
        List<String> foldersn2= cmisUtils.listFolder(2, homes);
        List<OutPutGed> files2 = cmisUtils.getListFile(homes);
        foldersn2.forEach(fil2 -> {

            Folder homesf = cmisUtils.getFolderByName(homes, "/" + fil2);

            List<OutPutGed> fileed = cmisUtils.getListFile(homesf);

            fileed.forEach(fil3 -> {
                fil3.setPathFolder("DIGITAL_HOME" + "/" + fil +"/" + fil2);
                String path = "/" + fil3.getPathFolder() + "/" + fil3.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                String titled = doc.getPropertyValue("cm:title");
                String description = doc.getPropertyValue("cmis:description");
                String ch = doc.getContentStreamMimeType();  // get document content strema
                LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
                String content = cmisUtils.readTheContentsOfTheDocument(doc);

                String ext;
                if ((ch.equals("application/pdf")) || (ch.equals("text/plain")) || (ch.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                    ext = "Texte";
                } else if ((ch.equals("image/jpeg")) || (ch.equals("image/png")) || (ch.equals("image/svg")) || (ch.equals("image/gif")) || (ch.equals("image/jpg"))) {
                    ext = "Image";
                } else {
                    ext = "Vidéo";
                }
                fil3.setTitreFile(titled);
                fil3.setDescripFile(description);
                fil3.setTypeFile(ch);
                fil3.setDateModif(de);
                if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null) && (champ.getM() != null) && (doc.getName() != null) && (doc.getName().contains(champ.getM())) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() == null) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2() == null) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() != null) && ((titled != null) && (titled.contains(champ.getM1()))) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0() != null) && ((content != null) && (content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1() == null) && (champ.getM2() != null) && (description != null) && (description.contains(champ.getM2())) && (champ.getM3() != null) && ((champ.getM3().equals(ext)) && (ext != null)) && (champ.getM4() != null) && (champ.getM5() != null) && (((de.isAfter(champ.getM4())) && (de.isBefore(champ.getM5()))) && (de != null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3() == null) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() != null) && ((doc.getName() != null) && (doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM() == null) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (champ.getM4() == null) && (champ.getM5() == null)) {
                    rslt.add(fil3);
                } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2() == null) && (champ.getM3()!=null)&& (champ.getM3() == null) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1() == null) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM() == null) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if (champ.getM0() == null && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                } else if ((champ.getM0()!=null) && ((content!=null)&&(content.contains(champ.getM0()))) && (champ.getM()!= null) && ((doc.getName() !=null) &&(doc.getName().contains(champ.getM()))) && (champ.getM1()!=null)&& ((titled!=null)&&(titled.contains(champ.getM1()))) && (champ.getM2()!=null)&& (description!=null)&& (description.contains(champ.getM2())) && (champ.getM3()!=null)&& ((champ.getM3().equals(ext))&&(ext != null)) && (champ.getM4()!=null)&&(champ.getM5()!=null) && (((de.isAfter(champ.getM4()))&&(de.isBefore(champ.getM5())))&&(de!=null))) {
                    rslt.add(fil3);
                }
            });
        });
    }

        log.info("fin --------" + rslt);
        return rslt;
    }

    @GetMapping("/recherche")
    @ResponseBody
    public List<OutPutGed> rechercheGeneral() {
        List<OutPutGed> rslt = new ArrayList<OutPutGed>();
        List<OutPutGed> rsltfin = new ArrayList<OutPutGed>();
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Folder entrepot = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME");
        List<String> folders= cmisUtils.listFolder(2, home);
        List<OutPutGed> files = cmisUtils.getListFile(home);

        files.forEach(fil -> {

            fil.setPathFolder("DIGITAL_HOME");

            String path = "/" + fil.getPathFolder() + "/" + fil.getNameFile();  // creating file absolute path

            Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
            String titled = doc.getPropertyValue("cm:title");
            String description = doc.getPropertyValue("cmis:description");
            String ch = doc.getContentStreamMimeType();  // get document content strema
            LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
            String ext;
            if ((ch.equals("application/pdf")) || (ch.equals("text/plain")) || (ch.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                ext = "Texte";
            } else if ((ch.equals("image/jpeg")) || (ch.equals("image/png")) || (ch.equals("image/svg")) || (ch.equals("image/gif")) || (ch.equals("image/jpg"))) {
                ext = "Image";
            } else {
                ext = "Vidéo";
            }
            fil.setTitreFile(titled);
            fil.setDescripFile(description);
            fil.setTypeFile(ch);
            fil.setDateModif(de);
            rslt.add(fil);
        });
        for (String fil : folders) {
            Folder homes = cmisUtils.getFolderByName(home, "/" + fil);
            List<OutPutGed> files2 = cmisUtils.getListFile(homes);
            files2.forEach(fil2 -> {

                fil2.setPathFolder("DIGITAL_HOME" + "/" + fil);
                String path = "/" + fil2.getPathFolder() + "/" + fil2.getNameFile();  // creating file absolute path

                Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                String titled = doc.getPropertyValue("cm:title");
                String description = doc.getPropertyValue("cmis:description");
                String ch = doc.getContentStreamMimeType();  // get document content strema
                LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
                String ext;
                if ((ch.equals("application/pdf")) || (ch.equals("text/plain")) || (ch.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                    ext = "Texte";
                } else if ((ch.equals("image/jpeg")) || (ch.equals("image/png")) || (ch.equals("image/svg")) || (ch.equals("image/gif")) || (ch.equals("image/jpg"))) {
                    ext = "Image";
                } else {
                    ext = "Vidéo";
                }
                fil2.setTitreFile(titled);
                fil2.setDescripFile(description);
                fil2.setTypeFile(ch);
                fil2.setDateModif(de);
                rslt.add(fil2);
            });
        }
        for (String fil : folders) {
            Folder homes = cmisUtils.getFolderByName(home, "/" + fil);
            List<String> foldersn2= cmisUtils.listFolder(2, homes);
            foldersn2.forEach(fil2 -> {

                Folder homesf = cmisUtils.getFolderByName(homes, "/" + fil2);

                List<OutPutGed> fileed = cmisUtils.getListFile(homesf);

                fileed.forEach(fil3 -> {
                    fil3.setPathFolder("DIGITAL_HOME" + "/" + fil +"/" + fil2);
                    String path = "/" + fil3.getPathFolder() + "/" + fil3.getNameFile();  // creating file absolute path

                    Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
                    String titled = doc.getPropertyValue("cm:title");
                    String description = doc.getPropertyValue("cmis:description");
                    String ch = doc.getContentStreamMimeType();  // get document content strema
                    LocalDate de = doc.getLastModificationDate().toZonedDateTime().toLocalDate();
                    fil3.setTitreFile(titled);
                    fil3.setDescripFile(description);
                    fil3.setTypeFile(ch);
                    fil3.setDateModif(de);
                    rslt.add(fil3);
                });
            });
        }
        log.info(" new methode " + rsltfin);
        return rslt;

    }

    @GetMapping("/getmot/{term}")
    public void getMot(@PathVariable(name = "term") String term) {
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        Session session = cmisUtils.getCmisSession();
        OperationContext ctxt = session.getDefaultContext();
        List<QueryResult> list = new ArrayList<QueryResult>();
        // Build WHERE clause(cmis:document)
        MessageFormat docFormat = new MessageFormat("cmis:name LIKE ''%{0}%'' OR cmis:description LIKE ''%{0}%'' OR CONTAINS(''{0}'')");
        String docStatement = "";
        if (StringUtils.isNotBlank(term)) {
            docStatement = docFormat.format(new String[] { term });
        }
        ItemIterable<QueryResult> docResults = session.query("SELECT * FROM cmis:document",false);
//    ItemIterable<CmisObject> queryObjects("SELECT * FROMcmis:document");
        for (QueryResult item : docResults) {
            System.out.println("property cmis:createdBy on test.txt is "
                + item.getPropertyByQueryName("cmis:createdBy").getFirstValue());
        }
        Iterator<QueryResult> docItr = docResults.iterator();
        while (docItr.hasNext()) {


            QueryResult doc = docItr.next();
            log.info("doc doc" + doc);

            log.info("doc" +doc.getPropertyById("cmis:name"));
            boolean val = doc.getPropertyValueById("cmis:isLatestVersion");
            if (!val)
                continue;
            list.add(doc);
        }
        log.info("Mot clé" + list);

    }
    @PostMapping("/typem2")
//    @ResponseBody
    public String typem2(@RequestBody List<OutPutGed> att) {
        log.info("attachement{} " + att);
      String path = "/" + att.get(0).getPathFolder()+ "/"+ att.get(0).getNameFile();;  // creating file absolute path
        log.info(".......path......" + path);
        CmisUtilsGed cmisUtils = new CmisUtilsGed();
        cmisUtils.connect();
        Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
        log.info("bbbbbbbbb" + doc);
        String ch = doc.getContentStreamMimeType();  // get document content strema
        att.get(0).setContentType(ch);
        return ch;
    }

    @PostMapping("/downloadm2")
    @ResponseBody
    public HttpEntity<byte[]> download(@RequestBody List<OutPutGed> att) {
        log.info("attachement{} " + att);
        byte[] bytesArray = null;
        try {
            String path = "/" + att.get(0).getPathFolder()+ "/"+ att.get(0).getNameFile();  // creating file absolute path
            CmisUtilsGed cmisUtils = new CmisUtilsGed();
            cmisUtils.connect();
            Document doc = cmisUtils.getDocumentByPath(path);  // get document  by its path
            log.info("path is {}", path);
            String ch = doc.getContentStreamMimeType();  // get document content strema
            att.get(0).setContentType(ch);
            InputStream stream = doc.getContentStream().getStream();  // get document content strema

            bytesArray = IOUtils.toByteArray(stream);  // converting inputstream to byte
            HttpHeaders header = new HttpHeaders();
            if (bytesArray != null) {
                header.setContentType(MediaType.parseMediaType(att.get(0).getContentType()));
                header.set("Content-Disposition", "inline");
                header.setContentLength(bytesArray.length);
            } else {
                log.info("bytes is null");
            }
//            log.info("bytesArray{} " ,bytesArray);
            return new HttpEntity<byte[]>(bytesArray, header);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
