package adventOfCode.twentytwenty.day20;

import adventOfCode.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Day20Test {

    @ParameterizedTest
    @ValueSource(strings = SAMPLE_DATA)
    public void sampleImageTest(final String data) {
        List<String> list = Arrays.asList(data.split("\n"));
        Day20 d = new Day20();
        d.buildTiles(list);
        d.doMatching();
        assertEquals(4, d.getCorners().size());
        long l = d.multipliedCornerValue();
        System.out.println("Product : " + l);
        assertEquals(20899048083289L, l);
        d.buildMap();
        int sum = d.findTheMonsters();
        System.out.println("Monsters : " + sum);
        assertEquals(2, sum);
        long hashCount = d.countHashInLine() - (sum * 15L);
        System.out.println("Hash : " + hashCount);
        assertEquals(273L, hashCount);
    }

    @ParameterizedTest
    @ValueSource(strings = MAP_WITH_MONSTERS)
    public void findMonsterTest(final String data) {
        Day20 d = new Day20()  ;
        d.setImageInLine(data);
        int count = d.findTheMonsters();
        System.out.println(count);
        assertEquals(2, count);
    }

    @Test
    public void fullImageTest() {
        List<String> list = Utils.getFileContents("y2020d20.txt");
        Day20 d = new Day20();
        d.buildTiles(list);
        d.doMatching();
        assertEquals(4, d.getCorners().size());
        long l = d.multipliedCornerValue();
        System.out.println("Product : " + l);
        assertEquals(16192267830719L, l);
        d.buildMap();
        int sum = d.findTheMonsters();
        System.out.println("Monsters : " + sum);
        long hashCount = d.countHashInLine() - (sum * 15L);
        System.out.println("Hash : " + hashCount);

        assertEquals(1909L, hashCount);
    }

    private static final String MAP_WITH_MONSTERS = """
            .#.#..#.##...#.##..#####
            ###....#.#....#..#......
            ##.##.###.#.#..######...
            ###.#####...#.#####.#..#
            ##.#....#.##.####...#.##
            ...########.#....#####.#
            ....#..#...##..#.#.###..
            .####...#..#.....#......
            #..#.##..#..###.#.##....
            #.####..#.####.#.#.###..
            ###.#.#...#.######.#..##
            #.####....##..########.#
            ##..##.#...#...#.#.#.#..
            ...#..#..#.#.##..###.###
            .#.#....#.##.#...###.##.
            ###.#...#..#.##.######..
            .#.#.###.##.##.#..#.##..
            .####.###.#...###.#..#.#
            ..#.#..#..#.#.#.####.###
            #..####...#.#.#.###.###.
            #####..#####...###....##
            #.##..#..#...#..####...#
            .#.###..##..##..####.##.
            ...###...##...#...#..###""";

    private static final String SAMPLE_DATA = """
            Tile 2311:
            ..##.#..#.
            ##..#.....
            #...##..#.
            ####.#...#
            ##.##.###.
            ##...#.###
            .#.#.#..##
            ..#....#..
            ###...#.#.
            ..###..###

            Tile 1951:
            #..#..#.##
            ..####....
            .#####..##
            ..#.#...##
            ##.#.##.#.
            #..##.###.
            ....#.#...
            ##.##.#..#
            ..###.##.#
            .#..#####.

            Tile 1171:
            ####...##.
            #..##.#..#
            ##.#..#.#.
            .###.####.
            ..###.####
            .##....##.
            .#...####.
            #.##.####.
            ####..#...
            .....##...

            Tile 1427:
            ###.##.#..
            .#..#.##..
            .#.##.#..#
            #.#.#.##.#
            ....#...##
            ...##..##.
            ...#.#####
            .#.####.#.
            ..#..###.#
            ..##.#..#.

            Tile 1489:
            ##.#.#....
            ..##...#..
            .##..##...
            ..#...#...
            #####...#.
            #..#.#.#.#
            ...#.#.#..
            ##.#...##.
            ..##.##.##
            ###.##.#..

            Tile 2473:
            #....####.
            #..#.##...
            #.##..#...
            ######.#.#
            .#...#.#.#
            .#########
            .###.#..#.
            ########.#
            ##...##.#.
            ..###.#.#.

            Tile 2971:
            ..#.#....#
            #...###...
            #.#.###...
            ##.##..#..
            .#####..##
            .#..####.#
            #..#.#..#.
            ..####.###
            ..#.#.###.
            ...#.#.#.#

            Tile 2729:
            ...#.#.#.#
            ####.#....
            ..#.#.....
            ....#..#.#
            .##..##.#.
            .#.####...
            ####.#.#..
            ##.####...
            ##..#.##..
            #.##...##.

            Tile 3079:
            #.#.#####.
            .#..######
            ..#.......
            ######....
            ####.#..#.
            .#...#.##.
            #.#####.##
            ..#.###...
            ..#.......
            ..#.###...""";
}