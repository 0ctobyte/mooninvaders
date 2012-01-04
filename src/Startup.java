package mooninvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import sun.audio.*;
import javax.swing.Timer;

public class Startup extends JPanel {
    
    private ImageIcon image;
    private ImageIcon title;
    private String message;
    private JFrame frame;
    private JProgressBar progressBar;
    private boolean done = false;
    private Timer timer;
    private int ctr = 0;
    
    public Startup( JFrame f ) {
        image = new ImageIcon( getClass().getResource("images/background.jpg") );
        title = new ImageIcon( getClass().getResource( "images/Title.png" ) );
        setLayout( null );
        setBackground( Color.BLACK );
        addKeyListener( new KeyInputHandler() );
        setFocusable( true );
        setPreferredSize( new Dimension( 800, 650 ) );
        frame = f;
        
        progressBar = new JProgressBar( 0, 100 );
        progressBar.setBounds( 325, 300, 182, 25 );
        progressBar.setValue(0);
        progressBar.setStringPainted( true );
        setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
        add( progressBar );
        timer = new Timer( 50, new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                ctr++;
                progressBar.setValue( ctr );
                if ( ctr == 100 ) {
                    timer.stop();
                    done = true;
                    Toolkit.getDefaultToolkit().beep();
                    progressBar.setValue(progressBar.getMinimum());
                    setVisible( false );
                    frame.add( new ModesPanel( frame ) );
                }
            }
        });
        timer.start();
    }
    
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        g.drawImage( image.getImage(), 0, 0, null );
        g.drawImage( title.getImage(), 325, 200, null );
    }
    
    private class KeyInputHandler extends KeyAdapter {
        
        public void keyTyped( KeyEvent e ) {
            if ( e.getKeyChar() == 27 )
                System.exit( 0 );
        }
    }
}
        