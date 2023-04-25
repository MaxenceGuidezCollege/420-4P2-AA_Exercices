package org.calma.pig.exercices.laboSpacial;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.calma.pig.exercices.laboSpacial.models.cell.Cell;
import org.calma.pig.exercices.laboSpacial.models.cell.CellListener;
import org.calma.pig.exercices.laboSpacial.models.cell.CellState;
import org.calma.pig.exercices.laboSpacial.models.obstacle.Obstacle;
import org.calma.pig.exercices.laboSpacial.repositories.obstacle.IObstacleRepository;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SpaceGrid extends Grid {
    private IObstacleRepository emplacementRepository;

    public SpaceGrid(IObstacleRepository emplacementRepository, int columns, int rows) throws IOException {
        super(columns, rows);

        this.emplacementRepository = emplacementRepository;

        // Cell clicked
        this.setOnMouseClicked(new CellListener.CellClickListener(this));
        // Cell dragged into
        this.setOnMouseDragged(new CellListener.CellClickListener(this));
        // Cell hovered over
        this.setOnMouseMoved(new CellListener.CellHoverListener(this));

        this.initializeGrid(this.emplacementRepository.findAll());
    }

    public void initializeGrid(List<Obstacle> obstacles) {
        // On update la position des cellules en fonction de la vraie position de l'emplacement

        for (Iterator<Obstacle> iterator = obstacles.iterator(); iterator.hasNext(); ) {
            Obstacle obstacle = iterator.next();

            List<Cell> cells = obstacle.getGeometry();
            for (Iterator<Cell> cellIterator = cells.iterator(); cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();

                Cell cellUpdated = new Cell((int) (cell.getX() + obstacle.getRealPosition().getX()),
                                        (int) (cell.getY() + obstacle.getRealPosition().getY()),
                                        obstacle.getColor()
                                );

                if(cell.getState() == CellState.NOT_WALKABLE) {
                    cellUpdated.setColor(obstacle.getColor().darker());
                }
                else{
                    cellUpdated.setColor(obstacle.getColor().brighter());
                }

                cellUpdated.setLabel(obstacle.getDescription() + " " + obstacle.getType().name() + " " + cell.getType() + " " + cell.getState());

                this.setCell(cellUpdated);
            }

            List<Cell> entryPoints = obstacle.getEntryPoints();
            for (Iterator<Cell> cellIterator = entryPoints.iterator(); cellIterator.hasNext(); ) {
                Cell entryPoint = cellIterator.next();

                Cell cellUpdated = null;

                cellUpdated = new Cell( (int) (entryPoint.getX() + obstacle.getRealPosition().getX()),
                                        (int) (entryPoint.getY() + obstacle.getRealPosition().getY()),
                                        Color.ORANGE
                );

                cellUpdated.setLabel(obstacle.getDescription() + " " + obstacle.getType().name() + " " + entryPoint.getType() + " " + entryPoint.getState());

                this.setCell(cellUpdated);
            }
        }
    }

    @Override
    public void onCellClick(MouseEvent event, Cell cell, int gridX, int gridY) {
        //Si la touche CTLR n'est pas enfoncé, on retourne. Voir https://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/MouseEvent.html#isControlDown--
        if(!event.isControlDown()){
            return;
        }
    }


    @Override
    public void onCellHover(Cell cell, int gridX, int gridY) {
        //this.informationsLabel.setText("x: " + gridX + " y:" + gridY + " " + cell.getLabel());
        System.out.println("x: " + gridX + " y:" + gridY + " " + cell.getLabel());
    }
}
