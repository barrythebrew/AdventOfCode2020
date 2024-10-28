package adventOfCode.twentytwenty.day20;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static adventOfCode.twentytwenty.day20.Day20.rotate90DegreeClockwise;
import static adventOfCode.twentytwenty.day20.Day20.transformTileImage;

public class Tile {
    long id;
    Set<String> edgeValues = new HashSet<>();
    String topEdge;
    String bottomEdge;
    String leftEdge;
    String rightEdge;

    int x = -1;
    int y = -1;

    List<String> fullImageWithEdges;

    List<String> imageWithRemovedEdges;

    Set<Long> matchedEdges = new HashSet<>();

    Set<Long> checked = new HashSet<>();

    private final Day20 board;


    /**
     * read the string data. Populate the id, image and the edges
     * @param d20Board the parent Day20
     * @param data the list of string data
     */
    public Tile(final Day20 d20Board, final List<String> data) {
        board = d20Board;
        String tileData = data.get(0);
        int colonIndex = tileData.indexOf(":");
        id = Integer.parseInt(data.get(0).substring(5, colonIndex));
        if (id == -1) {
            throw new RuntimeException("Id = -1");
        }
        data.remove(0);
        if (data.get(data.size() - 1).trim().isEmpty()) {
            data.remove(data.size() - 1);
        }

        setEdgeValues(data);

        fullImageWithEdges = data;
    }

    /**
     * read each of the edges and save both the string and inverse of the string as an edge
     * @param data the full tile data
     */
    private void setEdgeValues(List<String> data) {
        topEdge = data.get(0);
        bottomEdge = data.get(data.size() - 1);
        readSides(data);
        edgeValues.add(topEdge);
        edgeValues.add(bottomEdge);
        edgeValues.add(invertEdgeValue(topEdge));
        edgeValues.add(invertEdgeValue(bottomEdge));
    }


