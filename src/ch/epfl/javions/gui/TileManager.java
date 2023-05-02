package ch.epfl.javions.gui;

import ch.epfl.javions.Preconditions;
import ch.epfl.javions.WebMercator;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TileManager {

    private String serverName;
    private Path diskCachePath;
    private Map<TileId, Image> memoryCache;


    public TileManager(Path cachePath, String serverName) {
        this.serverName = serverName;
        diskCachePath = cachePath;
        memoryCache = new LinkedHashMap<>();
    }

    public Image getTileImageAt(TileId id) throws IOException {
        Image value = memoryCache.get(id);
        if (value != null) {
            return value;
        }

        String zoomFolder ="/" + id.zoom;
        String imageFolder = "/" + id.x;
        String imageLocation = "/" + id.y + ".png";
        String filePath = diskCachePath.toString() + zoomFolder + imageFolder + imageLocation;
        if (Files.exists(Path.of(filePath))) {
            return new Image(new FileInputStream(filePath));
        }

        URLConnection connection = new URL("https://" + serverName + zoomFolder +
                imageFolder + imageLocation).openConnection();
        connection.setRequestProperty("User-Agent", "Javions");
        byte[] imageByte;
        try (InputStream in = connection.getInputStream()) {
            imageByte = in.readAllBytes();
            if (!Files.exists(diskCachePath)) {
                Files.createDirectory(diskCachePath);
            }
            if (!Files.exists(Path.of(diskCachePath.toString() + zoomFolder))) {
                Files.createDirectory(Path.of(diskCachePath.toString() + zoomFolder));
            }
            if (!Files.exists(Path.of(diskCachePath.toString() + zoomFolder + imageFolder))) {
                Files.createDirectory(Path.of(diskCachePath.toString() + zoomFolder + imageFolder));
            }

            try(OutputStream out = new FileOutputStream(filePath)) {
                out.write(imageByte);
            }
        }
        value = new Image(new FileInputStream(filePath));
        if (memoryCache.size() >= 100) {
            memoryCache.remove(memoryCache.keySet().iterator().next());
        }
        memoryCache.put(id, value);
        return value;
    }
    public record TileId(int zoom, int x, int y) {

        public static boolean isValid(int zoom, double x, double y) {
            return (zoom >= 6) && (zoom <= 19) && (x < Math.pow(2, zoom))
                    && (0 <= x) && (y < Math.pow(2, zoom)) && (0 <= y);
        }

    }

}
