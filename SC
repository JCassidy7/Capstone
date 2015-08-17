package sorcererscave;

// File: SorcerersCave.java
// Date: March 6, 2015
// Authors: Group 4

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class SorcerersCave extends JFrame {
    static final long serialVersionUID = 123L;
    
    JTextArea jta = new JTextArea ();
    JComboBox <String> jcb;
    JComboBox <String> jcbSortCreature;
    JComboBox <String> jcbSortTreasure;
    JTextField jtf;    
    
    JFrame jf = new JFrame();
    JPanel jrun = new JPanel();
  //  JPanel jtree = new JPanel();    
    Cave cave = new Cave ();

    public SorcerersCave () {
       // System.out.println ("In constructor");
        setTitle ("Sorcerer's Cave");
        setSize (600, 300);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setVisible (true);
        
        
        JScrollPane jsp = new JScrollPane (jta);
        add (jsp, BorderLayout.CENTER);        
        
        JButton jbr = new JButton ("Select File");
        JButton jbd = new JButton ("Display");
        JButton jbs = new JButton ("Search");
        JButton jbCreatureSort = new JButton ("Sort");
        JButton jbTreasureSort = new JButton ("Sort");
        
        JLabel jls = new JLabel ("Search target");
        
        jtf = new JTextField (10);
        
        jcb = new JComboBox <String> ();
        jcb.addItem ("Index");
        jcb.addItem ("Type");
        jcb.addItem ("Name");
        
        JLabel jlSortCreature = new JLabel ("Sort creatures by:");
        
        jcbSortCreature = new JComboBox<String>();
        jcbSortCreature.addItem("Empathy");
        jcbSortCreature.addItem("Fear");
        jcbSortCreature.addItem("Carrying Capacity");
        
        JLabel jlSortTreasure = new JLabel ("Sort treasures by:");
        
        jcbSortTreasure = new JComboBox<String>();
        jcbSortTreasure.addItem("Weight");
        jcbSortTreasure.addItem("Value");   
        
        JPanel jp = new JPanel ();        
        jp.add (jbr);
        jp.add (jbd);
        jp.add (jls);
        jp.add (jtf);
        jp.add (jcb);
        jp.add (jbs);
        jp.add (jlSortCreature);
        jp.add (jcbSortCreature);
        jp.add (jbCreatureSort);
        jp.add (jlSortTreasure);
        jp.add (jcbSortTreasure);
        jp.add (jbTreasureSort);
  
        
        jp.setPreferredSize (new java.awt.Dimension (10, 80));
        add (jp, BorderLayout.PAGE_START);  
        
        validate();
        
        jbr.addActionListener ( new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    try {
                        readFile ();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "The file contains invalid leading character(s). Only a, c, j, p, and t are valid leading characters. Request canceled.");
                        System.exit(0);
                    }
                }            } // end local definition of inner class
        ); // the anonymous inner class
        
        jbd.addActionListener ( new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    displayCave ();
                } // end required meth0d
            } // end local definition of inner class
        ); // the anonymous inner class
        
        jbs.addActionListener ( new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    search ((String)(jcb.getSelectedItem()), jtf.getText());
                } // end required method
            } // end local definition of inner class
        ); // the anonymous inner class
        
        jbCreatureSort.addActionListener ( new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    sortCreatures ((String) (jcbSortCreature.getSelectedItem()));
                } 
            } 
        ); 
        
        jbTreasureSort.addActionListener( new ActionListener () {
                public void actionPerformed (ActionEvent e) {
                    sortTreasures ((String) (jcbSortTreasure.getSelectedItem()));
                }
            }
        );
  
    jf.setTitle ("Jobs");
    jrun.setLayout(new GridLayout (0, 5));
    JScrollPane jsRun = new JScrollPane(jrun);
    jf.add(jsRun);    
    jf.add(new JLabel("Close any window to end program "), BorderLayout.PAGE_START);
    jf.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    jf.setSize(1000, 500);
    jf.setVisible(true);
    jf.setLocationRelativeTo(null); 
    
    } // end no-parameter constructor
    
    public void readFile () throws IOException{
        
        HashMap <Integer, Party> hmParties = new HashMap <Integer, Party> ();
        HashMap <Integer, Creature> hmCreatures = new HashMap <Integer, Creature> ();

        JFileChooser jfc = new JFileChooser(".");
        int returnVal = jfc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION){
            System.out.println("You chose to open this file: " +
                    jfc.getSelectedFile().getName());
        }
        else if (returnVal == JFileChooser.CANCEL_OPTION){
            JOptionPane.showMessageDialog(null, "Action canceled.");
            System.exit(0);
        }
        // open and read file
          try {
              Scanner scan = new Scanner(jfc.getSelectedFile());
              while (scan.hasNextLine()) {
              String line = scan.nextLine();
              if (line.length() == 0) continue;
              Scanner sLine = new Scanner (line).useDelimiter("\\s*:\\s*");
              if (line.charAt(0) == '/') continue;
              switch (line.charAt(0)){
                  case 'p':
                  case 'P': addParty(sLine, hmParties); break;
                  case 'c':
                  case 'C': addCreature (sLine, hmParties, hmCreatures); break;
                  case 't':
                  case 'T': addTreasure (sLine, hmCreatures); break;
                  case 'a':
                  case 'A': addArtifact (sLine, hmParties, hmCreatures); break;   
                  case 'j':
                  case 'J': addJob (sLine, hmParties, hmCreatures); break;  
                  default : throw new IOException(" ");
              } // end switch structure
            } // end while loop     
              JTree jtree = new JTree(cave);
                  } // end try
          catch(FileNotFoundException e) {
              JOptionPane.showMessageDialog(null, "File not found.");
          } // end catch
        /*  catch (IOException e){
              JOptionPane.showMessageDialog(null, "The file contains invalid characters or formatting. Request canceled.");
              System.exit(0);
          }
         */
    } // end method readFile
    
    
    
    public void displayCave () {
        jta.setText ("Display Cave button pressed\n");
        jta.append ("" + cave);       
    } // end method displayCave
    
    public void search (String type, String target) {
        jta.append (String.format ("\nSearch button pressed, type: >%s<, target: >%s<\n", type, target));
        jta.append("\nResults: \n");
        if ("Index".equals(type)){
            int indexSearch = Integer.parseInt(target);
            for(Party p: cave.parties){
                if (indexSearch == p.getPartyIndex())
                    jta.append (p.toString());    
                for (Creature c: p.members){
                    if (indexSearch == c.getCreatureIndex())
                        jta.append(c.toString());
                    for (Treasure t: c.treasures){
                        if (indexSearch == t.getTreasureIndex())
                            jta.append(t.toString());
                    }
                    for (Artifact a: c.artifacts){
                            if (indexSearch == a.getArtifactIndex())
                                jta.append(a.toString());
                    }
                }
            }
        }
        else if ("Type".equals(type)){
            for(Party p: cave.parties){   
                for (Creature c: p.members){
                    if (target.equalsIgnoreCase(c.getCreatureType()))
                        jta.append(c.toString());
                    for (Treasure t: c.treasures){
                        if (target.equalsIgnoreCase(t.getTreasureType()))
                            jta.append(t.toString());
                    }
                    for (Artifact a: c.artifacts){
                            if (target.equalsIgnoreCase(a.getArtifactType()))
                                jta.append(a.toString());
                    }
                }
            }
            
        }
        else if ("Name".equals(type)){
            for(Party p: cave.parties){    
                for (Creature c: p.members){
                    if (target.equalsIgnoreCase(c.getCreatureName()))
                        jta.append(c.toString());
                    for (Artifact a: c.artifacts){
                            if (target.equalsIgnoreCase(a.getArtifactName()))
                                jta.append(a.toString());
                    }
                }
            }
        }
    } // end method search
    
    public void sortCreatures(String type) {
        jta.setText(null);
        if ("Empathy".equals(type)){
            for (Party p: cave.parties)
                Collections.sort(p.members, Creature.EmpathyComparator);
            jta.append ("\n+++++++++++++++++\nCreatures sorted by empathy: " + cave);
         }
        else if ("Fear".equals(type)) {
            for (Party p: cave.parties)
                Collections.sort(p.members, Creature.FearComparator);
            jta.append ("\n+++++++++++++++++\nCreatures sorted by fear: " + cave); 
        }
        else {
            for (Party p: cave.parties)
                Collections.sort(p.members, Creature.CarryingCapacityComparator);
            jta.append ("\n+++++++++++++++++\nCreatures sorted by carrying capacity: " + cave);
        }
        
    } // end method sortCreatures
    
    public void sortTreasures (String type) {
        jta.setText(null);
        if ("Weight".equals(type)){
            for (Party p: cave.parties)
            for (Creature c: p.members)
                Collections.sort(c.treasures, Treasure.TreasureWeightComparator);
        jta.append ("\n+++++++++++++++++++\nTreasures sorted by weight: " + cave);
        }
        else {
            for (Party p: cave.parties)
            for (Creature c: p.members)
                Collections.sort(c.treasures, Treasure.TreasureValueComparator);
        jta.append ("\n+++++++++++++++++++\nTreasures sorted by value: " + cave);
        }
    } // end method sortTreasures
    
    
    
    void addParty(Scanner sLine, HashMap <Integer, Party> hmParties){
        int partyIndex;
        String partyName;
        String st = sLine.nextLine();
        String splitLine[] = st.split("\\s*:\\s*");
        partyIndex = Integer.parseInt(splitLine[1]);
        partyName = splitLine[2];
        Party p = new Party(partyIndex, partyName);
        cave.parties.add(p);
        hmParties.put(partyIndex, p);
    }
    
    void addCreature(Scanner sLine, HashMap <Integer, Party> hmParties, HashMap <Integer, Creature> hmCreatures){
        String st = sLine.nextLine();
        String splitLine [] = st.split("\\s*:\\s*");
        int creatureIndex = Integer.parseInt(splitLine[1]);
        String creatureType = splitLine[2];
        String creatureName = splitLine[3];
        int creaturePartyIndex = Integer.parseInt(splitLine[4]);
        int creatureEmpathy = Integer.parseInt(splitLine[5]);
        int creatureFear = Integer.parseInt(splitLine[6]);
        int creatureCarryingCapacity = Integer.parseInt(splitLine[7]);
        
        Creature c = new Creature(creatureIndex, creatureType, creatureName, creaturePartyIndex, creatureEmpathy, creatureFear, creatureCarryingCapacity);
        hmCreatures.put(creatureIndex, c);
        Party p = hmParties.get(c.getCreaturePartyIndex());
        if (p == null)
            cave.stuff.add(c);
        else
            p.members.add(c);
              
    }
    
    void addTreasure(Scanner sLine, HashMap <Integer, Creature> hmCreatures){
        String st = sLine.nextLine();
        String splitLine [] = st.split("\\s*:\\s*");
        int treasureIndex = Integer.parseInt(splitLine[1]);
        String treasureType = splitLine[2];
        int treasureCreatureIndex = Integer.parseInt(splitLine[3]);
        double treasureWeight = Double.parseDouble(splitLine[4]);
        double treasureValue = Double.parseDouble(splitLine[5]);
        Treasure t = new Treasure(treasureIndex, treasureType, treasureCreatureIndex, treasureWeight, treasureValue);
        Creature c = hmCreatures.get(t.getTreasureCreatureIndex());
        if (c == null)
            cave.stuff.add(t);
        else
            c.treasures.add(t);
    }
    
    void addArtifact(Scanner sLine, HashMap <Integer, Party> hmParties, HashMap <Integer, Creature> hmCreatures){
        int artifactIndex;
        String artifactType;
        int artifactCreatureIndex;
        String artifactName;
        String st = sLine.nextLine();
        String splitLine [] = st.split("\\s*:\\s*");
        if (splitLine.length == 5){
            artifactIndex = Integer.parseInt(splitLine[1]);
            artifactType = splitLine[2];
            artifactCreatureIndex = Integer.parseInt(splitLine[3]);
            artifactName = splitLine[4];
        }
        else{
            artifactIndex = Integer.parseInt(splitLine[1]);
            artifactType = splitLine[2];
            artifactCreatureIndex = Integer.parseInt(splitLine[3]);
            artifactName = "Artifact has no name.";
        }
       
        Artifact a = new Artifact(artifactIndex, artifactType, artifactCreatureIndex, artifactName);
        Creature c = hmCreatures.get(a.getArtifactCreatureIndex());
        Party p = new Party();
        if (c == null)
            cave.stuff.add(a);
        else
        {
            p = hmParties.get(c.getCreaturePartyIndex());
            c.artifacts.add(a);
            p.pool.add(a);
        }
    }
    

    
    void addJob (Scanner sLine, HashMap <Integer, Party> hmParties, HashMap<Integer, Creature> hmCreatures){
        ArrayList <String> requiredArtifacts = new ArrayList <String>();  
     //   int jobIndex;
       // String jobName;
       // int jobCreatureIndex;
        //double jobTime;
        String st = sLine.nextLine();
        String splitLine [] = st.split("\\s*:\\s*");
        int jobIndex = Integer.parseInt(splitLine[1]);
        String jobName = splitLine[2];
        int jobCreatureIndex = Integer.parseInt(splitLine[3]);
        double jobTime = Double.parseDouble(splitLine[4]);
        if (splitLine.length == 11){           
                    
            for (int i = 0; i < Integer.parseInt(splitLine[6]); i++) {
                requiredArtifacts.add(splitLine[5]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[8]); i++) {
                requiredArtifacts.add(splitLine[7]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[10]); i++) {
                requiredArtifacts.add(splitLine[9]);
            }
        }
        if (splitLine.length == 13){
                
            for (int i = 0; i < Integer.parseInt(splitLine[6]); i++) {
                requiredArtifacts.add(splitLine[5]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[8]); i++) {
                requiredArtifacts.add(splitLine[7]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[10]); i++) {
                requiredArtifacts.add(splitLine[9]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[12]); i++) {
                requiredArtifacts.add(splitLine[11]);
            }
        }
        if (splitLine.length == 15){
            
            for (int i = 0; i < Integer.parseInt(splitLine[6]); i++) {
                requiredArtifacts.add(splitLine[5]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[8]); i++) {
                requiredArtifacts.add(splitLine[7]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[10]); i++) {
                requiredArtifacts.add(splitLine[9]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[12]); i++) {
                requiredArtifacts.add(splitLine[11]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[14]); i++){
                requiredArtifacts.add(splitLine[13]);
            }
        }
        else if (splitLine.length == 17){
            
            for (int i = 0; i < Integer.parseInt(splitLine[6]); i++) {
                requiredArtifacts.add(splitLine[5]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[8]); i++) {
                requiredArtifacts.add(splitLine[7]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[10]); i++) {
                requiredArtifacts.add(splitLine[9]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[12]); i++) {
                requiredArtifacts.add(splitLine[11]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[14]); i++){
                requiredArtifacts.add(splitLine[13]);
            }
            for (int i = 0; i < Integer.parseInt(splitLine[16]); i++){
                requiredArtifacts.add(splitLine[15]);
            }
        }
                
    //  String jobArtifactType = splitLine[5];
    //  int jobArtifactNumber = Integer.parseInt(splitLine[6]);
        
        Job j = new Job(jobIndex, jobName, jobCreatureIndex, jobTime, jrun, hmParties, hmCreatures, requiredArtifacts);
        Creature c = hmCreatures.get(j.getJobCreatureIndex());
        if (c == null)
            cave.stuff.add(j);
        else
            c.jobs.add(j);
    }
    
    public static void main (String [] args) {
        SorcerersCave sc = new SorcerersCave ();
    } // end main
} // end class SorcerersCave

