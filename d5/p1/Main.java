import static java.lang.Integer.parseInt;

record Rule(int l, int r){

    static Rule parse(String line){
        String[] strs = line.split("\\|");
        return new Rule(parseInt(strs[0]), parseInt(strs[1]));
    }

    boolean applies(int[] update) {
        boolean foundSecond = false;
        for (int i = 0; i < update.length; i++) {
            if(update[i] == l && foundSecond) {
                return false;
            }
            if(update[i] == r) {
                foundSecond = true;
            }
        }
        return true;
    }
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt")).toList();
    List<Rule> rules = new ArrayList<>();
    List<int[]> updates = new ArrayList<>();

    boolean parseRules = true;
    for (String line : lines) {
        if(parseRules){
            if(line.equals("")){
                parseRules = false;
                continue;
            }
            rules.add(Rule.parse(line));
        }else{
            var strs = line.split(",");
            updates.add(Arrays.stream(strs).mapToInt(Integer::parseInt).toArray());
        }
    }
    int total = 0;
    for (var update : updates) {

        boolean rulesApply = true;
        for (Rule rule : rules) {
            rulesApply &= rule.applies(update);
            if(!rulesApply) break;
        }
        if(!rulesApply) continue;
        total += update[update.length/2];
    }
    println(total);
}
