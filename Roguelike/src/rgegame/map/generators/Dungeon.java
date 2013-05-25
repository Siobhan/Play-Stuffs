/*
 * Dungeon.java
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

package rgegame.map.generators;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Random;

import rgegame.demo.pathfinding.PathSourceLink;
import rgegame.map.data.LayeredMap;
import rgegame.map.display.ColorCodes;
import rgegame.pathfinding.CoordinatePathDestination;
import rgegame.pathfinding.Path;
import rgegame.pathfinding.PathSource;

/**
 * This generator creates a classical rooms and corridor maze.
 * The rooms are positioned in a grid with random deviations.
 *
 * @see MapGenerator
 * 
 * @author Hj. Malthaner
 */
public class Dungeon extends AbstractMapGenerator
{
    private Properties props;


    // Hajo: walls for all squares of this map
    private int [] walls = {'#' + ColorCodes.WHITE,
                            '#' + ColorCodes.WHITE,
                            '#' + ColorCodes.WHITE,
    };
    
    // Hajo: Floor for all squares of this map that have no other floor
    //       Floors are randomly picked from the interval, including both borders.
    private int [] standardGrounds = {'.' + ColorCodes.WHITE};
    
    private int roomWidthMin = 4;
    private int roomHeightMin = 4;
    private int roomWidthMax = 9;
    private int roomHeightMax = 6;
    
    private int roomGapHoriz = 6;
    private int roomGapVert = 4;
    
    private int corridorWidth = 1;
    
    /** Hajo: standard floor for rooms */
    private int [] roomFloors = {'.' + ColorCodes.WHITE};
    
    /** Hajo: some rooms have special features */
    private int [] roomFeatures = {'~' + ColorCodes.BLUE};

    /** Hajo: floor tile range for corridors */
    private int [] corridorFloors = {'.' + ColorCodes.WHITE};
    
    private Random rand;
    
    /**
     * Base light radius for this map.
     */
    private int viewRadius = -1;
    
    /**
     * Floor to be displayed outside the map
     */
    private int outsideFloor;
    
    /**
     * This is used for walls that are buried within other walls
     */
    private int hiddenWall = ' ';
    
    /**
     * The exit (assuming the dungeon is underground)
     */
    private int stairsUp = '<' + ColorCodes.RED;

    /**
     * Going deeper undergorund (assuming the dungeon is underground)
     */
    private int stairsDown = '>' + ColorCodes.RED;
    
    /**
     * Door symbol
     */
    private int door = '+' + ColorCodes.RED;
    
    /**
     * Chance to have a damaged floor tile in a room (in percent)
     */
    private int damagedFloorChance = 20;
    
    /**
     * Chance to have a special feature in a room (in percent)
     */
    private int roomFeatureChance = 25;

    /**
     * Generate maps with this many layers (no less than 2).
     */
    private int mapLayers = 2;

    /**
     * Get random number from interval [low ... high]
     * including both borders.
     */
    private int getRandomFromInterval(int low, int high)
    {
        final int r = rand.nextInt(high-low+1);
        return (low + r);
    }
    
    
    /**
     * Excarvate a room.
     *
     * @param lmap The map with the room.
     * @param x x coordinate of room top-left corner.
     * @param y y coordinate of room top-left corner.
     * @param w Room width.
     * @param h Room height.
     * @param withFeatures Place random features in this room?
     */
    private void makeRoom(LayeredMap lmap,
                          int x, int y, int w, int h,
                          boolean withFeatures)
    {
        for(int j=y; j<y+h; j++) {
            for(int i=x; i<x+w; i++) {
                
                if(getRandomFromInterval(0, 100) < damagedFloorChance) {
                    // Hajo: damaged floor
                    lmap.set(0, i, j, standardGrounds[Randomlib.linearRange(0, standardGrounds.length-1)]);
                } else {
                    // Hajo: proper floor
                    lmap.set(0, i, j, roomFloors[Randomlib.linearRange(0, roomFloors.length-1)]);
                }

                lmap.set(1, i, j, 0);
            }
        }

        if(withFeatures) {
            int xpos = x + w/2;
            int ypos = y + h/2;

            if(getRandomFromInterval(0, 100) < roomFeatureChance) {
                int feature = roomFeatures[Randomlib.linearRange(0, roomFeatures.length-1)];
                lmap.set(1, xpos, ypos, feature);
            }

        }

        /*
        if(withTemplate && rooms.size() > 0) 
        {
            final int n = rand.nextInt(rooms.size());
            final LayeredMap structure = (LayeredMap)rooms.get(n);
            final int xpos = x + w/2 - structure.getWidth()/2;
            final int ypos = y + h/2 - structure.getHeight()/2;

            lmap.insert(structure, xpos, ypos);
        }
         */
    }