class Cave {
    ArrayList <Party> parties = new ArrayList <Party> ();
    ArrayList <CaveElement> stuff = new ArrayList <CaveElement> ();
    
    public ArrayList<Party> getParties(){
        return parties;
    }
    
    public ArrayList<CaveElement> getStuff(){
        return stuff;
    }
    
    public String toString () {
        String st = "Cave.toString:\nThe Parties:\n";
        for (Party p: parties) 
            st += p + "\n";
        st += "\n+++++++\nThe unassociated stuff:\n";
        for (CaveElement e: stuff)
            st += "     " + e + "\n";
        return st;
    } // end toString method
} // end class Cave

class CaveElement {
    String name = ""; // make sure we have a default value here, NOT null
    int index = 0;
    
    public String toString () {
        return "CaveElement: " + name;
    } // end method toString
} // end class CaveElement



class Party extends CaveElement {
    ArrayList <Creature> members = new ArrayList <Creature> ();
    ArrayList <Artifact> pool = new ArrayList <Artifact> ();
    private int partyIndex;
    private String partyName;
    
    public Party () {}
    
    public Party (int index, String name){
        this.partyIndex = index;
        this.partyName = name;
        
    }
    
    public int getPartyIndex(){
        return partyIndex;
    }
    
    public String getPartyName(){
        return partyName;
    }
    
    
    public String toString () {
        String st = "Party Index: " + partyIndex + "  " + "Party name: " + partyName 
                + "  " + "\nMembers:\n";
        for (Creature c: members) 
            st += "    " + c + "\n";
        st += "Resource pool: \n";
        for (Artifact a: pool)
            st += "    Artifact index: " + a.getArtifactIndex() + "Artifact name: " 
                    + a.getArtifactName() + ", Artifact type: " + a.getArtifactType() + "\n";
        return st;
    } // end method toString
} // end class Party

