package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;


import java.io.IOException;

public final class BaseMapController {

    private final TileManager tm;
    private final MapParameters mp;
    private final Canvas canvas;
    private final Pane pane;

    private final static int TILE_SIZE = 256;


    private boolean redrawNeeded;

    public BaseMapController(TileManager tileManager, MapParameters mapParameters) {
        canvas = new Canvas();
        pane = new Pane(canvas);
        tm = tileManager;
        mp = mapParameters;

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
    }

    public Pane pane() throws IOException {
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        GraphicsContext context = canvas.getGraphicsContext2D();
        try {
            context.drawImage(tm.getTileImageAt
                    (new TileManager.TileId(mp.getZoom(), (int)(mp.getMinX()/TILE_SIZE), (int)(mp.getMinY()/TILE_SIZE))), 0, 0);
            System.out.println(canvas.getHeight());
            System.out.println(canvas.getWidth());
        }catch (IOException e) {
            System.out.println("Missing tile");
        }

        return pane;
    }

    public void centerOn(GeoPos pos){

    }

    private void redrawIfNeeded(){
        if (!redrawNeeded) return;
        redrawNeeded = false;
        try {
            pane();
        } catch (IOException e) {
            System.out.println("Missing tile");
        }
    }

    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }


}
