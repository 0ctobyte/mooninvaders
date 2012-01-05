package mooninvaders;

import javax.swing.Timer;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import sun.audio.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

public class ClassicBoard extends JPanel {    
    
    private ImageIcon image;
    private ImageIcon title;
    private RocketShip rocketship;
    private AlienShip alienship;
    private Flame flame;
    private Alien alien;
    private Shields shield[];
    private ShieldFragment shieldfrag;
    private Vector aliens;
    private Enumeration vector;
    private LaserBeam laser;
    private BackgroundImage background;
    private Timer timer;
    private JFrame frame;
    private File file;
    private JButton back;
    private JButton load;
    private JButton save;
    private JFileChooser chooser;
    
    private int alienX = 160;
    private int alienY = 25;
    private int laserX;
    private int laserY;
    private int direction = -1;
    private int alienDeaths = 0;
    private int numAliens = 30;
    private int scoreCount = 0;
    private int INITIAL_SIZE = 61;
    private int ctr = 1;
    private int lives = 3;
    private int speedIncrease = 0;
    private int counter = 0;
    private int chance =  2001;
    private int rows = 3;
    private int course = 0;
    private int speed = 0;
    private int multiplier;
    private int initialScore = 0;
    private int pointsCtr;
    private int alienshipX = 0;
    private int round;
    private int ctrShields = 0;
    private int ctr4 = 0;
    private boolean gameRunning = false;
    private boolean gameStarted = false;
    private boolean gamePaused = false;
    private boolean roundOver = false;
    private boolean drawBonus = false;
    private boolean ctrlDown = false;
    private boolean renewShields = true;
    private boolean shields = false;
    private String score = " ";
    private String message;
    private String explosion = "images/explosion.gif";
    