class Creature extends CaveElement {
    ArrayList <Treasure> treasures = new ArrayList <Treasure> ();
    ArrayList <Artifact> artifacts = new ArrayList <Artifact> ();
    ArrayList <Job> jobs = new ArrayList <Job>();
    private int creatureIndex;
    private String creatureType;
    private String creatureName;
    private int creaturePartyIndex;
    private int creatureEmpathy;
    private int creatureFear;
    private int creatureCarryingCapacity;    
    boolean busyFlag = false;

    public Creature () {}
    
    public Creature (int index, String type, String name, int partyIndex, int empathy, int fear, int carryingCapacity){
        this.creatureIndex = index;
        this.creatureType = type;
        this.creatureName = name;
        this.creaturePartyIndex = partyIndex;
        this.creatureEmpathy = empathy;
        this.creatureFear = fear;
        this.creatureCarryingCapacity = carryingCapacity;
    }
    
    public static Comparator <Creature> EmpathyComparator = new Comparator <Creature>(){
        @Override
        public int compare(Creature c, Creature c1){
            return c.creatureEmpathy - c1.creatureEmpathy;
        }
    };
    
    public static Comparator <Creature> FearComparator = new Comparator <Creature> (){
        @Override
        public int compare (Creature c, Creature  c1){
            return c.creatureFear - c1.creatureFear;
        }
    };
    
