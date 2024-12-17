record Vector(int x, int y) {
    Vector rotateCW(){
        return new Vector(-y, x);
    }
    Vector rotateCCW(){
        return new Vector(y, -x);
    }

    Vector add(Vector o){
        return new Vector(x+o.x, y+o.y);
    }
}

record Reindeer(Vector pos, Vector dir) {}

record State(Reindeer reindeer, long cost, List<Vector> path) {}

Set<Vector> walls = new HashSet<>();
Reindeer reindeer;
Vector target;

int height = 0;
int width = 0;
void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    // println(lines);
    for (height = 0; height < lines.size(); height++) {
        String line = lines.get(height);
        for (int x = 0; x < line.length(); x++) {
            switch (line.charAt(x)) {
                case '#' -> walls.add(new Vector(x, height));
                case 'S' -> reindeer = new Reindeer(new Vector(x, height), new Vector(1, 0));
                case 'E' -> target = new Vector(x, height);
                default -> {}
            }
            width = x;
        }
    }
    // println(walls);
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            if(walls.contains(new Vector(x,y))){
                print('#');
            }else{
                print('.');
            }
        }
        println("");
    }
    PriorityQueue<State> queue = new PriorityQueue<>((State l, State r) -> Long.compare(l.cost(), r.cost()));
    queue.add(new State(reindeer, 0, new ArrayList<>(List.of(reindeer.pos()))));
    Set<Reindeer> visited = new HashSet<>();
    long minimalCost = Long.MAX_VALUE;
    Map<Long, List<Vector>> pathScores = new HashMap<>();
    while(queue.size() > 0){
        // println("queue.size(): " + queue.size());
        // println("visited: " + visited);
        State state = queue.poll();
        if (state.cost() > minimalCost){
            continue;
        }
        // println("state: " + state);
        var reindeer = state.reindeer();
        visited.add(reindeer);
        if(reindeer.pos().equals(target)){
            minimalCost = state.cost();
            pathScores.compute(minimalCost, (k,v) -> {
                if(null == v){
                    return state.path();
                } 
                v.addAll(state.path());
                return v;
            });
            // println("path: " + state.path());
            continue;
        }
        var cw = new State(new Reindeer(reindeer.pos(), reindeer.dir().rotateCW()), state.cost() + 1000, state.path());
        if(!visited.contains(cw.reindeer()) && !walls.contains(cw.reindeer().pos()))
            queue.add(cw);
        var ccw = new State(new Reindeer(reindeer.pos(), reindeer.dir().rotateCCW()), state.cost() + 1000, state.path());
        if(!visited.contains(ccw.reindeer()) && !walls.contains(ccw.reindeer().pos()))
            queue.add(ccw);
        var path = new ArrayList<>(state.path());
        var nextPos = reindeer.pos().add(reindeer.dir());
        path.add(nextPos);
        var step = new State(new Reindeer(nextPos, reindeer.dir()), state.cost() + 1, path);
        if(!visited.contains(step.reindeer()) && !walls.contains(step.reindeer().pos()))
            queue.add(step);
    }
    println(pathScores.values().stream().flatMap(List::stream).distinct().count());

}