    public ClassicBoard( JFrame f ) {
        background = new BackgroundImage();
        title = new ImageIcon( getClass().getResource( "images/classicmode.png" ) );
        addKeyListener( new KeyInputHandler() );
        setFocusable( true );
        setPreferredSize( new Dimension( 800, 650 ) );
        setLayout( new FlowLayout() );
        setOpaque( false );
        setDoubleBuffered( true );
        frame = f;
        frame.setCursor( new Cursors( "curRed.png", "Red" ).getCursor() );
		frame.setVisible(false);
		frame.setVisible(true);
        
        back = new JButton( new ImageIcon( getClass().getResource( "images/back_arrow2.png" ) ) );
        back.setRolloverIcon( new ImageIcon( getClass().getResource( "images/back_arrow.png" ) ) );
        back.setCursor( new Cursors( "curPurple.png", "Purple" ).getCursor() );
        back.setBounds( 70, 550, 200, 50 );
        back.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                frame.remove( load );
                frame.remove( back );
                frame.remove( save );
                setVisible( false );
                frame.add( new ModesPanel( frame ) );
                new Sound("sounds/Button.wav").play();
            }
        });
        frame.add( back );
        back.setVisible( false );
        
        load = new JButton( new ImageIcon( getClass().getResource( "images/loadbutton2.png" ) ) );
        load.setRolloverIcon( new ImageIcon( getClass().getResource( "images/loadbutton.png" ) ) );
        load.setCursor( new Cursors( "curYellow.png", "Yellow" ).getCursor() );
        load.setBounds( 530, 550, 200, 50 );
        load.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                new Sound("sounds/Button.wav").play();
                chooser = new JFileChooser( new File( "." ) );
                chooser.setDialogTitle( "Load Game" );
                chooser.setAcceptAllFileFilterUsed( false );
                chooser.setMultiSelectionEnabled( false );
                chooser.addChoosableFileFilter( new FileNameExtensionFilter( "MoonInvaders Classic Mode Save File (*.mnc)", "mnc" ) );
                if ( chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ) {
                    file = chooser.getSelectedFile();
                    try {
                        FileInputStream fis = new FileInputStream( file );
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        DataInputStream dis = new DataInputStream(bis);
                        
                        direction = dis.readInt();
                        numAliens = dis.readInt();
                        lives = dis.readInt();
                        rows = dis.readInt();
                        chance = dis.readInt();
                        speedIncrease = dis.readInt();
                        counter = dis.readInt();
                        INITIAL_SIZE = dis.readInt();
                        initialScore = dis.readInt();
                        scoreCount = dis.readInt();
                        ctr = dis.readInt();
                        
                        bis.close();
                        dis.close();                    
                    } catch (FileNotFoundException f) {
                    } catch (IOException f) {}
                    setVisible( false );
                    setVisible( true );
                    load.setVisible( false );
                    save.setVisible( false );
                    back.setVisible( false );
                    alienDeaths = 0;
                    shield = new Shields[3];
                    shield[0] = new Shields( 80, 400 );
                    shield[1] = new Shields( 358, 400 );
                    shield[2] = new Shields( 636, 400 );
                    gameRunning = true;
                    gameStarted = true;
                    gamePaused = false;
                    roundOver = true;
                } 
                chooser = null;
                requestFocusInWindow();
            }
        });
        frame.add( load );
        load.setVisible( false );
        
        save = new JButton( new ImageIcon( getClass().getResource( "images/savebutton2.png" ) ) );
        save.setRolloverIcon( new ImageIcon( getClass().getResource( "images/savebutton.png" ) ) );
        save.setCursor( new Cursors( "curDarkGray.png", "DarkGray" ).getCursor() );
        save.setBounds( 300, 550, 200, 50 );
        save.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                new Sound("sounds/Button.wav").play(); 
                chooser = new JFileChooser( new File( "." ) );
                chooser.setDialogTitle( "Save Game" );
                chooser.setAcceptAllFileFilterUsed( false );
                chooser.setMultiSelectionEnabled( false );
                chooser.setSelectedFile( new File( ".mnc" ) );
                chooser.addChoosableFileFilter( new FileNameExtensionFilter( "MoonInvaders Classic Mode Save File (*.mnc)", "mnc" ) );
                if ( chooser.showSaveDialog(frame)== JFileChooser.APPROVE_OPTION ) {
                    file = chooser.getSelectedFile();
                    if ( !file.getName().toLowerCase().endsWith( ".mnc" ) && ( file.getName().toLowerCase().indexOf( "." ) != -1 ) ) {
                        String str = file.getName().split( "\\." )[0];
                        file = new File( str + ".mnc" );
                    }                        
                    if ( !file.getName().toLowerCase().endsWith( ".mnc" ) ){
                        file = new File( file.getName() + ".mnc" );
                    } 
                    try {                    
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        DataOutputStream dos = new DataOutputStream(bos);
                        
                        dos.writeInt(direction);
                        dos.writeInt(numAliens);
                        dos.writeInt(lives);
                        dos.writeInt(rows);
                        dos.writeInt(chance);
                        dos.writeInt(speedIncrease);
                        dos.writeInt(counter);
                        dos.writeInt(INITIAL_SIZE);
                        dos.writeInt(initialScore);
                        dos.writeInt(scoreCount);
                        dos.writeInt(ctr);
                        
                        bos.close();
                        dos.close();
                        
                    } catch (IOException f) {}
                }
                chooser = null;
                requestFocusInWindow();
            }
        });
        frame.add( save );
        save.setVisible( false );
    }
    
    @SuppressWarnings("unchecked")
    public void startGame() {
        Graphics g = getGraphics();
        countDown( g );      
        aliens = new Vector( INITIAL_SIZE, 10 );
        
        for ( int a = 0; a < rows; a++ ) {
            for ( int b = 0; b < 10; b++ ) {
                alien = new Alien( alienX + 50 * b, alienY + 50 * a );
                
                if ( rows == 3 ) {
                    if ( a == 2 || a == 1 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                        alien.setSpecies( 0 );
                    } else {
                        image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                        alien.setSpecies( 1 );
                    }
                } else if ( rows == 4 ) {
                    if ( a == 3 || a == 2 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                        alien.setSpecies( 0 );
                    } else {
                        image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                        alien.setSpecies( 1 );
                    }
                } else if ( rows == 5 ) {
                    if ( a == 4 || a == 3 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                        alien.setSpecies( 0 );
                    } else if ( a == 2 || a == 1 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                        alien.setSpecies( 1 );
                    } else {
                        image = new ImageIcon( this.getClass().getResource( "images/alien2.gif" ) );
                        alien.setSpecies( 2 );
                    }
                } else if ( rows == 6 ) {
                    if ( a == 5 || a == 4 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                        alien.setSpecies( 0 );
                    } else if ( a == 3 || a == 2 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                        alien.setSpecies( 1 );
                    } else {
                        image = new ImageIcon( this.getClass().getResource( "images/alien2.gif" ) );
                        alien.setSpecies( 2 );
                    } 
                } else {
                    if ( a == 6 || a == 5 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                        alien.setSpecies( 0 );
                    } else if ( a == 4 || a == 3 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                        alien.setSpecies( 1 );
                    } else if ( a == 2 || a == 1 ) {
                        image = new ImageIcon( this.getClass().getResource( "images/alien2.gif" ) );
                        alien.setSpecies( 2 );
                    } else {
                        image = new ImageIcon( this.getClass().getResource( "images/alien3.gif" ) );
                        alien.setSpecies( 3 );
                    } 
                }
                
                alien.setImage( image.getImage() );
                aliens.add( alien );
            }
        }
        
        rocketship = new RocketShip();
        flame = new Flame();
        flame.setVisible( false );
        laser = new LaserBeam( rocketship.getX(), rocketship.getY() );
        laser.setVisible( false );
        alienship = new AlienShip();
        alienship.setVisible( false );
                
        if ( renewShields ) {
            shield = new Shields[3];
            shield[0] = new Shields( 80, 400 );
            shield[1] = new Shields( 358, 400 );
            shield[2] = new Shields( 636, 400 );
        }
        
        timer = new Timer( 40, new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                if ( gameRunning && !gamePaused && !roundOver && lives != 0 ) {
                    repaint();
                    rocketshipAnimations();
                    alienAnimations();
                    laserAnimations();
                    fireballAnimations();
                    alienshipAnimations();
                    if ( drawBonus ) {
                        pointsCtr++;
                    }
                    if( shields ) {                        
                        ctrShields--;
                        if( ctrShields < 75 ) {
                            ctr4++;
                        }
                    }
                }
            } 
        });
        timer.start();
    }
    
    public void paintComponent( Graphics g ) {
        super.paintComponent( g ); 
        
        if ( !gameStarted ) {
            load.setVisible( true );
            back.setVisible( true );
            save.setVisible( false );
            drawBackgroundImage( g );
            newGame( g );
        }
        
        if ( !gameRunning ) {
            load.setVisible( true );
            back.setVisible( true );
            save.setVisible( false );
            drawBackgroundImage( g );
            gameOver( g );
        }
        
        if ( gameRunning && !roundOver ) {
            drawBackgroundImage( g ); 
            drawAliens( g );
            drawRocketShip( g );
            drawFlame( g );
            drawLaserBeam( g );
            drawFireball( g );
            drawAlienShip( g );
            drawRocketLives( g );
            drawPoints( g );
            drawShield( g );
            updateScore( g );
        }
        
        if ( roundOver ) {
            load.setVisible( true );
            back.setVisible( true );
            save.setVisible( true );
            drawBackgroundImage( g );
            nextRound( g ); 
        }
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void drawShield( Graphics g ) {
        for ( int a = 0; a < 3; a++ ) {
            if ( shield[a] != null ) {
                Enumeration vectShield = shield[a].elements();
                while ( vectShield.hasMoreElements() ) {
                    shieldfrag = (ShieldFragment) vectShield.nextElement();
                    g.drawImage( shieldfrag.getImage(), shieldfrag.getX(), shieldfrag.getY(), this );
                }
            }
        }
    }
    
    public void drawBackgroundImage( Graphics g ) {
        background.setVisible( true );
        g.drawImage( background.getImage(), background.getX(), background.getY(), this );
    }
    
    public void drawAliens( Graphics g ) {  
        Iterator vectIt = aliens.iterator();
        while ( vectIt.hasNext() ) {
            alien = (Alien) vectIt.next();
            if ( alien.isVisible() ) {
                g.drawImage( alien.getImage(), alien.getX(), alien.getY(), this );
                //g.drawRect( alien.getX(), alien.getY(), 45, 31 );
            }
            
            if ( alien.isDying() ) {
                alien.die();
                vectIt.remove();
            }
        }
    }
    
    public void drawAlienShip( Graphics g ) {
        if ( alienship.isVisible() ) {
            g.drawImage( alienship.getImage(), alienship.getX(), alienship.getY(), this );
            //g.drawRect( alienship.getX(), alienship.getY(), 50, 23 );
        }
        
        if ( alienship.isDying() ) {
            alienship.die();
        }
    }
    
    public void drawRocketShip( Graphics g ) {
        if ( rocketship.isVisible() ) {
            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY(), this );            
            if ( shields ) {
                g.setColor( Color.GREEN );
                if ( ctrShields < 75 ) {
                    if ( ctr4 % 5 == 0 ) {
                        g.setColor( Color.RED );
                    } else {
                        g.setColor( Color.GREEN );
                    }
                }
                g.drawOval( rocketship.getX() - 10, rocketship.getY() - 10, 55, 118 );
                if ( ctrShields < 0 ) {
                    shields = false;
                    ctr4 = 0;
                    ctrShields = 0;
                }
            }
            
            //g.drawRect(  rocketship.getX(), rocketship.getY(), 35, 98 );
        }
        
        if (rocketship.isDying()) {
            rocketship.die();
        }
    }
    
    public void drawFlame( Graphics g ) {  
        if ( flame.isVisible() ) {
            g.drawImage( flame.getImage(), flame.getX(), flame.getY(), this );
            //g.drawRect( flame.getX(), flame.getY(), 17, 36 );
        }
        
        if ( rocketship.isDying() ) {
            flame.die();
        }
    }
    
    public void drawLaserBeam( Graphics g ) {
        if ( laser.isVisible() ) {
            g.drawImage( laser.getImage(), laser.getX(), laser.getY(), this );
            //g.drawRect( laser.getX(), laser.getY(), 12, 21 );
        }
        
        if ( laser.isDying() ) {
            laser.die();
        }
    }
    
    public void drawFireball( Graphics g ) {
        vector = aliens.elements();
        while ( vector.hasMoreElements() )  {
            alien = (Alien) vector.nextElement();
            Alien.Fireball fireball = alien.getMissile();
            
            if( !fireball.isDestroyed() ) {
                g.drawImage( fireball.getImage(), fireball.getX(), fireball.getY(), this );
                //g.drawRect( fireball.getX(), fireball.getY(), 14, 25 );
            }
        }
    }
    
    public void drawRocketLives( Graphics g ) {
        int x;
        x = 100;
        ImageIcon image = new ImageIcon( this.getClass().getResource( "images/rocketshiplives.gif" ) );
        if ( lives >= 1 ) {
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( lives >= 2 ) {
            x = 158;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( lives >= 3 ) {
            x = 216;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( lives >= 4 ) {
            x = 274;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( lives >= 5 ) {
            x = 332;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( lives == 6 ) {
            x = 390;
            g.drawImage( image.getImage(), x, 620, this );
        }
    }  
    
    public void drawPoints( Graphics g ) {
        if ( drawBonus ) {
            Font small = new Font( "Helvetica", Font.BOLD, 16 );
            g.setColor( Color.WHITE );
            g.setFont( small );
            if ( alienshipX < 0 ) {
                alienshipX = -20;
                g.drawString( Integer.toString( 50 * multiplier ), alienshipX + 25, 23 );
            } else if ( alienshipX > 750 ) {
                alienshipX = 740;
                g.drawString( Integer.toString( 50 * multiplier ), alienshipX + 25, 23 );
            } else {
                g.drawString( Integer.toString( 50 * multiplier ), alienshipX + 25, 23 );
            }
        }
        if ( pointsCtr > 40 ) {
            pointsCtr = 0;
            drawBonus = false;
        }
    }
    
    public void updateScore( Graphics g ) {
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        Font small = new Font( "Helvetica", Font.BOLD, 16 );
        g.setFont( medium );
        g.setColor( Color.WHITE );
        score = Integer.toString( scoreCount );
        g.drawString( score, 0, 640 );
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void newGame( Graphics g ) { 
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        Font large = new Font( "Dialog", Font.BOLD, 32 );
        g.drawImage( title.getImage(), 300, 0, this );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "NEW GAME";
        g.drawString( message, 350, 300 );
        g.setFont( small );
        g.setColor( Color.WHITE );
        message = "Press Any Key To Start A New Game";
        g.drawString( message, 285, 330 );
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void gameOver( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        Font large = new Font( "Dialog", Font.BOLD, 32 );
        g.drawImage( title.getImage(), 300, 0, this );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "GAME OVER";
        g.drawString( message, 330, 250 );
        g.setColor( Color.WHITE );
        g.setFont( small );
        message = "Press Any Key To Start a New Game";
        g.drawString( message, 265, 280 );    
        
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "YOUR SCORE";
        g.drawString( message, 325, 350 );
        g.setColor( Color.WHITE );
        g.drawString( score, 325, 380 );
        g.drawString( "Round " + ctr, 325, 410 );
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void nextRound( Graphics g ) {
        Font small = new Font( "Helvetica", Font.BOLD, 14 );
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        Font large = new Font( "Dialog", Font.BOLD, 32 );
        g.drawImage( title.getImage(), 300, 0, this );
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "Get Ready For Round " + ctr + "!";
        g.drawString( message, 290, 300 );
        g.setColor( Color.WHITE );
        g.setFont( small );
        message = "Press Any Key To Begin";
        g.drawString( message, 320, 330 ); 
        drawRocketLives( g );
        updateScore( g );
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void countDown( Graphics g ) {
        Font large = new Font( "Dialog", Font.BOLD, 72 );
        g.setFont( large );
        g.setColor( Color.WHITE );
        drawBackgroundImage( g );
        message = "3";
        g.drawString( message, 410, 300 );
        new Sound("sounds/Beep.wav").play();
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {}
        drawBackgroundImage( g );
        message = "2";
        g.drawString( message, 410, 300 );
        new Sound("sounds/Beep.wav").play();
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {}
        drawBackgroundImage( g );
        message = "1";
        g.drawString( message, 410, 300 );
        new Sound("sounds/Beep.wav").play();
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {}
        drawBackgroundImage( g );
        message = "GO!";
        g.drawString( message, 360, 300 );
        new Sound("sounds/Go.wav").play();
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {}
        drawBackgroundImage( g );
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void drawPause( Graphics g ) {
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        g.setFont( medium );
        g.setColor( Color.WHITE );
        g.drawString( "PAUSED", 370, 300 );
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public boolean collisionDetector( int shotX, int shotY, int shotWidth, int targetX, int targetY, int targetWidth, int targetHeight, int choice ) {
        if ( choice == 1 ) {
            if ( ( shotX >= targetX && shotX <= ( targetX + targetWidth ) ) && ( ( shotX + shotWidth) >= targetX && ( shotX + shotWidth ) <= ( targetX + targetWidth ) ) && ( shotY >= targetY && shotY <= ( targetY + targetHeight ) ) )
                return true;
            else
                return false;
        } else {
            if ( ( ( shotX >= targetX && shotX <= ( targetX + targetWidth ) ) || ( ( shotX + shotWidth) >= targetX && ( shotX + shotWidth )  <= ( targetX + targetWidth ) ) ) && ( shotY >= targetY && shotY <= ( targetY + targetHeight ) ) )
                return true;
            else
                return false; 
        }
    }
    
    public void rocketshipAnimations() {
        rocketship.move();
        flame.move();
    }
    
    public void laserAnimations() {
        if ( !laser.isVisible() && ctrlDown ) {
            laser = new LaserBeam( rocketship.getX(), rocketship.getY() );
            laser.setVisible( true ); 
            new Sound("sounds/Laser.wav").play();
        }
        
        if ( laser.isVisible() ) {
            int laserX = laser.getX();
            int laserY = laser.getY();
            
            for ( int a = 0; a < 3; a++ ) {
                if ( shield[a] != null ) {
                    Iterator vectShield = shield[a].iterator();
                    while ( vectShield.hasNext() ) {
                        shieldfrag = (ShieldFragment) vectShield.next();            
                        if ( laser.isVisible() && shieldfrag.isVisible() ) {
                            if ( collisionDetector( laserX, laserY, 12, shieldfrag.getX(), shieldfrag.getY(), 14, 14, 2 ) ) {
                                laser.die();
                                vectShield.remove();
                                if ( shield[a].size() == 0 ) {
                                    shield[a] = null;
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            Iterator vectIt = aliens.iterator();
            while ( vectIt.hasNext() ) {
                alien = (Alien) vectIt.next();
                int alienX = alien.getX();
                int alienY = alien.getY();
                
                if ( alien.isVisible() && laser.isVisible() ) {
                    if ( collisionDetector( laserX, laserY, 12, alienX, alienY, 45, 31, 2 ) ) {
                        ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                        alien.setImage( image.getImage() );
                        alien.setDying( true );
                        laser.die();
                        new Sound("sounds/Alien.wav").play();
                        alienDeaths++;
                        if ( alienDeaths % 10 == 0 ) {
                            speedIncrease += 1;
                        }
                        int species = alien.getSpecies();
                        if ( species == 0 ) {
                            scoreCount += 10;
                        } else if ( species == 1 ) {
                            scoreCount += 20;
                        } else if ( species == 2 ) {
                            scoreCount += 40;
                        } else if ( species == 3 ) {
                            scoreCount += 50;
                        }
                        if ( alienDeaths == numAliens ) {
                            speedIncrease = 0;
                            alienDeaths = 0;
                            ctr++;
                            roundOver = true;
                        }
                    }
                }
            }
            
            if ( laser.isVisible() && alienship.isVisible() ) {
                if ( collisionDetector( laser.getX(), laser.getY(), 12, alienship.getX(), alienship.getY(), 50, 23, 2 ) ) {
                    Random r = new Random();
                    ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                    alienshipX = alienship.getX();
                    alienship.setImage( image.getImage() );
                    alienship.setDying( true );
                    laser.die();
                    new Sound("sounds/Powerup.wav").play();
                    multiplier = r.nextInt(10) + 1;
                    scoreCount += ( 50 * multiplier );
                    drawBonus = true;
                }
            }
            
            int y = laser.getY();
            y -= 16;
            if ( y < 0 )
                laser.die();
            else 
                laser.setY( y );
        }
    }
    
    @SuppressWarnings("unchecked")
    public void alienAnimations() {
        vector = aliens.elements();
        while ( vector.hasMoreElements() )  {
            alien = (Alien) vector.nextElement();
            int alienXX = alien.getX();
            int alienYY = alien.getY();
            
            aliens.trimToSize();
            
            if ( alienXX  >= 700 && direction > 0 ) {
                direction = -1 + ( -1 * speedIncrease );
                Enumeration vector2 = aliens.elements();
                while ( vector2.hasMoreElements() )  {
                    Alien alien2 = (Alien) vector2.nextElement();
                    alien2.setY( alien2.getY() + 16  );
                }
            }
            
            if ( alienXX <= 60 && direction < 0 ) {
                direction = 1 + speedIncrease;
                Enumeration vector2 = aliens.elements();
                while ( vector2.hasMoreElements() ) {
                    Alien alien2 = (Alien) vector2.nextElement();
                    alien2.setY( alien2.getY() + 16 );
                }
            }
        }
        
        vector = aliens.elements();
        while ( vector.hasMoreElements() ) {
            alien = (Alien) vector.nextElement();
            if ( alien.isVisible() ) {
                int y = alien.getY();
                for ( int a = 0; a < 3; a++ ) {
                    if ( shield[a] != null ) {
                        Iterator vectShield = shield[a].iterator();
                        while ( vectShield.hasNext() ) {
                            shieldfrag = (ShieldFragment) vectShield.next();            
                            if ( alien.isVisible() && shieldfrag.isVisible() ) {
                                if ( collisionDetector( alien.getX(), alien.getY() + 32, 45, shieldfrag.getX(), shieldfrag.getY(), 14, 14, 2 ) ) {
                                    vectShield.remove();
                                    if ( shield[a].size() == 0 ) {
                                        shield[a] = null;
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
                if ( y >= 480 ) {
                    Graphics g = getGraphics();
                    rocketship.setImage( new ImageIcon( getClass().getResource( explosion ) ).getImage() );
                    lives--;
                    repaint();
                    scoreCount = initialScore;
                    score = Integer.toString( scoreCount );
                    new Sound("sounds/Explosion.wav").play();
                    if ( lives > 0 ) {
                        timer.stop();
                        g.drawImage( new ImageIcon( getClass().getResource( "images/invasion.png" ) ).getImage(), 300, 0, this );
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY(), this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 45, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 90, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 67, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 22, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        alienDeaths = 0;
                        speedIncrease = counter;
                        startGame();
                        break;
                    } else {
                        g.drawImage( new ImageIcon( getClass().getResource( "images/invasion.png" ) ).getImage(), 300, 0, this );
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY(), this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 45, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 90, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 67, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 22, this );
                        try {
                            Thread.sleep( 500 );
                        } catch (InterruptedException e) {}
                        gameRunning = false;
                        break;
                    }
                }
                alien.move( direction );
            }
        }
    }
    
    public void alienshipAnimations() {
        Random r = new Random();
        int x;
        int y;
        if ( !alienship.isVisible() ) {
            int num = 500;
            int luckyNum = r.nextInt( num );
            int shipAppears = r.nextInt( num );
            if ( shipAppears == luckyNum ) {
                alienship = new AlienShip();
                if ( r.nextBoolean() ) {
                    x = -80;
                    course = 1;
                    alienship.setX( x );
                } else {
                    x = 880;
                    course = -1;
                    alienship.setX( x );
                }
                switch (r.nextInt(5)) {
                    case 0: speed = 4; break;
                    case 1: speed = 5; break;
                    case 2: speed = 6; break;
                    case 3: speed = 7; break;
                    case 4: speed = 8; break;
                }
                alienship.setVisible( true );
                new Sound("sounds/Alienship.wav").play();
            } 
        } else {
            alienship.move( course, speed );
            if ( ( alienship.getX()  < -80 ) ||( alienship.getX() > 880 ) ) {
                alienship.die();
            }
        }
    }   
    
    public void fireballAnimations() {
        Random r = new Random();        
        vector = aliens.elements();
        while ( vector.hasMoreElements() ) {
            alien = (Alien) vector.nextElement();
            int num = chance - alienDeaths * 25;
            if ( num < 0 ) {
                num = 100;
            }
            int luckyNum = r.nextInt( num );
            int dropsFireball = r.nextInt( num );
            Alien.Fireball fireball = alien.getMissile();
            if ( ( dropsFireball == luckyNum ) && alien.isVisible() && fireball.isDestroyed() ) {
                fireball.setDestroyed( false );
                fireball.setX( alien.getX() );
                fireball.setY( alien.getY() );
                new Sound("sounds/Fireball.wav").play();
            }
            
            int fireballX = fireball.getX();
            int fireballY = fireball.getY();
            int rocketshipX = rocketship.getX();
            int rocketshipY = rocketship.getY();            
            int laserX = laser.getX();
            int laserY = laser.getY();
            
            for ( int a = 0; a < 3; a++ ) {
                if ( shield[a] != null ) {
                    Iterator vectShield = shield[a].iterator();
                    while ( vectShield.hasNext() ) {
                        shieldfrag = (ShieldFragment) vectShield.next();            
                        if ( !fireball.isDestroyed() && shieldfrag.isVisible() ) {
                            if ( collisionDetector( fireballX, fireballY + 25, 14, shieldfrag.getX(), shieldfrag.getY(), 14, 14, 2 ) ) {
                                fireball.setDestroyed( true );
                                vectShield.remove();
                                if( shield[a].size() == 0 ) {
                                    shield[a] = null;
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            
            if ( laser.isVisible() && !fireball.isDestroyed() ) {
                if ( collisionDetector( laserX, laserY, 12, fireballX, fireballY, 14, 25, 2 ) ) {
                    laser.setImage( new ImageIcon( getClass().getResource( explosion ) ).getImage() );
                    laser.setDying( true );
                    fireball.setDestroyed( true );
                }
            }
            
            if ( rocketship.isVisible() && !fireball.isDestroyed() ) {     
                if ( collisionDetector( fireballX, fireballY, 14, rocketshipX + 6, rocketshipY, 23, 98, 2 ) ) {
                    if ( shields ) {
                        fireball.setDestroyed( true );
                    } else {
                        ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                        Graphics g = getGraphics();
                        rocketship.setImage( image.getImage() );
                        rocketship.setDying( true );
                        fireball.setDestroyed( true );
                        lives--;
                        repaint();
                        new Sound("sounds/Explosion.wav").play();
                        if ( lives > 0 ) {
                            if ( laser.isVisible() ) {
                                laser.die();
                            }
                            timer.stop();
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY(), this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 45, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 90, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 67, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 22, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            shields = true;
                            ctrShields = 100;
                            rocketship = new RocketShip();
                            flame = new Flame();
                            flame.setVisible( false );
                            timer.start();
                        } else {
                            if ( laser.isVisible() ) {
                                laser.die();
                            }
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY(), this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 45, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 90, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 67, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            g.drawImage( rocketship.getImage(), rocketship.getX(), rocketship.getY() + 22, this );
                            try {
                                Thread.sleep( 500 );
                            } catch (InterruptedException e) {}
                            gameRunning = false;
                        }
                    }
                }
            }
            
            if ( !fireball.isDestroyed() ) {
                int y = fireball.getY();
                y += 12;
                if ( y > 580 )
                    fireball.setDestroyed( true );
                else
                    fireball.setY( y );
            }    
        }
    } 
    
    private class KeyInputHandler extends KeyAdapter {
        
        public void keyReleased( KeyEvent e ) {
            if ( gameRunning ) {     
                if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                    flame.setVisible( false );
                    flame.stopRight();
                    rocketship.stopRight();
                } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
                    flame.setVisible( false );
                    flame.stopLeft(); 
                    rocketship.stopLeft();
                }
                
                if ( e.getKeyCode() == KeyEvent.VK_CONTROL ) {
                    ctrlDown = false;  
                }
            }
        }
        
        public void keyPressed( KeyEvent e ) {   
            if ( gameRunning ) { 
                
                if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                    flame.setVisible( true );
                    flame.moveRight();
                    rocketship.moveRight();
                } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
                    flame.setVisible( true );
                    flame.moveLeft();
                    rocketship.moveLeft();
                }
                
                if ( e.getKeyCode() == KeyEvent.VK_CONTROL ) {
                    ctrlDown = true;
                }     
            }   
        }
        
        public void keyTyped( KeyEvent e ) {
            int code = e.getKeyChar();
            
            if ( code == 27 ) {
                System.exit(0);
            } else if ( code == KeyEvent.VK_P || code == 112 ) {
                if ( gamePaused ) {
                    load.setVisible( false );
                    back.setVisible( false );
                    save.setVisible( false );
                    gamePaused = false;
                    if ( timer != null ) {
                        timer.start();
                    }
                } else {
                    Graphics g = getGraphics();
                    load.setVisible( true );
                    back.setVisible( true );
                    save.setVisible( false );
                    gamePaused = true;
                    if ( timer != null ) {
                        timer.stop();
                    }
                    drawPause( g );
                }
            } else if ( code == KeyEvent.VK_M || code == 109 ) {
                if ( timer != null ) {
                    timer.stop();
                }
                frame.remove( load );
                frame.remove( back );
                frame.remove( save );
                setVisible( false );
                frame.add( new ModesPanel( frame ) );
                frame.setVisible( false );
                frame.setVisible( true );
            } else if ( !gameStarted && ( code != KeyEvent.VK_M || code != 109 ) && ( code != KeyEvent.VK_P || code != 112 ) ) {
                load.setVisible( false );
                back.setVisible( false );
                save.setVisible( false );
                gameStarted = true;
                gameRunning = true;
                startGame();
            } else if ( !gameRunning && ( code != KeyEvent.VK_M || code != 109 ) && ( code != KeyEvent.VK_P || code != 112 ) ) {
                if ( timer != null ) {
                    timer.stop();
                }
                load.setVisible( false );
                back.setVisible( false );
                save.setVisible( false );
                alienDeaths = 0;
                speedIncrease = 0;
                counter = 0;
                direction = -1;
                numAliens = 30;
                scoreCount = 0;
                INITIAL_SIZE = 61;
                ctr = 1;
                lives = 3;
                speedIncrease = 0;
                counter = 0;
                chance =  2001;
                rows = 3;
                course = 0;
                speed = 0;
                initialScore = 0;
                alienshipX = 0;
                score = " ";
                shields = false;
                ctrShields = 0;
                ctr4 = 0;
                renewShields = true;
                drawBonus = false;
                gameRunning = true;
                startGame();
            } else if ( roundOver ) {  
                load.setVisible( false );
                back.setVisible( false );
                save.setVisible( false );
                alienDeaths = 0;
                renewShields = false;
                roundOver = false;
                shields = false;
                ctrShields = 0;
                ctr4 = 0;
                if ( timer != null ) {
                    timer.stop();
                }
                lives++;
                initialScore = scoreCount;
                speedIncrease = counter;
                if ( lives > 6 ) {
                    lives = 6;
                }
                if( ctr % 3 == 0 ) {
                    chance -= 200;
                    if ( chance < 100 ) {
                        chance = 100;
                    }
                    INITIAL_SIZE += 10;
                    rows++;
                    numAliens += 10;
                    lives++;
                    if ( lives > 6 ) {
                        lives = 6;
                    }                    
                }
                if ( ctr % 6 == 0 && rows != 7 ) {
                    speedIncrease += 1;
                    counter += 1;
                }
                if ( ctr % 9 == 0 ) {
                    renewShields = true;
                }
                if ( rows >= 7 ) {
                    counter = 4;
                    speedIncrease = counter;
                    chance = 4100;
                    INITIAL_SIZE = 71;
                    rows = 7;
                    numAliens = 70;
                }
                startGame();
            }
        }
    }
}
