package ch.epfl.javions.gui;

import ch.epfl.javions.Preconditions;
import ch.epfl.javions.WebMercator;
import javafx.scene.image.Image;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        String param = "/" + id.zoom + "/" + id.x + "/";
        String imageLocation = id.y + ".png";
        String filePath = diskCachePath.toString() + param + imageLocation;
        if (Files.exists(Path.of(filePath))) {
            return new Image(filePath);
        }

        URLConnection connection = new URL("https://" + serverName + imageLocation).openConnection();
        connection.setRequestProperty("User-Agent", "Javions");
        byte[] imageByte;
        try (InputStream in = connection.getInputStream();
             OutputStream out = new FileOutputStream(filePath)) {
            imageByte = in.readAllBytes();
            if (!Files.exists(diskCachePath)) {
                Files.createDirectory(diskCachePath);
            }
            if (!Files.exists(Path.of(diskCachePath.toString(), param))) {
                Files.createDirectory(Path.of(diskCachePath.toString() + param));
            }
            out.write(imageByte);
        }
        value = new Image(filePath);
        memoryCache.put(id, value);
        return value;
    }

    //double ???
    public record TileId(int zoom, double x, double y) {

        //Ca qu'il faut faire ?
        public static boolean isValid(int zoom, double x, double y) {
            return (zoom >= 6) && (x >= 0) && (y >= 0);
        }

    }

}
