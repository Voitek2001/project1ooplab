package agh.ics.oop.MapElements;

import agh.ics.oop.MapDirection;
import agh.ics.oop.MoveDirection;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Genotype implements Comparable<Genotype>{

    /**
     * Genotyp zawiera kierunki ruchów jako liste
     * natomiast kolejność w jakiej przechodzimy tą liste przechowywana jest w genesTravelOrder
     * a więc jeśli chcemy przechodzić gen w pełnej predyscynacji kolejność przechodzenia to po prostu lista od 1..n
     * natomiast jeśli zaaplikować trochę szaleństwa, to gen sam w sobie sie nie zmienia natomiast kolejność 1..n
     * listy genesTravelOrder zostaje pomieszana co 10 elementów aby tylko 20% genów było w innej kolejności
     * (przeniesienie jednego indeksu zmienia kolejność dwóch dlatego zmieniamy co 10 element a nie 5)
     * natomiast lekka korekta używa przesuwa w losowym kierunku w góre lub w dół movedirection
     * natomiast pełna losowość to po prostu losowe nowe movedirection
     */
    private List<MoveDirection> genes;
    private final List<Integer> genesTravelOrder;
    private Iterator<Integer> genIterator;


    public Genotype(List<MoveDirection> genes) {
        this.genes = genes;
        this.genesTravelOrder = IntStream.rangeClosed(0, this.genes.size()-1)
                .boxed().collect(Collectors.toList());
        this.genIterator = Iterables.cycle(this.genesTravelOrder).iterator();
    }
    public Genotype(List<MoveDirection> genes1, List<MoveDirection> genes2) {
        this(Stream.concat(genes1.stream(), genes2.stream()).toList());
    }

    public List<MoveDirection> cutLeftSide(int indexOfCut) {
        return this.genes.subList(0, indexOfCut);
    }

    public List<MoveDirection> cutRightSide(int indexOfCut) {
        return this.genes.subList(indexOfCut, genes.size());
    }

    public void applySmallCorrect() {
        Random rand = new Random();

        List<MoveDirection> newGenes = new ArrayList<>();
        for (MoveDirection moveDirection : this.genes) {
            if (rand.nextInt(2) == 1) {
                newGenes.add(moveDirection.next());
            } else {
                newGenes.add(moveDirection.previous());
            }
        }
        this.genes = newGenes;

//        this.genes.replaceAll(moveDirection -> {
//            if (rand.nextInt(2) == 1) {
//                return moveDirection.next();
//            }
//            return moveDirection.previous();
//        });
    }

    public void applyABitOfMadness() {
        Random rand = new Random();
        for(int i = 0; i < this.genesTravelOrder.size(); i++) {
            int operation = rand.nextInt(10);
            if (operation == 9) {
                int rand_ind = rand.nextInt(i, this.genesTravelOrder.size());
                int tmp = this.genesTravelOrder.get(i);
                this.genesTravelOrder.set(i, this.genesTravelOrder.get(rand_ind));
                this.genesTravelOrder.set(rand_ind, tmp);
            }
        }
        this.genIterator = Iterables.cycle(this.genesTravelOrder).iterator();
    }

    public void applyFullyRandomness() {
        Random rand = new Random();

        List<MoveDirection> newGenes = new ArrayList<>();
        for (int i = 0; i < this.genes.size(); i++) {
            Optional<MoveDirection> newValue = MoveDirection.getByValue(rand.nextInt(8));
            newValue.ifPresent(newGenes::add);
        }
        this.genes = newGenes;
    }

    public List<Integer> getGenesTravelOrder() {
        return this.genesTravelOrder;
    }

    public List<MoveDirection> getGenes() {
        return this.genes;
    }

    public MoveDirection getCurrentMove() {
        return this.genes.get(this.genIterator.next());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype = (Genotype) o;
        return Objects.equals(genes, genotype.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }

    @Override
    public int compareTo(Genotype other) {
        return 0;
    }

}