    /**
     * Place doors in room walls.
     *
     * @param lmap The map with the room.
     * @param x x coordinate of room top-left corner.
     * @param y y coordinate of room top-left corner.
     * @param w Room width.
     * @param h Room height.
     */
    private void makeDoors(LayeredMap lmap,
                          int x, int y, int w, int h)
    {
        for(int i=x; i<x+w; i++) {
            if(lmap.get(1, i, y-1) == 0 &&
               lmap.get(1, i+1, y-1) != 0 &&
               lmap.get(1, i-1, y-1) != 0) {
                lmap.set(1, i, y-1, door);
            }
        }
        for(int i=x; i<x+w; i++) {
            if(lmap.get(1, i, y+h) == 0 &&
               lmap.get(1, i+1, y+h) != 0 &&
               lmap.get(1, i-1, y+h) != 0) {
                lmap.set(1, i, y+h, door);
            }
        }

        for(int j=y; j<y+h; j++) {
            if(lmap.get(1, x-1, j) == 0 &&
               lmap.get(1, x-1, j-1) != 0 &&
               lmap.get(1, x-1, j+1) != 0) {
                lmap.set(1, x-1, j, door);
            }
        }
        for(int j=y; j<y+h; j++) {
            if(lmap.get(1, x+w, j) == 0 &&
               lmap.get(1, x+w, j-1) != 0 &&
               lmap.get(1, x+w, j+1) != 0) {
                lmap.set(1, x+w, j, door);
            }
        }
    }

    /**
     * Test for and create side chambers if possible.
     *
     * @param lmap The map to use.
     * @param x x coordinate of parent room top-left corner.
     * @param y y coordinate of parent room top-left corner.
     */
    private void makeSideRoom(LayeredMap lmap, int x, int y)
    {
        // Hajo: check if there is space for a side room
        boolean ok = true;
        
        for(int j=y-7; j<y; j++) {
            for(int i=x-5; i<x+2; i++) {
                
                if(i>=0 && j>=0) {
                    ok &= (lmap.get(1, i, j) != 0);
                } else {
                    ok = false;
                }
            }
        }
        
        if(ok) {
            makeRoom(lmap, x-4, y-6, 5, 5, false);
            makeRoom(lmap, x, y-1, 1, 1, false);

            /*
            // Hajo: try to place template
            if(sideRooms.size() > 0) {
                int n = rand.nextInt(sideRooms.size());
                lmap.insert((LayeredMap)sideRooms.get(n), x-5, y-7);
            }
             */
        }
    }
    
    /**
     * Normalize an integer vector (length == 1). 
     * Decides randomly on otherwise indecisive cases.
     */
    private void calcNormalizedDirection(Point result, 
                                         int x1, int y1, int x2, int y2)
    {
        // Hajo: calculate normalized vector components
        int dxn = (x1 == x2) ? 0 : (x1 < x2) ? 1 : -1;
        int dyn = (y1 == y2) ? 0 : (y1 < y2) ? 1 : -1;

        if(dxn != 0 && dyn != 0) {
            // Hajo: diagonal cases ... we try each direction by random, but
            //       never both in one step, to get a walkable corridor.
            if(rand.nextInt(1000) > 500) {
                result.x = dxn;
                result.y = 0;
            } else {
                result.x = 0;
                result.y = dyn;
            }
        } else {
            // Hajo: clean cases, length of (dx,dy) already 1
            result.x = dxn;
            result.y = dyn;
        }
    }
    
    
    /**
     * Build a corridor from (x1, y1) to (x2, y2) within (0,0) and (width, height).
     *
     * @author Hj. Malthaner
     */
    private void makeCorridor(LayeredMap lmap,
                              int width, int height, int x1, int y1, int x2, int y2) 
    {
        Point dv = new Point();
        
        int [] cX = new int [128];
        int [] cY = new int [128];

        int length = 0;
        int x = x1;
        int y = y1;
        
        cX[0] = x;
        cY[0] = y;
        
        int lock = 0;
        
        while(length < 127 && (x != x2  || y != y2)) {
                        
            // Hajo: make corridors with bends
            // - lock determines how many sqaures to go without a bend
            // - calculate real direction to go
            // - with a certain chance, make deviatons to create bends
            
            lock --;
            
            if(lock <= 0) {
                calcNormalizedDirection(dv, x, y, x2, y2);
    
                lock = Randomlib.linearRange(0, 3);
                final int r = Randomlib.linearRange(0, 600);
                if(r < 100) {
                    rot90l(dv);
                } else if (r >= 100 && r < 200) {
                    rot90r(dv);
                }
            }
            
            if(x+dv.x >= 0 && x+dv.x < width && y+dv.y >= 0 && y+dv.y <height) {

                x += dv.x;
                y += dv.y;
                length ++;

                // Hajo: need bounds check here ?

                cX[length] = x;
                cY[length] = y;
            } else {
                lock = 0;
            }
        }
        
        for(int i=0; i<length; i++) {
            final int w2 = corridorWidth/2;
            
            for(int yd=0; yd<corridorWidth; yd++) {
                for(int xd=0; xd<corridorWidth; xd++) {
                    lmap.set(0, cX[i]+xd-w2, cY[i]+yd-w2, corridorFloors[Randomlib.linearRange(0, corridorFloors.length-1)]);
                    lmap.set(1, cX[i]+xd-w2, cY[i]+yd-w2, 0);
                }                
            }
        }
    }
    
