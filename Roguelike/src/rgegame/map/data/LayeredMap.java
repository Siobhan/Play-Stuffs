/*
 * LayeredMap.java
 *
 * Created on 06.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.map.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * A rectangular map structure consisting of several layers.
 * 
 * @author Hj. Malthaner
 */
public class LayeredMap
{
    private ArrayList <MapLayer> layers;
    private String [] scripts;

    /** default player spawn x coordinate */
    private int spawnX;
    /** default player spawn y coordinate */
    private int spawnY;

    /**
     * 0 = isometric
     * 1 = rectangular
     * 2 = isometric, horizontal-run
     */
    public int layoutType;

    /**
     * @return The number of layers in this map
     */
    public int getLayerCount()
    {
        return layers.size();
    }

    /**
     * Player spawn X coordinate
     */
    public int getSpawnX() {
        return spawnX;
    }

    /**
     * Player spawn Y coordinate
     */
    public int getSpawnY() {
        return spawnY;
    }

    /**
     * Player spawn X coordinate
     */
    public void setSpawnX(int x) {
        spawnX = x;
    }

    /**
     * Player spawn Y coordinate
     */
    public void setSpawnY(int y) {
        spawnY = y;
    }

    /**
     * @return The width (x axis extension) of this map
     */
    public int getWidth()
    {
        return layers.get(0).getWidth();
    }

    /**
     * @return The height (y axis extension) of this map
     */
    public int getHeight()
    {
        return layers.get(0).getHeight();
    }

    public String getTileSetFileName(int layer)
    {
        return layers.get(layer).getTileSetFileName();
    }

    public void setTileSetFileName(int layer, String tileSetFileName)
    {
        System.err.println("Using tileset for layer " +
                           layer + ": " + tileSetFileName);
        layers.get(layer).setTileSetFileName(tileSetFileName);
    }

    /**
     * Constructs a new layered map with the given size
     * and layer count.
     *
     * @param layerCount The number of layers to construct
     * @param width The x axis extension of the map
     * @param height The y axis extension of the map
     */
    public LayeredMap(int layerCount, int width, int height)
    {
        layers = new ArrayList<MapLayer>(layerCount);
        for(int i=0; i<layerCount; i++) {
            layers.add(new MapLayer(width, height));
        }

        final int size = width * height;
        scripts = new String [size];

        for(int i=0; i<size; i++) {
            scripts[i] = "";
        }
    }

    /**
     * Get the tile/graphics object index for a coordinate
     *
     * @param layer The layer, 0 is lowermost layer
     * @param x The X coordinate 
     * @param y The Y coordinate 
     */
    public int get(final int layer, final int x, final int y)
    {
        if(layer >= 0 && layer < layers.size()) {
            MapLayer mapLayer = layers.get(layer);
            return mapLayer.get(x, y);
        } else {
            return -1;
        }
    }

    /**
     * Set the tile/graphics object index for a coordinate
     *
     * @param layer The layer, 0 is lowermost layer
     * @param x The X coordinate 
     * @param y The Y coordinate 
     * @param value The tile/graphics object index to set
     */
    public void set(int layer, int x, int y, int value)
    {
        if(layer >= 0 && layer < layers.size()) {
            MapLayer mapLayer = layers.get(layer);
            mapLayer.set(x, y, value);
        }
    }

    /**
     * Set the value for places outside this map.
     *
     * @param layer the map layer to set
     * @param value the outside value to set
     */
    public void setOutside(int layer, int value)
    {
        if(layer >= 0 && layer < layers.size()) {
            MapLayer mapLayer = layers.get(layer);
            mapLayer.setOutside(value);
        }
    }


    /**
     * Tile/graphics objects can be positioned at a certain offset
     * from the map square's center.
     *
     * @return the offset along the X axis 
     */
    public int getXoff(final int layer, final int x, final int y)
    {
        if(layer >= 0 && layer < layers.size()) {
            MapLayer mapLayer = layers.get(layer);
            return mapLayer.getXoff(x, y);
        } else {
            return 0;
        }
    }

    /**
     * Tile/graphics objects can be positioned at a certain offset
     * from the map square's center.
     *
     * @return the offset along the Y axis 
     */
    public int getYoff(final int layer, final int x, final int y)
    {
        if(layer >= 0 && layer < layers.size()) {
            MapLayer mapLayer = layers.get(layer);
            return mapLayer.getYoff(x, y);
        } else {
            return 0;
        }
    }

