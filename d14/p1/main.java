static final int MAX_X = 101;
static final int MAX_Y = 103;
static final Vector MAX_VEC = new Vector(MAX_X, MAX_Y);

record Vector(int x, int y) {

    Vector add(Vector o) {
        return new Vector(x + o.x, y + o.y);
    }

    Vector mod(Vector o) {
        int newX = x % o.x;
        if (newX < 0) {
            newX += o.x;
        }
        int newY = y % o.y;
        if (newY < 0) {
            newY += o.y;
        }
        return new Vector(newX, newY);
    }
}


static class Robot{
    Vector pos;
    Vector vel;

    public Robot(Vector pos, Vector vel) {
        this.pos = pos;
        this.vel = vel;
    }


    static Robot parse(String line){
        var p = line.split(" ")[0];
        var v = line.split(" ")[1];
        int x = Integer.parseInt(p.substring(2, p.indexOf(',')));
        int y = Integer.parseInt(p.substring(p.indexOf(',')+1, p.length()));
        int vx = Integer.parseInt(v.substring(2, v.indexOf(',')));
        int vy = Integer.parseInt(v.substring(v.indexOf(',')+1, v.length()));
        return new Robot(new Vector(x, y), new Vector(vx, vy));
    }

    Vector sim(int seconds) {
        for (int i = 0; i < seconds; i++) {
            this.pos = this.pos.add(this.vel);
            this.pos = this.pos.mod(MAX_VEC);
        }
        return pos;
    }

}

long safetyFactor(List<Vector> robotPositions){
    long q1 = 0;
    long q2 = 0;
    long q3 = 0;
    long q4 = 0;
    for (Vector v : robotPositions) {
        switch (v) {
            case Vector(int x, int y) when x < MAX_X/2 && y < MAX_Y/2 -> q1++;
            case Vector(int x, int y) when x < MAX_X/2 && y > MAX_Y/2 -> q2++;
            case Vector(int x, int y) when x > MAX_X/2 && y < MAX_Y/2 -> q3++;
            case Vector(int x, int y) when x > MAX_X/2 && y > MAX_Y/2 -> q4++;
            default -> {}
        }
    }

    return q1*q2*q3*q4;
}

void main() throws Exception {
    var robots = Files.lines(Path.of("input.txt")).map(Robot::parse).toList();
    List<Vector> endPositions = new ArrayList<>();
    for (Robot robot : robots) {
        endPositions.add(robot.sim(100));
    }
    print(safetyFactor(endPositions));
}