    /**
     * need to rotate or flip the image until the correct edge is lined up with the previous tile
     *
     * @param prev        the previous tile
     * @param matchBottom are we matching the bottom or right edge of the previous tile
     */
    public void changeOrientation(final Tile prev, final boolean matchBottom) {

        String toMatch = matchBottom ? prev.bottomEdge : prev.rightEdge;

        int len = fullImageWithEdges.size() - 1;
        // find the matching side on this tile then work out the action to move this tile to the correct
        // orientation
        if ((leftEdge.equals(toMatch) && !matchBottom) || (topEdge.equals(toMatch) && matchBottom)) {
            setImageWithRemovedEdges(fullImageWithEdges);
        } else {
            if ((rightEdge.equals(toMatch) && !matchBottom) || (invertEdgeValue(topEdge).equals(toMatch) && matchBottom)) {
                // ok
                fullImageWithEdges = flipYCoordinates(len);
            } else if ((invertEdgeValue(rightEdge).equals(toMatch) && matchBottom)) {
                fullImageWithEdges = flipXCoordinates(len);
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 3);
            } else if ((bottomEdge.equals(toMatch) && matchBottom)) {
                // ok
                fullImageWithEdges = flipXCoordinates(len);
            } else if (rightEdge.equals(toMatch) && matchBottom) {
                // ok
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 3);
            } else if (((invertEdgeValue(bottomEdge)).equals(toMatch) && !matchBottom)) {
                // ok for bottom
                fullImageWithEdges = flipYCoordinates(len);
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 1);
            } else if ((invertEdgeValue(leftEdge).equals(toMatch) && !matchBottom)) {
                // ok for invert left
                fullImageWithEdges = flipXCoordinates(len);
            } else if ((invertEdgeValue(topEdge).equals(toMatch) && !matchBottom)) {
                // checked from here onwards
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 3);
            } else if ((bottomEdge.equals(toMatch) && !matchBottom) || (invertEdgeValue(leftEdge).equals(toMatch) && matchBottom)) {
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 1);
            } else if (invertEdgeValue(leftEdge).equals(toMatch) && matchBottom) {
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 1);
            } else if ((leftEdge.equals(toMatch) && matchBottom)) {
                fullImageWithEdges = flipXCoordinates(len);
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 1);
            } else if (((invertEdgeValue(rightEdge)).equals(toMatch) && !matchBottom)) {
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 2);
            } else if ((topEdge.equals(toMatch) && !matchBottom)) {
                fullImageWithEdges = flipYCoordinates(len);
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 3);
            } else if ((invertEdgeValue(bottomEdge).equals(toMatch) && matchBottom)) {
                fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, 2);
            }

            setEdgeValues(fullImageWithEdges);
            setImageWithRemovedEdges(fullImageWithEdges);

            if (!topEdge.equals(prev.bottomEdge) && !leftEdge.equals(prev.rightEdge)) {
                throw new RuntimeException("Doesn't match Prev:" + prev.id
                        + " current: " + id + ", matching "
                        + (matchBottom ? "bottom" : "top"));
            }
        }
    }

    private List<String> flipXCoordinates(int len) {
        return transformTileImage(fullImageWithEdges, (x, y) -> len - x, (x, y) -> y);
    }

    private List<String> flipYCoordinates(int len) {
        return transformTileImage(fullImageWithEdges, (x, y) -> x, (x, y) -> len - y);
    }


    public void setImageWithRemovedEdges(List<String> data) {
        imageWithRemovedEdges = new ArrayList<>();
        for (int i = 1; i < (data.size() - 1); i++) {
            String s = data.get(i);
            imageWithRemovedEdges.add(s.substring(1, s.length() - 1));
        }
    }

    public void setCoOrd(int xc, int yc) {
        x = xc;
        y = yc;
    }

    public void printEdges() {
        System.out.println();
        System.out.print(id + " Edges: ");
        matchedEdges.forEach(l -> System.out.print(l + ", "));
        System.out.println();
    }


    public boolean isCorner() {
        if (matchedEdges.isEmpty()) {
            System.out.println("Error id " + id + " not matched");
        }
        return matchedEdges.size() == 2;
    }

    public boolean isTopLeft() {
        List<Tile> joined = board.getTiles().stream()
                .filter(t -> matchedEdges.contains(t.id))
                .toList();

        return joined.stream().anyMatch(t -> t.edgeValues.contains(rightEdge))
                && joined.stream().anyMatch(t -> t.edgeValues.contains(bottomEdge));
    }

    /**
     * get the tile that matches the edge
     *
     * @param edgePattern the edge to match
     * @return the linked tile or null
     */
    public Tile getMatch(final String edgePattern) {
        List<Tile> joined = board.getTiles().stream()
                .filter(t -> matchedEdges.contains(t.id))
                .toList();
        return joined.stream()
                .filter(t -> t.edgeValues.contains(edgePattern))
                .findFirst().orElse(null);
    }

    /**
     * rotate the tile so the matching edges are at the left and bottom
     *
     * @return the rotated tile
     */
    public Tile convertToFirstTile() {
        boolean isTopMatch = getMatch(topEdge) != null;
        boolean isRightMatch = getMatch(rightEdge) != null;
        boolean isLeftMatch = getMatch(leftEdge) != null;
        boolean isBottomMatch = getMatch(bottomEdge) != null;

        int numberOfTurns = getNumberTurns(isTopMatch, isRightMatch, isBottomMatch, isLeftMatch);
        fullImageWithEdges = rotate90DegreeClockwise(fullImageWithEdges, numberOfTurns);
        setEdgeValues(fullImageWithEdges);
        return this;
    }

    /**
     * calculate how many 90 degree rotations are needed to make this the first tile
     *
     * @param isTopMatch    is the matching tile at the top
     * @param isRightMatch  is the matching tile at the right
     * @param isBottomMatch is the matching tile at the bottom
     * @param isLeftMatch   is the matching tile at the left
     * @return the number of moves
     */
    private int getNumberTurns(final boolean isTopMatch, final boolean isRightMatch,
                               final boolean isBottomMatch, final boolean isLeftMatch) {
        if (isTopMatch) {
            if (isRightMatch) {
                return 1;
            } else if (isLeftMatch) {
                return 2;
            }
        } else if (isBottomMatch) {
            if (isLeftMatch) {
                return 3;
            }
        }
        return 0;
    }

    /**
     * get the next tile
     *
     * @param matchRightSide is it the tile to the right
     * @return the next tile in the image
     */
    public Tile getNext(final boolean matchRightSide) {
        if (matchRightSide) {
            return getMatch(rightEdge);
        }
        return getMatch(bottomEdge);
    }

    public void findMatchingEdge(Tile other) {
        if (id != other.id && !checked.contains(other.id)) {
            checked.add(other.id);
            other.checked.add(id);
            if (edgeValues.stream().anyMatch(s -> other.edgeValues.contains(s))) {
                matchedEdges.add(other.id);
                other.matchedEdges.add(id);
            }
        }
    }

    @Override
    public String toString() {
        return "id: " + id;
    }

    private String invertEdgeValue(String s) {
        StringBuilder b = new StringBuilder(s);
        return b.reverse().toString();
    }

    private void readSides(List<String> data) {
        leftEdge = rightEdge = "";
        data.forEach(s -> {
            leftEdge += s.substring(0, 1);
            rightEdge += s.substring(s.length() - 1);
        });
        edgeValues.add(leftEdge);
        edgeValues.add(rightEdge);
        edgeValues.add(invertEdgeValue(leftEdge));
        edgeValues.add(invertEdgeValue(rightEdge));
    }

}
