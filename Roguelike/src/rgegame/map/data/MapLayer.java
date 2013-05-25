/*
 * MapLayer.java
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

/**
 * One layer for a map.
 * 
 * @author Hj. Malthaner
 */
public class MapLayer
{
    private String tileSetFileName;

    private int width;
    private int height;

    private int [] content;
    private int [] xoff;
    private int [] yoff;

    /**
     * The value for the area outside of this map.
     */
    private int outside;

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    /**
     * Get the value for places outside this map
     * @return the outside
     */
    public int getOutside() {
        return outside;
    }

    /**
     * Set the value for places outside this map
     * @param outside the outside to set
     */
    public void setOutside(int outside) {
        this.outside = outside;
    }

    /**
     * @return the tileSetFileName
     */
    public String getTileSetFileName() {
        return tileSetFileName;
    }

    /**
     * @param tileSetFileName the tileSetFileName to set
     */
    public void setTileSetFileName(String tileSetFileName) {
        this.tileSetFileName = tileSetFileName;
    }

    public MapLayer(int width, int height)
    {
        this.width = width;
        this.height = height;

        content = new int[width * height];
        xoff = new int[width * height];
        yoff = new int[width * height];
    }

    public int get(final int x, final int y)
    {
        if(x>=0 && y>=0 && x<width && y<height) {
            return content[y*width + x];
        } else {
            return getOutside();
        }
    }

    public void set(int x, int y, int value)
    {
        if(x>=0 && y>=0 && x<width && y<height) {
            content[y*width + x] = value;
        }
    }

    public int getXoff(final int x, final int y)
    {
        if(x>=0 && y>=0 && x<width && y<height) {
            return xoff[y*width + x];
        } else {
            return 0;
        }
    }

    public int getYoff(final int x, final int y)
    {
        if(x>=0 && y>=0 && x<width && y<height) {
            return yoff[y*width + x];
        } else {
            return 0;
        }
    }

    public void setXoff(int x, int y, int value)
    {
        if(x>=0 && y>=0 && x<width && y<height) {
            xoff[y*width + x] = value;
        }
    }

    public void setYoff(int x, int y, int value)
    {
        if(x>=0 && y>=0 && x<width && y<height) {
            yoff[y*width + x] = value;
        }
    }

    public void write(Writer writer) throws IOException
    {
        /*
        writer.write("Map Layer\n");
        writer.write("v.2\n");
        writer.write("" + width + "\n");
        writer.write("" + height + "\n");
        writer.write("End Of Header\n");

        for(int i=0; i<content.length; i++) {
            writer.write("" + content[i] + "\n");
        }
         */

        writer.write("Map Layer\n");
        writer.write("v.3\n");
        writer.write("" + width + "\n");
        writer.write("" + height + "\n");
        writer.write("End Of Header\n");

        for(int i=0; i<content.length; i++) {
            writer.write("" + content[i] + " " +
                    xoff[i] + " " +
                    yoff[i] + "\n");
        }
    }

    public void read(BufferedReader reader) throws IOException
    {
        String line;

        line = reader.readLine();
        if(!"Map Layer".equals(line)) {
            throw new IOException("Wrong header: " + line);
        }

        String version = reader.readLine();
        if(!"v.1".equals(version) &&
           !"v.2".equals(version) &&
           !"v.3".equals(version)) {
            throw new IOException("Wrong version: " + version);
        }

        line = reader.readLine();
        width = Integer.parseInt(line);

        line = reader.readLine();
        height = Integer.parseInt(line);

        if("v.2".equals(version) || "v.3".equals(version)) {
            do {
                line = reader.readLine();
                if(line == null) {
                   throw new IOException("Unexpected end of file.");
                }
            } while(!"End Of Header".equals(line));
        }

        final int size = width * height;
        content = new int [size];
        xoff = new int [size];
        yoff = new int [size];

        boolean hasOffsets = "v.3".equals(version);

        for(int i=0; i<size; i++) {
            line = reader.readLine();

            if(hasOffsets) {
                String [] parts = line.split(" ");
                content[i] = Integer.parseInt(parts[0]);
                xoff[i] = Integer.parseInt(parts[1]);
                yoff[i] = Integer.parseInt(parts[2]);

            } else {
                content[i] = Integer.parseInt(line);
            }
        }

        if("v.1".equals(version)) {
            for(int i=0; i<size; i++) {
                content[i] ++;
            }
        }

    }
}
