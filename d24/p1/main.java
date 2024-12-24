
sealed interface Statement{
    Map<String, Statement> wires = new HashMap<>();

    default boolean valueAndCache(String wire){
        println(getClass() + ".valueAndCache(" + wire);
        boolean value = value();
        wires.put(wire, new Literal(value));
        return value;
    }

    boolean value();
}

record Literal(boolean value) implements Statement{
    @Override
    public boolean valueAndCache(String ignore){
        println(getClass() + ".valueAndCache(" + ignore);
        return value();
    }
}

record And(String l, String r) implements Statement {
    public boolean value() {
        return wires.get(l).valueAndCache(l) && wires.get(r).valueAndCache(r);
    }
}

record Or(String l, String r) implements Statement {
    public boolean value() {
        return wires.get(l).valueAndCache(l) || wires.get(r).valueAndCache(r);
    }
}

record Xor(String l, String r) implements Statement {
    public boolean value() {
        return wires.get(l).valueAndCache(l) ^ wires.get(r).valueAndCache(r);
    }
}

void main() throws Exception {
    Files.lines(Path.of("input.txt"))
        .forEach(l -> {
            if (l.contains(":")) {
                // x00: 1                
                String[] strs = l.split(": ");
                Statement.wires.put(strs[0], new Literal(strs[1].equals("1")));
            } else if (l.contains("XOR")) {
                // x01 XOR y01 -> z01
                String[] expressionLabel = l.split(" -> ");
                String[] lR = expressionLabel[0].split(" XOR ");
                Statement.wires.put(expressionLabel[1], new Xor(lR[0], lR[1]));
            } else if (l.contains("OR")) {
                String[] expressionLabel = l.split(" -> ");
                String[] lR = expressionLabel[0].split(" OR ");
                Statement.wires.put(expressionLabel[1], new Or(lR[0], lR[1]));
            } else if (l.contains("AND")) {
                String[] expressionLabel = l.split(" -> ");
                String[] lR = expressionLabel[0].split(" AND ");
                Statement.wires.put(expressionLabel[1], new And(lR[0], lR[1]));
            }
        });

    println(Statement.wires);
    int i = 0;
    long total = 0;
    do {
        String wire = String.format("z%02d", i);
        println(wire + ": " + total);
        if(Statement.wires.get(wire).valueAndCache(wire)){
            total += Math.pow(2, i);
        }
    } while (Statement.wires.containsKey(String.format("z%02d", ++i)));
    println(total);

}

