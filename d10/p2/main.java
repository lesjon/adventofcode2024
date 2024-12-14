record Vector(int x, int y){

    Vector add(Vector o){
        return new Vector(x + o.x(), y + o.y());
    }
}

static final Vector[] DIRS = new Vector[]{new Vector(1,0), new Vector(0,1), new Vector(-1,0), new Vector(0, -1)};

List<Vector> getReachablePositions(int[][] map, Vector pos){
    List<Vector> result = new ArrayList<>();
    var curr = map[pos.y()][pos.x()];
    for(var dir : DIRS){
        var check = pos.add(dir);
        try{
            int checkValue = map[check.y()][check.x()];
            if(checkValue - curr == 1) result.add(check);
        } catch (IndexOutOfBoundsException e) { }
    }
    return result;
}

int getReachablePeaks(int[][] map, Vector pos){
    if(map[pos.y()][pos.x()] == 9){
        return 1;
    }
    int result = 0;
    for (Vector reachablePos : getReachablePositions(map, pos)) {
        result += getReachablePeaks(map, reachablePos);
    }
    return result;
}

void main() throws Exception {
    int[][] map = Files.lines(Path.of("input.txt"))
        .map(String::chars)
        .map(chars -> chars.map(i -> i - '0'))
        .map(IntStream::toArray).toArray(int[][]::new);
    println(Arrays.deepToString(map));
    int total = 0;
    for (int y = 0; y < map.length; y++) {
        for (int x = 0; x < map[0].length; x++) {
            if(0 == map[y][x]){
                var reachablePeaks = getReachablePeaks(map, new Vector(x, y));
                total += reachablePeaks;
            }
        }
    }
    println(total);

}
