import static java.lang.Long.parseLong;

record Vector(long x, long y){

    Vector add(Vector o){
        return new Vector(x + o.x(), y + o.y());
    }

    Vector mult(int i){
        return new Vector(x*i, y*i);
    }
}

record Machine(Vector a, Vector b, Vector prize){}

enum State{A,B,PRIZE, SEPARATOR};

record Position(Vector location, int aPresses, int bPresses){

    long cost() {
        return (3L * aPresses) + bPresses;
    }
}

void aStar(Position curr, Machine machine, PriorityQueue<Position> positions){
    // println("aStar(" + curr + ", " + positions.size());
    var a = new Position(curr.location().add(machine.a()), curr.aPresses() + 1, curr.bPresses());
    if(a.location().x() <= machine.prize().x() && a.location().y() <= machine.prize().y()){
        if(!positions.contains(a))
            positions.add(a);
    }
    var b = new Position(curr.location().add(machine.b()), curr.aPresses(), curr.bPresses() + 1);
    if(b.location().x() <= machine.prize().x() && b.location().y() <= machine.prize().y()){
        if(!positions.contains(b))
            positions.add(b);
    }
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt")).toList();
    var machines = new ArrayList<Machine>();
    var state = State.A;
    Vector a = null;
    Vector b = null;
    for (String line : lines) {
        state = switch (state) {
            case A -> {
                long x = parseLong(line.substring(line.indexOf('X')+1,line.indexOf(',')));
                long y = parseLong(line.substring(line.indexOf('Y')+1,line.length()));
                a = new Vector(x, y);
                yield State.B;}
            case B -> {
                long x = parseLong(line.substring(line.indexOf('X')+1,line.indexOf(',')));
                long y = parseLong(line.substring(line.indexOf('Y')+1,line.length()));
                b = new Vector(x, y);
                yield State.PRIZE;}
            case PRIZE -> {
                long x = parseLong(line.substring(line.indexOf('X')+2,line.indexOf(',')));
                long y = parseLong(line.substring(line.indexOf('Y')+2,line.length()));
                machines.add(new Machine(a,b,new Vector(x, y)));
                yield State.SEPARATOR;}
            case SEPARATOR -> State.A;
        };
    }
    println(machines);
    long total = 0;
    for (Machine machine : machines) {
        var queue = new PriorityQueue<Position>((Position lhs, Position rhs) -> Long.compare(lhs.cost(), rhs.cost()));
        queue.add(new Position(new Vector(0,0), 0, 0));
        var curr = queue.poll();
        while(curr != null){
            if(curr.location().x() == machine.prize().x() && curr.location().y() == machine.prize().y()){
                println(machine + " cost:" + curr.cost());
                total += curr.cost();
                break;
            }
            aStar(curr, machine, queue);
            curr = queue.poll();
        }
    }
    println(total);
}

