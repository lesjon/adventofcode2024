sealed interface Size permits File, Space {
    long size();
}

record File(long size, long fileId) implements Size {

    public String toString() {
        var sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(""+fileId);
        }
        return sb.toString();
    }
}
record Space(long size) implements Size {
    public String toString() {
        var sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(".");
        }
        return sb.toString();
    }
}

enum State{
    FILE, SPACE
}

Optional<File> findFittingFile(List<Size> disk, int minIndex, long maxSize) {
    for (int i = disk.size()-1; i > minIndex; i--) {
        switch (disk.get(i)) {
            case File(long size, long fileId) when size <= maxSize -> {
                File f = (File) disk.get(i);
                disk.set(i, new Space(f.size()));
                return Optional.of(f);
            }
            default -> {}
        }
    }
    return Optional.empty();
}

void main() throws Exception {
    int[] sizes = Files.lines(Path.of("test.txt"))
        .findFirst().get()
        .chars().map(c -> c - '0').toArray();

    State state = State.FILE;
    List<Size> disk = new ArrayList<>();
    for (int fileId = 0; fileId < sizes.length; fileId++) {
        state = switch (state) {
            case FILE -> {
                disk.add(new File(sizes[fileId], fileId/2));
                yield State.SPACE;
            }
            case SPACE -> {
                disk.add(new Space(sizes[fileId]));
                yield State.FILE;
            }
        };
    }
    List<Size> newDisk = new ArrayList<>();
    for (int i = 0; i < disk.size(); i++) {
        switch (disk.get(i)) {
            case Space(long size) -> {
                List<File> filesToMove = new ArrayList<>();
                var fileOption = findFittingFile(disk, i, size);
                while(fileOption.isPresent()){
                    var f = fileOption.get();
                    filesToMove.add(f);
                    size -= f.size();
                    fileOption = findFittingFile(disk, i, size);
                }
                newDisk.addAll(filesToMove);
                newDisk.add(new Space(size));
            }
            case File f -> newDisk.add(f);
        }
    }
    long total = 0;
    long position = 0;
    for(Size s : newDisk){
        switch (s) {
            case File(long size, long fileId) -> {
                for (int i = 0; i < size; i++) {
                    total += position*fileId;
                    position++;
                }
            }
            case Space(long size) -> position += size;
        }
    }
    println(total);
}
