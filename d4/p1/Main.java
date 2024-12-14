record Point(int x, int y){

    Point add(Point rhs) {
        return new Point(x + rhs.x(), y+rhs.y());
    }

    Point mult(int rhs) {
        return new Point(x * rhs, y*rhs);
    }
}

Map<Character, Set<Point>> xmasMap = new HashMap<>();

boolean find(Point m, Point a, Point s){
    return xmasMap.get('M').contains(m) && xmasMap.get('A').contains(a) && xmasMap.get('S').contains(s);
}

void main() throws Exception {
    xmasMap.put('X', new HashSet<>());
    xmasMap.put('M', new HashSet<>());
    xmasMap.put('A', new HashSet<>());
    xmasMap.put('S', new HashSet<>());
    try(var raf = new RandomAccessFile("input.txt", "r")){
        int c;
        int lineLength = raf.readLine().length();
        raf.seek(0);
        println(lineLength);
        c = raf.read();
        int x = 0;
        int y = 0;
        while(c != -1){
            println((char)c + " " + x + " " + y);
            switch (c) {
                case 'X' -> xmasMap.get((char)c).add(new Point(x,y));
                case 'M' -> xmasMap.get((char)c).add(new Point(x,y));
                case 'A' -> xmasMap.get((char)c).add(new Point(x,y));
                case 'S' -> xmasMap.get((char)c).add(new Point(x,y));
                case '\n' -> {println("newline");x = -1; y++;}
                default -> {}
            }
            c = raf.read();
            x++;
        };
        int maxY = y;
        for (y = 0; y < maxY; y++) {
            for (x = 0; x < lineLength; x++) {
                Point p = new Point(x,y);
                if(xmasMap.get('X').contains(p)){
                    print('X');
                }else if(xmasMap.get('M').contains(p)){
                    print('M');
                }else if(xmasMap.get('A').contains(p)){
                    print('A');
                }else if(xmasMap.get('S').contains(p)){
                    print('S');
                }else{
                    print('.');
                }
            }
            println("");
        }
    }
    println(xmasMap);

    int total = 0;

    Point[] dirs = new Point[] {new Point(0,1), new Point(1,1), new Point(1,0), new Point(-1,1), new Point(-1,0), new Point(-1,-1), new Point(0,-1), new Point(1,-1)};

    for(Point x : xmasMap.get('X')) {
        for(Point dir : dirs) {
            if(find(x.add(dir), x.add(dir.mult(2)), x.add(dir.mult(3)))){
                total++;
            }
        }
    }

    println(total);
}
