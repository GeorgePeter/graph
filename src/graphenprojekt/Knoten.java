/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphenprojekt;

/**
 *
 * @author george
 */
public class Knoten {
    
    public int x;
    public int y;
    public char data; 
    boolean markiert = false;
    
    public Knoten(int a, int b, char d){
        
        this.x    = a;
        this.y    = b;
        this.data = d;
    }
}
