package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        tm = tileManager;
        mp = mapParameters;

        canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;
            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
        redrawOnNextPulse();
    }

    public Pane pane(){

        canvas.setOnScroll(s ->{
            if (s.getDeltaY() > 0) {
                mp.changeZoomLevel(1);
            } else {
                mp.changeZoomLevel(-1);
            }
            redrawOnNextPulse();

        });

        canvas.setOnMousePressed(e1 -> {
            double[] difference = {e1.getX(), e1.getY()};
            canvas.setOnMouseDragged(e2 -> {
                /*System.out.println("e1 " + e1.getX() + " " + e1.getY());*/
                System.out.println("e2 " + e2.getX() + " " + e2.getY());
                mp.scroll( difference[0] - e2.getX() ,difference[1] - e2.getY());
                difference[0] = e2.getX();
                difference[1] = e2.getY();
                redrawOnNextPulse();
            });
        });
        draw();
        return pane;
    }

    private void draw(){
        GraphicsContext context = canvas.getGraphicsContext2D();
        try {
            for (double h = -mp.getMinY() % TILE_SIZE; h < canvas.getHeight() ; h += TILE_SIZE) {
                for (double w = -mp.getMinX() % TILE_SIZE; w < canvas.getWidth(); w += TILE_SIZE) {
                    context.drawImage(tm.getTileImageAt(new TileManager.TileId
                                    (mp.getZoom(), (int) ((mp.getMinX() + w) / TILE_SIZE),
                                            (int) ((mp.getMinY() + h)/ TILE_SIZE))), w, h);
                }
            }
           /* System.out.println(canvas.getHeight());
            System.out.println(canvas.getWidth());*/
        }catch (IOException e) {
            System.out.println("Missing tile");
        }

    }
    public void centerOn(GeoPos pos){

    }

    private void redrawIfNeeded(){
        if (!redrawNeeded) return;
        redrawNeeded = false;
        draw();
    }

    private void redrawOnNextPulse() {
        redrawNeeded = true;
        Platform.requestNextPulse();
    }


}
