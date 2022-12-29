package agh.ics.oop;


import java.util.Arrays;
import java.util.Optional;

public enum MoveDirection {
    FORWARD(0),
    BACKWARD(4),
    RIGHT(2),
    LEFT(6),
    FORWARDRIGHT(1),
    FORWARDLEFT(7),
    BACWARDRIGHT(3),
    BACWARDLEFT(5);

    public final int value;

    MoveDirection(int i) {
        this.value = i;
    }

    public static Optional<MoveDirection> getByValue(int value) {
        return Arrays.stream(values()).filter(mapDirection -> mapDirection.value == value).findFirst();
    }

    @Override
    public String toString() {
        return switch (this) {
            case FORWARD -> "Północ";
            case BACKWARD -> "Południe";
            case RIGHT -> "Wschód";
            case LEFT -> "Zachód";
            case FORWARDRIGHT -> "Północny-Wschód";
            case FORWARDLEFT -> "Północny-Zachód";
            case BACWARDLEFT -> "Południony-Wschód";
            case BACWARDRIGHT -> "Południony-Zachód";
        };

    }

    public MoveDirection next() {
        return switch (this) {
            case FORWARD -> FORWARDRIGHT;
            case FORWARDRIGHT -> RIGHT;
            case RIGHT -> BACWARDRIGHT;
            case BACWARDRIGHT -> BACKWARD;
            case BACKWARD -> BACWARDLEFT;
            case BACWARDLEFT -> LEFT;
            case LEFT -> FORWARDLEFT;
            case FORWARDLEFT -> FORWARD;

        };
    }

    public MoveDirection previous() {
        return switch (this) {
            case FORWARD -> FORWARDLEFT;
            case FORWARDLEFT -> LEFT;
            case LEFT -> BACWARDLEFT;
            case BACWARDLEFT -> BACKWARD;
            case BACKWARD -> BACWARDRIGHT;
            case BACWARDRIGHT -> RIGHT;
            case RIGHT -> FORWARDRIGHT;
            case FORWARDRIGHT -> FORWARD;
        };
    }

//    public Vector2d toUnitVector() {
//        return switch (this) {
//            case NORTH -> new Vector2d(0, 1);
//            case EAST -> new Vector2d(1, 0);
//            case SOUTH -> new Vector2d(0, -1);
//            case WEST -> new Vector2d(-1, 0);
//            case NORTHEAST -> new Vector2d(1, 1);
//            case NORTHWEST -> new Vector2d(-1, 1);
//            case SOUTHEAST -> new Vector2d(1, -1);
//            case SOUTHWEST -> new Vector2d(-1, -1);
//        };
//    }
//


    }

