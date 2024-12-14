record Antenna(char freq){}
record Vector(int x, int y){

    Vector subtract(Vector o){
        return new Vector(x - o.x(), y - o.y());
    }

    Vector add(Vector o){
        return new Vector(x + o.x(), y + o.y());
    }
}

Stream<Vector> getAntinodes(List<Vector> antennas){
    if(antennas.size() == 1) return Stream.of();
    if(antennas.size() == 2) {
        var diff = antennas.get(0).subtract(antennas.get(1));
        var antinode1 = antennas.get(1).subtract(diff);
        var antinode2 = antennas.get(0).add(diff);
        return Stream.of(antinode1, antinode2);
    }
    List<Vector> antinodes = new ArrayList<>();
    for (int i = 0; i < antennas.size(); i++) {
        for (int j = i+1; j < antennas.size(); j++) {
            antinodes.addAll(getAntinodes(List.of(antennas.get(i), antennas.get(j))).toList());
        }
    }
    return antinodes.stream();
}

int width = 0;
int height = 0;

boolean withinRange(Vector antinode){
    return antinode.x() >= 0 && antinode.x() < width && antinode.y() >= 0 && antinode.y() < height;
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt"));
    Map<Antenna, List<Vector>> map = new HashMap<>();
    for (String line : lines.toList()) {
        width = 0;
        for (int c : line.chars().toArray()) {
            print((char) c);
            if(c == '.') {
                width++;
                continue;
            }
            var antenna = new Antenna((char) c);
            var loc = new Vector(width, height);
            if(map.containsKey(antenna)){
                var list = map.get(antenna);
                list.add(loc);
            }else{
                List<Vector> list = new ArrayList<>();
                list.add(loc);
                map.put(antenna, list);
            }
            width++;
        }
        println("");
        height +=1;
    }
    println("width: " + width + " height: " + height);
    Set<Vector> antinodes = new HashSet<>();
    for (List<Vector> antennas : map.values()) {
        getAntinodes(antennas)
            .filter(this::withinRange)
            .forEach(antinodes::add);
    }

    println(antinodes.size());
}
