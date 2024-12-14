static final int width = 130;
static final int height = 130;

record State(Point guard, Point direction){}

record Point(int x, int y) {
    Point rotate() {
        return new Point(-y, x);
    }

    Point add(Point o) {
        return new Point(x+o.x(), y+o.y());
    }
}

boolean doesLoop(Set<Point> obstacles, Point guard){
    Set<State> visited = new HashSet<>();
    Point dir = new Point(0,-1);
    while(guard.x() >= 0 && guard.x() < width && guard.y() >= 0 && guard.y() < height){
        var state = new State(guard, dir);
        if (visited.contains(state)) {
            return true;
        }
        visited.add(state);
        Point next = guard.add(dir);
        if(obstacles.contains(next)){
            dir = dir.rotate();
            continue;
        }
        guard = next;
    }
    return false;
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt")).toList();

    Point guard = null;
    Set<Point> obstacles = new HashSet<>();

    Set<Point> positions = new HashSet<>();
    int x = 0;
    int y = 0;
    for (var line : lines) {
        for (int c : line.chars().toArray()) {
            switch(c) {
                case '^' -> guard = new Point(x, y);
                case '#' -> obstacles.add(new Point(x,y));
                case '.' -> {}
                default -> println("default!?" + c);
            }
            x++;
        }
        y++;
        x = 0;
    }

    int total = 0;
    for (x = 0; x < width; x++) {
        for (y = 0; y < height; y++) {
            var newObstacle = new Point(x,y);
            if(newObstacle.equals(guard)){
                continue;
            }
            if(obstacles.contains(newObstacle)){
                continue;
            }
            obstacles.add(newObstacle);
            if(doesLoop(obstacles, guard)){
                total++;
            }
            obstacles.remove(newObstacle);
        }
    }
    println(total);
}
