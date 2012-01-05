package mooninvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import sun.audio.*;

public class ModesPanel extends JPanel {
    
    private ImageIcon image;
    private ImageIcon title;
    private String message;
    private JFrame frame;
    
    public ModesPanel( JFrame f ) {
        image = new ImageIcon( getClass().getResource("images/background.jpg") );
        title = new ImageIcon( getClass().getResource( "images/Modes.png" ) );
        setLayout( null );
        setBackground( Color.BLACK );
        addKeyListener( new KeyInputHandler() );
        setFocusable( true );
        setPreferredSize( new Dimension( 800, 650 ) );
        frame = f;
        frame.setCursor( new Cursors( "curLightGray.png", "LightGray" ).getCursor() );
        
        JButton survivalMode = new JButton( new ImageIcon( getClass().getResource( "images/playsurvivalmode2.png" ) ) );
        survivalMode.setRolloverIcon( new ImageIcon( getClass().getResource( "images/playsurvivalmode.png" ) ) );
        survivalMode.setCursor( new Cursors( "curBlue.png", "Blue" ).getCursor() );
        survivalMode.setBounds( 70, 100, 245, 50 );
        survivalMode.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                setVisible( false );
                frame.add( new SurvivalBoard( frame ) );
                frame.pack();
                frame.setVisible( false );
                frame.setVisible( true );
                new Sound("sounds/Button.wav").play();
            }
        });  
//        survivalMode.setEnabled( false );
        add( survivalMode );
        
        JButton classicMode = new JButton( new ImageIcon( getClass().getResource( "images/playclassicmode2.png" ) ) );
        classicMode.setRolloverIcon( new ImageIcon( getClass().getResource( "images/playclassicmode.png" ) ) );
        classicMode.setCursor( new Cursors( "curRed.png", "Red" ).getCursor() );
        classicMode.setBounds( 70, 250, 245, 50 );
        classicMode.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                setVisible( false );
                frame.add( new ClassicBoard( frame ) );
                frame.pack();
                frame.setVisible( false );
                frame.setVisible( true );
                new Sound("sounds/Button.wav").play();
            }
        });    
        add( classicMode );
        
        JButton targetPractice = new JButton( new ImageIcon( getClass().getResource( "images/playtargetmode2.png" ) ) );
        targetPractice.setRolloverIcon( new ImageIcon( getClass().getResource( "images/playtargetmode.png" ) ) );
        targetPractice.setCursor( new Cursors( "curGreen.png", "Green" ).getCursor() );
        targetPractice.setBounds( 70, 400, 245, 50 );
        targetPractice.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                setVisible( false );
                frame.add( new TargetBoard( frame ) );
                frame.pack();
                frame.setVisible( false );
                frame.setVisible( true );
                new Sound("sounds/Button.wav").play();
            }
        });    
//        targetPractice.setEnabled( false );
        add( targetPractice );
        
        JButton howToPlay = new JButton( new ImageIcon( getClass().getResource( "images/howtoplaybutton2.png" ) ) );
        howToPlay.setRolloverIcon( new ImageIcon( getClass().getResource( "images/howtoplaybutton.png" ) ) );
        howToPlay.setCursor( new Cursors( "curLightBlue.png", "LightBlue" ).getCursor() );
        howToPlay.setBounds( 30, 550, 245, 50 );
        howToPlay.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                setVisible( false );
                frame.add( new HowToPlay( frame ) );
                frame.pack();
                new Sound("sounds/Button.wav").play();
            }
        });
        add( howToPlay );
        
        JButton exit = new JButton( new ImageIcon( getClass().getResource( "images/exitbutton2.png" ) ) );
        exit.setRolloverIcon( new ImageIcon( getClass().getResource( "images/exitbutton.png" ) ) );
        exit.setCursor( new Cursors( "curOrange.png", "Orange" ).getCursor() );
        exit.setBounds( 525, 550, 245, 50 );
        exit.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                new Sound("sounds/Button.wav").play();
                System.exit(0);
            }
        });
        add( exit );
    }
    
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        g.drawImage( image.getImage(), 0, 0, null );
        g.drawImage( title.getImage(), 300, 0, null );
        drawModeOne( g );
        drawModeTwo( g );
        drawModeThree( g );
    }
    
    public void drawModeOne( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        Font large = new Font( "Dialog", Font.BOLD, 32 );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "SURVIVAL MODE";
        g.drawString( message, 230, 70 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        message = "Defend the moon against the never ending hordes";
        g.drawString( message, 360, 100 );
        message = "of incoming aliens!";
        g.drawString( message, 360, 120 );
        message = "Goal: Last the longest and obtain the most points,";
        g.drawString( message, 360, 140 );
        message = "you can't win in this mode!";
        g.drawString( message, 360, 160 );
        g.setColor( Color.BLUE );
        g.drawLine( 395, 62, 730, 62 );
        g.drawLine( 730, 62, 730, 170 );
        g.drawLine( 730, 170, 50, 170 );
        g.drawLine( 50, 170, 50, 62 );
        g.drawLine( 50, 62, 225, 62 );
    }
    
    public void drawModeTwo( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "CLASSIC MODE";
        g.drawString( message, 230, 220 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        message = "The Original Round based gameplay. Stop the group";
        g.drawString( message, 360, 250 );
        message = "of aliens from invading the moon!";
        g.drawString( message, 360, 270 );
        message = "Goal: Shoot the aliens and try to last as many";
        g.drawString( message, 360, 290 );
        message = "rounds as you can.";
        g.drawString( message, 360, 310 );
        g.setColor( Color.BLUE );
        g.drawLine( 382, 212, 730, 212 );
        g.drawLine( 730, 212, 730, 320 );
        g.drawLine( 730, 320, 50, 320 );
        g.drawLine( 50, 320, 50, 212 );
        g.drawLine( 50, 212, 225, 212 );
    }
    
    public void drawModeThree( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "TARGET PRACTICE";
        g.drawString( message, 230, 370 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        message = "Test your accuracy and speed in this timed";
        g.drawString( message, 360, 400 );
        message = "target practice!";
        g.drawString( message, 360, 420 );
        message = "Goal: Shoot the aliens as they appear";
        g.drawString( message, 360, 440 );
        message = "randomly across the screen in the fastest time.";
        g.drawString( message, 360, 460 );
        g.setColor( Color.BLUE );
        g.drawLine( 418, 362, 730, 362 );
        g.drawLine( 730, 362, 730, 470 );
        g.drawLine( 730, 470, 50, 470 );
        g.drawLine( 50, 470, 50, 362 );
        g.drawLine( 50, 362, 225, 362 );
    }
    
    private class KeyInputHandler extends KeyAdapter {
        
        public void keyTyped( KeyEvent e ) {
            if ( e.getKeyChar() == 27 )
                System.exit( 0 );
        }
    }
}