    public static Comparator <Creature> CarryingCapacityComparator = new Comparator <Creature> (){
        @Override
        public int compare (Creature c, Creature c1){
            return c.creatureCarryingCapacity - c1.creatureCarryingCapacity;
        }
    };
    
    public int getCreatureIndex(){
        return creatureIndex;
    }
    
    public String getCreatureType(){
        return creatureType;
    }
    
    public String getCreatureName(){
        return creatureName;
    }
    
    public int getCreaturePartyIndex(){
        return creaturePartyIndex;
    }
    
    public int getCreatureEmpathy(){
        return creatureEmpathy;
    }
    
    public int getCreatureFear(){
        return creatureFear;
    }
    
    public int getCreatureCarryingCapacity(){
        return creatureCarryingCapacity;
    }
    
   
    public String toString () {
        String st = "Creature Index: " + creatureIndex + "   Creature Type: " + creatureType +
                "   Creature Name: " + creatureName + "   Creature Party Index: " + creaturePartyIndex + 
                "\n    Creature Empathy: " + creatureEmpathy +
                "   Creature Fear: " + creatureFear + "   Creature Carrying Capacity: " + 
                creatureCarryingCapacity + "\n        Artifacts:\n";
        for (Artifact a: artifacts) 
            st += "        " + a + "\n";
        st += "        Treasures:\n";
        for (Treasure t: treasures) 
            st += "        " + t + "\n";
        st += "        Jobs:\n";
        for (Job j: jobs)
            st += "        " + j + "\n";
        return st;
    } // end method toString
} // end class Creature

