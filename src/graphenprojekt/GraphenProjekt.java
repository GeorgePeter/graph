package graphenprojekt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;


import javax.swing.JOptionPane;

/**
 * Hauptklasse für GUI-Aktionen und grafische Ausgabe
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
    JButton kante_entfernen;
    //optionsdialog
    JDialog d = new JDialog();
    Graph_adjmat graph;
    JFileChooser dateiauswahl;
    
    
    /**
     * Spielfeldvariablen
     */
    int maximale_Knotenanzahl = 12;
    int Knotendurchmesser = 30;
    int Knotendurchmesser_markiert = 34;
    int xpadding = 30;
    int ypadding_oben = 100;
    int ypadding = 20;
    int button_breite = 140;
    int button_höhe = 20;
    int font_size = 13;
    char tempknoten;
    
     // Menüleiste
    JMenuBar menueLeiste;
    // Menüleiste Elemente
    JMenuItem beenden;
    JMenuItem datei;
    JMenuItem speichern;
    JMenuItem laden;
    JMenuItem neu;
    
    static Color farbe_knoten_markiert = new Color(100, 200, 100);
    static Color farbe_knoten = Color.BLACK;
    static Color farbe_knoten_zeichen = Color.WHITE;
    static Color farbe_kante = Color.blue;
    static Color farbe_kante_markiert = new Color(100, 200, 100);
    
    
    static final int NICHTS = 0;
    static final int KNOTEN_LEGEN = 1;
    static final int NEUE_KANTE = 2;
    static final int KNOTEN_ENTFERNEN = 3;
    static final int KANTE_ENTFERNEN = 4;
    static final int WEG_ZEICHNEN = 5;
    int action = NICHTS;
    
    
    
    Knoten tmp_start = null;

    /**
     * Zeichnet eine Kante von - bis
     * @param von
     * @param bis
     * @param g 
     */
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
    
    /**
     * verwirft den aktuellen Graph und erzeugt einen neuen
     */
    public void Neu(){
        
        graph = new Graph_adjmat(maximale_Knotenanzahl); 
        graph.toleranz = Knotendurchmesser / 2;
        repaint();
    }

    @Override
    public void paint(Graphics gg) {

        //gebufferte ausgabe
        BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponents(g);
        //Durchlaufe das Spielfeld und Zeichne es
        g.setFont(new Font("Sans", Font.BOLD, font_size));

        g.setColor(Color.GRAY);
        g.drawRect(xpadding, ypadding_oben, this.getWidth()-2*xpadding,this.getHeight()-ypadding_oben-ypadding);
        
        g.setColor(Color.black);
        
        /**
         * *
         * erst die Linien zeichnen, damit sie nicht später die Knoten
         * überdecken...
         */
        for (int k = 0; k < maximale_Knotenanzahl; k++) {

            for (int j = 0; j < maximale_Knotenanzahl; j++) {
                if (graph.kante[k][j] != 0) {

                    g.setColor(farbe_kante);
                    //nur wenn der weg in Dijkstra-algorithmus 
                    //auch beabsichtigt ist wird zwischen benachbarten
                    //und markierten knoten eine Kante markiert
                    if(graph.knoten[k].markiert && graph.knoten[j].markiert
                       && graph.knoten[j].vor.equals(graph.knoten[k]))
                    g.setColor(farbe_kante_markiert);
                    
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
                        
                        g.setColor(farbe_knoten_markiert);
                        
                        g.fillOval(graph.knoten[j].x - Knotendurchmesser_markiert / 2,
                                graph.knoten[j].y - Knotendurchmesser_markiert / 2,
                                Knotendurchmesser_markiert,
                                Knotendurchmesser_markiert);
                    }
                    //standardknoten zeichnen
                    g.setColor(farbe_knoten);
                    g.fillOval(graph.knoten[j].x - Knotendurchmesser / 2, graph.knoten[j].y - Knotendurchmesser / 2, Knotendurchmesser, Knotendurchmesser);
                    //halbwegs in der Mitte platzierte Knotenzeichen
                    g.setColor(farbe_knoten_zeichen);
                    g.drawString("" + graph.knoten[j].data,
                            graph.knoten[j].x - font_size / 4,
                            graph.knoten[j].y + font_size / 2);

                }
            }



        }
        //Doppelpufferung
        Graphics2D g2dComponent = (Graphics2D) gg;
        
        g2dComponent.drawImage(bufferedImage, null, 0, 0);
        g.clearRect(xpadding, ypadding_oben, this.getWidth()-2*xpadding, this.getHeight()-ypadding-ypadding_oben);

    }

    GraphenProjekt() {

        //Angaben zum Fenster		
        setTitle("Graph");
        action = NICHTS;
        graph = new Graph_adjmat(maximale_Knotenanzahl);
        graph.toleranz = Knotendurchmesser / 2;

        setLocation(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        dateiauswahl = new JFileChooser();
        setResizable(true);
        this.setSize(600, 600);
        this.setMinimumSize(new Dimension(3*(xpadding+button_breite)+xpadding,300));
        menueLeiste = new JMenuBar();
        addMouseListener(new CMeinMausAdapter());

         
        // neuer Knoten
        neuer_knoten = new JButton("neuer Knoten");
        neuer_knoten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = KNOTEN_LEGEN;
                repaint();
                // Auswahldialog
                
                String tempknotenstring = (String) JOptionPane.showInputDialog(null,
                        "Zeichen (nur eins)",
                        "neuen knoten wählen",
                        JOptionPane.QUESTION_MESSAGE,
                        null, null,"");
                if(tempknotenstring != null)
                    tempknoten = tempknotenstring.charAt(0);
                else{
                    action = NICHTS;
                }
            }
        });
        neuer_knoten.setBounds(xpadding, 0, button_breite, button_höhe);
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
        neue_kante.setBounds(button_breite+xpadding*2, 0, button_breite, button_höhe);
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
        weg_zeichnen.setBounds((button_breite+xpadding)*2+xpadding , 0, button_breite, button_höhe);
        this.add(weg_zeichnen);
        
        
        // lösche Knoten
        stein_entfernen = new JButton("lösche Knoten");
        stein_entfernen.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent arg0) {
                 action = KNOTEN_ENTFERNEN;
                 repaint();
                 //Auswahldialog
                 
             }
        });
        stein_entfernen.setBounds(xpadding, button_höhe+3, button_breite, button_höhe);
        this.add(stein_entfernen);
        
        // Kante entfernen
        kante_entfernen = new JButton("Kante entfernen");
        kante_entfernen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = KANTE_ENTFERNEN;
                repaint();
            }
        });
        kante_entfernen.setBounds((button_breite)+xpadding*2, button_höhe+3, button_breite, button_höhe);
        this.add(kante_entfernen);
        
       

        // Menüelemente erzeugen
        datei = new JMenu("Datei");
        
        neu = new JMenuItem("Neu");

        neu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Neu();
            }
        });
        
        
        beenden = new JMenuItem("Beenden");

        beenden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });



        speichern = new JMenuItem("Speichern");
        speichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                dateiauswahl.setDialogType(JFileChooser.SAVE_DIALOG);
                int rueckgabeWert = dateiauswahl.showSaveDialog(null);
                
                /* Abfrage, ob auf "Öffnen" geklickt wurde */
                if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
                {
                     // Ausgabe der ausgewaehlten Datei
                    graph.dateischreiben(dateiauswahl.getSelectedFile().getPath());
                }
                
            }
          
        });

        laden = new JMenuItem("Laden");
        laden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                            
               
                int rueckgabeWert = dateiauswahl.showOpenDialog(null);
                dateiauswahl.setDialogType(JFileChooser.OPEN_DIALOG);
                /* Abfrage, ob auf "Öffnen" geklickt wurde */
                if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
                {
                     // Ausgabe der ausgewaehlten Datei
                    graph = new Graph_adjmat(maximale_Knotenanzahl); 
                    graph.toleranz = Knotendurchmesser / 2;
                    graph.dateilesen(dateiauswahl.getSelectedFile().getPath());
                }
                
              
               repaint();
            }

           
        });

        // Menüelemente hinzufügen
        menueLeiste.add(datei);
        
        // Untermenüelemente hinzufügen
        datei.add(neu);
        datei.add(laden);
        datei.add(speichern);
        datei.add(beenden);
        
        this.add(menueLeiste);
        this.setJMenuBar(menueLeiste);

        setVisible(true);
    }

    /**
     * für Click-Events auf das Graphenfeld
     */
    class CMeinMausAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            
            graph.AlleEntmarkieren();
            int x, y;
            x = e.getX();
            y = e.getY();
            Knoten tst;
            
            //damit die Knoten auch auf das Feld passen überprüfe deren Position
            if(x > xpadding+Knotendurchmesser/2 && y > ypadding_oben+Knotendurchmesser/2 &&
                    x < e.getComponent().getWidth() -  xpadding-Knotendurchmesser/2 &&
                    y < e.getComponent().getHeight() - ypadding-Knotendurchmesser/2
                    && action != NICHTS){
            switch (action) {

                case NEUE_KANTE:
                     
                     tst = graph.KnotenAnStelle(x, y);

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
                case KNOTEN_ENTFERNEN:
                    tst = graph.KnotenAnStelle(x, y);
                    if(tst != null){
                        graph.knotenloeschen(tst.data);
                    }
                    action = NICHTS;
                    break;
                case KNOTEN_LEGEN:
                    if (graph.knotenneu(x, y, tempknoten)) {

                        action = NICHTS;
                        tempknoten = ' ';
                    }
                    break;
                case WEG_ZEICHNEN:
                    tst = graph.KnotenAnStelle(x, y);

                    if (tst != null) {

                        if (tmp_start == null) {
                            tst.markiert = !tst.markiert;
                            tmp_start = tst;
                        } else {

                            if (!tmp_start.equals(tst)) {
                               
                               graph.Dijkstra(tmp_start, tst);
                               repaint();
                            }
                           
                            tmp_start = null;
                            action = NICHTS;

                        }
                    }
                    break;
                case KANTE_ENTFERNEN:
                    tst = graph.KnotenAnStelle(x, y);
                    if (tst != null) {
                        if (tmp_start == null) {
                            tst.markiert = !tst.markiert;
                            tmp_start = tst;
                        } 
                        else { 
                            if (!tmp_start.equals(tst)) {
                                graph.kanteloeschen(tmp_start.data, tst.data);
                            }
                            tst.markiert = false;
                            tmp_start.markiert = false;
                            tmp_start = null;
                            action = NICHTS;
                        }
                    }
                    break;

            }
            
            }repaint();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new GraphenProjekt();
    }
}
