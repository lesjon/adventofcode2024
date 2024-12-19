String[] towels;

Map<String, Long> cache = new HashMap<>(Map.of("", 1L));

long possibleDesignsCount(String design) {
    if(cache.containsKey(design)){
        return cache.get(design);
    }
    long count = Arrays.stream(towels)
        .filter(towel -> design.startsWith(towel))
        .mapToLong(towel -> possibleDesignsCount(design.substring(towel.length())))
        .sum();
    cache.put(design, count);
    return count;
}

void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    towels = lines.get(0).split(", ");
    var result = lines.stream()
        .skip(2)
        .mapToLong(design -> possibleDesignsCount(design))
        .sum();
    println(result);
}
