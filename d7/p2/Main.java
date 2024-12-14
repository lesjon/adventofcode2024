long concatLongs(long l, long r) {
    return Long.parseLong(l+""+r);
}

long resultIfCorrect(long result, List<Long> remaining) {
    if(remaining.size() == 1) {
        return remaining.get(0) == result ? result : 0;
    }
    if(remaining.get(0) > result) {
        return 0;
    }
    var plus = new ArrayList<>(remaining.subList(1, remaining.size()));
    plus.set(0, remaining.get(0) + plus.get(0));
    if (0 != resultIfCorrect(result, plus)) return result;
    var mult = new ArrayList<>(remaining.subList(1, remaining.size()));
    mult.set(0, remaining.get(0) * mult.get(0));
    if (0 != resultIfCorrect(result, mult)) return result;
    var concat = new ArrayList<>(remaining.subList(1, remaining.size()));
    concat.set(0, concatLongs(remaining.get(0), concat.get(0)));
    return resultIfCorrect(result, concat);
}

List<Long> parseLongs(String s) {
    return Arrays.stream(s.split(" ")).map(Long::parseLong).toList();
}

void main() throws Exception {
    long total = Files.lines(Path.of("input.txt"))
        .map(line -> line.split(": "))
        .map(strs -> resultIfCorrect(Long.parseLong(strs[0]), parseLongs(strs[1])))
        .reduce(0L, Long::sum);
    println(total);
}
