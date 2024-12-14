enum State{
    INIT, START, UP, DOWN
}

static Set<Integer> allowedDownChanges = Set.of(-3, -2, -1);
static Set<Integer> allowedUpChanges = Set.of(3, 2, 1);
boolean isSafe(List<Integer> report) {
    State state = State.INIT;
    int skipped = 0;
    for (int i = 0 ; i < report.size(); i++) {
        int curr = report.get(i);
        switch (state) {
            case INIT:
                state = State.START;
                break;
            case START:
                int diff = curr - report.get(i-1);
                if (allowedUpChanges.contains(diff)){
                    state = State.UP;
                    break;
                } else if (allowedDownChanges.contains(diff)){
                    state = State.DOWN;
                    break;
                }
                return false;
            case UP:
                if (!allowedUpChanges.contains(curr - report.get(i-1))){
                    return false;
                }
                break;
            case DOWN:
                if (!allowedDownChanges.contains(curr - report.get(i-1))){
                    return false;
                }
                break;
        }
    }

    return true;
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt"));
    int total = 0;
    for(String line : lines.toList()) {
        var strs = line.split(" ");
        List<Integer> report = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            report.add(Integer.parseInt(strs[i]));
        }
        if (isSafe(report)){
            total++;
        }
    }
    println(total);
}