    /**
     * Generate a new map of the "rooms and corridors" type.
     */
    public LayeredMap generate(final int width, final int height)
    {
        rand = getSeededRandom(props);

        final LayeredMap map = new LayeredMap(mapLayers, width, height);

        // Hajo: preset map default values.
        fillArea(map, 0, 0, 0, width, height, standardGrounds);
        fillArea(map, 1, 0, 0, width, height, walls);
        
        // Do the actual map generation steps
        
        final int roomColumns = width / (roomWidthMax + roomGapHoriz*2);
        final int roomRows = height / (roomHeightMax + roomGapVert*2);
        
        final int roomCount = roomColumns * roomRows;
        
        // Room position and sizes
        final int [] rX = new int [roomCount];
        final int [] rY = new int [roomCount];
        final int [] rW = new int [roomCount];
        final int [] rH = new int [roomCount];
        
        for(int j=0; j<roomRows; j++) {
            for(int i=0; i<roomColumns; i++) {
                final int index = j*roomColumns + i;
                rW[index] = getRandomFromInterval(roomWidthMin, roomWidthMax);
                rH[index] = getRandomFromInterval(roomHeightMin, roomHeightMax);

                rX[index] = i*(roomWidthMax + roomGapHoriz*2) + roomGapHoriz;
                rY[index] = j*(roomHeightMax + roomGapVert*2) + roomGapVert;

                rX[index] += rand.nextInt(roomWidthMax + roomGapHoriz - rW[index] + 1) - 1;
                rY[index] += rand.nextInt(roomHeightMax + roomGapVert - rH[index] + 1) - 1;
            }
        }
        
        for(int j=0; j<roomRows; j++) {
            for(int i=0; i<roomColumns; i++) {

                final int startRoom = j*roomColumns + i;

                int xs=rX[startRoom] + rW[startRoom]/2;
                int ys=rY[startRoom] + rH[startRoom]/2;

                int xd;
                int yd;

                // Hajo: skip some corridors by chance, but never both for a
                // given room. There still is a chance to create a dungeon
                // with unreachable rooms.
                int count = 0;
                
                if(i<roomColumns-1 &&
                   rand.nextInt(1000) < 800) {
                    xd=rX[startRoom+1] + rW[startRoom+1]/2;
                    yd=rY[startRoom+1] + rH[startRoom+1]/2;

                    makeCorridor(map, width, height, xs, ys, xd, yd);
                    count ++;
                }
                
                if(j<roomRows-1 &&
                   (count == 0 || rand.nextInt(1000) < 800)) {
                    xd=rX[startRoom+roomColumns] + rW[startRoom+roomColumns]/2;
                    yd=rY[startRoom+roomColumns] + rH[startRoom+roomColumns]/2;

                    makeCorridor(map, width, height, xs, ys, xd, yd);
                    count ++;
                }
            }
        }

        for(int i=0; i<roomCount; i++) {
            makeRoom(map, rX[i], rY[i], rW[i], rH[i], true);
            
            if(i != 0 && rand.nextInt(1000) > 500) {
                makeSideRoom(map, rX[i], rY[i]);
            }
        }
               
        // Hajo: "black out" invisible parts of the dungeon
        calculateHiddenWalls(map);

        for(int i=0; i<roomCount; i++) {
            makeDoors(map, rX[i], rY[i], rW[i], rH[i]);
        }

        map.setSpawnX(rX[0] + 2);
        map.setSpawnY(rY[0] + 2);
        // result.setViewRadius(viewRadius);
        
        map.setOutside(0, outsideFloor);


        // placing the stairs
        // very rough set of code to place a valid stairs that the player can always get to
        Path path = new Path();
        PathSource pathSource = new PathSourceLink(map);
        int sx = map.getSpawnX();
        int sy = map.getSpawnY();
        int dx, dy;
        boolean go = true;        
        int stairRoom;
        int stairPos;

        while(go){
            do {        
              stairRoom = rand.nextInt(rX.length - 1) + 1;
              stairPos = rand.nextInt(2);
//              map.set(1, rX[stairRoom] + (stairPos+1), rY[stairRoom]+ (stairPos+1), stairsDown);
                dx = (int) (rX[stairRoom] + (stairPos+1));
                dy = (int) (rY[stairRoom]+ (stairPos+1));
            } while(pathSource.isMoveAllowed(sx, sy, dx, dy) == false);
            
            CoordinatePathDestination destination =
                    new CoordinatePathDestination(dx, dy);

            // Try to find a path there
            boolean ok = path.findPath(pathSource, destination, sx, sy);
            if(ok){
            	map.set(1, dx, dy, stairsDown);
            	go = false;
            }
        }
        
        
        
        return map;
    }

