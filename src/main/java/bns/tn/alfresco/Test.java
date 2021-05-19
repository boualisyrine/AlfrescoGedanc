package bns.tn.alfresco;

import bns.tn.alfresco.config.CmisUtilsGed;
import bns.tn.alfresco.model.Node;
import bns.tn.alfresco.model.Tree;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class Test implements CommandLineRunner {
    CmisUtilsGed cmisUtils = new CmisUtilsGed();
    Node<String> root = new Node<>("DIGITAL_HOME");
    List<Tree> myTrees = new ArrayList<>();
    Tree tree = new Tree();
    @Override
    public void run(String... args) throws Exception {

        Folder entrepot = cmisUtils.connect();
        Folder home = cmisUtils.getFolderByName(entrepot, "DIGITAL_HOME/exemple1");
      System.out.println("****************************************************************************");

       /* tree.setName("DIGITAL_HOME");
        myTrees.add(tree);*/
        getSub(home);

/*   System.out.println("parent =" +tree.getName());
   tree.getChildrens().forEach(c -> System.out.println(c.getName()));*/
printTree(root, "");
    }


   void  getSub( Folder home) {
        for (Iterator<CmisObject> it = home.getChildren().iterator(); it.hasNext(); ) {
            CmisObject o = it.next();

        //   System.out.println(o.getName());
          //  if(root.getParent() != null)
               root.addChild(new Node<String>(o.getName()));
            if (BaseTypeId.CMIS_FOLDER.equals(o.getBaseTypeId())) {

          //      node.addChild(new Node<String>(o.getName()));
                getSub((Folder) o);

               // node1.addChild(new Node<String>(o.getName()));
            }
            /*Tree child = new Tree();
            child.setName(o.getName());
            tree.addChildren(child);*/
        }
    }

    private static <T> void printTree(Node<T> node, String appender) {
        System.out.println(appender + node.getData());
        node.getChildren().forEach(each ->  printTree(each, appender + appender));
    }
    private static Node<String> createTree() {
        Node<String> root = new Node<>("root");

        Node<String> node1 = root.addChild(new Node<String>("node 1"));

        Node<String> node11 = node1.addChild(new Node<String>("node 11"));
        Node<String> node111 = node11.addChild(new Node<String>("node 111"));
        Node<String> node112 = node11.addChild(new Node<String>("node 112"));

        Node<String> node12 = node1.addChild(new Node<String>("node 12"));

        Node<String> node2 = root.addChild(new Node<String>("node 2"));

        Node<String> node21 = node2.addChild(new Node<String>("node 21"));
        Node<String> node211 = node2.addChild(new Node<String>("node 22"));
        return root;
    }
}