class Artifact extends CaveElement {
    private int artifactIndex;
    private String artifactType;
    private int artifactCreatureIndex;
    private String artifactName;
    boolean inUse = false;

    public Artifact () {}
    
    public Artifact (int index, String type, int creatureIndex, String name){
       
            artifactIndex = index;
            artifactType = type;
            artifactCreatureIndex = creatureIndex;
            artifactName = name;
        }
    
    public int getArtifactIndex(){
        return artifactIndex;
    }
    
    public String getArtifactType(){
        return artifactType;
    }
    
    public int getArtifactCreatureIndex(){
        return artifactCreatureIndex;
    }
    
    public String getArtifactName(){
        return artifactName;
    }
        
    
    public String toString () {
        return ("Artifact Index: " + artifactIndex + "   Artifact Type: "
                + artifactType + "   Artifact Creature Index: " + artifactCreatureIndex
                + "   Artifact Name: " + artifactName);
    } // end method toString
} // end class Artifact

class Treasure extends CaveElement {
    private int treasureIndex;
    private String treasureType;
    private int treasureCreatureIndex;
    private double treasureWeight;
    private double treasureValue;
    
    public Treasure () {}
    
    public Treasure (int index, String type, int creatureIndex, double weight, double value){
        treasureIndex = index;
        treasureType = type;
        treasureCreatureIndex = creatureIndex;
        treasureWeight = weight;
        treasureValue = value;
    }
   
