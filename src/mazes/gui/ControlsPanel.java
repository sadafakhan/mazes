package mazes.gui;

import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeSolver;
import mazes.logic.carvers.MazeCarver;
import mazes.logic.generators.MazeBaseGenerator;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.Set;

/**
 * The UI component with the maze generation/solving options/buttons.
 */
public class ControlsPanel extends JPanel {
    private final ImageDrawer drawer;

    private final Map<String, MazeBaseGenerator> baseGenerators;
    private final Map<String, MazeCarver> baseCarvers;
    private final MazeSolver mazeSolver;

    private String baseGeneratorName;
    private String baseCarverName;

    private Room start;
    private Room end;
    private Set<Wall> pathways;

    public ControlsPanel(ImageDrawer drawer,
                         Map<String, MazeBaseGenerator> baseGenerators,
                         Map<String, MazeCarver> baseCarvers,
                         MazeSolver mazeSolver) {
        this.drawer = drawer;
        this.baseGenerators = baseGenerators;
        this.baseCarvers = baseCarvers;
        this.mazeSolver = mazeSolver;

        this.buildLayout();

        this.generateMaze(null);
    }

    private void buildLayout() {
        this.setLayout(new GridBagLayout());

        JLabel label1 = new JLabel();
        label1.setText("Maze base shape");
        this.add(label1, 0, 0, GridBagConstraints.WEST);

        JLabel label2 = new JLabel();
        label2.setText("Maze generator");
        this.add(label2, 0, 1, GridBagConstraints.WEST);

        JComboBox<String> baseShapeComboBox = new JComboBox<>(this.getKeysAsArray(this.baseGenerators));
        baseShapeComboBox.addItemListener(this::onBaseShapeChange);
        this.baseGeneratorName = (String) baseShapeComboBox.getSelectedItem();
        this.add(baseShapeComboBox, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0);

        JComboBox<String> mazeGeneratorComboBox = new JComboBox<>(this.getKeysAsArray(this.baseCarvers));
        mazeGeneratorComboBox.addItemListener(this::onBaseCarverChange);
        this.baseCarverName = (String) mazeGeneratorComboBox.getSelectedItem();
        this.add(mazeGeneratorComboBox, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0);

        this.add(new JPanel(), 2, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 0.5);
        this.add(new JPanel(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 0.5);

        JButton generateMazeButton = new JButton();
        generateMazeButton.setText("Generate new maze");
        generateMazeButton.addActionListener(this::generateMaze);
        this.add(generateMazeButton, 3, 0, GridBagConstraints.EAST);

        JButton findPathButton = new JButton();
        findPathButton.setText("Find shortest path");
        findPathButton.addActionListener(this::drawShortestPath);
        this.add(findPathButton, 3, 1, GridBagConstraints.EAST);
    }

    private String[] getKeysAsArray(Map<String, ?> map) {
        return map.keySet().toArray(String[]::new);
    }

    private void onBaseShapeChange(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            this.baseGeneratorName = (String) event.getItem();
        }
    }

    private void onBaseCarverChange(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            this.baseCarverName = (String) event.getItem();
        }
    }

    private void generateMaze(ActionEvent event) {
        this.drawer.clear();

        MazeBaseGenerator generator = this.baseGenerators.get(this.baseGeneratorName);
        MazeCarver carver = this.baseCarvers.get(this.baseCarverName);

        Rectangle bound = this.drawer.getBoundingBox();
        Maze baseMaze = generator.generateBaseMaze(bound);
        MazeCarver.CarvedMaze finalMaze = carver.carveMaze(baseMaze);

        this.pathways = finalMaze.pathways();
        this.start = baseMaze.getStart();
        this.end = baseMaze.getEnd();

        this.drawer.drawWalls(finalMaze.walls());
        this.drawer.drawPoint(this.start.getCenter());
        this.drawer.drawPoint(this.end.getCenter());
    }

    private void drawShortestPath(ActionEvent event) {
        this.mazeSolver.solveMaze(this.pathways, this.start, this.end).ifPresentOrElse(
            this.drawer::drawPath,
            () -> JOptionPane.showMessageDialog(
                null,
                "This maze seems to be unsolvable: no path could be found between the start and the end!")
        );
    }

    private void add(JComponent component, int x, int y, int anchor, int fill, double weightX) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = weightX;
        this.add(component, gbc);
    }

    private void add(JComponent component, int x, int y, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        gbc.weightx = 0.0;
        this.add(component, gbc);
    }
}
