package graphenprojekt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Graph_adjmat {

    private int knzahl = 7;
    int[][] kante;                  // Kantenmatrix
    public Knoten[] knoten;         // Knotenvektor
    public int toleranz = 0;    // Radius in dem der Knoten noch als der 
    // selbe gilt (radius der Knoten in der GUI)

    Graph_adjmat(int kn) {          // kn maximale Knotenzahl

        kante = new int[kn][kn];
        knoten = new Knoten[kn];
        for (int i = 0; i < knzahl; i++) // Initialisieren: alle Kanten-
        {
            for (int j = 0; j < knzahl; j++) {
                kante[i][j] = 0;
                knoten[i]=null;
            }
        }
    }

    public boolean knotenneu(int x, int y, char Kn) {

        if (KnotenAnStelle(x, y) == null) {
            if (!enthält(Kn)) {
                for(int i = 0; i<=knzahl;i++) {
                    if(knoten[i] == null) {
                        knoten[i] = new Knoten(x, y, Kn);
                        return true;   
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Knoten mit diesem Zeichen bereits vorhanden", "Info", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Knoten zu nah an einem bereits existierenden Knoten", "Info", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public boolean enthält(char a) {

        for (int i = 0; i < knzahl; i++) // Initialisieren: alle Kanten-
        {
            if (this.knoten[i] != null) {
                if (knoten[i].data == a) {
                    return true;
                }
            }

        }
        return false;
    }

    public Knoten KnotenAnStelle(int x, int y) {


        for (int i = 0; i < knzahl; i++) // durchlaufe Knoten
        {
            if (this.knoten[i] != null) {
                if (x <= this.knoten[i].x + this.toleranz && x >= this.knoten[i].x - this.toleranz
                        && y <= this.knoten[i].y + this.toleranz && y >= this.knoten[i].y - this.toleranz) {
                    return this.knoten[i];
                }
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

    /**
     * erzeugt im Kantenarray einen neuen Eintrag Kante[K1][K2] = wert:
     *
     * @param K1
     * @param K2
     * @param wert
     */
    public void kanteneu(char K1, char K2, int wert) {

        int n1, n2;

        n1 = knotennr(K1);
        n2 = knotennr(K2);
        if (n1 >= 0 && n2 >= 0) {
            kante[n1][n2] = wert;
        }
    }

    /**
     * TODO: beim löschen wärs besser zu sagen knoten[i] = null == erledigt
     *
     * @param Kn
     * @param graph
     */
    public void knotenloeschen(char Kn, Graph_adjmat graph) {
        if (graph.enthält(Kn)) {
            for (int i = 0; i < knzahl; i++) {
                if (knoten[i] != null && knoten[i].data == Kn) {
                    knoten[i]= null;
                    for (int t = 0; t < knzahl; t++) {
                        kante[i][t] = 0;
                        kante[t][i] = 0;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Knoten nicht vorhanden", "Info", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void kanteloeschen(char K1, char K2) {
        int n1, n2;
        //Prüfen, ob beide Knoten existieren !
        n1 = knotennr(K1);
        n2 = knotennr(K2);
        if (n1 >= 0 && n2 >= 0) {
            kante[n1][n2] = 0;
            kante[n2][n1] = 0;
        } else {
            JOptionPane.showMessageDialog(null, "Fehler! Knoten nicht vorhanden", "Info", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void dateischreiben(String file) {
        BufferedWriter f;
        String s;

        try {
            f = new BufferedWriter(
                    new FileWriter(file));

            f.write(this.knzahl + "");
            f.newLine();
            Knoten cur;
            for (int i = 1; i <= this.knzahl; ++i) {
                if(knoten[i-1] != null) {
                    cur = this.knoten[i - 1];
                    s = cur.x + ";" + cur.y + ";" + cur.data + ";" + cur.markiert;
                    f.write(s);
                    f.newLine();
                }

            }
            int c = 0;
            for (int i = 0; i < this.knzahl; i++) {
                for (int k = 0; k < this.knzahl; k++) {
                    c = this.kante[i][k];
                    s = c + ";";
                    f.write(s);
                }
                f.newLine();
            }

            f.newLine();
            f.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim erstellen der Datei", "Info", JOptionPane.WARNING_MESSAGE);
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
            w = f.readLine();
            nzahl = Integer.parseInt(w);

            System.out.println("" + nzahl);
            for (int i = 0; i < nzahl; i++) {


                w = f.readLine();

                splitres = w.split(";");

                this.knotenneu(
                        Integer.parseInt(splitres[0]) //x
                        , Integer.parseInt(splitres[1])//y
                        , splitres[2].charAt(0));      //data

            }
            for (int i = 0; i < nzahl; i++) {
                w = f.readLine();

                splitres = w.split(";");
                for (int k = 0; k < nzahl; k++) {
                    this.kante[i][k] = Integer.parseInt(splitres[k]);
                }

            }


            f.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler beim lesen der Datei", "Info", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void AlleEntmarkieren() {
        for (int i = 0; i < this.knzahl; i++) {
            if(knoten[i] != null){
                this.knoten[i].markiert = false;
            }      
        }

    }

    public void AlleStatusZurücksetzen() {

        for (int i = 0; i < this.knzahl; i++) {
            if(knoten[i] != null) {
                this.knoten[i].markiert = false;
                this.knoten[i].vor = null;
                this.knoten[i].distanz_zum_start = Integer.MAX_VALUE;
            }
        }

    }

    /**
     * markiert dann die Knoten auf dem kürzesten Weg von Knoten a nach Knoten b
     *
     * @param a
     * @param b
     */
    public void Dijkstra(Knoten a, Knoten b) {


        this.AlleStatusZurücksetzen();
        //zu betrachten
        ArrayList<Knoten> erreichbare_Knoten = new ArrayList<Knoten>();
        ArrayList<Knoten> besuchte_Knoten = new ArrayList<Knoten>();
        Knoten current;

        besuchte_Knoten.add(a);
        a.vor = a;
        //erreichbare Knoten von a:

        /**
         * initialisieren mit Knoten a
         */
        for (int i = 0; i < this.knzahl; i++) {
            if (this.knoten[i] != null && !((this.knoten[i].data + "").equals(""))) {
                if (this.kante[this.knotennr(a.data)][i] != 0) {
                    erreichbare_Knoten.add(this.knoten[i]);

                }
            }
        }
        int kleinster_index = 0;
        int kleinste_entfernung = Integer.MAX_VALUE;
        for (int i = 0; i < erreichbare_Knoten.size(); i++) {
            erreichbare_Knoten.get(i).distanz_zum_start = this.kante[this.knotennr(a.data)][this.knotennr(erreichbare_Knoten.get(i).data)];
            erreichbare_Knoten.get(i).vor = a;
            if (erreichbare_Knoten.get(i).distanz_zum_start < kleinste_entfernung && !erreichbare_Knoten.get(i).equals(a)) {
                kleinste_entfernung = erreichbare_Knoten.get(i).distanz_zum_start;
                kleinster_index = i;
            }
        }

        if (!erreichbare_Knoten.isEmpty()) {
            current = erreichbare_Knoten.get(kleinster_index);


            /**
             * alle erreichbaren Knoten durchlaufen
             */
            while (!erreichbare_Knoten.isEmpty()) {

                //aktuellen knoten aus erreichbaren entfernen
                erreichbare_Knoten.remove(current);
                if (current.equals(b)) {
                    break;
                }
                //aktuellen knoten zu den besuchten hinzufügen
                besuchte_Knoten.add(current);
                //erreichbare Knoten von current:
                for (int i = 0; i < this.knzahl; i++) {
                    if (this.knoten[i] != null) {
                        if (this.kante[this.knotennr(current.data)][i] != 0) {
                            if (!besuchte_Knoten.contains(this.knoten[i]) && !erreichbare_Knoten.contains(this.knoten[i])) {
                                erreichbare_Knoten.add(this.knoten[i]);
                                this.knoten[i].vor = current;
                                this.knoten[i].distanz_zum_start = current.distanz_zum_start + this.kante[this.knotennr(current.data)][i];
                            }
                        }
                    }
                }

                kleinster_index = 0;
                kleinste_entfernung = Integer.MAX_VALUE;
                int entfernung = Integer.MAX_VALUE;

                for (int i = 0; i < erreichbare_Knoten.size(); i++) {

                    entfernung = Integer.MAX_VALUE;
                    if (this.kante[this.knotennr(current.data)][this.knotennr(erreichbare_Knoten.get(i).data)] > 0) {
                        entfernung = current.distanz_zum_start + this.kante[this.knotennr(current.data)][this.knotennr(erreichbare_Knoten.get(i).data)];
                    }

                    if (entfernung < erreichbare_Knoten.get(i).distanz_zum_start) {
                        erreichbare_Knoten.get(i).distanz_zum_start = entfernung;
                        erreichbare_Knoten.get(i).vor = a;
                    }
                    if (erreichbare_Knoten.get(i).distanz_zum_start < kleinste_entfernung) {
                        kleinste_entfernung = erreichbare_Knoten.get(i).distanz_zum_start;
                        kleinster_index = i;
                    }
                }
                if (erreichbare_Knoten.size() > 0) {
                    current = erreichbare_Knoten.get(kleinster_index);
                }



            }
            //jetzt backtracken
            if (current.equals(b)) {

                while (!current.equals(a)) {
                    current.markiert = true;

                    current = current.vor;
                }
                a.markiert = true;
            } else {
                JOptionPane.showMessageDialog(null, "Keine Verbindung", "Info", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Keine Verbindung", "Info", JOptionPane.WARNING_MESSAGE);
        }
    }
// fakultativ Breitensuche, Tiefensuche oder Wegsuche
}
