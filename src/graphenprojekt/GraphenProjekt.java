/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphenprojekt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//schallala
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JDialog;


import javax.swing.JOptionPane;

/**
 *
 * @author george,Sascha
 */
public class GraphenProjekt extends JFrame {

    /**
     * Fenstervariablen
     */
    JButton neuer_knoten;
    JButton neue_kante;
    JButton weg_zeichnen;
    JButton stein_entfernen;
    //optionsdialog
    JDialog d = new JDialog();
    Graph_adjmat graph;
    /**
     * Spielfeldvariablen
     */
    int maximale_Knotenanzahl = 7;
    int Knotendurchmesser = 30;
    int Knotendurchmesser_markiert = 34;
    int xpadding = 70;
    int ypadding = 50;
    int button_breite = 140;
    int button_höhe = 20;
    int font_size = 13;
    char tempknoten;
    static final int NICHTS = 0;
    static final int STEIN_LEGEN = 1;
    static final int NEUE_KANTE = 2;
    static final int STEIN_ENTFERNEN = 3;
    static final int KANTE_ENTFERNEN = 4;
    static final int WEG_ZEICHNEN = 5;
    int action = NICHTS;
    Knoten tmp_start = null;

    public ArrayList<Knoten> Dijkstra(Knoten a, Knoten b) {

        
        Knoten current = a;
        
        //zu betrachten
        ArrayList<Knoten> Kb    = new ArrayList<Knoten>(); 
        ArrayList<Knoten> route = new ArrayList<Knoten>(); 
        int[] dist = new int[maximale_Knotenanzahl]; 
        
        //alle knoten außer a müssen betrachtet werden
        for (int i = 0; i < maximale_Knotenanzahl; i++) {
            if(graph.knoten[i] != a){
                Kb.add(graph.knoten[i]);
                if (graph.kante[graph.knotennr(a.data)][i] != 0) {
                    //verbindung besteht
                    route.add(graph.knoten[i]);   
                    dist[i] = graph.kante[graph.knotennr(a.data)][i];
                }else{
                    route.add(null);    
                    dist[i] = -1;
                }
            }
        }
        while(!Kb.isEmpty()){
            
            int u=0;
            for (int i = 0; i < maximale_Knotenanzahl; i++) {
                
                if(dist[i] < dist[u] && 
                        Kb.contains(graph.knoten[i])){
                    if(dist[u] == -1){
                        //fehler
                    }
                    u = i;
                    Kb.remove(graph.knoten[i]);
                }
            }
                 for (int k = 0; k < maximale_Knotenanzahl; k++) {
                     if (graph.kante[u][k] != 0) {
                         int L = dist[u]+ graph.kante[u][k];
                         if(L < dist[k]){
                             route.set(k, graph.knoten[u]);
                             dist[k] = L;
                         }
                     }
                 }
                    
            }
            
            
        return route;
       
    }

    public void KanteZeichnen(Knoten von, Knoten bis, Graphics g) {

        int x = (bis.x - von.x);
        int y = bis.y - von.y;
        //nx, ny = normalisierter vektor in pfeilrichtung
        double nx = x / Math.sqrt(x * x + y * y);
        double ny = y / Math.sqrt(x * x + y * y);

        int vonX = von.x;
        int vonY = von.y;
        //damit unser Pfeil auf den Rand vom nächsten Knoten zeigt
        //muss der Steinradius in x und y richtung korrekt abgezogen
        //werden. also vom Mittelpunkt des  Zielknotens Knotendurchmesser                     
        //in Richtung des Anfangsknotens. 
        int bisX = (int) (bis.x - nx * Knotendurchmesser / 2);
        int bisY = (int) (bis.y - ny * Knotendurchmesser / 2);

        double l = 10.0; // Pfeilspitzenlänge 
        double a = Math.PI / 4 - Math.atan2((bisY - vonY), (bisX - vonX));
        double c = Math.cos(a) * l;
        double s = Math.sin(a) * l;
        g.drawLine(bisX, bisY, (int) (bisX - s), (int) (bisY - c));
        g.drawLine(bisX, bisY, (int) (bisX - c), (int) (bisY + s));
        g.drawLine(von.x, von.y, bisX, bisY);

    }

