enum State{
    ENABLED, DISABLED, M, U, L, LEFT, RIGHT, OPEN
}

enum DoState {
    INIT, D, O, OPEN
}
enum DontState {
    INIT, D, O, N, APO, T, OPEN
}

void main() throws Exception {
    int total = 0;
    try(var fis = new FileReader("input.txt")){
        int c;
        State state = State.ENABLED;
        DoState doState = DoState.INIT;
        DontState dontState = DontState.INIT;
        int left = 0;
        int right = 0;
        do {
            c = fis.read();
            // println(state + " char: " + (char) c + " total:" + total);
            dontState = switch (dontState) {
                case INIT -> c == 'd' ? DontState.D : DontState.INIT;
                case D -> c == 'o' ? DontState.O : DontState.INIT;
                case O -> c == 'n' ? DontState.N : DontState.INIT;
                case N -> c == '\'' ? DontState.APO : DontState.INIT;
                case APO -> c == 't' ? DontState.T : DontState.INIT;
                case T -> c == '(' ? DontState.OPEN : DontState.INIT;
                case OPEN -> {
                    if (c == ')') {
                        state = State.DISABLED;
                    }
                    yield DontState.INIT;
                }
            };

            state = switch (state) {
                case ENABLED -> {
                    doState = DoState.INIT;
                    if(c == 'm') {
                       yield State.M ;
                    }
                    yield State.ENABLED;
                }
                case M -> c == 'u' ? State.U : State.ENABLED;
                case U -> c == 'l' ? State.L : State.ENABLED;
                case L -> c == '(' ? State.OPEN : State.ENABLED;
                case OPEN -> {
                    if(c >= '0' && c <='9') {
                        left += c - '0';
                        yield State.LEFT;
                    }
                    yield State.ENABLED;
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
                            yield State.ENABLED;
                        }
                        yield State.LEFT;
                    }
                    left = 0;
                    yield State.ENABLED;
                }
                case RIGHT -> {
                    if(c == ')') {
                        total += left * right;
                        left = 0;
                        right = 0;
                        yield State.ENABLED;
                    }
                    if(c >= '0' && c <='9') {
                        right *= 10;
                        right += c - '0';
                        if(right > 999){
                            left = 0;
                            right = 0;
                            yield State.ENABLED;
                        }
                        yield State.RIGHT;
                    }
                    left = 0;
                    right = 0;
                    yield State.ENABLED;
                }
                case DISABLED -> {
                    State returnState = State.DISABLED;
                    doState = switch(doState){
                        case INIT -> c == 'd' ? DoState.D : DoState.INIT;
                        case D -> c == 'o' ? DoState.O : DoState.INIT;
                        case O -> c == '(' ? DoState.OPEN : DoState.INIT;
                        case OPEN -> {
                            if (c == ')') {
                                println("enabled!");
                                returnState = State.ENABLED;
                            }
                            yield DoState.INIT;
                        }
                    };
                    yield returnState;
                }
            };
        } while (c != -1);

        println(total);
    }
}
