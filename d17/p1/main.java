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

    int getComboValue(int a, int b, int c){
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
    int a;
    int b;
    int c;
    List<Instruction> program = new ArrayList<>();

    public String toString(){
        var sb = new StringBuilder();
        sb.append("a:").append(a).append("\n");
        sb.append("b:").append(b).append("\n");
        sb.append("c:").append(c).append("\n");
        sb.append("program:").append(program).append("\n");
        return sb.toString();
    }

    int execute(int p){
        Instruction instr = program.get(p);
        switch (instr) {
            case adv -> a /= (int) Math.pow(2, program.get(p+1).getComboValue(a,b,c));
            case bxl -> b ^= program.get(p+1).opcode; //  calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.
            case bst -> b = program.get(p+1).getComboValue(a,b,c) % 8; //calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.
            case jnz -> {
        // does nothing if the A register is 0. However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand; if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
                if(a == 0){
                    break;
                }
                p = program.get(p+1).opcode - 2;
            } 
            case bxc -> b ^= c;
            case out -> out.append(program.get(p+1).getComboValue(a,b,c) % 8).append(',');
            case bdv -> b = a / (int) Math.pow(2, program.get(p+1).getComboValue(a,b,c));
            case cdv -> c = a / (int) Math.pow(2, program.get(p+1).getComboValue(a,b,c));
        }
        return p + 2;
    }
}

int parseCombo(Instruction instr, int a, int b, int c){
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
StringBuilder out = new StringBuilder();

void main() throws Exception {
    Computer computer = new Computer();
    Files.lines(Path.of("input.txt")).forEach(line -> {
            switch(line){
                case String s when s.startsWith("Register A: ") -> computer.a = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Register B: ") -> computer.b = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Register C: ") -> computer.c = Integer.parseInt(s.substring(12));
                case String s when s.startsWith("Program: ") -> {
                    Arrays.stream(s.substring(9).split(","))
                        .map(Integer::parseInt)
                        .map(Instruction::from)
                        .forEach(computer.program::add);
                }
                default -> {}
            };
        }
    );
    int p = 0;
    while(p < computer.program.size()) {
        println(p + " " + computer.program.get(p) + " " + computer);
        p = computer.execute(p);
    }
    println(out.toString());
}

