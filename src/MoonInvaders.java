package mooninvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MoonInvaders {  
    
    private JFrame frame;
    
    public MoonInvaders() {
        frame = new JFrame( "Moon Invaders!" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.getContentPane().setBackground( Color.BLACK );
        frame.setBounds( 0, 0, 800, 650 );
        frame.setLocationRelativeTo( null );
        frame.setResizable( false );      
        frame.setCursor( new Cursors( "curLightGray.png", "LightGray" ).getCursor() );       
        frame.add( new Startup( frame ) );
        frame.pack();
        frame.setVisible( true ); 
    } 
    
    public static void main( String[] args ) {
        new MoonInvaders();
    }
}
