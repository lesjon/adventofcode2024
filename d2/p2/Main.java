enum State{
    START, UP, DOWN
}

static Set<Integer> allowedDownChanges = Set.of(-3, -2, -1);
static Set<Integer> allowedUpChanges = Set.of(3, 2, 1);
Optional<Integer> isSafe(List<Integer> report) {
    State state = State.START;
    for (int i = 1 ; i < report.size(); i++) {
        switch (state) {
            case START -> {
                int diff = report.get(i) - report.get(i-1);
                if (allowedUpChanges.contains(diff)){
                    state = State.UP;
                    break;
                } else if (allowedDownChanges.contains(diff)){
                    state = State.DOWN;
                    break;
                }
                return Optional.of(i);
            }
            case UP -> {
                if (!allowedUpChanges.contains(report.get(i) - report.get(i-1))){
                    return Optional.of(i);
                }
            }
            case DOWN -> {
                if (!allowedDownChanges.contains(report.get(i) - report.get(i-1))){
                    return Optional.of(i);
                }
            }
        }
    }

    return Optional.empty();
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt"));
    int total = 0;
    for(String line : lines.toList()) {
        // println("total: " + total);
        var strs = line.split(" ");
        List<Integer> report = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            report.add(Integer.parseInt(strs[i]));
        }
        Optional<Integer> unsafeIndexOption = isSafe(report);
        if (unsafeIndexOption.isEmpty()){
            // println("report safe " + report);
            total++;
            continue;
        } 
        var startReport = new ArrayList<>(report);
        startReport .remove(0);
        Optional<Integer> startUnsafeIndexOption = isSafe(startReport);
        if (startUnsafeIndexOption.isEmpty()){
            // println("start report ok");
            total++;
            continue;
        }
        int unsafeIndex = unsafeIndexOption.get();
        // println("remove from report" + report);
        var leftReport = new ArrayList<>(report);
        leftReport.remove(unsafeIndex-1);
        var rightReport = new ArrayList<>(report);
        rightReport.remove(unsafeIndex);
        // println("leftReport" + leftReport);
        // println("rightReport" + rightReport);
        Optional<Integer> leftUnsafeIndexOption = isSafe(leftReport);
        if (leftUnsafeIndexOption.isEmpty()){
            // println("left report ok");
            total++;
            continue;
        }
        Optional<Integer> rightUnsafeIndexOption = isSafe(rightReport);
        if (rightUnsafeIndexOption.isEmpty()){
            // println("right report ok");
            total++;
            continue;
        }
        // println(line + " unsafe");
    }
    println(total);
}
