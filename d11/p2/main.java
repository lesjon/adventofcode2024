// If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
// If the stone is engraved with a number that has an even number of digits, it is replaced by two stones. The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
// If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.

record StoneCount(String stone, int blinks){}

Map<StoneCount, Long> cache = new HashMap<>();

String removeLeadingZeros(String stone){
    return "" + Long.parseLong(stone);
}

long stoneCount(String stone, int blinks){
    if(blinks == 0) return 1;
    var stoneCountRecord = new StoneCount(stone, blinks);
    if(cache.containsKey(stoneCountRecord)){
        return cache.get(stoneCountRecord);
    }
    long total = 0;
    total += switch (stone) {
        case "0" -> stoneCount("1", blinks - 1);
        case String s when (s.length() % 2 == 0) -> stoneCount(s.substring(0,s.length()/2), blinks - 1) + stoneCount(removeLeadingZeros(s.substring(s.length()/2, s.length())), blinks - 1);
        default -> stoneCount("" + 2024L * Long.parseLong(stone), blinks - 1 );
    };
    cache.put(stoneCountRecord, total);
    return total;
}

void main() throws Exception {
    String[] stones = Files.lines(Path.of("input.txt")).findFirst().get().split(" ");
    println(Arrays.toString(stones));
    long total = 0;
    for (var stone : stones) {
        total += stoneCount(stone, 75);
    }
    println(total);
}
