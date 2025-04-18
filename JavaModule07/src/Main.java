import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Animal {
    private String name;
    private String id;
    private String species;
    private String gender;
    private String color;
    private int age;
    private int weight;
    private String origin;
    private String birthSeason;
    private String birthDate;
    private String arrivalDate;

    public Animal(String id, String name, int age, String species, String gender, String color, int weight,
                  String origin, String birthSeason, String birthDate, String arrivalDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.species = species;
        this.gender = gender;
        this.color = color;
        this.weight = weight;
        this.origin = origin;
        this.birthSeason = birthSeason;
        this.birthDate = birthDate;
        this.arrivalDate = arrivalDate;
    }

    public String getSpecies() {
        return species.toLowerCase();
    }

    public String getGender() {
        return gender.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    @Override
    public String toString() {
        return id + "; " + (name != null ? name : "[Unnamed]") + "; birth date: " + birthDate + "; " + color + "; " + gender + "; " +
                weight + " pounds; from " + origin + "; arrived " + arrivalDate;
    }
}

public class Main {
    private static final Map<String, Integer> speciesCounters = new HashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        Map<String, List<String>> originalNameMap = new HashMap<>();
        Set<String> usedNames = new HashSet<>();
        Map<String, List<Animal>> habitatMap = new LinkedHashMap<>();

        File namesFile = new File("animalNames.txt");
        File animalsFile = new File("arrivingAnimals.txt");

        if (!namesFile.exists() || !animalsFile.exists()) {
            System.out.println("Missing input file(s).");
            return;
        }

        // Read animalNames.txt
        try (BufferedReader reader = new BufferedReader(new FileReader(namesFile))) {
            String line;
            String currentSpecies = "";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.endsWith("Names:")) {
                    currentSpecies = line.replace(" Names:", "").trim().toLowerCase();
                    originalNameMap.put(currentSpecies, new ArrayList<>());
                } else if (!currentSpecies.isEmpty()) {
                    String[] names = line.split(", ");
                    for (String name : names) {
                        originalNameMap.get(currentSpecies).add(name);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading animalNames.txt: " + e.getMessage());
        }

        // Read arrivingAnimals.txt
        try (BufferedReader reader = new BufferedReader(new FileReader(animalsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length < 6) continue;

                String[] tokens = parts[0].split(" ");
                int age = Integer.parseInt(tokens[0]);
                String gender = tokens[3];
                String species = tokens[4].toLowerCase();

                String birthSeason = parts[1].toLowerCase().contains("unknown") ? "unknown"
                        : parts[1].replace("born in ", "").trim().toLowerCase();

                String color = parts[2].replace("color", "").trim();
                int weight = Integer.parseInt(parts[3].replace("pounds", "").trim());
                String origin = parts[4].replace("from ", "") + ", " + parts[5];

                String name = assignName(originalNameMap, species, usedNames);
                String id = genUniqueID(species);
                String birthDate = genBirthDay(age, birthSeason);
                String arrivalDate = LocalDate.now().format(formatter);

                Animal animal = new Animal(id, name, age, capitalize(species), capitalize(gender), color,
                        weight, origin, birthSeason, birthDate, arrivalDate);

                habitatMap.computeIfAbsent(species, k -> new ArrayList<>()).add(animal);
            }
        } catch (IOException e) {
            System.out.println("Error reading arrivingAnimals.txt: " + e.getMessage());
        }

        // Sort animals by name and reset IDs
        for (Map.Entry<String, List<Animal>> entry : habitatMap.entrySet()) {
            String species = entry.getKey();
            List<Animal> animals = entry.getValue();

            animals.sort(Comparator.comparing(Animal::getName));

            String prefix = speciesPrefix(species);
            for (int i = 0; i < animals.size(); i++) {
                String newId = String.format("%s%02d", prefix, i + 1);
                animals.get(i).setId(newId);
            }
        }

        // Output to file and console
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("zooPopulation.txt"))) {
            writer.write("Zoo Population Report\n=====================\n\n");
            System.out.println("Zoo Population Report\n=====================\n");

            // Summary by species
            writer.write("Summary by Species:\n");
            System.out.println("Summary by Species:");
            int totalAnimals = 0;
            for (String species : habitatMap.keySet()) {
                List<Animal> animals = habitatMap.get(species);
                int males = (int) animals.stream().filter(a -> a.getGender().equals("male")).count();
                int females = (int) animals.stream().filter(a -> a.getGender().equals("female")).count();
                totalAnimals += animals.size();
                String emoji = getSpeciesEmoji(species);
                System.out.println("  • " + emoji + " " + capitalize(species) + "s: " + animals.size() + " (" + males + "M, " + females + "F)");
                writer.write(emoji + " " + capitalize(species) + "s: " + animals.size() + " total (" + males + " male, " + females + " female)\n");
            }
            writer.write("\nTotal: " + totalAnimals + " animals\n----------------------------\n\n");
            System.out.println("\nTotal: " + totalAnimals + " animals\n----------------------------\n");

            for (String species : habitatMap.keySet()) {
                List<Animal> animals = habitatMap.get(species);
                int maleCount = (int) animals.stream().filter(a -> a.getGender().equals("male")).count();
                int femaleCount = (int) animals.stream().filter(a -> a.getGender().equals("female")).count();
                String emoji = getSpeciesEmoji(species);
                String sectionTitle = emoji + " " + capitalize(species) + " Habitat (" + animals.size() + " total; "
                        + maleCount + " male, " + femaleCount + " female):\n";

                writer.write(sectionTitle);
                System.out.print(sectionTitle);

                for (Animal animal : animals) {
                    writer.write(animal.toString() + "\n");
                    System.out.println(animal.toString());
                }
                writer.write("\n");
                System.out.println();
            }

            System.out.println("✅ zooPopulation.txt created and displayed above.");

        } catch (IOException e) {
            System.out.println("Error writing to zooPopulation.txt: " + e.getMessage());
        }
    }

    private static String assignName(Map<String, List<String>> originalNameMap,
                                     String species,
                                     Set<String> usedNames) {
        List<String> allNames = new ArrayList<>(originalNameMap.getOrDefault(species, new ArrayList<>()));

        List<String> availableNames = new ArrayList<>();
        for (String name : allNames) {
            if (!usedNames.contains(name)) {
                availableNames.add(name);
            }
        }

        if (availableNames.isEmpty()) {
            usedNames.removeAll(allNames);
            availableNames.addAll(allNames);
        }

        String selectedName = null;
        boolean nameAssigned = false;

        while (!nameAssigned && !availableNames.isEmpty()) {
            Collections.shuffle(availableNames);
            for (String name : availableNames) {
                if (!usedNames.contains(name)) {
                    selectedName = name;
                    usedNames.add(name);
                    nameAssigned = true;
                    break;
                }
            }
        }

        if (!nameAssigned) {
            selectedName = "[Unnamed]";
        }

        return selectedName;
    }

    private static String genUniqueID(String species) {
        String prefix = speciesPrefix(species);
        int count = speciesCounters.getOrDefault(species, 0) + 1;
        speciesCounters.put(species, count);
        return prefix + String.format("%02d", count);
    }

    private static String genBirthDay(int age, String season) {
        int year = LocalDate.now().getYear() - age;
        Month month;

        switch (season.toLowerCase()) {
            case "spring": month = Month.MARCH; break;
            case "summer": month = Month.JUNE; break;
            case "fall": month = Month.SEPTEMBER; break;
            case "winter": month = Month.DECEMBER; break;
            default: month = Month.JANUARY;
        }

        LocalDate date = LocalDate.of(year, month, 21);
        return date.format(formatter);
    }

    private static String speciesPrefix(String species) {
        return switch (species.toLowerCase()) {
            case "hyena" -> "Hy";
            case "lion" -> "Li";
            case "tiger" -> "Ti";
            case "bear" -> "Be";
            default -> "Un";
        };
    }

    private static String capitalize(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private static String getSpeciesEmoji(String species) {
        return switch (species.toLowerCase()) {
            case "lion" -> "🦁";
            case "tiger" -> "🐯";
            case "bear" -> "🐻";
            case "hyena" -> "🦝";
            default -> "🐾";
        };
    }
}
