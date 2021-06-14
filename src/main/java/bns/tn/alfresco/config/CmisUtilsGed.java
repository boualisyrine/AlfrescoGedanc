package bns.tn.alfresco.config;

import bns.tn.alfresco.model.*;
import bns.tn.alfresco.model.Tree;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import bns.tn.alfresco.domain.OutPutGed;
import jdk.internal.org.objectweb.asm.ClassWriter;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Configuration
@Component
public class CmisUtilsGed {

    /**
     * The Constant LOGGER.
     */
    private static final Logger log = LoggerFactory.getLogger(CmisUtilsGed.class);


    @Autowired
    private Environment env;

    private static Session session;


    private static String ALFRSCO_SERVER_URL;
    private static String ALFRSCO_ATOMPUB_URL;
    private static String ALFRSCO_USER;
    private static String ALFRSCO_PASSWORD;
    private static String ALFRSCO_PDF_URL;
    private static String ALFRSCO_HOME_FOLDER;


    private static String AUTH;
    private static String COOKIES;
    private static String FAC_CLASS;

    @PostConstruct
    public void init() {

        ALFRSCO_SERVER_URL = env.getProperty("ged.repository.urlserver");
        ALFRSCO_ATOMPUB_URL = env.getProperty("ged.repository.urlRepo");
        ALFRSCO_USER = env.getProperty("ged.repository.username");
        ALFRSCO_PDF_URL = env.getProperty("ged.repository.urlpdf");
        ALFRSCO_PASSWORD = env.getProperty("ged.repository.pwd");
        ALFRSCO_HOME_FOLDER = env.getProperty("ged.repository.home_folder");
        AUTH = env.getProperty("ged.repository.auth");
        COOKIES = env.getProperty("ged.repository.cookies");
        FAC_CLASS = env.getProperty("ged.repository.fac_class");

    }

    public FolderResponse getAllFolder(String path) {

        path = "DIGITAL_HOME" + path;

        FolderResponse folderResponse = new FolderResponse();
        Folder entrepot = connect();
        Folder home = getFolderByName(entrepot, path);
        FolderManager folderManager = new FolderManager();
        folderManager.setName(home.getName());
        folderManager.setFilterPath("\\" + home.getName() + "\\");
        folderManager.setIsFile(false);
        if (home.getChildren().iterator().hasNext()) {
            folderManager.setHasChild(true);
        }
        folderResponse.setFolder(folderManager);

        folderResponse.setFiles(getChildrens(home));
        return folderResponse;
    }

