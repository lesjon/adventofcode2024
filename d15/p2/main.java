record Vector(int x, int y){
    Vector add(Vector o){
        return new Vector(x+ o.x, y+o.y);
    }
    Vector invert(){
        return new Vector(-x, -y);
    }
    boolean isEmpty(){
        return x == 0 && y ==0;
    }
}

sealed interface MapObject {
    Vector location();
    Vector move(Vector step);
    boolean isAt(Vector loc);
    void display(Vector loc);
    default void revert(Vector step) {};
}

final class Robot implements MapObject{
    Vector location;

    Robot(Vector location) {
        this.location = location;
    }

    public Vector location() {
        return location;
    }

    public boolean isAt(Vector loc) {
        return location.equals(loc);
    }

    public Vector move(Vector step){
        // println("robot.move(" + step);
        for (MapObject o : obstacles) {
            if(o.isAt(location.add(step))){
                // println("Met: " + o);
                step = o.move(step);
                // println("that moved: " + step);
                if(step.isEmpty()) break;
            }
        }
        location = location.add(step);
        return step;
    }
    public void display(Vector loc) {
        if(loc.equals(location)) print('@');
    }
} 

final class Box implements MapObject{
    static final Vector HITBOX = new Vector(1,0);

    Vector location;

    List<MapObject> pushed = new ArrayList<>(); 

    Box(Vector location){
        this.location = location;
    }

    public Vector location() {return location;}

    public boolean isAt(Vector loc) {
        return location.equals(loc) || location.add(HITBOX).equals(loc);
    }

    public Vector move(Vector step){
        // println("box.move(" + step);
        pushed = new ArrayList<>();
        if(step.isEmpty()) return step;
        var initStep = step;
        for (MapObject o : obstacles) {
            if(o == this) continue;
            if(o.isAt(location.add(step))){
                step = o.move(step);
                if(step.isEmpty()) {
                    break;
                }
                pushed.add(o);
            }
            if(o.isAt(location.add(HITBOX).add(step))){
                step = o.move(step);
                if(step.isEmpty()) {
                    break;
                }
                pushed.add(o);
            }
        }
        if(step.isEmpty()) {
            pushed.forEach(b -> b.revert(initStep));
        }
        location = location.add(step);
        return step;
    }

    public void display(Vector loc) {
        if(location.equals(loc))print('[');
        if(location.add(HITBOX).equals(loc))print(']');
    }

    public void revert(Vector step) {
        location = location.add(step.invert());
        pushed.forEach(o -> o.revert(step));
    }
} 

record Wall(Vector location) implements MapObject {
    public Vector move(Vector ignore){
        return new Vector(0,0);
    }

    public boolean isAt(Vector loc){
        return location.equals(loc);
    }
    public void display(Vector loc) {
        if(location.equals(loc))print('#');
    }
}

MapObject robot = null;
List<MapObject> obstacles = new ArrayList<>();

int width = 0;
int height = 0;

long sumGPS(){
    long total = 0;
    for (MapObject obstacle : obstacles) {
        switch (obstacle) {
            case Box box -> {
                total += box.location().x() + 100 * box.location().y();
            }
            default -> {}
        }
    }
    return total;
}

void display(){
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width+3; x++) {
            if(x == width/2 && y == height/2) {
                print('X');
                continue;
            }
            var check = new Vector(x,y);
            if(robot.isAt(check)){
                print('@');
                continue;
            }
            MapObject obstacle = null;
            for (var o: obstacles) {
                if(o.isAt(check)){
                    obstacle = o;
                    break;
                }
            }
            if(obstacle == null){
                print(' ');
                continue;
            }
            obstacle.display(check);
        }
        println("");
    }
}
void main() throws Exception {
    var lines = Files.readAllLines(Path.of("input.txt"));
    var parseMap = true;
    List<Vector> instructions = new ArrayList<>();
    for (String line : lines) {
        if (parseMap) {
            if(line.equals("")) parseMap = false;
            for (int x = 0; x < line.length(); x++) {
                switch (line.charAt(x)) {
                    case '#' -> obstacles.addAll(List.of(new Wall(new Vector(x*2,height)), new Wall(new Vector(x*2+1,height))));
                    case 'O' -> obstacles.add(new Box(new Vector(x*2,height)));
                    case '@' -> robot = new Robot(new Vector(x*2,height));
                    default -> {}
                }            
                width = x*2;
            }
            height++;
        } else {
            line.chars().forEach(c -> {
                switch (c) {
                    case '^' -> instructions.add(new Vector(0,-1));
                    case '>' -> instructions.add(new Vector(1,0));
                    case '<' -> instructions.add(new Vector(-1,0));
                    case 'v' -> instructions.add(new Vector(0, 1));
                    default -> {}
                };
            });
        }
    }

    // println("obstacles:" + obstacles);
    // println("robot:" + robot);
    // println("isntructions:" + instructions);
    for (Vector instruction : instructions) {
        // display();
        // String input = readln(instruction + " continue? :");
        // if(input.equals("q")) break;
        robot.move(instruction);
    }
    display();
    println(sumGPS());
}
