enum State{
    INIT, M, U, L, LEFT, RIGHT, OPEN
}

void main() throws Exception {
    int total = 0;
    try(var fis = new FileReader("input.txt")){
        println(fis);
        int c;
        State state = State.INIT;
        int left = 0;
        int right = 0;
        do {
            c = fis.read();
            println(state + " char: " + (char) c + " total:" + total);
            state = switch (state) {
                case INIT -> c == 'm' ? State.M : State.INIT;
                case M -> c == 'u' ? State.U : State.INIT;
                case U -> c == 'l' ? State.L : State.INIT;
                case L -> c == '(' ? State.OPEN : State.INIT;
                case OPEN -> {
                    if(c >= '0' && c <='9') {
                        left += c - '0';
                        yield State.LEFT;
                    }
                    yield State.INIT;
                }
                case LEFT -> {
                    if (c == ','){
                        yield State.RIGHT;
                    }
                    if(c >= '0' && c <='9') {
                        left *= 10;
                        left += c - '0';
                        if(left > 999){
                            left = 0;
                            yield State.INIT;
                        }
                        yield State.LEFT;
                    }
                    left = 0;
                    yield State.INIT;
                }
                case RIGHT -> {
                    if(c == ')') {
                        total += left * right;
                        left = 0;
                        right = 0;
                        yield State.INIT;
                    }
                    if(c >= '0' && c <='9') {
                        right *= 10;
                        right += c - '0';
                        if(right > 999){
                            left = 0;
                            right = 0;
                            yield State.INIT;
                        }
                        yield State.RIGHT;
                    }
                    left = 0;
                    right = 0;
                    yield State.INIT;
                }
            };
        } while (c != -1);

        println(total);
    }
}
