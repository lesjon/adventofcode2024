record Vector(int x, int y) {

    Vector add(Vector o) {
        return new Vector(x + o.x(), y + o.y());
    }

    Vector rotateLeft() {
        return new Vector(-y, x);
    }

    Vector rotateRight() {
        return new Vector(y, -x);
    }
}

record Edge(Vector loc, Vector dir){

    Edge move(Vector translation) {
        return new Edge(loc.add(translation), dir);
    }

    Edge left() {
        return this.move(this.dir.rotateLeft());
    }
    Edge right() {
        return this.move(this.dir.rotateRight());
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

void getSide(Set<Edge> edges, Edge edge, Set<Edge> visitedEdges, Set<Edge> side) {
    if(visitedEdges.contains(edge)) {
        return;
    }
    side.add(edge);
    visitedEdges.add(edge);
    Edge left = edge.left();
    if(edges.contains(left)){
        getSide(edges, left, visitedEdges, side);
    }
    Edge right = edge.right();
    if(edges.contains(right)){
        getSide(edges, right, visitedEdges, side);
    }
}

long regionSides(Set<Vector> region) {
    Set<Edge> edges = new HashSet<>();
    for (Vector loc : region) {
        for (Vector dir : DIRS) {
            if(region.contains(loc.add(dir))) {
               continue; 
            }
            edges.add(new Edge(loc, dir));
        }
    }

    List<Set<Edge>> sides = new ArrayList<>();
    Set<Edge> visitedEdges = new HashSet<>();
    for (Edge edge : edges) {
        if(visitedEdges.contains(edge)){
            continue;
        }
        Set<Edge> side = new HashSet<>();
        getSide(edges, edge, visitedEdges, side);
        sides.add(side);
    }

    return sides.size();
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
    println(regions.stream().mapToLong(r -> regionSize(r) * regionSides(r)).sum());
}
