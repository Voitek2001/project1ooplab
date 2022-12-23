package agh.ics.oop;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Genotype {
    private final List<MapDirection> genes;
    private final int[] genesTravelOrder;
    public Genotype(List<MapDirection> genes) {
        this.genes = genes;
        this.genesTravelOrder = IntStream.rangeClosed(0, this.genes.size()-1).toArray();
    }
    public Genotype(List<MapDirection> genes1, List<MapDirection> genes2) {
        this(Stream.concat(genes1.stream(), genes2.stream()).toList());
    }

    public List<MapDirection> cutLeftSide(int indexOfCut) {
        return this.genes.subList(0, indexOfCut);
    }

    public List<MapDirection> cutRightSide(int indexOfCut) {
        return this.genes.subList(indexOfCut, genes.size()-1);
    }

    public void applySmallCorrect() {
        Random rand = new Random();
        this.genes.replaceAll(mapDirection -> {
            if (rand.nextInt(2) == 1) {
                return mapDirection.next();
            }
            return mapDirection.previous();
        });
    }

    public void applyABitOfMadness() {
        Random rand = new Random();
        for(int i = 0; i < this.genesTravelOrder.length; i++) {
            int operation = rand.nextInt(5);
            if (operation == 4) {
                int rand_ind = rand.nextInt(i + 1, this.genesTravelOrder.length);
                int tmp = this.genesTravelOrder[i];
                this.genesTravelOrder[i] = this.genesTravelOrder[rand_ind];
                this.genesTravelOrder[rand_ind] = tmp;
            }
        }
    }

    public void applyFullyRandomness() {
    }
    public int[] getGenesTravelOrder() {
        return this.genesTravelOrder;
    }

    public List<MapDirection> getGenes() {
        return this.genes;
    }


}