    @Override
    public void paint(Graphics gg) {

        //gebufferte ausgabe
        BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        super.paintComponents(g);
        //Durchlaufe das Spielfeld und Zeichne es
        g.setFont(new Font("Sans", Font.BOLD, font_size));


        g.setColor(Color.black);
        ArrayList<Knoten> markieren = new ArrayList<Knoten>();
       
        /**
         * *
         * erst die Linien zeichnen, damit sie nicht später die Knoten
         * überdecken...
         */
        for (int k = 0; k < maximale_Knotenanzahl; k++) {

            for (int j = 0; j < maximale_Knotenanzahl; j++) {
                if (graph.kante[k][j] != 0) {

                    g.setColor(Color.blue);
                    KanteZeichnen(graph.knoten[k], graph.knoten[j], g);

                }
            }
            /**
             * jetzt die eigentlichen Knoten zeichnen
             */
            for (int j = 0; j < maximale_Knotenanzahl; j++) {

                if (graph.knoten[j] != null) {

                    //markierte Knoten zeichnen
                    if (graph.knoten[j].markiert) {
                        g.setColor(Color.yellow);
                        g.fillOval(graph.knoten[j].x - Knotendurchmesser_markiert / 2,
                                graph.knoten[j].y - Knotendurchmesser_markiert / 2,
                                Knotendurchmesser_markiert,
                                Knotendurchmesser_markiert);
                    }
                    //standardknoten zeichnen
                    g.setColor(Color.black);
                    g.fillOval(graph.knoten[j].x - Knotendurchmesser / 2, graph.knoten[j].y - Knotendurchmesser / 2, Knotendurchmesser, Knotendurchmesser);
                    g.setColor(Color.white);
                    g.drawString("" + graph.knoten[j].data,
                            graph.knoten[j].x - font_size / 4,
                            graph.knoten[j].y + font_size / 2);

                }
            }



        }
        //Doppelpufferung
        Graphics2D g2dComponent = (Graphics2D) gg;
        g2dComponent.drawImage(bufferedImage, null, 0, 0);
        g.clearRect(xpadding, ypadding, this.getWidth(), this.getHeight());

    }

    GraphenProjekt() {

        //	Angaben zum Fenster		
        setTitle("Graph");

        graph = new Graph_adjmat(maximale_Knotenanzahl);
        graph.toleranz = Knotendurchmesser / 2;

        setLocation(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        setVisible(true);
        setResizable(true);
        this.setSize(600, 600);


        // neuer Knoten
        neuer_knoten = new JButton("neuer Knoten");
        neuer_knoten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = STEIN_LEGEN;
                repaint();
                // Auswahldialog
                String tempknotenstring = (String) JOptionPane.showInputDialog(null,
                        "Zeichen",
                        "neuen knoten wählen",
                        JOptionPane.QUESTION_MESSAGE,
                        null, null,
                        maximale_Knotenanzahl + "");
                tempknoten = tempknotenstring.charAt(0);
            }
        });
        neuer_knoten.setBounds(0, 0, button_breite, button_höhe);
        this.add(neuer_knoten);

        // neue Kante
        neue_kante = new JButton("neue Kante");
        neue_kante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = NEUE_KANTE;
                repaint();
            }
        });
        neue_kante.setBounds(button_breite + 10, 0, button_breite, button_höhe);
        this.add(neue_kante);

        
        // neue Kante
        weg_zeichnen = new JButton("zeige Weg");
        weg_zeichnen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = WEG_ZEICHNEN;
                repaint();
            }
        });
        weg_zeichnen.setBounds(button_breite*2 + 10, 0, button_breite, button_höhe);
        this.add(weg_zeichnen);
        
        
        // lösche Knoten
        stein_entfernen = new JButton("lösche Knoten");
        stein_entfernen.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent arg0) {
                 action = STEIN_ENTFERNEN;
                 repaint();
                 //Auswahldialog
                 String tempknotenstring = (String) JOptionPane.showInputDialog(null,
                        "Zeichen",
                        "neuen knoten wählen",
                        JOptionPane.QUESTION_MESSAGE,
                        null, null,
                        maximale_Knotenanzahl + "");
                tempknoten = tempknotenstring.charAt(0);
             }
        });
        stein_entfernen.setBounds(button_breite*3 + 10, 0, button_breite, button_höhe);
        this.add(stein_entfernen);
        addMouseListener(new CMeinMausAdapter());
    }

    /**
     * für Click-Events auf das Graphenfeld
     */
    class CMeinMausAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x, y;
            x = e.getX();
            y = e.getY();

            switch (action) {

                case NEUE_KANTE:

                    Knoten tst = graph.KnotenAnStelle(x, y);

                    if (tst != null) {

                        if (tmp_start == null) {
                            tst.markiert = !tst.markiert;
                            tmp_start = tst;
                        } else {

                            int Abstand =
                                    (int) Math.sqrt(Math.pow(tmp_start.x - tst.x, 2) + Math.pow(tmp_start.y - tst.y, 2));
                            if (!tmp_start.equals(tst)) {
                                graph.kanteneu(tmp_start.data, tst.data, Abstand);
                            }
                            tst.markiert = false;
                            tmp_start.markiert = false;
                            tmp_start = null;
                            action = NICHTS;

                        }
                    }
                    break;
                case STEIN_ENTFERNEN:
                    graph.knotenloeschen(tempknoten, graph);
                    action = NICHTS;
                    break;
                case STEIN_LEGEN:
                    if (graph.knotenneu(x, y, tempknoten)) {

                        action = NICHTS;
                        tempknoten = ' ';
                    }
                    break;
                case WEG_ZEICHNEN:
                    break;

            }
            repaint();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new GraphenProjekt();
    }
}
