package adventOfCode.twentytwenty.day20;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day20 {

    /**
     * all the tiles
     */
    Set<Tile> tiles = new HashSet<>();

    /**
     * coordinate and tile
     */
    Map<String, Tile> map = new HashMap<>();

    /**
     * number of tiles in each row and column
     */
    int maxx = 0;
    int maxy = 0;

    /**
     * the full map image
     */
    List<String> initialImage = new ArrayList<>();
    /**
     * the full map image on a single line torun the regex on
     */
    String imageInLine;

    /**
     * start at the top left of the map.
     * Find the next tile to the left and flip so the edges match
     * If there are no tiles to the left start with the first column of the row and find the tile beneath it
     */
    public void buildMap() {
        Tile t = getStartCorner();
        int x = 0;
        int y = 0;
        t.setCoOrd(x, y);
        map.put(getCoordKey(x, y), t);
        t.setImageWithRemovedEdges(t.fullImageWithEdges);
        Tile next = t.getNext(true);
        boolean matchBottom = false;
        while (next != null) {
            y += 1;
            next.changeOrientation(t, matchBottom);
            matchBottom = false;
            next.setCoOrd(x, y);
            map.put(getCoordKey(x, y), next);
            t = next;
            next = t.getNext(true);
            if (next == null) {
                t = map.get(getCoordKey(x, 0));
                next = t.getNext(false);
                if (next != null) {
                    y = -1;
                    x += 1;
                    matchBottom = true;
                }
            }
        }

        maxx = x;
        maxy = y;

        printlayout();

        buildImage();
    }

    /**
     * count the number of times the monster pattern appears in the image
     * @return the count
     */
    private int countMonsters() {
        Pattern p = getMonsterRegex();

        Matcher m = p.matcher(imageInLine);
        int index = 0;
        int matches = 0;
        while(m.find(index)) {
            matches++;
            int pos = m.start();
            index = pos + 1;
        }
        return matches;
    }

    public int findTheMonsters() {
        printFullImage();
        int sum = findTheMonsters(0);
        if (sum == 0) {
            // flip the image
            initialImage = transformTileImage(initialImage, (x, y) -> (initialImage.size() - 1 -x), (x, y) -> y);
            imageInLine = "";
            initialImage.forEach(s -> imageInLine += s);
            sum = findTheMonsters(0);
        }
        return sum;
    }

    private int findTheMonsters(final int rotation) {
        if (rotation < 4) {
            int sum = countMonsters();
            if (sum > 0) {
                return sum;
            }
            initialImage = rotate90DegreeClockwise(initialImage, 1);
            imageInLine = "";
            initialImage.forEach(s -> imageInLine += s);
            return findTheMonsters(rotation + 1);
        }
        return 0;
    }


    /**
     * count the # in the line
     * @return the count of all the # characters
     */
    public long countHashInLine() {
        return imageInLine.chars().filter(num -> num == '#').count();
    }

    /**
     * print the full image in its current orientation
     */
    public void printFullImage() {
        System.out.println();
        initialImage.forEach(System.out::println);
        System.out.println();
    }

    /**
     * the regex pattern for the monster:
     *                   #
     * #    ##    ##    ###
     *  #  #  #  #  #  #
     *
     * @return the pattern
     */
    private Pattern getMonsterRegex() {
        String p = "#.{" + (initialImage.size() - 19) + "}#.{4}##.{4}##.{4}###.{" + (initialImage.size() -19)
                + "}#.{2}#.{2}#.{2}#.{2}#.{2}#";
        return Pattern.compile(p);
    }

    /**
     * put all the images together into the complete map
     */
    private void buildImage() {
        for (int i = 0; i <= maxx; i++) {
            for (int j = 0; j <= maxy; j++) {

                Tile t = map.get(getCoordKey(i, j));
                addToImage(t);

            }
        }
        imageInLine = "";
        initialImage.forEach(s -> imageInLine += s);
    }

    /**
     * read in the image list into a single line to apply the monster regex to
     * @param s the string to parse
     */
    public void setImageInLine(String s) {
        initialImage = Arrays.asList(s.split("\n"));
        imageInLine = "";
        initialImage.forEach(m -> imageInLine += m);
    }

    /**
     * add the tile data to the image
     * @param t the tile data to add
     */
    private void addToImage(Tile t) {
        if (t.y == 0) {
            initialImage.addAll(t.imageWithRemovedEdges);
        } else {
            int line = t.x * t.imageWithRemovedEdges.size();
            for (int i = 0; i < t.imageWithRemovedEdges.size(); i++) {
                String s = initialImage.get(line + i) + t.imageWithRemovedEdges.get(i);
                initialImage.remove(line + i);
                initialImage.add(line + i, s);
            }
        }
    }

    /**
     * print the layout of the tiles by the tile ids
     */
    private void printlayout() {
        System.out.println("Layout");
        for (int i = 0; i <= maxx; i++) {
            System.out.println();
            for (int j = 0; j <= maxy; j++) {
                System.out.print(map.get(getCoordKey(i, j)).id + " ");
            }
        }
        System.out.println();
        System.out.println();
    }

    final AtomicInteger counter = new AtomicInteger();

    public void buildTiles(List<String> full) {
        full.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 12))
                .values()
                .forEach(l -> tiles.add(new Tile(this, l)));
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public void doMatching() {
        tiles.forEach(t -> {
            tiles.forEach(t::findMatchingEdge);
        });
    }

    public long multipliedCornerValue() {
        tiles.forEach(Tile::printEdges);

        System.out.println("Corners");
        List<Tile> corners = getCorners();

        return corners.stream()
                .map(t -> t.id)
                .reduce(1L, (subtotal, id) -> subtotal * id);
    }

    public List<Tile> getCorners() {
        return tiles.stream()
                .filter(Tile::isCorner)
                .collect(Collectors.toList());
    }

    public Tile getStartCorner() {
        List<Tile> corners = getCorners();
        Tile topLeft = corners.stream()
                .filter(Tile::isTopLeft)
                .findFirst().orElse(null);
        if (topLeft != null) {
            return topLeft;
        }
        Tile makeTop = corners.get(0);
        return makeTop.convertToFirstTile();
    }

    public static List<String> transformTileImage(List<String> orig, BiFunction<Integer, Integer, Integer> newX, BiFunction<Integer, Integer, Integer> newy) {
        List<String> transform = new ArrayList<>();
        char[][] origArray = new char[orig.size()][orig.size()];
        char[][] newArray = new char[orig.size()][orig.size()];

        for (int i = 0; i < orig.size(); i++) {
            origArray[i] = orig.get(i).toCharArray();
        }

        for (int x = 0; x < orig.size(); x++) {
            for (int y = 0; y < orig.size(); y++) {
                newArray[newX.apply(x,y)][newy.apply(x,y)] = origArray[x][y];
            }
        }

        for (int i = 0; i < orig.size(); i++) {
            String s = String.copyValueOf(newArray[i]);
            transform.add(s);
        }
        return transform;
    }

    public static List<String> rotate90DegreeClockwise(List<String> orig, int times) {
        if (times == 0) {
            return orig;
        }
        List<String> next = transformTileImage(orig, (x, y) -> y, (x, y) -> orig.size() -1 - x);
        return rotate90DegreeClockwise(next, times - 1);
    }

    public String getCoordKey(int x, int y) {
        return "x"+x+"y"+y;
    }

}
