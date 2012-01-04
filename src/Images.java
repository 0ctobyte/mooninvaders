package mooninvaders;

import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.*;

public class Images {
    
    private boolean visible;
    transient private Image image;
    protected int x;
    protected int y;
    protected boolean dying;
    protected int dx;
    
    public Images() {
        visible = true;
    }
    
    public void die() {
        visible = false;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    protected void setVisible( boolean visible ) {
        this.visible = visible;
    }
    
    public void setImage( Image image ) {
        this.image = image;
    }
    
    public Image getImage() {
        return image;
    }
    
    public void setX( int x ) {
        this.x = x;
    }
    
    public void setY( int y ) {
        this.y = y;
    }
    public int getY() {
        return y;
    }
    
    public int getX() {
        return x;
    }
    
    public void setDying( boolean dying ) {
        this.dying = dying;
    }
    
    public boolean isDying() {
        return this.dying;
    }
}

class Alien extends Images {
    
    private Fireball fireball;
    private Upgrade upgrade;
    private final String alien = "images/alien.gif";
    private int specie;
    private int direction;
    private int speed;
    static final int RAPID_FIRE = 0;
    static final int HEALTH = 1;
    static final int SHIELD = 2;
    static final int BOMB = 3;
    
    public Alien( int x, int y ) {
        this.x = x;
        this.y = y;
        
        upgrade = new Upgrade( x, y );
        fireball = new Fireball( x, y );
        ImageIcon image = new ImageIcon( this.getClass().getResource( alien ) );
        setImage( image.getImage() );
        
        setX( x );
        setY( y );
    }
    
    public void move( int direction ) {
        x += direction;
        setX( x );
    }
    
    public void setDirection( int direction ) {
        this.direction = direction;
    }
    
    public void setSpeed( int speed ) {
        this.speed = speed;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpecies( int spec ) {
        specie = spec;
    }
    
    public int getSpecies() {
        return specie;
    }
    
    public Fireball getMissile() {
        return fireball;
    }
    
    public Upgrade getUpgrade() {
        return upgrade;
    }
    
    public class Upgrade extends Images {
        
        private final String rapidfire = "images/rapidfire.png";
        private final String health = "images/health.png";
        private final String shield = "images/shield.png";
        private final String bomb = "images/bomb.png";
        private int upgradeType;
        private boolean destroyed = true;
        
        public Upgrade( int x, int y ) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            Random r = new Random();
            int num = 4;
            int chance = r.nextInt( num );
            
            if ( chance == RAPID_FIRE ) {
                ImageIcon image = new ImageIcon( this.getClass().getResource( rapidfire ) );
                upgradeType = 0;
                setImage( image.getImage() );
            } else if ( chance == HEALTH ) {
                ImageIcon image = new ImageIcon( this.getClass().getResource( health ) );
                upgradeType = 1;
                setImage( image.getImage() );
            } else if ( chance == SHIELD ) {
                ImageIcon image = new ImageIcon( this.getClass().getResource( shield ) );
                upgradeType = 2;
                setImage( image.getImage() );
            } else {
                ImageIcon image = new ImageIcon( this.getClass().getResource( bomb ) );
                upgradeType = 3;
                setImage( image.getImage() );
            }
            
        }
        
        public void setDestroyed( boolean destroyed ) {
            this.destroyed = destroyed;
        }
        
        public boolean isDestroyed() {
            return destroyed;
        }
        
        public int getUpgradeType() {
            return upgradeType;
        }    
    }
    
    public class Fireball extends Images {
        
        private final String fireball = "images/fireball.gif";
        private boolean destroyed = true;
        
        public Fireball( int x, int y ) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon image = new ImageIcon( this.getClass().getResource( fireball ) );
            setImage( image.getImage() );
        }
        
        public void setDestroyed( boolean destroyed ) {
            this.destroyed = destroyed;
        }
        
        public boolean isDestroyed() {
            return destroyed;
        }
    }
}

class BackgroundImage extends Images {
    
    private final int START_X = 0; 
    private final int START_Y = 0;
    
    private final String background = "images/background.jpg";
    private int width;
    
    public BackgroundImage() {
        
        ImageIcon image = new ImageIcon( this.getClass().getResource( background ) );
        
        width = image.getImage().getWidth( null );
        
        setImage( image.getImage() );
        setX( START_X );
        setY( START_Y );
    }
}

class Flame extends Images {
    
    private int x = 408; 
    private int y = 572;
    private int dx;
    private int dy;
    
    private final String flame = "images/flame.gif";
    private int width;
    
    public Flame() {
        
        ImageIcon image = new ImageIcon( this.getClass().getResource( flame ) );
        
        setImage( image.getImage() );
        setX( x );
        setY( y );
    }
    
    public void move() {
        x += dx;  
        if ( x <= 8 )
            x = 8;  
        if ( x >= 773 )
            x = 773;   
        setX( x );
    }
    
    public void moveRight() {
        dx = 10;
    }
    
    public void moveLeft() {
        dx = -10;
    }
    
    
    public void stopRight() {
        dx = 0;
    }
    
    public void stopLeft() {
        dx = 0;
    }    
}

class LaserBeam extends Images {
    
    private final String laserbeam = "images/laserbeam.gif";
    
    public LaserBeam( int x, int y ) {  
        ImageIcon image = new ImageIcon( this.getClass().getResource( laserbeam ) );
        setImage( image.getImage() );
        
        setX( x + 12 );
        setY( y + 5 );
    }
}

class Bomb extends Images {
    
    private final String bomb = "images/bombing.png";
    
    public Bomb( int x, int y ) {
        ImageIcon image = new ImageIcon( this.getClass().getResource( bomb ) );
        setImage( image.getImage() );
        
        setX( x + 12 );
        setY( y + 5 );
    }
}

class RocketShip extends Images {
    
    private int x = 400; 
    private int y = 480;
    private int dx;
    private int dy;
    
    private final String rocketship = "images/rocketship.gif";
    private int width;
    
    public RocketShip() {
        
        ImageIcon image = new ImageIcon( this.getClass().getResource( rocketship ) );
        
        width = image.getImage().getWidth( null ); 
        
        setImage( image.getImage() );
        setX( x );
        setY( y );
    }
    
    public void move() {
        x += dx;  
        if ( x <= 0)
            x = 0;  
        if ( x >= 765 )
            x = 765;   
        setX( x );
    }
    
    public void moveRight() {
        dx = 10;
    }
    
    public void moveLeft() {
        dx = -10;
    }
    
    
    public void stopRight() {
        dx = 0;
    }
    
    public void stopLeft() {
        dx = 0;
    }
}

class AlienShip extends Images {
    
    private final String alienship = "images/Alienship.gif";
    private int y = 0;
    
    public AlienShip() {
        this.x = x;
        ImageIcon image = new ImageIcon( this.getClass().getResource( alienship ) );
        setImage( image.getImage() );
        setX( x );
        setY( y );
    }
    
    public void move( int direction, int speed ) {
        x += ( direction * speed );
        setX( x );
    }
}