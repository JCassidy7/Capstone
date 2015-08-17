
package sorcererscave;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.JTree;
import javax.swing.border.TitledBorder;

public class JTree {
     
    
    public JTree(Cave cave) {
      Node top = createDataTree (cave);
      DefaultMutableTreeNode jtTop = createNodes(top);
      javax.swing.JTree tree = new javax.swing.JTree(jtTop);
      JScrollPane treeView = new JScrollPane(tree);
      JFrame jft = new JFrame ("JTree");
      jft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jft.setSize (230, 400);
    //  jft.setLocationRelativeTo(null);
      jft.setVisible (true);
      jft.add (treeView, BorderLayout.CENTER);
    }
    
    DefaultMutableTreeNode createNodes (NodeIF t) {
      DefaultMutableTreeNode top = new DefaultMutableTreeNode (t);
      for (NodeIF n: t.getChildren()) top.add (createNodes (n));
      return top;
   } // end createNodes
    
    Node createDataTree(Cave cave) {
        Node partyNode, creatureNode, treasureNode, artifactNode, jobNode, u, v, w;
        Node z = new Node("Cave");

        for (Party p : cave.parties) {
            z.children.add(partyNode = new Node("Party: " + p.getPartyIndex() + ": " + p.getPartyName()));
            for (Creature c : p.members) {
                partyNode.children.add(creatureNode = new Node("Creature: " + c.getCreatureName() + ": " + c.getCreatureType()));
                creatureNode.children.add(u = new Node("Treasures"));
                for (Treasure t : c.treasures) {
                    u.children.add(treasureNode = new Node(t.getTreasureType()));
                }
                creatureNode.children.add(v = new Node("Artifacts"));
                for (Artifact a : c.artifacts) {
                    if (a.getArtifactName().equals("Artifact has no name.")) {
                        v.children.add(artifactNode = new Node("Nameless, type: " + a.getArtifactType()));
                    } else {
                        v.children.add(artifactNode = new Node(a.getArtifactName() + ", type: " + a.getArtifactType()));
                    }
                }
                creatureNode.children.add(w = new Node("Jobs"));
                for (Job j : c.jobs) {
                    w.children.add(jobNode = new Node(j.getJobName()));
                }
            }
        }
     
        return z;
    } 

}


interface NodeIF {
  public ArrayList <NodeIF> getChildren ();
} // end NodeIF

class Node implements NodeIF {
   String data;
   ArrayList <NodeIF> children = new ArrayList <NodeIF> ();

   public Node (String s) {data = s;}
  
   public ArrayList <NodeIF> getChildren () {return children;}
  
   public String toString () {return data;}
} // end Node
