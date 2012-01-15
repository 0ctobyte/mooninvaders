package mooninvaders;

import javax.swing.Timer;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import sun.audio.*;
import java.io.*;
import java.text.DecimalFormat;

public class TargetBoard extends JPanel {    
    
    private ImageIcon image;
    private ImageIcon title;
    private RocketShip rocketship;
    private Flame flame;
    private Alien alien;
    private AlienShip alienship;
    private Vector<Alien> aliens;
    private Enumeration vector;
    private LaserBeam laser;
    private BackgroundImage background;
    private Timer timer;
    private JFrame frame;
    private DecimalFormat decimal;
    private JComboBox timeLimit;
    
    private int alienX = 160;
    private int alienY = -50;
    private int laserX;
    private int laserY;
    private int directionChanges = 0;
    private int direction = -2;
    private int alienDeaths = 0;
    private int scoreCount = 0;
    private int seconds = 20;
    private int minutes = 0;
    private final int INITIAL_SIZE = 61;
    private int ctr = 0;
    private int lives = 3;
    private int aliensInit = 0;
    private int ctrShields = 0;
    private int ctr4 = 0;
    private int course = 0;
    private int speed = 0;
    private int pointsCtr;
    private int alienshipX = 0;
    private int multiplier;
    private boolean gameRunning = false;
    private boolean gameStarted = false;
    private boolean gamePaused = false;
    private boolean ctrlDown = false;
    private boolean shields = false;
    private boolean drawBonus = false;
    private String score = " ";
    private String aliensKilled = " ";
    private String time = " ";
    private String message;
    private String explosion = "images/explosion.gif";
    
    
    public TargetBoard( JFrame f ) {
        background = new BackgroundImage();
        title = new ImageIcon( getClass().getResource( "images/targetmode.png" ) );
        addKeyListener( new KeyInputHandler() );
        setFocusable( true );
        setPreferredSize( new Dimension( 800, 650 ) );
        setLayout( new FlowLayout() );
        setOpaque( false );
        setDoubleBuffered( true );
        frame = f;
        frame.setCursor( new Cursors( "curGreen.png", "Green" ).getCursor() );
		frame.setVisible(false);
		frame.setVisible(true);
        
        timeLimit = new JComboBox();
        for ( int x = 10; x <= 90; x += 10 ) {
            timeLimit.addItem( new String( x + " Seconds" ) );
        }
        timeLimit.setSelectedIndex( 1 );
        timeLimit.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                String item = (String) timeLimit.getSelectedItem();
                String selection = item.substring( 0, 2 );
                seconds = Integer.parseInt( selection );
                if ( seconds >= 60 ) {
                    minutes = seconds / 60;
                    seconds %= 60;
                } else {
                    minutes = 0;
                }
                requestFocusInWindow();
            }
        });
        frame.add( timeLimit );
        timeLimit.setVisible( false );
    }
    
    public void startGame() {
        Graphics g = getGraphics();
        countDown( g );
        
        decimal = new DecimalFormat( "00" );        
        aliens = new Vector<Alien>( INITIAL_SIZE, 10 );
        rocketship = new RocketShip();
        alienship = new AlienShip();
        alienship.setVisible( false );
        flame = new Flame();
        flame.setVisible( false );
        laser = new LaserBeam( rocketship.getX(), rocketship.getY() );
        laser.setVisible( false );
        
        timer = new Timer( 40, new ActionListener() {
            public void actionPerformed( ActionEvent event ) {
                if ( gameRunning && !gamePaused && lives != 0 ) {
                    rocketshipAnimations();
                    alienAnimations();
                    laserAnimations();
                    alienshipAnimations();
                    fireballAnimations();
                    ctr++;
                    if ( seconds == 0 && minutes == 0 ) {
                        gameRunning = false;
                    }
                    if ( drawBonus ) {
                        pointsCtr++;
                    }
                    if( shields ) {                        
                        ctrShields--;
                        if( ctrShields < 75 ) {
                            ctr4++;
                        }
                    }
                    repaint();
                }
            } 
        });
        timer.start();
    }
    
    public void paintComponent( Graphics g ) {
        super.paintComponent( g ); 
        
        if ( !gameStarted ) {
            drawBackgroundImage( g );
            timeLimit.setBounds( 320, 390, 182, 25 );
            timeLimit.setVisible( true );
            newGame( g );
        }
        
        if ( !gameRunning && gameStarted ) {
            drawBackgroundImage( g );
            timeLimit.setBounds( 300, 465, 182, 25 );
            timeLimit.setVisible( true );
            gameOver( g );
        }
        
        if ( gameRunning ) {
            drawBackgroundImage( g ); 
            drawAliens( g );
            drawAlienShip( g );
            drawRocketShip( g );
            drawFlame( g );
            drawLaserBeam( g );
            drawFireball( g );
            drawPoints( g );
            drawRocketLives( g, lives );
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
                aliensInit--;
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
            // g.drawRect( laser.getX(), laser.getY(), 12, 21 );
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
    
    public void drawRocketLives( Graphics g, int lives ) {
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
        g.setFont( medium );
        g.setColor( Color.WHITE );
        score = Integer.toString( scoreCount );
        g.drawString( score, 0, 640 );
        
        aliensKilled = Integer.toString( alienDeaths );
        g.drawString( aliensKilled, 400, 640 );
        
        if ( ctr == 25 ) {
            ctr = 0;
            seconds--;
            if ( minutes > 0 && seconds < 0 ) {
                seconds = 59;
                minutes--;
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
        
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        g.drawString( "CHOOSE TIME LIMIT", 310, 375 );
        
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
        g.drawString( aliensKilled, 325, 410 );
        
        g.setFont( medium );
        g.setColor( Color.LIGHT_GRAY );
        message = "CHOOSE TIME LIMIT";
        g.drawString( message, 290, 450 );
        
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
    
    public void alienAnimations() {
        while ( aliensInit < 10 ) {
            Random num = new Random();
            alien = new Alien( num.nextInt( 600 ) + 101, num.nextInt( 250 ) + 101 );
            
            int specie = num.nextInt( 4 );
            if ( specie == 0 ) {
                image = new ImageIcon( this.getClass().getResource( "images/alien.gif" ) );
                alien.setSpecies( 0 );
            } else if ( specie == 1 ) {
                image = new ImageIcon( this.getClass().getResource( "images/alien1.gif" ) );
                alien.setSpecies( 1 );
            } else if ( specie == 2 ) {
                image = new ImageIcon( this.getClass().getResource( "images/alien2.gif" ) );
                alien.setSpecies( 2 );
            } else {
                image = new ImageIcon( this.getClass().getResource( "images/alien3.gif" ) );
                alien.setSpecies( 3 );
            }   
            
            switch (num.nextInt(5)) {
                case 0: alien.setSpeed(4); break;
                case 1: alien.setSpeed(5); break;
                case 2: alien.setSpeed(6); break;
                case 3: alien.setSpeed(7); break;
                case 4: alien.setSpeed(8); break;
            }
            switch (num.nextInt(2)) {
                case 0: alien.setDirection(1); break;
                case 1: alien.setDirection(-1); break;
            }
            
            alien.setImage( image.getImage() );
            aliens.add( alien );
            aliensInit++;
            int alienAX = alien.getX();
            int alienAY = alien.getY();
            
            Iterator vectIt = aliens.iterator();
            while ( vectIt.hasNext() ) {
                Alien alienB = (Alien) vectIt.next();
                int alienBX = alienB.getX();
                int alienBY = alienB.getY();
                if ( (alien.getSpecies() != alienB.getSpecies()) && (alienAX != alienBX) && (alienAY != alienBY) ) {
                    if ( ( ( alienAX >= alienBX && alienAX <= alienBX + 45 ) || ( alienAX + 45 >= alienBX && alienAX + 45 <= alienBX + 45 ) ) && ( ( alienAY >= alienBY && alienAY <= alienBY + 32 ) || ( alienAY + 32 >= alienBY && alienAY + 32 <= alienBY +32 ) ) ) {
                        if ( ( ( alienBX >= alienAX && alienBX <= alienAX + 45 ) || ( alienBX + 45 >= alienAX && alienBX + 45 <= alienAX + 45 ) ) && ( ( alienBY >= alienAY && alienBY <= alienAY + 32 ) || ( alienBY + 32 >= alienAY && alienBY + 32 <= alienAY +32 ) ) ) {
                            aliens.remove( alien );
                            aliensInit--;
                            break;
                        }
                    }
                }
            }      
        }
        vector = aliens.elements();
        while ( vector.hasMoreElements() ) {
            alien = (Alien) vector.nextElement();
            alien.move( alien.getDirection() * alien.getSpeed() );
            
            if ( alien.getX() > 800 ) {
                alien.setDying( true );
            }
            if ( alien.getX() < 0 ) {
                alien.setDying( true );
            }
        }
        
        aliens.trimToSize();
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
    
    public void laserAnimations() {
        if ( !laser.isVisible() && ctrlDown ) {
            laser = new LaserBeam( rocketship.getX(), rocketship.getY() );
            laser.setVisible( true );
            new Sound("sounds/Laser.wav").play();
        }
        if ( laser.isVisible() ) {
            int laserX = laser.getX();
            int laserY = laser.getY();
            
            Iterator vectIt = aliens.iterator();
            while ( vectIt.hasNext() ) {
                alien = (Alien) vectIt.next();
                int alienX = alien.getX();
                int alienY = alien.getY();
                
                if ( alien.isVisible() && laser.isVisible() ) {
                    if ( collisionDetector( laserX, laserY, 21, alienX, alienY, 45, 31, 2 ) ) {
                        ImageIcon image = new ImageIcon( this.getClass().getResource( explosion ) );
                        alien.setImage( image.getImage() );
                        alien.setDying( true );
                        laser.die();
                        new Sound("sounds/Alien.wav").play();
                        alienDeaths++;
                        if ( alien.getSpecies() == 0 ) {
                            scoreCount += 5;
                        } else if ( alien.getSpecies() == 1 ) {
                            scoreCount += 10;
                        } else if ( alien.getSpecies() == 2 ) {
                            scoreCount += 20;
                        } else {
                            scoreCount += 40;
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
                    alienDeaths += 2;
                    drawBonus = true;
                }
            }
            
            int y = laser.getY();
            y -= 30;
            if ( y < 0 )
                laser.die();
            else 
                laser.setY( y );
        }
    } 

    public void fireballAnimations() {
        Random r = new Random();        
        vector = aliens.elements();
        while ( vector.hasMoreElements() ) {
            alien = (Alien) vector.nextElement();
            int num = 50;
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
                        new Sound("sounds/Explosion.wav").play();
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
                    timeLimit.setVisible( false );
                } else {
                    Graphics g = getGraphics();
                    gamePaused = true;
                    if ( timer != null ) {
                        timer.stop();
                    }
                    timeLimit.setVisible( false );
                    drawPause( g );
                }
            } else if ( code == KeyEvent.VK_M || code == 109 ) {
                if ( timer != null ) {
                    timer.stop();
                }
                frame.remove( timeLimit );
                setVisible( false );
                frame.add( new ModesPanel( frame ) );
                frame.setVisible( false );
                frame.setVisible( true );
            } else if ( !gameStarted && ( code != KeyEvent.VK_M || code != 109 ) && ( code != KeyEvent.VK_P || code != 112 ) ) {
                timeLimit.setVisible( false );
                gameStarted = true;
                gameRunning = true;
                startGame();
            } else if ( !gameRunning && ( code != KeyEvent.VK_M || code != 109 ) && ( code != KeyEvent.VK_P || code != 112 ) ) {
                if ( timer != null ) {
                    timer.stop();
                }
                timeLimit.setVisible( false );
                shields = false;
                course = 0;
                speed = 0;
                multiplier = 0;
                ctrShields = 0;
                ctr4 = 0;
                aliensInit = 0;
                seconds = 20;
                minutes = 0;
                directionChanges = 0;
                direction = -2;
                alienDeaths = 0;
                scoreCount = 0;
                ctr = 0;
                ctrlDown = false;
                lives = 3;
                score = " ";
                gameRunning = true;
                startGame();
            }
        }
    }
}
