enum Instruction {
    // he adv instruction (opcode 0) performs division. The numerator is the value in the A register. The denominator is found by raising 2 to the power of the instruction's combo operand. (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.) The result of the division operation is truncated to an integer and then written to the A register.
    adv(0),
    // The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.
    bxl(1),
    // The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.
    bst(2),
    // The jnz instruction (opcode 3) does nothing if the A register is 0. However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand; if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
    jnz(3),
    // The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then stores the result in register B. (For legacy reasons, this instruction reads an operand but ignores it.)
    bxc(4),
    // The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs that value. (If a program outputs multiple values, they are separated by commas.)
    out(5),
    // The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is stored in the B register. (The numerator is still read from the A register.)
    bdv(6),
    // The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register. (The numerator is still read from the A register.)
    cdv(7);

    int opcode;
    Instruction(int opcode){
        this.opcode = opcode;
    }

    static Instruction from(int opcode){
        for (Instruction i : Instruction.values()) {
            if (i.opcode == opcode) {
                return i;
            }
        }
        throw new RuntimeException("unknown opcode: " + opcode);
    }

    long getComboValue(long a, long b, long c){
        return switch (this) {
            case adv -> opcode;
            case bxl -> opcode;
            case bst -> opcode;
            case jnz -> opcode;
            case bxc -> a;
            case out -> b;
            case bdv -> c;
            case cdv -> throw new RuntimeException("tried to get comboValue of: " + this);
        };
    }
}

class Computer {
    long a;
    long b;
    long c;
    Instruction[] program;
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
        Instruction instr = program[p];
        switch (instr) {
            case adv -> a >>= program[p+1].getComboValue(a,b,c);
            case bxl -> b ^= program[p+1].opcode; //  calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.
            case bst -> b = program[p+1].getComboValue(a,b,c) % 8; //calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.
            case jnz -> {
        // does nothing if the A register is 0. However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand; if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
                if(a == 0){
                    break;
                }
                p = program[p+1].opcode - 2;
            } 
            case bxc -> b ^= c;
            case out -> out.add((int) program[p+1].getComboValue(a,b,c) % 8);
            case bdv -> b = a >> program[p+1].getComboValue(a,b,c);
            case cdv -> c = a >> program[p+1].getComboValue(a,b,c);
        }
        p += 2;
        if(instr.equals(Instruction.out)){
            return out.get(out.size()-1);
        }
        return null;
    }
}

long parseCombo(Instruction instr, long a, long b, long c){
    return switch (instr) {
        case adv -> instr.opcode;
        case bxl -> instr.opcode;
        case bst -> instr.opcode;
        case jnz -> instr.opcode;
        case bxc -> a;
        case out -> b;
        case bdv -> c;
        case cdv -> throw new RuntimeException();
    };
}

void main() throws Exception {
    Computer computer = new Computer();
    Files.lines(Path.of("test.txt")).forEach(line -> {
            switch(line){
                case String s when s.startsWith("Register A: ") -> computer.a = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Register B: ") -> computer.b = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Register C: ") -> computer.c = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Program: ") -> {
                    computer.program = Arrays.stream(s.substring(9).split(","))
                        .map(Integer::parseInt)
                        .map(Instruction::from)
                        .toArray(Instruction[]::new);
                }
                default -> {}
            };
        }
    );
    var result = LongStream.range(0, Long.MAX_VALUE).parallel().map(i -> {
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
            if(outOption != testComputer.program[testComputer.out.size()-1].opcode){
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
}

