package ch.epfl.javions.gui;

import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class used to manage all the map tile to draw the full map. It searches the tile in a disk cache, in a map in attribute
 * and also in internet.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class TileManager {

    public static final int MAX_SIZE_OF_CACHE = 100;
    private String serverName;
    private Path diskCachePath;
    private Map<TileId, Image> memoryCache;


    /**
     * Constructor of the tile manager. In parameter is given the path of the disk cache and the server name to
     * download the tiles.
     *
     * @param cachePath  (Path) : the disk cache path
     * @param serverName (String) : the tile server name
     */
    public TileManager(Path cachePath, String serverName) {
        this.serverName = serverName;
        diskCachePath = cachePath;
        memoryCache = new LinkedHashMap<>();
    }

    /**
     * Return the image tile for a specific tile id. It searches the tile in the set, the disk cache and after, in the server
     * Throw an Exception if something went wrong while searching/writing the tile in the disk cache or in internet.
     *
     * @param id (TileId) : the id of the tile
     * @return (Image) : the corresponding image tile
     * @throws IOException if something went wrong while searching/writing the tile in the disk cache or in internet
     */
    public Image getTileImageAt(TileId id) throws IOException {
        Image value = memoryCache.get(id);
        if (value != null) {
            return value;
        }

        String zoomFolder = "/" + id.zoom;
        String imageFolder = "/" + id.x;
        String imageLocation = "/" + id.y + ".png";
        String filePath = diskCachePath.toString() + zoomFolder + imageFolder + imageLocation;
        if (Files.exists(Path.of(filePath))) {
            value = new Image(new FileInputStream(filePath));
            if (memoryCache.size() >= MAX_SIZE_OF_CACHE) {
                memoryCache.remove(memoryCache.keySet().iterator().next());
            }
            memoryCache.put(id, value);
            return value;
        }

        URLConnection connection = new URL("https://"
                + serverName
                + zoomFolder
                + imageFolder
                + imageLocation).openConnection();
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

            try (OutputStream out = new FileOutputStream(filePath)) {
                out.write(imageByte);
            }
        }
        value = new Image(new FileInputStream(filePath));
        if (memoryCache.size() >= MAX_SIZE_OF_CACHE) {
            memoryCache.remove(memoryCache.keySet().iterator().next());
        }
        memoryCache.put(id, value);
        return value;
    }

    /**
     * Inner record used to represent a tile id. The id contain a zoom level and an x/y coordinate.
     *
     * @param zoom (int) : the zoom level
     * @param x (int) : the x coordinate
     * @param y (int) : the y coordinate
     */
    public record TileId(int zoom, int x, int y) {

        /**
         * Check if the given values are valid to make a new tile id. To be valid, the zoom must be between
         * 6 and 19 (include) and the x/y coordinate must be between 0 and 2 to the power of the zoom level (2^z).
         *
         * @param zoom (int) : the zoom level
         * @param x (int) : the x coordinate
         * @param y (int) : the y coordinate
         * @return (boolean) : true if the values are valid, false otherwise
         */
        public static boolean isValid(int zoom, double x, double y) {
            return (zoom >= 6) && (zoom <= 19) && (x < Math.pow(2, zoom))
                    && (0 <= x) && (y < Math.pow(2, zoom)) && (0 <= y);
        }

    }

}
