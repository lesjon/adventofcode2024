import static java.lang.Integer.valueOf;

static final int MAX = 70;
static final int BYTES_FALLEN = 1024;
static final String FILE = "input.txt";

record Vector(int x, int y){
    static Vector from(String line){
        return new Vector(valueOf(line.split(",")[0]), valueOf(line.split(",")[1]));
    }

    Vector distance(Vector o){
        return new Vector(x - o.x, y - o.y);
    }

    int magnitude(){
        return x*x+y*y;
    }

    Vector add(int addX, int addY){
        return new Vector(x+addX,y+addY);
    }
}

static final Vector TARGET = new Vector(MAX, MAX);

record State(Vector pos, int steps, Set<Vector> path){}

String display(Collection<Vector> bytes, Vector currPos){
    var sb = new StringBuilder();
    for (int y = 0; y <= MAX; y++) {
        for (int x = 0; x <= MAX; x++) {
            var checkPos = new Vector(x,y);
            if(currPos.equals(checkPos )){
                sb.append('O');
            }else if (bytes.contains(checkPos)) {
                sb.append('#');
            }else{
                sb.append('.');
            }
        }
        sb.append('\n');
    }
    return sb.toString();
};

int distanceFromTarget(Vector pos){
    return pos.distance(TARGET).magnitude();
}

void main() throws Exception {
    List<Vector> bytes = Files.lines(Path.of(FILE)).map(Vector::from).toList();
    println(bytes);
    Set<Vector> wanted = new HashSet<>();
    for (int y = 0; y <= MAX; y++) {
        for (int x = 0; x <= MAX; x++) {
            wanted.add(new Vector(x,y));
        }
    }


    for (int i = BYTES_FALLEN; i < bytes.size(); i++) {
        if(!wanted.contains(bytes.get(i-1))) {
            continue;
        }
        Set<Vector> visited = new HashSet<>();
        PriorityQueue<State> queue = new PriorityQueue<>((lhs, rhs) -> Integer.compare(lhs.steps, rhs.steps));
        queue.add(new State(new Vector(0,0), 0, Set.of()));
        boolean reachedTarget = false;
        while(queue.size() > 0) {
            // println(queue);
            var curr = queue.poll();
            // println(display(bytes.subList(0, i), curr.pos));
            // println("curr: " + curr);
            if(curr.pos.equals(TARGET)){
                println(curr.steps);
                reachedTarget = true;
                wanted = curr.path;
                break;
            }
            for (var next : List.of(curr.pos.add(0,-1), curr.pos.add(1,0), curr.pos.add(0,1), curr.pos.add(-1,0))) {
                boolean insideMap = next.x <= MAX && next.x >= 0 && next.y <= MAX && next.y >= 0;
                if(!insideMap) continue;
                boolean hitsByte = bytes.subList(0, i).contains(next);
                if(hitsByte) continue;
                if(visited.contains(next)) continue;
                visited.add(next);
                var newPath = new HashSet<>(curr.path);
                newPath.add(next);
                queue.add(new State(next, curr.steps+1, newPath));
            }
        }
        if(!reachedTarget){
            println(i + ": " + bytes.get(i-1));
            break;
        }
    }

}
