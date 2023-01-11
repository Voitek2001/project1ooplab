package agh.ics.oop;

import java.util.Arrays;
import java.util.Optional;

public enum MapDirection {
    NORTH(0),
    SOUTH(4),
    EAST(2),
    WEST(6),
    NORTHEAST(1),
    SOUTHEAST(3),
    SOUTHWEST(5),
    NORTHWEST(7);


    public final int value;

    MapDirection(int i) {
        this.value = i;
    }

    public static Optional<MapDirection> getByValue(int value) {
        return Arrays.stream(values()).filter(mapDirection -> mapDirection.value == value).findFirst();
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
            case NORTHEAST -> "Północny-Wschód";
            case NORTHWEST -> "Północny-Zachód";
            case SOUTHEAST -> "Południony-Wschód";
            case SOUTHWEST -> "Południony-Zachód";
        };

    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;

        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHWEST -> WEST;
            case WEST -> SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            case NORTHEAST -> NORTH;
        };
    }


    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHEAST -> new Vector2d(1, 1);
            case NORTHWEST -> new Vector2d(-1, 1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
        };
    }


}
