String[] towels;

Map<String, Boolean> cache = new HashMap<>(Map.of("", true));

boolean isPossible(String design) {
    // println("isPossible(" + design);
    if(cache.containsKey(design)){
        // println("cache hit");
        return cache.get(design);
    }
    for (String towel : towels) {
        if(!design.startsWith(towel)){
            continue;
        }
        if(isPossible(design.substring(towel.length()))){
            cache.put(design, true);
            return true;
        }
    }
    cache.put(design, false);
    return false;
}

void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    towels = lines.get(0).split(", ");
    println(Arrays.toString(towels));
    
    var result = lines.stream()
        .skip(2)
        // .peek(l -> println(l))
        .filter(design -> isPossible(design))
        .count();
    println(result);
}