    public static Comparator <Treasure> TreasureWeightComparator = new Comparator <Treasure> (){
        @Override
        public int compare (Treasure t, Treasure t1){
            if (t.treasureWeight < t1.treasureWeight) return -1;
            if (t.treasureWeight > t1.treasureWeight) return 1;
            return 0;            
        }
    };
    
    public static Comparator <Treasure> TreasureValueComparator = new Comparator <Treasure>(){
        @Override
        public int compare (Treasure t, Treasure t1){
            if (t.treasureValue < t1.treasureValue) return -1;
            if (t.treasureValue > t1. treasureValue) return 1;
            return 0;
        }
    };
    
    public int getTreasureIndex(){
        return treasureIndex;
    }
    
    public String getTreasureType(){
        return treasureType;
    }
    
    public int getTreasureCreatureIndex(){
        return treasureCreatureIndex;
    }
    
    public double getTreasureWeight(){
        return treasureWeight;
    }
    
    public double getTreasureValue(){
        return treasureValue;
    }
    
    public String toString () {
        return (" Treasure Index: " + treasureIndex + "   Treasure Type: "
                + treasureType + "   Treasure Creature Index: " + treasureCreatureIndex
                + "\n        Treasure Weight: " + treasureWeight + "   Treasure Value: "
                + treasureValue);
    } // end method toString
} // end class Treasure

class Job extends CaveElement implements Runnable {
    private int jobIndex;
    private String jobName;
    int jobCreatureIndex;
    long jobTime;
    ArrayList <String> jobRequiredArtifacts;
//  int jobArtifactType;
//  int jobArtifactNumber;
    
    static Random rn = new Random();
    JPanel parent;
    Creature worker = null;
    Party parentParty = null;
    JProgressBar pm = new JProgressBar();
    boolean goFlag = true, noKillFlag = true;
    JButton jbGo = new JButton ("Stop");
    JButton jbKill = new JButton ("Cancel");
    Status status = Status.SUSPENDED;
    
    enum Status {RUNNING, SUSPENDED, WAITING, DONE};    

    
    public Job () {}
    
