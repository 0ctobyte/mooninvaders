package mooninvaders;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Shields extends Vector<ShieldFragment> {
    
    ImageIcon image;
    ShieldFragment sf;
    String shieldfragment = "images/shieldfragment.png";
    int x;
    int y;
    
    public Shields( int x, int y ) {        
        for ( int a = 0; a < 5; a++ ) {
            sf = new ShieldFragment( x, y + 14 * a );
            sf.setImage( new ImageIcon( this.getClass().getResource( shieldfragment ) ).getImage() );
            add( sf );
        }
        
        for ( int a = 1; a < 5; a++ ) {
            sf = new ShieldFragment( x + 14 * a, y - 14 );
            sf.setImage( new ImageIcon( this.getClass().getResource( shieldfragment ) ).getImage() );
            add( sf );
        }
        
        for ( int a = 1; a < 5; a++ ) {
            sf = new ShieldFragment( x + 14 * a, y );
            sf.setImage( new ImageIcon( this.getClass().getResource( shieldfragment ) ).getImage() );
            add( sf );
        }
        
        for ( int a = 1; a < 5; a++ ) {
            sf = new ShieldFragment( x + 14 * a, y + 14 ); 
            sf.setImage( new ImageIcon( this.getClass().getResource( shieldfragment ) ).getImage() );
            add( sf );
        }
        
        for ( int a = 1; a < 5; a++ ) {
            sf = new ShieldFragment( x + 14 * a, y + 28 ); 
            sf.setImage( new ImageIcon( this.getClass().getResource( shieldfragment ) ).getImage() );
            add( sf );
        }
        
        for ( int a = 1; a < 5; a++ ) {
            sf = new ShieldFragment( x + 14 * a, y + 42 ); 
            sf.setImage( new ImageIcon( this.getClass().getResource( shieldfragment ) ).getImage() );
            add( sf );
        }
        
        for ( int a = 0; a < 5; a++ ) {
            sf = new ShieldFragment( x + 70, y + 14 * a ); 
            sf.setImage( new ImageIcon( this.getClass().getResource( shieldfragment ) ).getImage() );
            add( sf );
        }
    }
}

class ShieldFragment extends Images implements Serializable {
    
    public ShieldFragment( int x, int y ) {
        ImageIcon image = new ImageIcon( this.getClass().getResource( "images/shieldfragment.png" ) );
        setImage( image.getImage() );        
        setX( x );
        setY( y );
    }
}
    
