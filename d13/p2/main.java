import static java.lang.Long.parseLong;

record Vector(long x, long y){

    Vector add(Vector o){
        return new Vector(x + o.x(), y + o.y());
    }

    Vector subtract(Vector o){
        return new Vector(x - o.x(), y - o.y());
    }
    Vector mult(long i){
        return new Vector(x*i, y*i);
    }

    long magnitude(){
        return x*y;
    }
}

record Machine(Vector a, Vector b, Vector prize){
    enum Button{ A,B}

    Button cheapestButton(){
        if(a.magnitude()/3 > b.magnitude()){
            return Button.A;
        }
        return Button.B;
    }

}

enum State{A,B,PRIZE, SEPARATOR};

record Position(Vector location, long aPresses, long bPresses){

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
                long x = /* 10000000000000L + */ parseLong(line.substring(line.indexOf('X')+2,line.indexOf(',')));
                long y = /* 10000000000000L + */ parseLong(line.substring(line.indexOf('Y')+2,line.length()));
                machines.add(new Machine(a,b,new Vector(x, y)));
                yield State.SEPARATOR;}
            case SEPARATOR -> State.A;
        };
    }
    println(machines);
    long total = 0;
    for (Machine machine : machines) {
        var startButton = machine.cheapestButton();
        switch(startButton){
            case A -> {
                println("A");
                long max = Math.min(machine.prize().x()/machine.a().x(), machine.prize().y()/machine.a().y());
                for (long i = max; i >= 0 ; i--) {
                    var pos = new Position(machine.prize().subtract(machine.a().mult(i)), i, 0);
                    if (pos.location().x() % machine.b().x() == 0 && pos.location().y() % machine.b().y() == 0){
                        var xBPresses = pos.location().x()/machine.b().x();
                        var yBPresses = pos.location().y()/machine.b().y();
                        if(xBPresses == yBPresses){
                            var finalPos = new Position(pos.location().subtract(machine.b().mult(xBPresses)), pos.aPresses(), xBPresses);
                            total += finalPos.cost();
                            break;
                        }
                    }
                }
            }
            case B -> {
                println("B");
                long max = Math.min(machine.prize().x()/machine.b().x(), machine.prize().y()/machine.b().y());
                println("max" + max);
                for (long i = max; i >= 0 ; i--) {
                    var pos = new Position(machine.prize().subtract(machine.b().mult(i)), i, 0);
                    if (pos.location().x() % machine.a().x() == 0 && pos.location().y() % machine.a().y() == 0){
                        var xAPresses = pos.location().x()/machine.a().x();
                        var yAPresses = pos.location().y()/machine.a().y();
                        if(xAPresses == yAPresses){
                            var finalPos = new Position(pos.location().subtract(machine.a().mult(xAPresses)), xAPresses, pos.bPresses());
                            total += finalPos.cost();
                            break;
                        }
                    }
                }
            }
        }
        break;
    }
    println(total);
}