    public Job (int index, String name, int creatureIndex, double time, JPanel cv, HashMap<Integer, Party> hmParties, HashMap<Integer, Creature> hmCreatures, ArrayList <String> requiredArtifacts){
        parent = cv;
        jobIndex = index;
        jobName = name;
        jobCreatureIndex = creatureIndex;
        jobTime = (long)time;
        jobRequiredArtifacts = requiredArtifacts;
        worker = hmCreatures.get(jobCreatureIndex);
        parentParty = hmParties.get(worker.getCreaturePartyIndex());
        pm = new JProgressBar();
        pm.setStringPainted(true);
        parent.add(pm);
        parent.add(new JLabel(worker.getCreatureName(), SwingConstants.CENTER));
        parent.add(new JLabel(jobName  , SwingConstants.CENTER));
        (new Thread (this, worker.getCreatureName() + " " + jobName)).start();
        
        parent.add(jbGo);
        parent.add(jbKill);
        
        jbGo.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                toggleGoFlag();
            }
        });
        
        jbKill.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                setKillFlag();
            }
        });
    }//end constructor
    
    public void toggleGoFlag(){
        goFlag = !goFlag;
    }// end method toggleGoFlag
    
    public void setKillFlag(){
        noKillFlag = false;
        jbKill.setBackground (Color.red);
    }// end method setKillFlag
    
    void showStatus (Status st){
        status = st;
        switch (status){
            case RUNNING:
                jbGo.setBackground(Color.green);
                jbGo.setText("Running (click to suspend)");
                break;
            case SUSPENDED:
                jbGo.setBackground(Color.yellow);
                jbGo.setText("Suspended (click to resume)");
                break;
            case WAITING:
                jbGo.setBackground(Color.orange);
                jbGo.setText("Waiting turn");
                break;
            case DONE:
                jbGo.setBackground(Color.red);
                jbGo.setText("Done");
                break;
        }//end switch on status
     }//end method showStatus
    
    public void run(){
        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 1000 * jobTime;
        double duration = stopTime - time;
        
        synchronized (parentParty){
            int acquired = 0;
            while (acquired <= jobRequiredArtifacts.size()){
            System.out.println("Party object " + parentParty.getPartyIndex() + "locked");
            for (String s: jobRequiredArtifacts)
                for (Artifact a: parentParty.pool)
                    if (s.startsWith(a.getArtifactType()) && !a.inUse){
                        a.inUse = true;
                        acquired ++;
                    }
                    else {
                        for (Artifact b: parentParty.pool)
                            b.inUse = false;
                        }
                    /* try {
                      parentParty.wait();
                  }
                    catch (InterruptedException e){
            }*/
             System.out.println (parentParty.getPartyIndex() + "unlocked");
            }
            
        }
        
        synchronized (worker){
            while (worker.busyFlag){
                showStatus (Status.WAITING);
                try {
                    worker.wait();
                }
                catch (InterruptedException e){
                }// end try/catch block
            }// end while waiting for worker to be free
            worker.busyFlag = true;
        }// end synchronized on worker
        
        while (time<stopTime && noKillFlag) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e){}
            if (goFlag) {
                showStatus(Status.RUNNING);
                time += 100;
                pm.setValue((int)(((time - startTime) / duration) * 100));
            }
            else {
                showStatus(Status.SUSPENDED); // should wait here, not busy loop
            }//end if stepping
        }// end running
        synchronized (parentParty){
            for (Artifact a: parentParty.pool)
                a.inUse = false;
         //  parentParty.notifyAll();
        }
        
        pm.setValue(100);
        showStatus(Status.DONE);
        synchronized(worker){
            worker.busyFlag = false;
            worker.notifyAll();
        }
    } // end method run
    
    
    public int getJobIndex(){
        return jobIndex;
    }
    
    public String getJobName(){
        return jobName;
    }
    
    public int getJobCreatureIndex(){
        return jobCreatureIndex;
    }
    
    public long getJobTime(){
        return jobTime;
    }
    
    public String toString(){
        
        String st = "  Job Index: " + jobIndex + "  Job Name: " + jobName 
               + "Job Creature Index: " + jobCreatureIndex + "  Job Time: " + jobTime + "\n";
        for (String s: jobRequiredArtifacts) 
            st += "    Required artifacts: " + s + "\n";
        return st;
        
       /* return ("  Job Index: " + jobIndex + "  Job Name: " + jobName 
               + "Job Creature Index: " + jobCreatureIndex + "  Job Time: " + jobTime);*/
    }// end method toString
}//end class Job