    /**
     * Tile/graphics objects can be positioned at a certain offset
     * from the map square's center.
     *
     * @param layer The layer number of the affected square
     * @param x The X coordinate of the square
     * @param y The Y coordinate of the square
     * @param xoff The offset along the X axis
     * @param yoff the offset along the Y axis
     */
    public void setXYoff(int layer, int x, int y, int xoff, int yoff)
    {
        if(layer >= 0 && layer < layers.size()) {
            MapLayer mapLayer = layers.get(layer);
            mapLayer.setXoff(x, y, xoff);
            mapLayer.setYoff(x, y, yoff);
        }
    }

    public String getScript(int x, int y)
    {
        final int w = layers.get(0).getWidth();
        final int h = layers.get(0).getHeight();

        if(x >= 0 && x < w && y >= 0 && y<h) {
            return scripts[y*w + x];
        }

        return "";
    }

    public void setScript(int x, int y, String script)
    {
        final int w = layers.get(0).getWidth();
        final int h = layers.get(0).getHeight();

        if(x >= 0 && x < w && y >= 0 && y<h) {
            scripts[y*w + x] = script;
        }
    }

    /**
     * Insert an area from a map into this map at a given location.
     * @param source the map to read from
     * @param sx source x coordinate
     * @param sy source y coordinate
     * @param width width of the area to insert
     * @param height height of the area to insert
     * @param dx destination x coordinate
     * @param dy destination y coordinate
     */
    public void insert(final LayeredMap source, 
                       final int sx, final int sy,
                       final int width, final int height,
                       final int dx, final int dy)
    {
        final int minLayers = Math.min(source.getLayerCount(), getLayerCount());

        for(int layer=0; layer<minLayers; layer++) {
            for(int y=0; y<height; y++) {
                for(int x=0; x<width; x++) {
                    final int value = source.get(layer, sx+x, sy+y);
                    set(layer, dx+x, dy+y, value);
                }
            }
        }
    }

    /**
     * Write map data into a text file.
     */
    public void write(Writer writer) throws IOException
    {
        writer.write("Layered Map\n");
        writer.write("v.2\n");
        writer.write("" + layoutType + "\n");
        writer.write("End Of Header\n");
        writer.write("" + layers.size() + "\n");
        for(int i=0; i<layers.size(); i++) {
            layers.get(i).write(writer);
        }

        writer.write("Map Scripts\n");

        for(int i=0; i<scripts.length; i++) {
            String script = scripts[i];
            if(script != null && script.length() > 0) {
                writer.write("<script>\n");
                writer.write("" + i + "\n");
                writer.write("" + script + "\n");
                writer.write("</script>\n");
            }
        }

        writer.write("End of Map Scripts\n");
    }

    /**
     * Read map data from a text file.
     */
    public void read(BufferedReader reader) throws IOException
    {
        String line;

        line = reader.readLine();
        if(!"Layered Map".equals(line)) {
            throw new IOException("Wrong header: " + line);
        }

        line = reader.readLine();
        if(!"v.1".equals(line) &&
           !"v.2".equals(line)) {
            throw new IOException("Wrong version: " + line);
        }

        if("v.2".equals(line)) {
            line = reader.readLine();
            layoutType = Integer.parseInt(line);

            line = reader.readLine();
            if(!"End Of Header".equals(line)) {
                throw new IOException("Wrong header: " + line);
            }
        }

        line = reader.readLine();
        int size = Integer.parseInt(line);

        layers.clear();

        for(int i=0; i<size; i++) {
            layers.add(new MapLayer(0, 0));
            layers.get(i).read(reader);
        }

        int width = layers.get(0).getWidth();
        int height = layers.get(0).getHeight();
        
        size = width * height;
        scripts = new String [size];

        for(int i=0; i<size; i++) {
            scripts[i] = "";
        }

        line = reader.readLine();
        if(line != null) {
            if(!"Map Scripts".equals(line)) {
                throw new IOException("Wrong map script header: " + line);
            }

            do {
                line = reader.readLine();

                if(line != null && "<script>".equals(line)) {
                    line = reader.readLine();
                    int index = Integer.parseInt(line);
                    StringBuffer buf = new StringBuffer();

                    line = reader.readLine();
                    while(line != null && !"</script>".equals(line)) {
                        buf.append(line);
                        buf.append("\n");
                        line = reader.readLine();
                    }

                    if("</script>".equals(line)) {
                        scripts[index] = buf.toString();
                    }
                }
            } while(!"End of Map Scripts".equals(line) && line != null);
        }
    }
}
