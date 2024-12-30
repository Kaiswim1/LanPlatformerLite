import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class GameGrid extends JPanel {

    private int size;
    private Color bgColor = new Color(60, 100 ,100);
    public static HashSet<Rectangle> platforms = new HashSet<>();

    public GameGrid() throws IOException {
        this.setFocusable(true);
        this.addKeyListener(ServerIO.connectedPlayers.get(0));
    }

    private void drawAllOtherPlayers(Graphics g) throws IOException {
        int x;
        for (int i = 1; i < 5; i++) {
            try {
                x = ServerIO.getData("p" + i + "x");
                if (i != MultiplayerMenu.getPlayerNum()) {
                    g.drawImage(ServerIO.connectedPlayers.get(0).getSprite(), x, ServerIO.getData("p" + i + "y"), 40, 40, null);
                }
            } catch (NumberFormatException n) {
                continue;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Spawn platforms and draw players
        spawnPlatform(g, Color.green, new Rectangle(10, 100, 100, 10));
        spawnPlatform(g, Color.green, new Rectangle(80, 200, 100, 10));
        spawnPlatform(g, Color.orange, new Rectangle(0, 300, 500, 10));

        ServerIO.connectedPlayers.get(0).drawPlayer(g);

        try {
            drawAllOtherPlayers(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateUI();
    }

    private void spawnPlatform(Graphics g, Color c, Rectangle r) {
        g.setColor(c);
        g.fillRect(r.x, r.y, r.width, r.height);
        platforms.add(r);
    }

    public void eraseSection(Graphics g, Rectangle r) {
        // Logic to erase a section from the grid (clear and redraw as needed)
    }
}
