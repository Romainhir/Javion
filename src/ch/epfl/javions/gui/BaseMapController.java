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
    private final Canvas canvas = new Canvas();


    private boolean redrawNeeded;

    public BaseMapController(TileManager tileManager, MapParameters mapParameters) {
        tm = tileManager;
        mp = mapParameters;

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
    }

    public Pane pane() throws IOException {
        Pane pane = new Pane(canvas);
        canvas.widthProperty().bind(pane.widthProperty());
        GraphicsContext context = canvas.getGraphicsContext2D();
        try {
            context.drawImage(tm.getTileImageAt
                    (new TileManager.TileId(mp.getZoom(), mp.getMinX(),mp.getMinY())), 0, 0);
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
    }

    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }


}
