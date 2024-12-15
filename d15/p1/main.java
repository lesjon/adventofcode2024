record Vector(int x, int y){
    Vector add(Vector o){
        return new Vector(x+ o.x, y+o.y);
    }
}

sealed interface MapObject {
    Vector location();
    Vector move(Vector step);
}

final class Robot implements MapObject{
    Vector location;

    Robot(Vector location) {
        this.location = location;
    }

    public Vector location() {
        return location;
    }

    public Vector move(Vector step){
        // println("robot.move(" + step);
        for (MapObject o : obstacles) {
            if(o.location().equals(location.add(step))){
                // println("Met: " + o);
                step = o.move(step);
                // println("that moved: " + step);
                if(step.equals(new Vector(0,0))) break;
            }
        }
        location = location.add(step);
        return step;
    }
} 

final class Box implements MapObject{
    Vector location;
    Box(Vector location){
        this.location = location;
    }

    public Vector location() {return location;}

    public Vector move(Vector step){
        // println("box.move(" + step);
        for (MapObject o : obstacles) {
            if(o.location().equals(location.add(step))){
                // println("met:" + o);
                step = o.move(step);
                // println("that moved:" + step);
                if(step.equals(new Vector(0,0))) break;
            }
        }
        location = location.add(step);
        return step;
    }
} 

record Wall(Vector location) implements MapObject {
    public Vector move(Vector ignore){
        return new Vector(0,0);
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
        for (int x = 0; x < width+1; x++) {
            var check = new Vector(x,y);
            if(robot.location().equals(check)){
                print('@');
                continue;
            }
            MapObject obstacle = null;
            for (var o: obstacles) {
                if(o.location().equals(check)){
                    obstacle = o;
                    break;
                }
            }
            if(obstacle == null){
                print(' ');
                continue;
            }
            switch (obstacle) {
                case Box _ -> print('O');
                case Wall _ -> print('#');
                case Robot _ -> {}
            }
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
                    case '#' -> obstacles.add(new Wall(new Vector(x,height)));
                    case 'O' -> obstacles.add(new Box(new Vector(x,height)));
                    case '@' -> robot = new Robot(new Vector(x,height));
                    default -> {}
                }            
                width = x;
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

    println("obstacles:" + obstacles);
    println("robot:" + robot);
    println("isntructions:" + instructions);
    for (Vector instruction : instructions) {
        robot.move(instruction);
        // display();
    }
    display();
    println(sumGPS());
}
