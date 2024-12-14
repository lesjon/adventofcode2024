enum State{
    FILE, SPACE
}

Optional<Integer> lastFileId(int[] sizes, long lower){
    for (int i = sizes.length/2; i >= lower; i--) {
        if(sizes[i*2] > 0){
            return Optional.of(i);
        }
    }
    return Optional.empty();
}

void main() throws Exception {
    int[] sizes;
    try(var fr = new FileReader("input.txt"); var bfr = new BufferedReader(fr)){
        sizes = bfr.readLine().chars().map(c -> c - '0').toArray();
    }
    State state = State.FILE;
    long fileId = 0;
    long position = 0;
    long total = 0;
    for (int size : sizes) {
        state = switch (state) {
            case FILE -> {
                for(int i = 0; i < size; i++){
                    total += fileId * position;
                    position++;
                    sizes[((int)fileId)*2]--;
                }
                fileId++;
                yield State.SPACE;
            }
            case SPACE -> {
                for (int i = 0; i < size; i++) {
                    Optional<Integer> lastFile = lastFileId(sizes, fileId);
                    if(lastFile.isEmpty()) yield State.FILE;
                    int lastFileI = lastFile.get();
                    sizes[lastFileI*2]--;
                    total += lastFileI * position;
                    position++;
                }
                yield State.FILE;
            }
        };
    }
    println(total);
}