    private void calculateHiddenWalls(LayeredMap map)
    {
        final int width = map.getWidth();
        final int height = map.getHeight();
        
        for(int j=0; j<height; j++) {
            for(int i=0; i<width; i++) {

                boolean open = false;
                for(int yd=-1; yd<=1; yd++) {
                    for(int xd=-1; xd<=1; xd++) {

                        if(i+xd >= 0 && i+xd < width && j+yd >= 0 && j+yd < height) {
                            if(map.get(1, i+xd, j+yd) == 0) {
                                open = true;
                            }
                        }
                    }
                }

                if(open == false) {
                    map.set(1, i, j, hiddenWall);
                }
            }
        }
    }

    /** 
     * Reads settings into variables for use in generate().
     *
     * @param mapPath path for map templates (unused currently)
     * @param props map properties, which will override the default settings.
     */
    private void init(final String mapPath, final Properties props)
    {
        this.props = props;
        
        // Hajo: walls for all squares of this map
        if(props.get("walls") != null) {
            walls = getIntegers(props, "walls", '#');
        }
    
        // Hajo: Floor for all squares of this map that have no other floor
        //       Floors are randomly picked from the interval, including both borders.
        if(props.get("floors") != null) {
            standardGrounds = getIntegers(props, "floors", '.');
        }
        
        // Hajo: floor tile range for corridors
        if(props.get("corridor_floors") != null) {
            corridorFloors = getIntegers(props, "corridor_floors", '.');
        }
        
        // Hajo: standard floor for rooms
        // Hajo: floor tile range for corridors
        if(props.get("room_floors") != null) {
            roomFloors = getIntegers(props, "room_floors", '.');
        }
        
        if(props.get("room_features") != null) {
            roomFeatures = getIntegers(props, "room_features", '.');
        }

        roomWidthMin = getInt(props, "room_width_min", roomWidthMin);
        roomHeightMin = getInt(props, "room_height_min", roomHeightMin);
        roomWidthMax = getInt(props, "room_width_max", roomWidthMax);
        roomHeightMax = getInt(props, "room_height_max", roomHeightMax);
    
        roomGapHoriz = getInt(props, "room_gap_horiz", roomGapHoriz);
        roomGapVert = getInt(props, "room_gap_vert", roomGapVert);
        
        corridorWidth = getInt(props, "corridor_width", corridorWidth);
        
        hiddenWall = getInt(props, "hidden_wall", hiddenWall);
        outsideFloor = getInt(props, "outside_floor", outsideFloor);
        
        stairsUp = getInt(props, "stairs_up", stairsUp);
        door = getInt(props, "door", door);
        
        damagedFloorChance = getInt(props, "damaged_floor_chance", damagedFloorChance);
        viewRadius = getInt(props, "view_radius", viewRadius);

        // Hajo: at least 2 layers are needed.
        mapLayers = Math.max(getInt(props, "map_layers", mapLayers) , 2);
    }
    
    /** 
     * Creates a new instance of Dungeon.
     * Reads settings into variables for use in generate()
     */
    public Dungeon(final String mapPath, final String propsfile)
    {        
        final String filename = mapPath + propsfile + ".properties";
        props = new Properties();
        
        try {
            final File file = new File(filename);

            // Override default dungeon configuration.
            if(file.exists()) {
                System.err.println("Dungeon config: " + filename);
                props.load(new FileInputStream(file));
                init(mapPath, props);
            }
        } catch(Exception oops) {
            oops.printStackTrace();
        }
    }

    /** 
     * Creates a new instance of Dungeon.
     * Reads settings into variables for use in generate()
     */
    public Dungeon(final String mapPath, final Properties props)
    {
        init(mapPath, props);
    }
}
