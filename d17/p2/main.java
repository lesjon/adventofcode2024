class Computer {
    long a;
    long b;
    long c;
    int[] program;
    int p = 0;
    List<Integer> out = new ArrayList<>();

    public String toString(){
        var sb = new StringBuilder();
        sb.append("a:").append(a).append("\n");
        sb.append("b:").append(b).append("\n");
        sb.append("c:").append(c).append("\n");
        sb.append("program:").append(Arrays.toString(program)).append("\n");
        sb.append("p:").append(p).append("\n");
        sb.append("out:").append(out).append("\n");
        return sb.toString();
    }

    Integer execute(){
        int instr = program[p];
        switch (instr) {
            case 0 -> a >>= getComboValue(program[p+1]);
            case 1 -> b ^= program[p+1]; //  calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.
            case 2 -> b = getComboValue(program[p+1]) % 8; //calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.
            case 3 -> {
        // does nothing if the A register is 0. However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand; if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
                if(a == 0){
                    break;
                }
                p = program[p+1] - 2;
            } 
            case 4 -> b ^= c;
            case 5 -> out.add((int) getComboValue(program[p+1]) % 8);
            case 6 -> b = a >> getComboValue(program[p+1]);
            case 7 -> c = a >> getComboValue(program[p+1]);
            default -> throw new RuntimeException("Unknown instr: " + instr);
        }
        p += 2;
        if(instr == 5){
            return out.get(out.size()-1);
        }
        return null;
    }

    long getComboValue(int instr){
        return switch (instr) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> throw new RuntimeException("tried to get comboValue of: " + instr);
        };
    }
}

void main() throws Exception {
    var startTime = System.currentTimeMillis();
    Computer computer = new Computer();
    Files.lines(Path.of("input.txt")).forEach(line -> {
            switch(line){
                case String s when s.startsWith("Register A: ") -> computer.a = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Register B: ") -> computer.b = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Register C: ") -> computer.c = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Program: ") -> {
                    computer.program = Arrays.stream(s.substring(9).split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                }
                default -> {}
            };
        }
    );
    var result = LongStream.range(0, Long.MAX_VALUE).map(i -> {
        var testComputer = new Computer();
        testComputer.a = i;
        testComputer.b = computer.b;
        testComputer.c = computer.c;
        testComputer.program = computer.program;
        while(testComputer.p < testComputer.program.length) {
            // println(testComputer);
            var outOption = testComputer.execute();
            if(null == outOption){
                continue;
            }
            if(outOption != testComputer.program[testComputer.out.size()-1]){
                // println("outOption.get() != testComputer.program[testComputer.out.size(]-1).opcode" + outOption.get() +" "+ testComputer.program[testComputer.out.size(]-1).opcode);
                break;
            }
            if(testComputer.out.size() > testComputer.program.length){
                // println("testComputer.out.size() > testComputer.program.size()");
                break;
            }
            if(testComputer.out.size() == testComputer.program.length){
                return i;
            }
        }
        return 0;
    }).filter(i -> i != 0)
    .findFirst().getAsLong();
    println(result);
    println("Execution time: " + (System.currentTimeMillis() - startTime));
}

