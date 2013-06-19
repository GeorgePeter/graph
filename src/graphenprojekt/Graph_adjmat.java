package graphenprojekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Graph_adjmat {

    private int knzahl      = 0;
    int[][] kante;                  // Kantenmatrix
    public Knoten[] knoten;         // Knotenvektor
    public int toleranz     = 0;    // Radius in dem der Knoten noch als der 
                                    // selbe gilt (radius der Knoten in der GUI)

    Graph_adjmat(int kn) {          // kn maximale Knotenzahl
        
        kante = new int[kn][kn];
        knoten = new Knoten[kn];
        for (int i = 0; i < knzahl; i++) // Initialisieren: alle Kanten-
        {
            for (int j = 0; j < knzahl; j++) {
                kante[i][j] = 0;
            }
        }
    }

    public boolean knotenneu(int x, int y, char Kn) {
        
       
        if(KnotenAnStelle(x,y) == null && !enthält(Kn)){
            knoten[knzahl] = new Knoten(x,y,Kn);
            knzahl++;
            return true;
        }
        return false;
    }
    
    
    public boolean enthält(char a){
       
        for (int i = 0; i < knzahl; i++) // Initialisieren: alle Kanten-
        {
            if(this.knoten[i] != null)
            if(knoten[i].data == a)
                return true;
            
        }
        return false;
    }
    
    public Knoten KnotenAnStelle(int x, int y){
        
        
        for (int i = 0; i < knzahl; i++) // durchlaufe Knoten
        {
            if(this.knoten[i] != null)
            if(x<= this.knoten[i].x + this.toleranz && x >= this.knoten[i].x - this.toleranz &&
                 y<= this.knoten[i].y + this.toleranz && y >= this.knoten[i].y - this.toleranz){
                System.out.println("schallala "+this.knoten[i].x+" "+this.knoten[i].y);
                return this.knoten[i];
            }
        }
        return null;
    }
    

    public int knotennr(char kn) {
        
        int i;
        for (i = 0; i < knzahl; i++) {
            if (knoten[i].data == kn) {
                return i;
            }
        }
        return -1;
    }

    public void kanteneu(char K1, char K2, int wert) {
        
        int n1, n2;
//Prüfen, ob beide Knoten schon existieren !
        n1 = knotennr(K1);
        n2 = knotennr(K2);
        if (n1 >= 0 && n2 >= 0) {
            kante[n1][n2] = wert;
        } else {
            System.out.println("Kanten unmoeglich - Knoten nicht vorhanden");
        }
    }

// weiter Methoden noch zu realisieren
    public void knotenloeschen(char Kn, Graph_adjmat graph) {
        if(graph.enthält(Kn)) {
            for (int i = 0; i < knzahl; i++) {
                if(knoten[i].data == Kn) {
                    knoten[i].data=' ';
                    knoten[i].x = 0;
                    knoten[i].y = 0;
                }    
            }
        }
        else 
            JOptionPane.showMessageDialog(null,"Knoten nicht vorhanden","Info",JOptionPane.WARNING_MESSAGE);    
    }

    public void kanteloeschen(int K1, int K2) {
    }

    public void dateischreiben(String file) {
        BufferedWriter f;
    String s;
 
    try {
      f = new BufferedWriter(
           new FileWriter(file));
      
      f.write(this.knzahl+"");
      f.newLine();
      Knoten cur;
      for (int i = 1; i <= this.knzahl; ++i) {
        cur = this.knoten[i-1];  
        s = cur.x+";"+cur.y+";"+cur.data+";"+cur.markiert;
        f.write(s);
        f.newLine();
        
      }
      int c = 0;
      for (int i = 0; i < this.knzahl; i++) {
        for (int k = 0; k < this.knzahl; k++) {  
            c = this.kante[i][k];  
            s = c+";";
            f.write(s);
        }
        f.newLine();
      }
      
      f.newLine();
      f.close();
    } catch (IOException e) {
      System.out.println("Fehler beim Erstellen der Datei");
    }
    }

    public void dateilesen(String file) {
        BufferedReader f;
	String w;
        String[] splitres; 
        int nzahl;
	try {
            f = new BufferedReader(
	    new FileReader(file));
            w=f.readLine();
            nzahl=Integer.parseInt(w);
            
         System.out.println(""+nzahl);   
        for (int i = 0; i < nzahl; i++) {
            
            
             w = f.readLine();
             System.out.println(""+w);
             splitres = w.split(";");
            System.out.println(""+splitres[2]);   
             this.knotenneu(
                     Integer.parseInt(splitres[0])  //x
                     , Integer.parseInt(splitres[1])//y
                     , splitres[2].charAt(0));      //data
             
		}
            for (int i = 0; i < nzahl; i++) {
                w = f.readLine();
                System.out.println(""+w);
                splitres = w.split(";");
                for (int k = 0; k < nzahl; k++) {
                    this.kante[i][k] = Integer.parseInt(splitres[k]);
                }
                
            }
            System.out.println();
            
		f.close();
		}
                catch (Exception e) {
		  	      System.out.println("Fehler beim Lesen der Datei "+e.getLocalizedMessage());
		}
	}
    
    /**
     * markiert dann die Knoten auf dem kürzesten Weg
     * von Knoten a nach Knoten b
     * @param a
     * @param b 
     */
    public void Dijkstra(Knoten a, Knoten b) {

        //zu betrachten
        ArrayList<Knoten> erreichbare_Knoten = new ArrayList<Knoten>();
        
        //erreichbare Knoten von a:
        for (int i = 0; i < this.knzahl; i++) {
            if(this.knoten[i] != null){
               if(this.kante[this.knotennr(a.data)][i] != 0){
                   erreichbare_Knoten.add(this.knoten[i]);
               }
            }
        }
        int kleinster_index;
        int kleinste_entfernung = Integer.MAX_VALUE;
        for (int i = 0; i < erreichbare_Knoten.size();i++){
            erreichbare_Knoten.get(i).distanz_zum_start 
                    = this.kante[this.knotennr(a.data)][this.knotennr(erreichbare_Knoten.get(i).data)];
            erreichbare_Knoten.get(i).vor = a;
            if(erreichbare_Knoten.get(i).distanz_zum_start < kleinste_entfernung){
                kleinste_entfernung = erreichbare_Knoten.get(i).distanz_zum_start;
                kleinster_index = i;
            }
        }
        
        while(!erreichbare_Knoten.isEmpty()){
            
        }
                
        
    }
    
    
    
    
// fakultativ Breitensuche, Tiefensuche oder Wegsuche
}
