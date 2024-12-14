record Vector(int x, int y) {

    Vector add(Vector o) {
        return new Vector(x + o.x(), y + o.y());
    }
}

Set<Vector> visited = new HashSet<>();
static final Vector[] DIRS = new Vector[] {new Vector(1,0), new Vector(0,-1), new Vector(-1,0), new Vector(0,1)};

Set<Vector> getRegion(int[][] map, Vector loc) {
    int curr = map[loc.y()][loc.x()];
    Set<Vector> set = new HashSet<>();
    set.add(loc);
    for (Vector dir : DIRS) {
        var check = loc.add(dir);
        if(visited.contains(check)) continue;
        try{
            if (map[check.y()][check.x()] != curr) {
                continue;
            }
        } catch (IndexOutOfBoundsException e) {
            continue;
        }
        visited.add(check);
        set.add(check);
        set.addAll(getRegion(map, check));
    }
    return set;
}

long regionSize(Set<Vector> region) {
    return region.size();
}

long regionPerimeter(Set<Vector> region) {
    long result = 0;
    for (Vector loc : region) {
        for (Vector dir : DIRS) {
            if(region.contains(loc.add(dir))) {
               continue; 
            }
            result++;
        }
    }
    return result;
}

void main() throws Exception {
    int[][] map = Files.lines(Path.of("input.txt"))
        .map(String::chars)
        .map(IntStream::toArray)
        .toArray(int[][]::new);
    List<Set<Vector>> regions = new ArrayList<>();
    for (int y = 0; y < map.length; y++) {
        for (int x = 0; x < map.length; x++) {
            Vector loc = new Vector(x,y);
            if(visited.contains(loc)) continue;
            regions.add(getRegion(map, loc));
        }
    }
    println(regions.stream().mapToLong(r -> regionSize(r) * regionPerimeter(r)).sum());
}
