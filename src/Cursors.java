package mooninvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Cursors {
    
    private Cursor cursor;
    
    public Cursors( String filename, String name ) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = new ImageIcon( getClass().getResource( "cursors/" + filename ) ).getImage();
        Point hotSpot = new Point(0,0);
        cursor = toolkit.createCustomCursor(image, hotSpot, name);
    }
    
    public Cursor getCursor() {
        return cursor;
    }
}