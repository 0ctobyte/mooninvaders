package mooninvaders;

import javax.swing.Timer;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.text.DecimalFormat;
import sun.audio.*;
import java.io.*;

public class SurvivalBoard extends JPanel {    
    
    private ImageIcon image;
    private ImageIcon title;
    private RocketShip rocketship;
    private Flame flame;
    private Alien alien;
    private Vector aliens;
    private Enumeration vector;
    private LaserBeam[] laser;
    private Bomb bomb;
    private BackgroundImage background;
    private Timer timer;
    private JFrame frame;
    private DecimalFormat decimal;

    private int alienX = 160;
    private int alienY = -50;
    private int laserX;
    private int laserY;
    private int directionChanges = 0;
    private int direction = -3;
    private int alienDeaths = 0;
    private int scoreCount = 0;
    private int seconds = 0;
    private int minutes = 0;
    private int laserSpeed = 15;
    private int bombs = 0;
    private final int INITIAL_SIZE = 61;
    private int ctr = 0;
    private int lives = 1;
    private int ctr2 = 0;
    private int ctr3 = 0;
    private int ctr4 = 0;
    private int ctr5 = 0;
    private int ctrShields = 0;
    private int ctrLaser = 0;
    private int laserSpread = 8;
    private int ctrBomb = 0;
    private boolean gameRunning = false;
    private boolean gameStarted = false;
    private boolean gamePaused = false;
    private boolean ctrlDown = false;
    private boolean laserUpgrade = false;
    private boolean shields = false;
    private boolean releaseBomb = false;
    private boolean detonateBomb = false;
    private boolean detonateBomb2 = false;
    private boolean detonation = false;
    private String score = " ";
    private String message;
    private String time = " ";
    private String explosion = "images/explosion.gif";
    private String alienSoundLocation = "sounds/Alien.wav";
    private String laserSoundLocation = "sounds/Laser.wav";
    private String beepSoundLocation = "sounds/Beep.wav";
    private String goSoundLocation = "sounds/Go.wav";
    private String fireballSoundLocation = "sounds/Fireball.wav";
    private String explosionSoundLocation = "sounds/Explosion.wav";
    private String powerupSoundLocation = "sounds/Powerup.wav";
    
    public SurvivalBoard( JFrame f ) {
        background = new BackgroundImage();
        title = new ImageIcon( getClass().getResource( "images/survivalmode.png" ) );
        addKeyListener( new KeyInputHandler() );
        setFocusable( true );
        requestFocusInWindow();
        setPreferredSize( new Dimension( 800, 650 ) );
        setLayout( null );
        setOpaque( false );
        setDoubleBuffered( true );
        frame = f;
        frame.setCursor( new Cursors( "curBlue.png", "Blue" ).getCursor() );
    }
    
