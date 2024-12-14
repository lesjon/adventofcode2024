record Point(int x, int y) {

    Point rotate() {
        return new Point(-y, x);
    }

    Point add(Point o) {
        return new Point(x+o.x(), y+o.y());
    }
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt"));
    Point guard = null;
    Set<Point> obstacles = new HashSet<>();
    int width = 0;
    int height = 0;
    Set<Point> visited = new HashSet<>();
    int x = 0;
    int y = 0;
    for (var line : lines.toList()) {
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
        width = x;
        x = 0;
    }
    height = y;
    println("obstacles"+obstacles);
    Point dir = new Point(0,-1);
    while(guard.x() >= 0 && guard.x() < width && guard.y() >= 0 && guard.y() < height){
        visited.add(guard);
        Point next = guard.add(dir);
        if(obstacles.contains(next)){
            dir = dir.rotate();
            continue;
        }
        guard = next;
    }
    println("visited"+visited);
    println(visited.size());
}
