package agh.ics.oop.MapElements;

public enum AnimalStatus {
    DEAD,
    ALIVE;

    public String toString() {
        return switch (this) {
            case DEAD -> "DEAD";
            case ALIVE -> "ALIVE";
        };
    }
}
