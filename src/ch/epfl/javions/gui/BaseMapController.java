package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.WebMercator;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.Pane;


import java.io.IOException;


/**
 * Class used to manage the base map.
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public final class BaseMapController {

    private final TileManager tm;
    private final MapParameters mp;
    private final Canvas canvas;
    private final Pane pane;

    private final static int TILE_SIZE = 256;


    private boolean redrawNeeded;

    /**
     * Constructor of the base map controller. In parameter is given the tile manager and the map parameters.
     *
     * @param tileManager (TileManager) : the tile manager of the map
     * @param mapParameters (MapParameters) : the map parameters
     */
    public BaseMapController(TileManager tileManager, MapParameters mapParameters) {
        canvas = new Canvas();
        pane = new Pane(canvas);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        canvas.heightProperty().addListener(chg -> redrawOnNextPulse());
        canvas.widthProperty().addListener(chg -> redrawOnNextPulse());
        tm = tileManager;
        mp = mapParameters;

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
        redrawOnNextPulse();

        LongProperty minScrollTime = new SimpleLongProperty();
        pane.setOnScroll(s -> {
            int zoomDelta = (int) Math.signum(s.getDeltaY());
            if (zoomDelta == 0) return;

            long currentTime = System.currentTimeMillis();
            if (currentTime < minScrollTime.get()) return;
            minScrollTime.set(currentTime + 200);

            if (s.getDeltaY() > 0) {
                mp.scroll(s.getX(), s.getY());
                mp.changeZoomLevel(1);
                mp.scroll(-s.getX(), -s.getY());
            }
            if (s.getDeltaY() < 0) {
                mp.scroll(s.getX(), s.getY());
                mp.changeZoomLevel(-1);
                mp.scroll(-s.getX(), -s.getY());
            }
            redrawOnNextPulse();
        });

        canvas.setOnMousePressed(e1 -> {
            // Use an array because it has to be final so the use of double is not convenient there
            double[] difference = {e1.getX(), e1.getY()};
            canvas.setOnMouseDragged(e2 -> {
                mp.scroll(difference[0] - e2.getX(), difference[1] - e2.getY());
                difference[0] = e2.getX();
                difference[1] = e2.getY();
                redrawOnNextPulse();
            });
        });
        draw();
    }

    /**
     * Return the pane with the map drawn on.
     *
     * @return (Pane) : the pane with the map drawn on.
     */
    public Pane pane() {return pane;}

    private void draw() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        int[] pos = new int[3];
        for (double h = -mp.getMinY() % TILE_SIZE; h < canvas.getHeight(); h += TILE_SIZE) {
            for (double w = -mp.getMinX() % TILE_SIZE; w < canvas.getWidth(); w += TILE_SIZE) {
                pos[0] = mp.getZoom();
                pos[1] = (int) ((mp.getMinX() + w) / TILE_SIZE);
                pos[2] = (int) ((mp.getMinY() + h) / TILE_SIZE);
                try {
                    if(TileManager.TileId.isValid(pos[0], pos[1],
                            pos[2])) {
                        context.drawImage(tm.getTileImageAt(new TileManager.TileId
                                (pos[0], pos[1], pos[2])), w, h);
                    }
                } catch (IOException e) {
                    System.out.println("Missing tile");
                }
            }
        }
    }

    /**
     * Center the map on the given GeoPos.
     *
     * @param pos (GeoPos) : the position of the future center of the map
     */
    public void centerOn(GeoPos pos) {
        mp.scroll(WebMercator.x(mp.getZoom(), pos.longitude()) - (canvas.getWidth() / 2) - mp.getMinX(),
                WebMercator.y(mp.getZoom(), pos.latitude()) - (canvas.getHeight() / 2) - mp.getMinY());
        redrawOnNextPulse();

    }

    private void redrawIfNeeded() {
        if (!redrawNeeded) return;
        redrawNeeded = false;
        draw();
    }

    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }


}
