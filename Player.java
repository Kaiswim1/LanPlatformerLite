import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class Player implements KeyListener, Runnable {
    private BufferedImage sprite;
    private Rectangle hitBox;
    private int fallSpeed;
    private int moveSpeed = 6;
    private int width;
    private int height;
    private int row;
    private int col;
    private final HashSet<Integer> pressedKeys = new HashSet<>();
    private Color hitboxColor = new Color(255, 0, 0, 60);

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }


    public Player(String spritePath, int width, int height, int row, int col) throws IOException {
        this.sprite = ImageIO.read(new File(spritePath));
        this.width = width;
        this.height = height;
        this.row = row;
        this.col = col;
        this.hitBox = new Rectangle(this.col + 9, this.row + 4, this.width - 20, this.height - 6);
        new Thread(this).start();
    }

   public void drawPlayer(Graphics g) {g.drawImage(sprite, this.col, this.row, this.width, this.height, null);}



    public void drawHitBox(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
        g.setColor(hitboxColor);
        g.fillRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    private boolean isTouchingPlatform() {
        for (Rectangle r : GameGrid.platforms) {
            if (hitBox.intersects(r)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Typed");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Allow jumping only if the player is on a platform and the spacebar is pressed
        if (e.getKeyCode() == KeyEvent.VK_SPACE && isTouchingPlatform()) {
            fallSpeed = -10; // Apply jump force
            try {
                updatePlayerFall();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    private void updatePlayerFall() throws IOException {
        if (fallSpeed != 0) {
            updateYPos(fallSpeed);
            ServerIO.sendData("p" + MultiplayerMenu.getPlayerNum() + "y", this.row);
        }
    }


    private void updateXPos(int amount) throws IOException {
        this.col += amount;
        hitBox.x += amount;
        ServerIO.sendData("p"+MultiplayerMenu.getPlayerNum()+"x", this.col);
    }
    private void updateYPos(int amount) throws IOException {
        this.row += amount;
        hitBox.y += amount;
        ServerIO.sendData("p"+MultiplayerMenu.getPlayerNum()+"y", this.row);
    }

    private int getYourXData() throws IOException{
        return ServerIO.getData("p"+MultiplayerMenu.getPlayerNum()+"x");
    }

    private int getYourYData() throws IOException{
        return ServerIO.getData("p"+MultiplayerMenu.getPlayerNum()+"y");
    }

    private int getPlayerXData(int index) throws IOException {
        int s = ServerIO.getData("p"+index+"x");
        //System.out.println(s);
        return s;
    }





    private void updatePlayerMove() throws IOException {
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            updateXPos(-moveSpeed);
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            updateXPos(moveSpeed);
        }
    }

    private void nudgeUpHelper(int amount) throws IOException {
        updateYPos(amount);
    }

    private void nudgeUp() throws IOException {
        while (isTouchingPlatform()) {
            this.fallSpeed = 0; // Stop falling when touching the platform
            nudgeUpHelper(-1);
        }
        nudgeUpHelper(1);
    }

    public BufferedImage getSprite(){
        return sprite;
    }

    @Override
    public void run() {
        int i=0;

        while (true) {
            i++;
                this.fallSpeed++;
                try {
                    updatePlayerFall();
                    updatePlayerMove();
                    nudgeUp();
                    System.out.println("Mac is SLOWWWWWWW"+i);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            //Thread.sleep(2);
        }
    }


    @Override
    public String toString(){
        return "Player number, "+MultiplayerMenu.getPlayerNum()+" X: "+this.col+" Y: "+this.row;
    }
}
