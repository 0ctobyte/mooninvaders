package mooninvaders;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import sun.audio.*;

public class HowToPlay extends JPanel {
    
    private JFrame frame;
    private ImageIcon image;
    private ImageIcon title;
    private String message;
    private int ctr = 0;
    private JButton next;
    private JButton previous;
    private JButton back;
    
    public HowToPlay( JFrame f ) {
        image = new ImageIcon( getClass().getResource("images/background.jpg") );
        title = new ImageIcon( getClass().getResource("images/howtoplay.png") );
        setLayout( null );
        setBackground( Color.BLACK );
        addKeyListener( new KeyInputHandler() );
        setFocusable( true );
        setPreferredSize( new Dimension( 800, 650 ) );
        frame = f;
        frame.setCursor( new Cursors( "curLightBlue.png", "LightBlue" ).getCursor() );
        
        back = new JButton( new ImageIcon( getClass().getResource( "images/back_arrow2.png" ) ) );
        back.setRolloverIcon( new ImageIcon( getClass().getResource( "images/back_arrow.png" ) ) );
        back.setCursor( new Cursors( "curPurple.png", "Purple" ).getCursor() );
        back.setBounds( 70, 550, 200, 50 );
        back.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                setVisible( false );
                frame.add( new ModesPanel( frame ) );
                new Sound("sounds/Button.wav").play();
            }
        });
        add( back );
        
        next = new JButton(  new ImageIcon( getClass().getResource( "images/next_arrow2.png" ) ) );
        next.setRolloverIcon( new ImageIcon( getClass().getResource( "images/next_arrow.png" ) ) );
        next.setCursor( new Cursors( "curLightBlue.png", "LightBlue" ).getCursor() );
        next.setBounds( 530, 550, 200, 50 );
        next.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                ctr++;
                add( previous );
                repaint();
                new Sound("sounds/Button.wav").play();
            }
        });
        add( next );
        
        previous = new JButton(  new ImageIcon( getClass().getResource( "images/previous_arrow2.png" ) ) );
        previous.setRolloverIcon( new ImageIcon( getClass().getResource( "images/previous_arrow.png" ) ) );
        previous.setCursor( new Cursors( "curLightRed.png", "LightRed" ).getCursor() );
        previous.setBounds( 280, 550, 200, 50 );
        previous.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                ctr--;
                add( next );
                repaint();
                new Sound("sounds/Button.wav").play();
            }
        });
        add( previous );
    }
    
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        g.drawImage( image.getImage(), 0, 0, null );
        g.drawImage( title.getImage(), 200, 0, null );
        if ( ctr == 0 ) {
            drawInstructions( g );
            remove( previous );
        }
        if ( ctr == 1 ) {
            helpSurvival( g );
        }
        if ( ctr == 2 ) {
            helpClassic( g );
        }
        if ( ctr == 3 ) {
            helpTarget( g );
            remove( next );
        }
    }
    
    public void drawInstructions( Graphics g ) {
        String arrowr = "images/arrow_r.png";
        String arrowl = "images/arrow_l.png";
        String letterm = "images/letter_m.png";
        String letterz = "images/letter_z.png";
        String letterp = "images/letter_p.png";
        String esckey = "images/esc_key.png";
        String ctrlkey = "images/ctrl_key.png";
        ImageIcon keys;
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        
        keys = new ImageIcon( getClass().getResource( arrowr ) );
        g.drawImage( keys.getImage(), 200, 75, null );
        keys = new ImageIcon( getClass().getResource( arrowl ) );
        g.drawImage( keys.getImage(), 150, 75, null );
        message = "Moves your rocketship left or right";
        g.drawString( message, 300, 102 );
        keys = new ImageIcon( getClass().getResource( ctrlkey ) );
        g.drawImage( keys.getImage(), 150, 150, null );
        message = "Fires your rocketship's laser cannon";
        g.drawString( message, 300, 183 );
        keys = new ImageIcon( getClass().getResource( letterz ) );
        g.drawImage( keys.getImage(), 175, 240, null );
        message = "Fires a bomb, press the button again to detonate the bomb";
        g.drawString( message, 300, 267 );
        message = "(Survival Mode Only)";
        g.drawString( message, 300, 280 );
        keys = new ImageIcon( getClass().getResource( letterp ) );
        g.drawImage( keys.getImage(), 175, 315, null );
        message = "Pauses the game, to unpause press the key again";
        g.drawString( message, 300, 342 );
        keys = new ImageIcon( getClass().getResource( letterm ) );
        g.drawImage( keys.getImage(), 175, 390, null );
        message = "Forfeits the current game and returns to the main menu";
        g.drawString( message, 300, 417 );
        keys = new ImageIcon( getClass().getResource( esckey ) );
        g.drawImage( keys.getImage(), 175, 465, null ); 
        message = "Exits the game";
        g.drawString( message, 300, 492 );
    }
    
    public void helpSurvival( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        Font large = new Font( "Dialog", Font.BOLD, 32 );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "SURVIVAL MODE";
        g.drawString( message, 230, 70 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien.gif" ) ).getImage(), 60, 80, null );
        g.drawString( "= 20 pts", 120, 100 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien1.gif" ) ).getImage(), 60, 170, null );
        g.drawString( "= 30 pts", 120, 190 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien2.gif" ) ).getImage(), 60, 260, null );
        g.drawString( "= 40 pts", 120, 280 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien3.gif" ) ).getImage(), 60, 350, null );
        g.drawString( "= 50 pts", 120, 370 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/fireball.gif" ) ).getImage(), 75, 440, null );
        g.drawString( "= 10 pts", 120, 460 );
        message = "Defend the moon against the never ending hordes";
        g.drawString( message, 230, 100 );
        message = "of incoming aliens!";
        g.drawString( message, 230, 120 );
        message = "Goal: Last the longest and obtain the most points,";
        g.drawString( message, 230, 140 );
        message = "you can't win in this mode!";
        g.drawString( message, 230, 160 );
        message = "Collect the powerups to gain unique powers and bonus points,";
        g.drawString( message, 230, 220 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/rapidfire.png" ) ).getImage(), 250, 235, null );
        message = "Laser cannon shoots faster for ten seconds";
        g.drawString( message, 290, 247 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/health.png" ) ).getImage(), 250, 275, null );
        message = "Gives you an extra life to a total of six lives";
        g.drawString( message, 290, 292 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/shield.png" ) ).getImage(), 250, 315, null );
        message = "Fireballs and powerups have no effect for ten seconds";
        g.drawString( message, 290, 333 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/bomb.png" ) ).getImage(), 250, 355, null );
        message = "Adds a bomb to your arsenal to a total of six bombs";
        g.drawString( message, 290, 375 );
        message = "Each powerup collected gives you ten points";
        g.drawString( message, 230, 415 );    
        message = "You start off with only one life";
        g.drawString( message, 230, 435 );
        message = "If the aliens reach the surface of the moon, you lose";
        g.drawString( message, 230, 455 );            
        g.setColor( Color.BLUE );
        g.drawLine( 395, 62, 730, 62 );
        g.drawLine( 730, 62, 730, 470 );
        g.drawLine( 730, 470, 50, 470 );
        g.drawLine( 50, 470, 50, 62 );
        g.drawLine( 50, 62, 225, 62 );
    }
    
    public void helpClassic( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "CLASSIC MODE";
        g.drawString( message, 230, 70 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien.gif" ) ).getImage(), 60, 80, null );
        g.drawString( "= 10 pts", 120, 100 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien1.gif" ) ).getImage(), 60, 170, null );
        g.drawString( "= 20 pts", 120, 190 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien2.gif" ) ).getImage(), 60, 260, null );
        g.drawString( "= 40 pts", 120, 280 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien3.gif" ) ).getImage(), 60, 350, null );
        g.drawString( "= 50 pts", 120, 370 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/Alienship.gif" ) ).getImage(), 60, 440, null );
        g.drawString( "= ???", 120, 460 );
        message = "The Original Round based gameplay. Stop the group";
        g.drawString( message, 230, 100 );
        message = "of aliens from invading the moon!";
        g.drawString( message, 230, 120 );
        message = "Goal: Shoot the aliens and try to last as many";
        g.drawString( message, 230, 140 );
        message = "rounds as you can.";
        g.drawString( message, 230, 160 );
        message = "Destroy the rows of aliens to advance to the next round";
        g.drawString( message, 230, 220 );
        message = "An extra life is rewarded each round,";
        g.drawString( message, 230, 240 );
        message = "Two extra lives are rewarded every three rounds";
        g.drawString( message, 230, 260 );
        message = "At the end of each round you can save your progress";
        g.drawString( message, 230, 280 );
        message = "You start off with three shields to protect you from the fireballs";
        g.drawString( message, 230, 320 );
        message = "The shields disintegrate as it absorbs laser fire and fireballs";
        g.drawString( message, 230, 340 );
        message = "New shields regenerate every nine rounds";
        g.drawString( message, 230, 360 );
        message = "The initial speed and number of aliens increase as rounds progress";
        g.drawString( message, 230, 400 );
        message = "You are able to shoot down the fireballs";
        g.drawString( message, 230, 420 );
        message = "If the moon is invaded, the round starts over from the beginning,";
        g.drawString( message, 230, 440 );
        message = "and all points earned that round are lost";
        g.drawString( message, 230, 460 );
        g.setColor( Color.BLUE );
        g.drawLine( 382, 62, 730, 62 );
        g.drawLine( 730, 62, 730, 470 );
        g.drawLine( 730, 470, 50, 470 );
        g.drawLine( 50, 470, 50, 62 );
        g.drawLine( 50, 62, 225, 62 );
    }
    
    public void helpTarget( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "TARGET PRACTICE";
        g.drawString( message, 230, 70 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien.gif" ) ).getImage(), 60, 80, null );
        g.drawString( "= 5 pts", 120, 100 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien1.gif" ) ).getImage(), 60, 170, null );
        g.drawString( "= 10 pts", 120, 190 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien2.gif" ) ).getImage(), 60, 260, null );
        g.drawString( "= 20 pts", 120, 280 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/alien3.gif" ) ).getImage(), 60, 350, null );
        g.drawString( "= 40 pts", 120, 370 );
        g.drawImage( new ImageIcon( getClass().getResource( "images/Alienship.gif" ) ).getImage(), 60, 440, null );
        g.drawString( "= ???", 120, 460 );
        message = "Test your accuracy and speed in this timed";
        g.drawString( message, 230, 100 );
        message = "target practice!";
        g.drawString( message, 230, 120 );
        message = "Goal: Shoot the aliens as they appear";
        g.drawString( message, 230, 140 );
        message = "randomly across the screen in the fastest time.";
        g.drawString( message, 230, 160 );
        message = "Choose a time limit before starting the game,";
        g.drawString( message, 230, 220 );
        message = "20 seconds is the default time limit";
        g.drawString( message, 230, 240 );
        message = "Aliens will appear randomly on screen and move";
        g.drawString( message, 230, 280 );
        message = "in random direction and at random speeds";
        g.drawString( message, 230, 300 );
        message = "Hit as many targets as you possible can before time runs out";
        g.drawString( message, 230, 320 );
        message = "An alienship may or may not randomly appear across the screen";
        g.drawString( message, 230, 360 );
        message = "If you hit the alienship, you are rewarded bonus points";
        g.drawString( message, 230, 380 );
        message = "The alienship counts as two targets";
        g.drawString( message, 230, 400 );
        message = "You begin with three lives";
        g.drawString( message, 230, 440 );
        message = "Avoid the fireballs; you can't shoot them down in this mode!";
        g.drawString( message, 230, 460 );
        g.setColor( Color.BLUE );
        g.drawLine( 418, 62, 730, 62 );
        g.drawLine( 730, 62, 730, 470 );
        g.drawLine( 730, 470, 50, 470 );
        g.drawLine( 50, 470, 50, 62 );
        g.drawLine( 50, 62, 225, 62 );
    }
    
    private class KeyInputHandler extends KeyAdapter {
        
        public void keyTyped( KeyEvent e ) {
            if ( e.getKeyChar() == 27 )
                System.exit( 0 );
        }
    }
}
