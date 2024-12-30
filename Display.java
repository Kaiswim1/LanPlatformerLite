import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Display extends JPanel {
    private static int frameHeight = 400;
    private static int frameWidth = 400;

    public static int getFrameHeight(){
        return frameHeight;
    }
    public static int getFrameWidth(){
        return frameWidth;
    }
    private static JFrame frame;

    private static MultiplayerMenu multiplayerMenu = new MultiplayerMenu();
    public static JFrame getGameFrame(){return frame;}

    public static void startGameplay() throws IOException {
        System.out.println("Started gameplay");

        GameGrid gameGrid = new GameGrid();  // Create the game grid

        frame.remove(multiplayerMenu);       // Remove the multiplayer menu
        frame.add(gameGrid);                 // Add the game grid
        gameGrid.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // Optional: set bounds manually

        gameGrid.requestFocusInWindow();     // Ensure the game grid gets focus

        frame.revalidate();                  // Revalidate the frame after changes
        frame.repaint();                     // Repaint the frame to show the updated layout
    }



    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "true");
        frame = new JFrame("Grid Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(multiplayerMenu);
        //frame.add(new GameGrid(400, 400)); Actual game
        frame.setVisible(true);
    }
}
