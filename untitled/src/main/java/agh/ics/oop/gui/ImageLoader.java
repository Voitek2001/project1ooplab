package agh.ics.oop.gui;


import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

// wczytajemy wszystkie grafiki jedynie raz i przechowywujemy je w mapie
// aby nie wczytywać ani nie używać duplikatów ponieważ jest to pamięcio i czaso chłonne.
public class ImageLoader {

    private final static Map<String, Image> mapOfImages = new HashMap<>();
    private final static Map<String, Image> mapOfImagesEverything = new HashMap<>();

    /**    "Zasoby takie jak ImageView są pamięciochłonne,
     dlatego ważne jest aby nie były one tworzone bez potrzeby"
     idąc tą logiką warto zastanowić sie czy jest sens wczytywać obrazek jeśli
     wogóle go nie uzyjemy, utworzymy go tylko po to aby GC na końcu go usunął
     z drugiej jednak strony moglibyśmy wczytywać wszystko w folderze, co pozwala odrazu sprawdzić
     czy nie dostaniemy sytuacji kiedy chcielibyśmy użyć jakiego obrazka
     a nie zostanie on poprawnie wczytany
     */
    public static Image loadImage(String pathToDirectory) throws FileNotFoundException {

        if (!mapOfImages.containsKey(pathToDirectory)) {
            // jeśli mapa nie zawiera obiektu którego chcemy użyć to wczytujemy go
            Image image = new Image(new FileInputStream(pathToDirectory));

            mapOfImages.put(pathToDirectory, image);
        }
        return mapOfImages.get(pathToDirectory);

    }

    public static void LoadEverythingInDirectory(String pathToDirectory) throws FileNotFoundException {
        String[] filesToLoad = listFilesForFolder(pathToDirectory);

        for (String fileName : filesToLoad) {
            Image loadedImg = new Image(new FileInputStream(pathToDirectory + fileName));
            mapOfImagesEverything.put(pathToDirectory+fileName, loadedImg);
        }

    }

    private static String[] listFilesForFolder(String pathToFolder) throws NullPointerException {
        List<String> filesName = new LinkedList<>();
        File folder = new File(pathToFolder);
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.isDirectory()) {
                filesName.add(fileEntry.getName());
            }
        }
        return filesName.toArray(String[]::new);
    }
}
