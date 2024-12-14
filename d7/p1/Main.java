long canGetResult(long result, List<Long> remaining) {
    if(remaining.size() == 1) {
        if(remaining.get(0) == result){
            return result;
        }else{
            return 0;
        }
    }
    if(remaining.get(0) > result) {
        return 0;
    }
    var left = new ArrayList<>(remaining.subList(1, remaining.size()));
    left.set(0, remaining.get(0) + left.get(0));
    if (0 != canGetResult(result, left)) return result;
    var right = new ArrayList<>(remaining.subList(1, remaining.size()));
    right.set(0, remaining.get(0) * right.get(0));
    if (0 != canGetResult(result, right)) return result;
    return 0;
}

List<Long> parseLongs(String s) {
    return Arrays.stream(s.split(" ")).map(Long::parseLong).toList();
}

void main() throws Exception {
    var lines = Files.lines(Path.of("input.txt"));
    long total = lines
        .map(line -> line.split(": "))
        .map(strs -> canGetResult(Long.parseLong(strs[0]), parseLongs(strs[1])))
        .reduce(0L, Long::sum);
    println(total);
}
