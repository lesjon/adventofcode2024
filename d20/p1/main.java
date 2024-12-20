record Vec(int x, int y) {

    Vec add(Vec o){
        return new Vec(x+o.x, y+o.y);
    }

    Vec subtract(Vec o){
        return new Vec(x-o.x, y-o.y);
    }
}

static final Vec[] DIRS = new Vec[] {new Vec(1,0), new Vec(-1,0), new Vec(0,-1), new Vec(0,1) };

int height = 0;
int width = 0;

Set<Vec> walls = new HashSet<>();

record ProgramState(Vec pos) {}

Map<ProgramState, Long> visited = new HashMap<>();
Vec end;

record GameState(ProgramState programState, long ps){

    Set<GameState> nextStates(Set<Vec> walls, int width, int height, Set<Vec> wallsHit){

        var result = new HashSet<GameState>();

        for (Vec dir : DIRS) {
            var nextPos = programState.pos.add(dir);
            if(1 > nextPos.x || width-1 <= nextPos.x) continue;
            if(1 > nextPos.y || height-1 <= nextPos.y) continue;
            if(walls.contains(nextPos)){
                if(wallsHit != null) wallsHit.add(nextPos);
                continue;
            }
            var next = new GameState(new ProgramState(nextPos), ps+1);
            result.add(next);
        }
        return result;
    }
    
}

String display(GameState gameState, Set<Vec> walls){
    var sb = new StringBuilder();
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            var check = new Vec(x,y);
            if(gameState.programState.pos.equals(check)){
                sb.append('0');
            }else if(walls.contains(check )){
                sb.append('#');
            } else {
                sb.append('.');
            }
        }
        sb.append('\n');
    }
    return sb.toString();
}

void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));

    Vec start = null;
    for (String line : lines) {
        for (width = 0; width < line.length(); width++) {
            switch(line.charAt(width)){
                case '#' -> walls.add(new Vec(width, height));
                case 'S' -> start = new Vec(width, height);
                case 'E' -> end = new Vec(width, height);
            }
        }
        height++;
    }
    println("start: " + start);
    println("end: " + end);
    println(walls);
    PriorityQueue<GameState> queue = new PriorityQueue<>((lhs, rhs) -> Long.compare(lhs.ps, rhs.ps));

    var startState = new GameState(new ProgramState(start), 0);
    queue.add(startState);
    visited.put(startState.programState, 0L);
    long noCheatTime = Long.MAX_VALUE;

    Set<Vec> wallsHit = new HashSet<>();
    while(!queue.isEmpty()){
        var curr = queue.poll();
        // println(display(curr, walls));
        // if(readln(""+curr).equals("q")) break;
        if(curr.ps >= noCheatTime){
            continue;
        }
        if(curr.programState.pos.equals(end)){
            println("found noCheatTime: " + curr.ps);
            noCheatTime = curr.ps;
            break;
        }
        for(var next : curr.nextStates(walls, width, height, wallsHit)){
            if(visited.containsKey(next.programState)/*  && visited.get(next.programState) < next.ps */){
                continue;
            }
            queue.add(next);
            visited.put(next.programState, next.ps);
        }
    }

    long total = 0;
    for (Vec wall : wallsHit) {
        walls.remove(wall);

        startState = new GameState(new ProgramState(start), 0);
        queue.clear();
        queue.add(startState);
        visited.clear();
        visited.put(startState.programState, 0L);
        while(!queue.isEmpty()){
            var curr = queue.poll();
            // println(display(curr, walls));
            // if(readln(""+curr).equals("q")) break;
            if(curr.ps >= noCheatTime - 100){
                continue;
            }
            if(curr.programState.pos.equals(end)){
                println("found cheatTime: " + curr.ps);
                total++;
                break;
            }
            for(var next : curr.nextStates(walls, width, height, null)){
                if(visited.containsKey(next.programState)/*  && visited.get(next.programState) < next.ps */){
                    continue;
                }
                queue.add(next);
                visited.put(next.programState, next.ps);
            }
        }
        walls.add(wall);
    }
    println(total);
}

