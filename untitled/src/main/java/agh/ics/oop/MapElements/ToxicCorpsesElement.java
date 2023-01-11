package agh.ics.oop.MapElements;

import agh.ics.oop.Vector2d;

public class ToxicCorpsesElement implements Comparable<ToxicCorpsesElement> {

    private final Vector2d position;
    private int corpses;

    public ToxicCorpsesElement(Vector2d position, int corpses) {
        this.position = position;
        this.corpses = corpses;
    }

    @Override
    public int compareTo(ToxicCorpsesElement other) {
        if (this.corpses != other.corpses) {
            return this.corpses - other.corpses;
        }
        return this.position.x() - other.position.x();
    }
    public void setCorpses(int newCorpses) {
        this.corpses = newCorpses;
    }

    public void increaseCorpsesByOne() {
        this.corpses++;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getCorpses() {
        return corpses;
    }

}
