record Point(int x, int y){

    Point add(Point rhs) {
        return new Point(x + rhs.x(), y+rhs.y());
    }

    Point mult(int rhs) {
        return new Point(x * rhs, y*rhs);
    }
}

Map<Character, Set<Point>> xmasMap = new HashMap<>();

boolean find(Point a){
    println("find" + a);
boolean masfw = xmasMap.get('M').contains(a.add(new Point(-1,-1))) && xmasMap.get('S').contains(a.add(new Point(1,1)));
    boolean samfw = xmasMap.get('S').contains(a.add(new Point(-1,-1))) && xmasMap.get('M').contains(a.add(new Point(1,1)));
    boolean forwardSlash = masfw || samfw;
    boolean backwardSlash = (xmasMap.get('M').contains(a.add(new Point(1,-1))) && xmasMap.get('S').contains(a.add(new Point(-1,1)))) || (xmasMap.get('S').contains(a.add(new Point(1,-1))) && xmasMap.get('M').contains(a.add(new Point(-1,1))));
    return forwardSlash && backwardSlash;
}

void main() throws Exception {
    xmasMap.put('M', new HashSet<>());
    xmasMap.put('A', new HashSet<>());
    xmasMap.put('S', new HashSet<>());
    try(var raf = new RandomAccessFile("input.txt", "r")){
        int c;
        int lineLength = raf.readLine().length();
        raf.seek(0);
        c = raf.read();
        int x = 0;
        int y = 0;
        while(c != -1){
            println((char)c + " " + x + " " + y);
            switch (c) {
                case 'M' -> xmasMap.get((char)c).add(new Point(x,y));
                case 'A' -> xmasMap.get((char)c).add(new Point(x,y));
                case 'S' -> xmasMap.get((char)c).add(new Point(x,y));
                case '\n' -> {x = -1; y++;}
                default -> {}
            }
            c = raf.read();
            x++;
        };
    }

    int total = 0;


    for(Point a : xmasMap.get('A')) {
        if(find(a)){
            total++;
        }
    }

    println(total);
}
