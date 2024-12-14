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
    enum Button{ A, B }

    Button cheapestButton(){
        if(a.magnitude()/3 > b.magnitude()){
            return Button.A;
        }
        return Button.B;
    }

}

enum State{A,B,PRIZE, SEPARATOR};

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
                long x = 10000000000000L + parseLong(line.substring(line.indexOf('X')+2,line.indexOf(',')));
                long y = 10000000000000L + parseLong(line.substring(line.indexOf('Y')+2,line.length()));
                machines.add(new Machine(a,b,new Vector(x, y)));
                yield State.SEPARATOR;}
            case SEPARATOR -> State.A;
        };
    }
    println(machines);
    long total = 0;
    for (Machine machine : machines) {
        // machine.a().x() * a + machine.b().x() * b == machine.prize().x();
        // machine.a().y() * a + machine.b().y() * b == machine.prize().y();
        //
        // a == machine.prize().x() / machine.a().x() - machine.b().x() * b / machine.a().x() ;
        // machine.a().y() * a + machine.b().y() * b == machine.prize().y();
        //
        // machine.a().y() * machine.prize().x() / machine.a().x() - machine.a().y() * machine.b().x() * b / machine.a().x() + machine.b().y() * b == machine.prize().y();
        //
        // (machine.a().y() * machine.b().x() / machine.a().x()- machine.b().y()) * b == -machine.prize().y() + machine.a().y() * machine.prize().x() / machine.a().x();
        
        double bPresses = (-(double)machine.prize().y() + (double)machine.a().y() * (double)machine.prize().x() / (double)machine.a().x() ) / ((double)machine.a().y() * (double)machine.b().x() / (double)machine.a().x()- (double)machine.b().y());
        double aPresses = ((double)machine.prize().x() / (double)machine.a().x() - (double)machine.b().x() * bPresses / (double)machine.a().x());
        if(
            machine.a().x() * Math.round(aPresses) + machine.b().x() * Math.round(bPresses) == machine.prize().x()
            &&
            machine.a().y() * Math.round(aPresses) + machine.b().y() * Math.round(bPresses) == machine.prize().y()
        ){
            total += aPresses*3L+bPresses; 
        }
    }
    println(total);
}

