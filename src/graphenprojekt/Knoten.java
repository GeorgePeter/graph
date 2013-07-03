/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphenprojekt;

/**
 * Klasse für die einzelnen Knoten des Graphen
 * @author george, Sascha
 */
public class Knoten {
    
    public int x;                   //x,y-Koordinaten auf der Oberfläche
    public int y;
    
    public char data;               //Zeichen des Knotens
    public Knoten vor;              //Dijkstra Vorgänger
    public int distanz_zum_start;   //Dijkstra Distanz zum Start
    
    boolean markiert = false;       //falls true wird eine Markierung gezeichnet
    
    public Knoten(int a, int b, char d){
        
        this.x    = a;
        this.y    = b;
        this.data = d;
    }
}
