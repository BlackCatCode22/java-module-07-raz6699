# Module07 Activities: Design Document

# Zoo Animal Management System - Overview

This project is a **Zoo Animal Management System** that processes and outputs data about animals arriving at the zoo, along with their species, gender, origin, weight, and other details. The system generates a report that is written both to the console and to a file (`zooPopulation.txt`).

## Key Features
- **Animal Registration**: Animals are registered with their species, age, gender, weight, origin, and other relevant details.
- **Species-Specific Habitats**: The animals are categorized by their species into separate habitats, and each habitat contains a list of animals.
- **Gender Balance**: For each new animal, gender alternates between male and female.
- **Animal Name Assignment**: Names are assigned randomly from a predefined list specific to each species.

## Updates & Changes

### 1. **File Input & Animal Parsing**
- Two input files (`animalNames.txt` and `arrivingAnimals.txt`) are read to populate animal data and generate animals for the zoo.
- Animal names are parsed from the `animalNames.txt` file, while other attributes (species, age, gender, etc.) are parsed from the `arrivingAnimals.txt` file.

### 2. **Data Structure for Animal Management**
- **`habitatMap`**: A `LinkedHashMap` is used to map species to their respective lists of animals. Each species has its own list.
- **`speciesCounters`**: A `HashMap` that tracks the number of animals added for each species, ensuring unique IDs are generated correctly.

### 3. **Animal Object**
- An `Animal` class encapsulates data for each animal, including:
  - `name`, `id`, `species`, `gender`, `color`, `age`, `weight`, `origin`, `birthSeason`, `birthDate`, and `arrivalDate`.
- The `toString()` method outputs a human-readable format of the animal's data.

### 4. **Enhanced Reporting**
- Each species is reported in its own section, with the total number of animals, gender breakdown (male/female), and individual animal details.
- Animal reports are output both to the console and to the `zooPopulation.txt` file.
- Animal names are displayed with the species emoji for clarity and a touch of visual appeal.

### 5. **Emoji Integration**
- **Species Emojis** have been added to the report for better readability:
  - 🦁 for Lions
  - 🐯 for Tigers
  - 🐻 for Bears
  - 🦝 for Hyenas
  - 🐾 for Default

### 6. **Animal Generation Logic**
- **Gender Alternation**: Generated animals alternate between male and female, ensuring gender balance in the zoo.
- **Weight and Birthdate Simulation**: Weights and birthdates are simulated to maintain a consistent range (e.g., weight gradually increases, and birthdates are set to 5 years ago).
- **Unique ID Generation**: Each animal is assigned a unique ID based on its species.

### 7. **Code Refactoring & Optimizations**
- **Simplified Logic**: The code was optimized by using `LinkedHashMap` for maintaining species order, and redundant code was removed.
- **Streamlined Data Handling**: The input files are read efficiently, and unnecessary checks are minimized.

## Output Example:

For example, if there are hyenas, lions, tigers, and bears in the zoo, the output might look like this:

- 🦝 Hy01; Lou; birth date: 2013-09-21; brown; Male; 150 pounds; from Friguia Park, Tunisia; arrived 2025-04-18
- 🦁 Li01; Elsa; birth date: 2019-03-21; tan; Female; 300 pounds; from Zanzibar, Tanzania; arrived 2025-04-18
- 🐯 Ti01; ; birth date: 2023-03-21; gold and tan stripes; Male; 270 pounds; from Dhaka, Bangladesh; arrived 2025-04-18
- 🐻 Be01; Baloo; birth date: 2021-09-21; black; Female; 355 pounds; from Woodland park Zoo, Washington ; arrived 2025-04-18
- etc.

This system helps organize a zoo’s population by tracking each animal's details, ensuring that the zoo’s records remain clear and up-to-date.
