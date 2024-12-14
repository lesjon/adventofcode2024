import static java.lang.Integer.parseInt;

void sort(Collection<Rule> rules, int[] update){

}

record Page(int i) implements Comparable{
    static Map<Page, List<Rule>> rulesMap;

    public int compareTo(Object o){
        if (!(o instanceof Page oPage)) {
            return 0;
        }
        var rules = rulesMap.get(oPage);
        if (null == rules) return 0;
        for (var rule : rules) {
            if (rule.l().equals(this)){
                return -1;
            }
            if (rule.r().equals(this)){
                return 1;
            }
        }
        return 0;
    }
    static Page parse(String s) {
        return new Page(parseInt(s));
    }
}

record Rule(Page l, Page r){

    static Rule parse(String line){
        String[] strs = line.split("\\|");
        return new Rule(Page.parse(strs[0]), Page.parse(strs[1]));
    }

    boolean applies(Page[] update) {
        boolean foundSecond = false;
        for (int i = 0; i < update.length; i++) {
            if(update[i].equals(l) && foundSecond) {
                return false;
            }
            if(update[i].equals(r)) {
                foundSecond = true;
            }
        }
        return true;
    }
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt")).toList();
    Map<Page, List<Rule>> rules = new HashMap<>();
    List<Page[]> updates = new ArrayList<>();
    List<Rule> flatRules = new ArrayList<>();

    boolean parseRules = true;
    for (String line : lines) {
        if(parseRules){
            if(line.equals("")){
                parseRules = false;
                continue;
            }
            var rule = Rule.parse(line);
            flatRules.add(rule);
            if(rules.containsKey(rule.l())){
                rules.get(rule.l()).add(rule);
            }else{
                List<Rule> newList = new ArrayList<>();
                newList.add(rule);
                rules.put(rule.l(), newList);
            }
            if(rules.containsKey(rule.r())){
                rules.get(rule.r()).add(rule);
            }else{
                List<Rule> newList = new ArrayList<>();
                newList.add(rule);
                rules.put(rule.r(), newList);
            }
        }else{
            var strs = line.split(",");
            updates.add(Arrays.stream(strs).map(Page::parse).toArray(Page[]::new));
        }
    }
    int total = 0;
    for (var update : updates) {
        boolean rulesApply = true;
        for (Rule rule : flatRules) {
            rulesApply &= rule.applies(update);
            if(!rulesApply) break;
        }
        if(rulesApply) continue;
        Page.rulesMap = rules;
        Arrays.sort(update);
        total += update[update.length/2].i();
    }
    println(total);
}