    @SuppressWarnings("unchecked")
    public void startGame() {
        Graphics g = getGraphics();
        countDown( g );
        decimal = new DecimalFormat( "00" );
        aliens = new Vector( INITIAL_SIZE, 10 );
        
        for ( int a = 0; a < 6; a++ ) {
            for ( int b = 0; b < 10; b++ ) {
                alien = new Alien( alienX + 50 * b, alienY + 50 * a );
                Random num = new Random();
                int numm = num.nextInt( 4 );
                
                if ( numm == 0 ) {
                    image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                    alien.setSpecies( 0 );
                } else if ( numm == 1 ) {
                    image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                    alien.setSpecies( 1 );
                } else if ( numm == 2 ) {
                    image = new ImageIcon( this.getClass().getResource( "images/alien2.gif" ) );
                     alien.setSpecies( 2 );
                } else {
                    image = new ImageIcon( this.getClass().getResource( "images/alien3.gif" ) );
                     alien.setSpecies( 3 );
                }    
                
                alien.setImage( image.getImage() );
                aliens.add( alien );
            }
        }
        
        rocketship = new RocketShip();
        flame = new Flame();
        flame.setVisible( false );
        bomb = new Bomb( rocketship.getX(), rocketship.getY() );
        bomb.setVisible( false );
        laser = new LaserBeam[3];
        for ( int a = 0; a < 3; a++ ) {
            laser[a] = new LaserBeam( rocketship.getX(), rocketship.getY() );
            laser[a].setVisible( false );
        }
        
        timer = new Timer( 40, new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                if ( gameRunning && !gamePaused && lives != 0 ) {
                    repaint();
                    rocketshipAnimations();
                    alienAnimations();
                    laserAnimations();
                    bombAnimations();
                    fireballAnimations();
                    upgradeAnimations();
                    ctr++;
                    ctr3++;
                    if ( detonateBomb2 ) {
                        ctrBomb--;
                        if ( ctrBomb < 50 ) {
                            ctr5++;
                        }
                    }
                    if ( detonation ) {
                        ctr5--;
                    }
                    if( laserUpgrade ) {                        
                        ctrLaser--;
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
            drawBackgroundImage( g );
            newGame( g );
        }
        
        if ( !gameRunning ) {
            drawBackgroundImage( g );
            gameOver( g );
        }
        
        if ( gameRunning ) {
            drawBackgroundImage( g ); 
            drawAliens( g );
            drawRocketShip( g );
            drawFlame( g );
            drawLaserBeam( g );
            drawBomb( g );
            drawFireball( g );
            drawUpgrade( g );
            drawBombs( g );
            drawRocketLives( g );
            updateScore( g );
        }
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
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
        for ( int a = 0; a < 3; a++ ) {
            if ( laser[a].isVisible() ) {
                g.drawImage( laser[a].getImage(), laser[a].getX(), laser[a].getY(), this );
                // g.drawRect( laser[a].getX(), laser[a].getY(), 12, 21 );
            }
            if ( laser[a].isDying() ) {
                laser[a].die();
            }
        }
    }
    
    public void drawBomb( Graphics g ) {
        if ( bomb.isVisible() ) {
            g.drawImage( bomb.getImage(), bomb.getX(), bomb.getY(), this );
            if ( detonateBomb2 ) {
                bomb.setImage( new ImageIcon( getClass().getResource( "images/bombing2.png" ) ).getImage() );
                if ( ctrBomb < 50 ) {
                    if ( ctr5 % 5 == 0 ) {
                        bomb.setImage( new ImageIcon( getClass().getResource( "images/bombing.png" ) ).getImage() );
                    } else {
                        bomb.setImage( new ImageIcon( getClass().getResource( "images/bombing2.png" ) ).getImage() );
                    }
                }
                g.drawImage( bomb.getImage(), bomb.getX(), bomb.getY(), this );
                if ( ctrBomb < 0 ) {
                    bomb.die();
                    detonation = true;
                    releaseBomb = false;
                    detonateBomb = false;
                    detonateBomb2 = false;
                    ctrBomb = 0;
                    ctr5 = 15;
                    Sounds.loadAndPlay( explosionSoundLocation, 1 );
                }
            }
            // g.drawRect( bomb.getX(), bomb.getY(), 25, 30 );
        }
        if ( detonation ) {
            g.setColor( Color.RED );
            
            if ( ctr5 < 5 ) {
                g.drawOval( bomb.getX() - 75, bomb.getY() - 75, 175, 179 );
            } else if ( ctr5 < 10 ) {
                g.drawOval( bomb.getX() - 50, bomb.getY() - 50, 125, 129 );
            } else { 
                g.drawOval( bomb.getX() - 25, bomb.getY() - 25, 75, 79 );
            }
            if ( ctr5 < 0 ) {
                ctr5 = 0;
                detonation = false;
            }
        }    
        if ( bomb.isDying() ) {
            bomb.die();
        }
    }
    
    public void drawFireball( Graphics g ) {
        vector = aliens.elements();
        while ( vector.hasMoreElements() )  {
            alien = (Alien) vector.nextElement();
            Alien.Fireball fireball = alien.getMissile();
            
            if( !fireball.isDestroyed() ) {
                g.drawImage( fireball.getImage(), fireball.getX(), fireball.getY(), this );
                // g.drawRect( fireball.getX(), fireball.getY(), 20, 25 );
            }
        }
    }
    
    public void drawUpgrade( Graphics g ) {
        vector = aliens.elements();
        while ( vector.hasMoreElements() ) {
            alien = (Alien) vector.nextElement();
            Alien.Upgrade upgrade = alien.getUpgrade();
            
            if( !upgrade.isDestroyed() ) {
                g.drawImage( upgrade.getImage(), upgrade.getX(), upgrade.getY(), this );
                //  g.drawRect( upgrade.getX(), upgrade.getY(), 30, 15 );
            }
        }
    }
    
    public void drawBombs( Graphics g ) {
        int x;
        x = 500;
        ImageIcon image = new ImageIcon( this.getClass().getResource( "images/bomb.png" ) );
        if ( bombs >= 1 ) {
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( bombs >= 2 ) {
            x = 535;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( bombs >= 3 ) {
            x = 570;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( bombs >= 4 ) {
            x = 605;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( bombs >= 5 ) {
            x = 640;
            g.drawImage( image.getImage(), x, 620, this );
        }
        if ( bombs == 6 ) {
            x = 675;
            g.drawImage( image.getImage(), x, 620, this );
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
    
    public void updateScore( Graphics g ) {
        Font medium = new Font( "Helvetica", Font.BOLD, 20 );
        g.setFont( medium );
        g.setColor( Color.WHITE );
        score = Integer.toString( scoreCount );
        g.drawString( score, 0, 640 );
        
        if ( ctr == 25 ) {
            ctr = 0;
            seconds++;
            if ( seconds / 60 == 1 ) {
                seconds = 0;
                minutes++;
            }
        }
        time = decimal.format( minutes ) + ":" + decimal.format( seconds );
        g.drawString( time, 750, 640 );
        
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
        g.drawString( time, 325, 410 );
        
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
        Sounds.loadAndPlay( beepSoundLocation, 1 );
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {}
        drawBackgroundImage( g );
        message = "2";
        g.drawString( message, 410, 300 );
        Sounds.loadAndPlay( beepSoundLocation, 1 );
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {}
        drawBackgroundImage( g );
        message = "1";
        g.drawString( message, 410, 300 );
        Sounds.loadAndPlay( beepSoundLocation, 1 );
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {}
        drawBackgroundImage( g );
        message = "GO!";
        g.drawString( message, 360, 300 );
        Sounds.loadAndPlay( goSoundLocation, 1 );
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
        if ( ctrlDown ) {
            for ( int a = 0; a < 3; a++ ) {
                if ( laserUpgrade ) {
                    laser[a].setImage( new ImageIcon( getClass().getResource( "images/laserbeam2.png" ) ).getImage() );
                    if ( ctrLaser <= 0 ) {
                        ctrLaser = 0;
                        laserUpgrade = false;
                        laserSpeed = 15;
                        laserSpread = 8;
                    }
                }
                if ( !laser[a].isVisible() && ctr3 >= laserSpread ) {
                    laser[a] = new LaserBeam( rocketship.getX(), rocketship.getY() );
                    laser[a].setVisible( true );
                    Sounds.loadAndPlay( laserSoundLocation, 1 );
                    ctr3 = 0;
                }                
            }
        }
        for ( int a = 0; a < 3; a++ ) {
            if ( laser[a].isVisible() ) {
                int laserX = laser[a].getX();
                int laserY = laser[a].getY();
                
                Iterator vectIt = aliens.iterator();
                while ( vectIt.hasNext() ) {
                    alien = (Alien) vectIt.next();
                    int alienX = alien.getX();
                    int alienY = alien.getY();
                    
                    if ( alien.isVisible() && laser[a].isVisible() ) {
                        if ( collisionDetector( laserX, laserY, 21, alienX, alienY, 45, 31, 2 ) ) {
                            ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                            alien.setImage( image.getImage() );
                            alien.setDying( true );
                            laser[a].die();
                            Sounds.loadAndPlay( alienSoundLocation, 1 );
                            alienDeaths++;
                            if ( alienDeaths % 30 == 0 ) {
                                if ( direction < 0 ) {
                                    direction -= 1;
                                } else {
                                    direction += 1;
                                }
                            } else if ( direction > 12 ) {
                                direction = 12;
                            }
                            
                            if ( alien.getSpecies() == 0 ) {
                                scoreCount += 20;
                            } else if ( alien.getSpecies() == 1 ) {
                                scoreCount += 30;
                            } else if ( alien.getSpecies() == 2 ) {
                                scoreCount += 40;
                            } else {
                                scoreCount += 50;
                            }                                
                        }
                    }
                }
                
                int y = laser[a].getY();
                y -= laserSpeed;
                if ( y < 0 )
                    laser[a].die();
                else 
                    laser[a].setY( y );
            }
        }
    }
    
    public void bombAnimations() {
        if ( releaseBomb && !bomb.isVisible() && !detonateBomb2 && !detonation ) {
            releaseBomb = false;
            bomb = new Bomb( rocketship.getX(), rocketship.getY() );
            bomb.setVisible( true );
        }
        
        if ( bomb.isVisible() && !detonateBomb2 && !detonation ) {
            int y = bomb.getY();
            y -= 12;
            if ( y < 0 ) {
                bomb.die();
            } else {
                bomb.setY( y );
            }
        }
        
        if ( detonateBomb ) {
            detonateBomb = false;
            detonateBomb2 = true;
            ctrBomb = 75;
            ctr5 = 0;
        }  
        if ( detonation ) {            
            if ( ctr5 < 5 ) {
              Iterator vectIt = aliens.iterator();
              while ( vectIt.hasNext() ) {
                  alien = (Alien) vectIt.next();
                  int alienX = alien.getX();
                  int alienY = alien.getY();
                  
                  if ( alien.isVisible() ) {
                      if ( ( ( alienX + 45 >= bomb.getX() - 75 && alienX + 45 <= bomb.getX() + 100 ) && ( alienY + 31 >= bomb.getY() - 75  && alienY + 31 <= bomb.getY() + 104 ) )
                         || ( ( alienX + 45 >= bomb.getX() - 75 && alienX + 45 <= bomb.getX() + 100 ) && ( alienY >= bomb.getY() - 75  && alienY <= bomb.getY() + 104 ) )
                         || ( ( alienX >= bomb.getX() - 75 && alienX <= bomb.getX() + 100 ) && ( alienY >= bomb.getY() - 75  && alienY <= bomb.getY() + 104 ) )
                         || ( ( alienX >= bomb.getX() - 75 && alienX <= bomb.getX() + 100 ) && ( alienY + 31 >= bomb.getY() - 75  && alienY + 31 <= bomb.getY() + 104 ) ) ) {
                          ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                          alien.setImage( image.getImage() );
                          alien.setDying( true );
                          Sounds.loadAndPlay( alienSoundLocation, 1 );
                          alienDeaths++;
                          if ( alienDeaths % 30 == 0 ) {
                              if ( direction < 0 ) {
                                  direction -= 1;
                              } else {
                                  direction += 1;
                              }
                          } else if ( direction > 12 ) {
                              direction = 12;
                          }
                          
                          if ( alien.getSpecies() == 0 ) {
                              scoreCount += 20;
                          } else if ( alien.getSpecies() == 1 ) {
                              scoreCount += 30;
                          } else if ( alien.getSpecies() == 2 ) {
                              scoreCount += 40;
                          } else {
                              scoreCount += 50;
                          }                                
                      }
                  }
              }  
            } else if ( ctr5 < 10 ) {
                Iterator vectIt = aliens.iterator();
                while ( vectIt.hasNext() ) {
                    alien = (Alien) vectIt.next();
                    int alienX = alien.getX();
                    int alienY = alien.getY();
                    
                    if ( alien.isVisible() ) {
                        if ( ( ( alienX + 45 >= bomb.getX() - 50 && alienX + 45 <= bomb.getX() + 100 ) && ( alienY + 31 >= bomb.getY() - 50  && alienY + 31 <= bomb.getY() + 104 ) )
                                || ( ( alienX + 45 >= bomb.getX() - 50 && alienX + 45 <= bomb.getX() + 100 ) && ( alienY >= bomb.getY() - 50  && alienY <= bomb.getY() + 104 ) )
                                || ( ( alienX >= bomb.getX() - 50 && alienX <= bomb.getX() + 100 ) && ( alienY >= bomb.getY() - 50  && alienY <= bomb.getY() + 104 ) )
                                || ( ( alienX >= bomb.getX() - 50 && alienX <= bomb.getX() + 100 ) && ( alienY + 31 >= bomb.getY() - 50  && alienY + 31 <= bomb.getY() + 104 ) ) ) {
                            ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                            alien.setImage( image.getImage() );
                            alien.setDying( true );
                            Sounds.loadAndPlay( alienSoundLocation, 1 );
                            alienDeaths++;
                            if ( alienDeaths % 30 == 0 ) {
                                if ( direction < 0 ) {
                                    direction -= 1;
                                } else {
                                    direction += 1;
                                }
                            } else if ( direction > 12 ) {
                                direction = 12;
                            }
                            
                            if ( alien.getSpecies() == 0 ) {
                                scoreCount += 20;
                            } else if ( alien.getSpecies() == 1 ) {
                                scoreCount += 30;
                            } else if ( alien.getSpecies() == 2 ) {
                                scoreCount += 40;
                            } else {
                                scoreCount += 50;
                            }                                
                        }
                    }
                }
            } else { 
                Iterator vectIt = aliens.iterator();
                while ( vectIt.hasNext() ) {
                    alien = (Alien) vectIt.next();
                    int alienX = alien.getX();
                    int alienY = alien.getY();
                    
                    if ( alien.isVisible() ) {
                        if ( ( ( alienX + 45 >= bomb.getX() - 25 && alienX + 45 <= bomb.getX() + 100 ) && ( alienY + 31 >= bomb.getY() - 25  && alienY + 31 <= bomb.getY() + 104 ) )
                                || ( ( alienX + 45 >= bomb.getX() - 25 && alienX + 45 <= bomb.getX() + 100 ) && ( alienY >= bomb.getY() - 25  && alienY <= bomb.getY() + 104 ) )
                                || ( ( alienX >= bomb.getX() - 25 && alienX <= bomb.getX() + 100 ) && ( alienY >= bomb.getY() - 25  && alienY <= bomb.getY() + 104 ) )
                                || ( ( alienX >= bomb.getX() - 25 && alienX <= bomb.getX() + 100 ) && ( alienY + 31 >= bomb.getY() - 25  && alienY + 31 <= bomb.getY() + 104 ) ) ) {
                            ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                            alien.setImage( image.getImage() );
                            alien.setDying( true );
                            Sounds.loadAndPlay( alienSoundLocation, 1 );
                            alienDeaths++;
                            if ( alienDeaths % 30 == 0 ) {
                                if ( direction < 0 ) {
                                    direction -= 1;
                                } else {
                                    direction += 1;
                                }
                            } else if ( direction > 12 ) {
                                direction = 12;
                            }
                            
                            if ( alien.getSpecies() == 0 ) {
                                scoreCount += 20;
                            } else if ( alien.getSpecies() == 1 ) {
                                scoreCount += 30;
                            } else if ( alien.getSpecies() == 2 ) {
                                scoreCount += 40;
                            } else {
                                scoreCount += 50;
                            }                                
                        }
                    }
                }
            }
        }    
    }
    
    @SuppressWarnings("unchecked")
    public void alienAnimations() {
        vector = aliens.elements();
        while ( vector.hasMoreElements() )  {
            alien = (Alien) vector.nextElement();
            int x = alien.getX();
            
            if ( ( directionChanges == 3 ) && ( alien.getX() <= 60 ) ) {                    
                directionChanges = 0;
                for ( int a = 0; a < 1; a++ ) {
                    for ( int b = 0; b < 10; b++ ) {
                        alien = new Alien( 60 + 50 * b, -50 );
                        Random num = new Random();
                        int numm = num.nextInt( 4 );
                        
                        if ( numm == 0 ) {
                            image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                            alien.setSpecies( 0 );
                        } else if ( numm == 1 ) {
                            image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                            alien.setSpecies( 1 );
                        } else if ( numm == 2 ) {
                            image = new ImageIcon( this.getClass().getResource( "images/alien2.gif" ) );
                            alien.setSpecies( 2 );
                        } else {
                            image = new ImageIcon( this.getClass().getResource( "images/alien3.gif" ) );
                            alien.setSpecies( 3 );
                        }    
                        
                        alien.setImage( image.getImage() );
                        aliens.add( alien );                            
                    }
                }
                aliens.trimToSize();
            }
            
            if ( ( directionChanges == 3 ) && ( alien.getX() >= 700 ) ) {                    
                directionChanges = 0;
                for ( int a = 0; a < 1; a++ ) {
                    for ( int b = 0; b < 10; b++ ) {
                        alien = new Alien( 700 - 50 * b, -50 );
                        Random num = new Random();
                        int numm = num.nextInt( 4 );
                        
                        if ( numm == 0 ) {
                            image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                            alien.setSpecies( 0 );
                        } else if ( numm == 1 ) {
                            image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                            alien.setSpecies( 1 );
                        } else if ( numm == 2 ) {
                            image = new ImageIcon( this.getClass().getResource( "images/alien2.gif" ) );
                            alien.setSpecies( 2 );
                        } else {
                            image = new ImageIcon( this.getClass().getResource( "images/alien3.gif" ) );
                            alien.setSpecies( 3 );
                        }    
                        
                        alien.setImage( image.getImage() );
                        aliens.add( alien );                            
                    }
                }
                aliens.trimToSize();
            }
            
            if ( x  >= 700 && direction > 0 ) {
                direction *= -1;
                directionChanges++;
                Enumeration vector2 = aliens.elements();
                while ( vector2.hasMoreElements() )  {
                    Alien alien2 = (Alien) vector2.nextElement();
                    alien2.setY( alien2.getY() + 16  );
                }
            }
            
            if ( x <= 60 && direction < 0 ) {
                direction *= -1;
                directionChanges++;
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
                if ( y >= 480 ) {
                    Graphics g = getGraphics();
                    rocketship.setImage( new ImageIcon( getClass().getResource( explosion ) ).getImage() );
                    lives -= 1;
                    Sounds.loadAndPlay( explosionSoundLocation, 0 );
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
                        gameRunning = false;
                        break;
                    } else {
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
                        gameRunning = false;
                        break;
                    }
                }
                alien.move( direction );
            }
        }
    }
    
    public void upgradeAnimations() {
        Random r = new Random();        
        vector = aliens.elements();
        while ( vector.hasMoreElements() ) {
            alien = (Alien) vector.nextElement();               
            int num = 20000;
            if ( alienDeaths % 20 == 0 ) {
                num -= 30 * ctr2;
                ctr2++;
            }
            if ( num < 5000 ) {
                num = 5000;
            }
            int luckyNum = r.nextInt( num );
            int dropsUpgrade = r.nextInt( num );
            Alien.Upgrade upgrade = alien.getUpgrade();
            if ( ( dropsUpgrade == luckyNum ) && alien.isVisible() && upgrade.isDestroyed() ) {
                upgrade.setDestroyed( false );
                upgrade.setX( alien.getX() );
                upgrade.setY( alien.getY() );
                Sounds.loadAndPlay( fireballSoundLocation, 1 );
            }
            
            int upgradeX = upgrade.getX();
            int upgradeY = upgrade.getY();
            int rocketshipX = rocketship.getX();
            int rocketshipY = rocketship.getY();
            int upgradeType = upgrade.getUpgradeType();
            if ( rocketship.isVisible() && !upgrade.isDestroyed() ) {   
                if ( upgradeType == Alien.RAPID_FIRE ) {
                    if ( collisionDetector( upgradeX, upgradeY, 30, rocketshipX + 6, rocketshipY, 23, 98, 2 ) ) {
                        if ( shields ) {
                            upgrade.setDestroyed( true );
                        } else {
                            upgrade.setDestroyed( true );
                            laserUpgrade = true;
                            laserSpeed = 30;
                            ctrLaser += 250;
                            laserSpread = 4;
                            scoreCount += 10;
                            Sounds.loadAndPlay( powerupSoundLocation, 1 );
                        }
                    }
                } else if ( upgradeType == Alien.HEALTH ) {
                    if ( collisionDetector( upgradeX, upgradeY, 25, rocketshipX + 6, rocketshipY, 23, 98, 2 ) ) {
                        if ( shields ) {
                            upgrade.setDestroyed( true );
                        } else {
                            upgrade.setDestroyed( true );
                            lives++;
                            if( lives > 6 ) {
                                lives = 6;
                            }
                            scoreCount += 10;
                            Sounds.loadAndPlay( powerupSoundLocation, 1 );
                        }
                    }
                } else if ( upgradeType == Alien.SHIELD ) {
                    if ( collisionDetector( upgradeX, upgradeY, 25, rocketshipX + 6, rocketshipY, 23, 98, 2 ) ) {
                        if ( shields ) {
                            upgrade.setDestroyed( true );
                        } else {
                            upgrade.setDestroyed( true );
                            shields = true;
                            ctrShields += 250;
                            scoreCount += 10;
                            Sounds.loadAndPlay( powerupSoundLocation, 1 );
                        }
                    }
                } else {
                    if ( collisionDetector( upgradeX, upgradeY, 25, rocketshipX + 6, rocketshipY, 23, 98, 2 ) ) {
                        if ( shields ) {
                            upgrade.setDestroyed( true );
                        } else {
                            upgrade.setDestroyed( true );
                            bombs++;
                            if ( bombs > 6 ) {
                                bombs = 6;
                            }
                            scoreCount += 10;
                            Sounds.loadAndPlay( powerupSoundLocation, 1 );
                        }
                    }
                }
            }
            
            if ( !upgrade.isDestroyed() ) {
                int y = upgrade.getY();
                y += 12;
                if ( y > 580 )
                    upgrade.setDestroyed( true );
                else
                    upgrade.setY( y );
            }
        }
    }
    
    public void fireballAnimations() {
        Random r = new Random();        
        vector = aliens.elements();
        while ( vector.hasMoreElements() ) {
            alien = (Alien) vector.nextElement();               
            int num = 2001;
            if ( alienDeaths % 20 == 0 ) {
                num -= 30 * ctr2;
                ctr2++;
            }
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
                Sounds.loadAndPlay( fireballSoundLocation, 1 );
            }
            
            int fireballX = fireball.getX();
            int fireballY = fireball.getY();
            int rocketshipX = rocketship.getX();
            int rocketshipY = rocketship.getY();
            
            for ( int a = 0; a < 3; a++ ) {
                if ( laser[a].isVisible() && !fireball.isDestroyed() ) {
                    if ( collisionDetector( laser[a].getX(), laser[a].getY(), 12, fireballX, fireballY, 14, 25, 2 ) ) {
                        laser[a].setImage( new ImageIcon( getClass().getResource( explosion ) ).getImage() );
                        laser[a].setDying( true );
                        fireball.setDestroyed( true );
                        scoreCount += 10;
                    }
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
                        lives -= 1;
                        Sounds.loadAndPlay( explosionSoundLocation, 1 );
                        if ( lives > 0 ) {
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
                y += 16;
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
                    gamePaused = false;
                    if ( timer != null ) {
                        timer.start();
                    }
                } else {
                    Graphics g = getGraphics();
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
                setVisible( false );
                frame.add( new ModesPanel( frame ) );
                frame.setVisible( false );
                frame.setVisible( true );
            } else if ( !gameStarted && ( code != KeyEvent.VK_M || code != 109 ) && ( code != KeyEvent.VK_P || code != 112 ) ) {
                gameStarted = true;
                gameRunning = true;
                startGame();
            } else if ( !gameRunning && ( code != KeyEvent.VK_M || code != 109 ) && ( code != KeyEvent.VK_P || code != 112 ) ) {
                if ( timer != null ) {
                    timer.stop();
                }
                bombs = 0;
                directionChanges = 0;
                direction = -2;
                alienDeaths = 0;
                scoreCount = 0;
                seconds = 0;
                minutes = 0;
                ctr = 0;
                lives = 1;                
                score = " ";
                time = " ";
                ctr4 = 0;
                ctrLaser = 0;
                ctrShields = 0;
                ctrBomb = 0;
                ctr5 = 0;
                ctrlDown = false;
                shields = false;
                laserUpgrade = false;
                releaseBomb = false;
                detonateBomb = false;
                detonateBomb2 = false;
                detonation = false;
                gameRunning = true;
                startGame();
            } else if ( code == 90 || code == 122 ) {
                if ( bombs > 0 ) {
                    if ( !releaseBomb && !bomb.isVisible() && !detonation ) {
                        releaseBomb = true;
                        bombs--;
                    }                     
                }
                if ( bomb.isVisible() && !detonateBomb2 ) {
                    releaseBomb = false;
                    detonateBomb = true;
                }
            }
        }
    }
}