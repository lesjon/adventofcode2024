void main() throws Exception {
    var linesStream = Files.lines(Path.of("input.txt"));
    Iterable<String> lines = () -> linesStream.iterator();
    List<Long> locks = new ArrayList<>();
    List<Long> keys = new ArrayList<>();
    int state = 0;
    boolean processingLock = true;
    int[] counts = new int[5];
    for (String line : lines) {
        if (line.isEmpty()) {
            state = 0;   
            continue;
        }
        println(state + ", processingLock: " + processingLock + ", line: " + line);
        switch (state) {
            case 0 -> {
                processingLock = line.equals("#####");
            }
            case 1,2,3,4,5 -> {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '#') {
                        counts[i]++;
                    }
                }
            }
            case 6 -> {
                println("counts: " + Arrays.toString(counts));
                if(processingLock){
                    locks.add((long) (counts[0] << 4*4) + (counts[1] << 3*4) + (counts[2] << 2*4) + (counts[3] << 4) + counts[4]);
                } else {
                    keys.add((long) (counts[0] << 4*4) + (counts[1] << 3*4) + (counts[2] << 2*4) + (counts[3] << 4) + counts[4]);
                }
                counts = new int[5];
            }
            default -> throw new RuntimeException("Reached state: " + state);
        }
        state++;
    }
    println(keys);
    println(locks);
    Set<List<Long>> matches = new HashSet<>();
    for (long key : keys) {
        for (long lock : locks) {
            long mask = 0x55555;
            // println(String.format("lock:0x%05X key:0x%05X mask:0x%05X", lock, key, mask));
            // println(String.format("sum:0x%05X", key + lock));
            // println(String.format("sum2:0x%05X", key + lock + 0x22222));
            // println(String.format("sum2-mask:0x%05X", (key + lock + 0x22222) & ~0x77777));
            if ((key + lock + 0x22222 & ~0x77777) == 0L) {
                matches.add(List.of(lock, key));
            }
        }
    }
    for (var match : matches) {
        println(String.format("key:0x%05X lock:0x%05X", match.get(0), match.get(1) & 0x77777));
    }
    println(matches.size());
}
