String[] towels;

Map<String, Long> cache = new HashMap<>(Map.of("", 1L));

long possibleDesignsCount(String design) {
    // println("possibleDesignsCount(" + design);
    if(cache.containsKey(design)){
        println("cache hit");
        return cache.get(design);
    }
    long count = 0;
    for (String towel : towels) {
        if(!design.startsWith(towel)){
            continue;
        }
        count += possibleDesignsCount(design.substring(towel.length()));
    }
    cache.put(design, count);
    return count;
}

void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    towels = lines.get(0).split(", ");
    println(Arrays.toString(towels));
    
    var result = lines.stream()
        .skip(2)
        // .peek(l -> println(l))
        .mapToLong(design -> possibleDesignsCount(design))
        // .peek(l -> println(l))
        .sum();
    println(result);
}
