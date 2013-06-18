package graphenprojekt;

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
            
            if(knoten[i].data == a)
                return true;
            
        }
        return false;
    }
    
    public Knoten KnotenAnStelle(int x, int y){
        
        for (int i = 0; i < knzahl; i++) // durchlaufe Knoten
        {
            if( x<= this.knoten[i].x + this.toleranz && x >= this.knoten[i].x - this.toleranz &&
                 y<= this.knoten[i].y + this.toleranz && y >= this.knoten[i].y - this.toleranz){
                
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

    public void dateischreiben() {
    }

    public void dateilesen() {
    }
// fakultativ Breitensuche, Tiefensuche oder Wegsuche
}