    public List<FolderManager> getChildrens(Folder home) {

        List<FolderManager> childrens = new ArrayList<>();
        for (Iterator<CmisObject> it = home.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();


            if (BaseTypeId.CMIS_FOLDER.equals(o.getBaseTypeId())) {
                FolderManager child = new FolderManager();


                Folder folder = ((Folder) o);
                //  child.setId(folder.getId());
                if (folder.getChildren().iterator().hasNext()) {
                    child.setHasChild(true);
                }
                child.setName(folder.getName());
                child.setIsFile(false);
                child.setDateCreated(folder.getCreationDate().getTime());
                child.setDateModified(folder.getLastModificationDate().getTime());
                child.setFilterPath("\\" + folder.getName() + "\\");

                childrens.add(child);
            }

            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                FolderManager child = new FolderManager();


                Document doc = (Document) o;
                child.setName(doc.getName());
                //  child.setId(folder.getId());
                child.setHasChild(false);
                child.setIsFile(true);
                child.setDateCreated(doc.getCreationDate().getTime());
                child.setDateModified(doc.getLastModificationDate().getTime());
                child.setFilterPath("\\" + doc.getName() + "\\");
                child.setType(getExtension(doc.getName()).orElse("txt"));
                childrens.add(child);

            }
        }
        return childrens;
    }

    public FolderResponse createFolder(String path, String name) {

        Folder entrepot = connect();
        Folder home = getFolderByName(entrepot, "DIGITAL_HOME" + path);
        Folder newFolder = getFolderByName(home, name);
        if (newFolder == null) {
            Map<String, String> props = new HashMap<String, String>();
            props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            props.put(PropertyIds.NAME, name);
            home.createFolder(props);
        }
        return getAllFolder(path);
    }

    public FolderResponse renameFileOrFolder(String path, String name, String newName) {

        Folder entrepot = connect();
        Folder home = getFolderByName(entrepot, "DIGITAL_HOME" + path);
        Folder newFolder = getFolderByName(home, "/" + name);

        if (newFolder != null) {
            Map<String, String> props = new HashMap<String, String>();
            props.put(PropertyIds.NAME, newName);
            newFolder.updateProperties(props);
        } else {
            Document document = getDocumentByPath("/DIGITAL_HOME" + path + name);
            Map<String, String> props = new HashMap<String, String>();
            props.put(PropertyIds.NAME, newName);
            document.updateProperties(props);
        }
        return getAllFolder(path);
    }


    public FolderResponse search(String path, String searchKey) {


        FolderResponse folderResponse = getAllFolder(path);
        System.out.println(searchKey);
        String valueKey = searchKey.replace("*", "");
        System.out.println(valueKey);
        List<FolderManager> filtredList = folderResponse.getFiles().stream().filter(f -> f.getName().contains(valueKey)).collect(Collectors.toList());
        filtredList.forEach(f -> System.out.println(f.getName()));
        folderResponse.setFiles(filtredList);
        return folderResponse;
    }


    public FolderResponse deleteFolderOrFile(String path, List<String> names) {

        Folder entrepot = connect();
        Folder home = getFolderByName(entrepot, "DIGITAL_HOME" + path);
        for (String name : names) {

            Folder newFolder = getFolderByName(home, "/" + name);
            if (newFolder != null) {

                newFolder.deleteTree(true, UnfileObject.DELETE, true);
            } else {
                Document document = getDocumentByPath("/DIGITAL_HOME" + path + name);
                document.delete(true);

            }
        }
        return getAllFolder(path);
    }


    public FolderResponse detailFolderOrFile(String path, List<String> names) {

        Details details = new Details();
        if (names.size() > 1) {
            details.setMultipleFiles(true);
        }

        Folder entrepot = connect();

        Folder home = getFolderByName(entrepot, "DIGITAL_HOME" + path);
        String nameFolder = "";
        for (String name : names) {
            nameFolder += name + ", ";
            if (names.size() == 1) {
                details.setLocation("/DIGITAL_HOME" + path + name);
            }
            Folder newFolder = getFolderByName(home, "/" + name);
            if (newFolder != null) {
                details.setCreated(newFolder.getCreationDate().getTime());
                details.setModified(newFolder.getLastModificationDate().getTime());

            } else {
                Document document = getDocumentByPath("/DIGITAL_HOME" + path + name);
                details.setCreated(document.getCreationDate().getTime());
                details.setModified(document.getLastModificationDate().getTime());
                if (names.size() == 1) {
                    details.setFile(true);
                }

            }
        }
        if (names.size() == 0) {
            details.setName(path.replace("/", ""));
        } else {

            details.setName(nameFolder.substring(0, nameFolder.length() - 2));
        }

        if (names.size() != 1) {
            details.setLocation("/DIGITAL_HOME" + path);
        }
        FolderResponse folderResponse = new FolderResponse();
        folderResponse.setDetails(details);
        return folderResponse;
    }


    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".")));
    }

    public ResourceResponse download(String folderRequest) {

        ResourceResponse resourceResponse = new ResourceResponse();
        try {
            ObjectMapper mapper = new ObjectMapper();
            FolderRequest request =   mapper.readValue(folderRequest, FolderRequest.class);
            Folder entrepot = connect();

            Folder home = getFolderByName(entrepot, "DIGITAL_HOME" + request.getPath());

            List<File> files = new ArrayList<>();

            for (String name : request.getNames()) {
                Resource resource = null;

                Folder newFolder = getFolderByName(home, "/" + name);

                if (newFolder != null) {

                } else {
                    Document document = getDocumentByPath("/DIGITAL_HOME" + request.getPath() + name);
                    ContentStream contentStream = document.getContentStream(null);
                /*    resource = new InputStreamResource(contentStream.getStream());*/


                    File tempFile = File.createTempFile(document.getName(),
                        getExtension(document.getName()).orElse(".txt"));
                    Files.copy(contentStream.getStream(), Paths.get(tempFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
                    files.add(tempFile);
                }

            }
            if(request.getNames().size()>1) {
                resourceResponse.setAttachementName(home.getName() +".zip");

               FileOutputStream fos = new FileOutputStream(resourceResponse.getAttachementName());

                byte[] buffer = new byte[1024];
                ZipOutputStream zos = new ZipOutputStream(fos);
                for (File file : files) {

                    FileInputStream fis = new FileInputStream(file);
                    String [] splitted = file.getName().split("\\.");

                    String fileName = splitted[0]+"."+ splitted[2];
                    zos.putNextEntry(new ZipEntry(fileName));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();

                    // close the InputStream
                    fis.close();
                }
                zos.close();


                resourceResponse.setResource(new FileSystemResource(resourceResponse.getAttachementName()));

            } else {
                resourceResponse.setAttachementName(request.getNames().get(0));
                resourceResponse.setResource(new FileSystemResource(files.get(0)));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }




        return resourceResponse;
    }

    /**
     * Connect to alfresco repository
     *
     * @return root folder object
     */

/////////////////////////////////////////////////////////////////********************************************//////////////
    public String getTicket() {

        RestTemplate restTemplate = new RestTemplate();

        String url = ALFRSCO_SERVER_URL + "/alfresco/service/api/login?u=" + ALFRSCO_USER + "&pw=" + ALFRSCO_PASSWORD;

        String consumeJSONString = restTemplate.getForObject(url, String.class);

        int debut = consumeJSONString.indexOf("<ticket>") + 8;
        int fin = consumeJSONString.indexOf("</ticket>");
        String ticket = consumeJSONString.substring(debut, fin);

        return "?alf_ticket=" + ticket;
    }

    public Folder connect() {


        try {

            SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

            Map<String, String> parameters = new HashMap<String, String>();

            log.debug("--------------setting user Session Params--------------");
            // User credentials.

            parameters.put(SessionParameter.USER, ALFRSCO_USER);
            parameters.put(SessionParameter.PASSWORD, ALFRSCO_PASSWORD);

            // Connection settings.
            parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            parameters.put(SessionParameter.ATOMPUB_URL, ALFRSCO_SERVER_URL + ALFRSCO_ATOMPUB_URL);
            parameters.put(SessionParameter.AUTH_HTTP_BASIC, AUTH);
            parameters.put(SessionParameter.COOKIES, COOKIES);
            parameters.put(SessionParameter.OBJECT_FACTORY_CLASS, FAC_CLASS);
            parameters.put(SessionParameter.REPOSITORY_ID, "bedroom");


            // Create session.
            // Alfresco only provides one repository.

            Repository repository = sessionFactory.getRepositories(parameters).get(0);
            session = repository.createSession();
            Folder root = session.getRootFolder();
            log.debug("--------------connected to Alfresco Ged--------------");
            // log.info(root.getName());

            return root;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }

    public void disconnect() {
        // session.clear();
        // session = null;
    }

    public int getNbDocs(Folder target) {
        int s = 0;
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                s++;
            }
        }
        return s;
    }

    public Folder createFolder2(Folder target, String newFolderName) {
        Folder rootFolder = connect();
        Folder newFolder = getFolderByName(rootFolder, newFolderName);
        if (newFolder == null) {
            Map<String, String> props = new HashMap<String, String>();
            props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            props.put(PropertyIds.NAME, newFolderName);
            newFolder = target.createFolder(props);
        }
        return newFolder;
    }

    public Folder createFolder1(Folder target, String newFolderName, String newPath, String description) {

        Folder rootFolder = connect();
        Folder newFolder = getFolderByName(rootFolder, newPath);
        if (newFolder == null) {
            Map<String, String> props = new HashMap<String, String>();
            props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            props.put(PropertyIds.NAME, newFolderName);

            newFolder = target.createFolder(props);
        }
        return newFolder;
    }

    public String getIdDocs(Folder target, String fileName) {
        List<Document> listeDoc = new ArrayList<Document>();
        String ch = "";
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                Document doc = (Document) o;
                if (doc.getName().equals(fileName)) {
                    ch = doc.getId();
                    ch = ch.substring(0, ch.indexOf(";"));
                }
            }
        }

        return ch;
    }

    public void supprimerAll(Folder target) {
        List<Document> listeDoc = new ArrayList<Document>();
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                Document doc = (Document) o;

                doc.delete(true);

            }
        }
    }

    public Document getDocumentByPath(String path) {
        try {

            Document doc = (Document) session.getObjectByPath(path);

            return doc;
        } catch (final CmisObjectNotFoundException e) {

            return null;
        }

    }

    public OutPutGed getDocumentByPaths(String path) {
        try {

            Document doc = (Document) session.getObjectByPath(path);
            log.info("+++++++ " + doc);
            String ch = doc.getId().substring(0, doc.getId().indexOf(";"));
            String[] t = ch.split("/");
            ch = t[t.length - 1];
            OutPutGed rslt = new OutPutGed();
            rslt.setNameFile(doc.getName());
            rslt.setIdDoc(doc.getId());
            rslt.setPathFolder(doc.getPaths().get(0));
            rslt.setIdDoc(ch);
            rslt.setUrlFile(ALFRSCO_SERVER_URL + ALFRSCO_PDF_URL + ch + getTicket());
            return rslt;
        } catch (final CmisObjectNotFoundException e) {

            return null;
        }

    }


    public void copyDoc(String id, Folder target) {
        try {
            Document doc = (Document) session.getObject(id);
            doc.copy(target);
        } catch (final CmisObjectNotFoundException e) {

            log.error("---------> " + e.getMessage());
        }
    }

    public void createDocument(Folder target, String newDocName, byte[] pjFichier, String contentType,
                               Map<String, String> props) {


        ByteArrayInputStream input = new ByteArrayInputStream(pjFichier);
        ContentStream contentStream = session.getObjectFactory().createContentStream(newDocName, pjFichier.length,
            contentType, input);
        Document document = null;
        try {
            document = target.createDocument(props, contentStream, VersioningState.MAJOR);
        } catch (CmisNameConstraintViolationException e) {
            System.out.println("----file already existe----" + newDocName);

        }

    }

    public void createDocument1(Folder target, String newDocName, byte[] pjFichier, String contentType) {

        Map<String, String> props = new HashMap<String, String>();

        props.put(PropertyIds.NAME, newDocName);


        ByteArrayInputStream input = new ByteArrayInputStream(pjFichier);

        ContentStream contentStream = session.getObjectFactory().createContentStream(newDocName, pjFichier.length,
            contentType, input);

        target.createDocument(props, contentStream, VersioningState.MAJOR);

    }

    public Folder createFolder1(Folder target, String newFolderName, String newPath) {

        Folder rootFolder = connect();
        Folder newFolder = getFolderByName(rootFolder, newPath);
        if (newFolder == null) {
            Map<String, String> props = new HashMap<String, String>();
            props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            props.put(PropertyIds.NAME, newFolderName);
            newFolder = target.createFolder(props);
        }
        return newFolder;
    }

    public void supprimer(Folder target, String nom) {
        List<Document> listeDoc = new ArrayList<Document>();
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                Document doc = (Document) o;
                if (doc.getName().equals(nom)) {
                    doc.delete(true);
                }
            }
        }
    }

    public void supprimerById(Folder target, String idDoc) {
        List<Document> listeDoc = new ArrayList<Document>();
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                Document doc = (Document) o;
                doc.getType();
                String ch = doc.getId().substring(0, doc.getId().indexOf(";"));
                if (ch.equals(idDoc)) {
                    doc.delete(true);
                }
            }
        }
    }

    public List<OutPutGed> getListFile(Folder target) {
        List<OutPutGed> listeDoc = new ArrayList<OutPutGed>();
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {
                Document doc = (Document) o;
                OutPutGed outPutGed = new OutPutGed();
                String ch = doc.getId().substring(0, doc.getId().indexOf(";"));
                String[] t = ch.split("/");
                ch = t[t.length - 1];

                outPutGed.setUrlFile(ALFRSCO_SERVER_URL + ALFRSCO_PDF_URL + ch + getTicket());
                outPutGed.setIdDoc(ch);


                outPutGed.setNameFile(doc.getName());
                outPutGed.setIdDoc(ch);
                listeDoc.add(outPutGed);
            }
        }

        return listeDoc;
    }


    /**
     * Clean up test folder before executing test
     *
     * @param target
     * @param delFolderName
     */
    private static void cleanup(Folder target, String delFolderName) {
        try {
            CmisObject object = session.getObjectByPath(target.getPath() + delFolderName);
            Folder delFolder = (Folder) object;
            delFolder.deleteTree(true, UnfileObject.DELETE, true);
        } catch (CmisObjectNotFoundException e) {

            log.error("No need to clean up.");
        }
    }

    /**
     * Get FolderManager By Name
     *
     * @param target
     */
    public Folder getFolderByName(Folder target, String folderName) {
        Folder foundFolder;
        try {
            CmisObject object = session.getObjectByPath(target.getPath() + folderName);
            foundFolder = (Folder) object;
        } catch (Exception e) {
            foundFolder = null;

        }

        return foundFolder;
    }

    public OutPutGed getFileByName(Folder target, String fileName) {
        OutPutGed foundFile;
        try {
            CmisObject object = session.getObjectByPath(target.getPath() + fileName);
            foundFile = (OutPutGed) object;
        } catch (Exception e) {
            foundFile = null;

        }
        return foundFile;
    }

    /**
     * @param target
     */
    public List<String> listFolder(int depth, Folder target) {
        List<String> liste = new ArrayList<>();
        String indent = StringUtils.repeat("\t", depth);
        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_DOCUMENT.equals(o.getBaseTypeId())) {

                //log.info(indent + "[Docment] " + o.getName());

            } else if (BaseTypeId.CMIS_FOLDER.equals(o.getBaseTypeId())) {

                // log.info(indent + "[FolderManager] " + o.getName());
                listFolder(++depth, (Folder) o);
                liste.add(o.getName());
            }
        }
        return liste;
    }


    public List<String> listFolder(Folder target) {
        List<String> liste = new ArrayList<>();

        for (Iterator<CmisObject> it = target.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();
            if (BaseTypeId.CMIS_FOLDER.equals(o.getBaseTypeId())) {

                liste.add(o.getName());
            }
        }
        return liste;
    }

    /**
     * Delete test document
     *
     * @param target
     * @param delDocName
     */
    public static void DeleteDocument(Folder target, String delDocName) {
        try {
            CmisObject object = session.getObjectByPath(target.getPath() + delDocName);
            Document delDoc = (Document) object;
            delDoc.delete(true);
        } catch (CmisObjectNotFoundException e) {

            log.error("Document is not found: " + delDocName);
        }
    }

    public boolean deleteDocumentByPath(String path) {
        try {
            CmisObject object = session.getObjectByPath(path);
            Document delDoc = (Document) object;
            delDoc.delete(true);
            return true;
        } catch (CmisObjectNotFoundException e) {

            log.error("Document is not found: " + path);
            return false;
        }
    }

    /**
     * Create test document with content
     *
     * @param target
     * @param newDocName
     */
    public void createDocument(Folder target, String newDocName, byte[] pjFichier, String contentType) {
        Map<String, String> props = new HashMap<String, String>();
        props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        props.put(PropertyIds.NAME, newDocName);

        ByteArrayInputStream input = new ByteArrayInputStream(pjFichier);
        ContentStream contentStream = session.getObjectFactory().createContentStream(newDocName, pjFichier.length,
            contentType, input);
        target.createDocument(props, contentStream, VersioningState.MAJOR);

    }

    private static void createDocumentFromFile(Folder target, String newDocName, File file, String contentType) {
        Map<String, String> props = new HashMap<String, String>();

        String parentID = file.getParentFile().getPath();
        props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        props.put(PropertyIds.NAME, newDocName);


        try {
            Document docFromFile = FileUtils.createDocumentFromFile(parentID, file, newDocName, VersioningState.MAJOR,
                session);
            target.addToFolder(docFromFile, true);
        } catch (java.io.FileNotFoundException ex) {

            log.error("  FileNotFoundException " + ex.getMessage());
        }

    }

    public void gettingFolders() {

        Folder root = session.getRootFolder();
        log.info("root " + root.getName());
        ItemIterable<CmisObject> children = root.getChildren();

        for (CmisObject o : children) {

            log.info(o.getName() + " -- which is of type  -> " + o.getType().getDisplayName());
        }
    }

    /**
     * Create test folder directly under target folder
     *
     * @param target
     * @param
     * @return newly created folder
     */
    public Folder createFolder(Folder target, String newFolderName) {
        Map<String, String> props = new HashMap<String, String>();
        props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        props.put(PropertyIds.NAME, newFolderName);
        Folder newFolder = target.createFolder(props);
        return newFolder;
    }

    public byte[] fileToByteArray(String path) {
        FileInputStream fileInputStream = null;

        File file = new File(path);

        byte[] bFile = new byte[(int) file.length()];

        try {

            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }


    public String getPathFile(Folder target, String delDocName) {
        CmisObject object = session.getObjectByPath(target.getPath() + delDocName);
        String path = target.getPath() + delDocName;
        log.info("path of file" + path);
        return path;

    }

    public void test(String test) {
        OperationContext operationContext = session.createOperationContext(null,
            true, true, false, IncludeRelationships.BOTH, null, false, null, true, 100);
        //Build query statement
        MessageFormat format = new MessageFormat("cmis:name LIKE ''%{0}%'' OR CONTAINS(''{0}'')");
        String statement = "";
        if (StringUtils.isNotBlank(test)) {
            statement = format.format(new String[]{test});
        }
    }

    public Session getCmisSession() {

        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Map<String, String> parameters = new HashMap<String, String>();

        // default factory implementation
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        parameters.put(SessionParameter.USER, ALFRSCO_USER);
        parameters.put(SessionParameter.PASSWORD, ALFRSCO_PASSWORD);

        // Connection settings.
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameters.put(SessionParameter.ATOMPUB_URL, ALFRSCO_SERVER_URL + ALFRSCO_ATOMPUB_URL);
        parameters.put(SessionParameter.AUTH_HTTP_BASIC, AUTH);
        parameters.put(SessionParameter.COOKIES, COOKIES);
        parameters.put(SessionParameter.OBJECT_FACTORY_CLASS, FAC_CLASS);
        parameters.put(SessionParameter.REPOSITORY_ID, "bedroom");
        List<Repository> repositories = factory.getRepositories(parameters);

        return repositories.get(0).createSession();
    }
    //Execute query
//    ItemIterable<CmisObject> results = session.queryObjects("cmis:document", statement, false, operationContext);
//    Iterator<CmisObject> itr = results.iterator();
//    List<CmisObject> list = new ArrayList<CmisObject>();
//    while (itr.hasNext()) {
//        list.add(itr.next());
//    }


//    public static void search(String repositoryId, String term) {
//        //Session session = getCmisSession(repositoryId);
//        OperationContext ctxt = session.getDefaultContext();
//        List<CmisObject> list = new ArrayList<CmisObject>();
//        // Build WHERE clause(cmis:document)
//        MessageFormat docFormat = new MessageFormat("cmis:name LIKE ''%{0}%'' OR cmis:description LIKE ''%{0}%'' OR CONTAINS(''{0}'')");
//        String docStatement = "";
//        if (StringUtils.isNotBlank(term)) {
//            docStatement = docFormat.format(new String[] { term });
//        }
//        ItemIterable<CmisObject> docResults = session.queryObjects("cmis:document", docStatement, false, ctxt);
//        Iterator<CmisObject> docItr = docResults.iterator();
//        while (docItr.hasNext()) {
//            CmisObject doc = docItr.next();
//            boolean val = doc.getPropertyValue("cmis:isLatestVersion");
//            if (!val)
//                continue;
//            list.add(doc);
//        }

////        return ok(search.render(repositoryId, term, list));
//    }

    public String readTheContentsOfTheDocument(Document document) {
        ContentStream contentStream = document.getContentStream();
        String cc = "";
        try (InputStream inputStream = contentStream.getStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            byte[] buffer = new byte[65535];
            int bytesRead = -1;

            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                String str = new String(buffer, 0, bytesRead);
//                log.info("buffer " +buffer );
                // log.info("str " +str );
                cc = cc + str;
//                log.info("cc " +cc );

            }
        } catch (IOException e) {
//            System.out.println("Error occurred while processing the file content");
            System.out.println(e.getMessage());
        }
        return cc;

    }


}
