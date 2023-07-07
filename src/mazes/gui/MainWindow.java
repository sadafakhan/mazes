package mazes.gui;

import mazes.logic.MazeSolver;
import mazes.logic.carvers.MazeCarver;
import mazes.logic.generators.MazeBaseGenerator;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * The main entry point of the user interface.
 */
public class MainWindow extends JFrame {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    private final MazeSolver mazeSolver;

    /**
     * Responsible for configuring and launching the GUI.
     */
    public static void launch(Map<String, MazeBaseGenerator> baseGenerators,
                              Map<String, MazeCarver> mazeCarvers,
                              MazeSolver mazeSolver) {
        EventQueue.invokeLater(() -> {
            MainWindow window = new MainWindow(
                    "MazeGenerator",
                    DEFAULT_WIDTH,
                    DEFAULT_HEIGHT,
                baseGenerators,
                mazeCarvers,
                mazeSolver);
            window.construct();
        });
    }

    private final String title;
    private final int width;
    private final int height;
    private ImageDrawer drawer;

    private final Map<String, MazeBaseGenerator> baseGenerators;
    private final Map<String, MazeCarver> baseCarvers;

    public MainWindow(String title, int width, int height,
                      Map<String, MazeBaseGenerator> baseGenerators,
                      Map<String, MazeCarver> baseCarvers, MazeSolver mazeSolver) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.baseGenerators = baseGenerators;
        this.baseCarvers = baseCarvers;
        this.mazeSolver = mazeSolver;
    }

    public void construct() {
        this.setUpMainWindow();

        ImagePanel imagePanel = this.makeDrawingPane();
        ControlsPanel controlsPanel = new ControlsPanel(
            this.drawer, this.baseGenerators, this.baseCarvers, this.mazeSolver);

        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Set up pane
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(imagePanel);
        pane.add(controlsPanel);
        this.add(pane);

        // Finish setting up geometry
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setUpMainWindow() {
        this.setTitle(this.title);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Ignore
        }
    }

    public ImagePanel makeDrawingPane() {
        BufferedImage image = new BufferedImage(
                this.width,
                this.height, BufferedImage.TYPE_BYTE_INDEXED);

        // Set up initial background color
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width + 1, this.height + 1);

        this.drawer = new ImageDrawer(image, 5);

        ImagePanel panel = new ImagePanel(image);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
}
