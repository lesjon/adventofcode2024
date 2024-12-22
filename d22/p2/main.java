
long mix(long s, long given){
    return s ^ given;
}

long prune(long s){
    return s % 16777216L;
}

record Seq(int a, int b, int c, int d){}

Map<Seq, Integer> calcSecret(long s, int steps, int[] changes){
    if(steps == 0){
        return new HashMap<>();
    }
    long nextSecret = prune(mix(s, s << 6));
    nextSecret = prune(mix(nextSecret, nextSecret >> 5));
    nextSecret = prune(mix(nextSecret, nextSecret << 11));;
    int i = 2000-steps;
    changes[i] = onesDiff(s, nextSecret);
    var resultMap = calcSecret(nextSecret, steps-1, changes);
    if(i >= 3){
        String priceString = Long.toString(nextSecret);
        var seq = new Seq(changes[i-3], changes[i-2], changes[i-1], changes[i]);
        resultMap.put(seq, priceString.charAt(priceString.length()-1) - '0');
    }
    return resultMap;
}

int onesDiff(long l, long r){
    var ls = Long.toString(l); 
    var rs = Long.toString(r); 
    return rs.charAt(rs.length()-1) - ls.charAt(ls.length()-1) ;
}

void main() throws Exception {
    var seqValMap = Files.lines(Path.of("input.txt"))
        .peek(s -> print(s+ ": "))
        .mapToLong(Long::parseLong)
        .mapToObj(s -> calcSecret(s, 2000, new int[2000]))
        .toList();

    var result = seqValMap.stream()
        .map(Map::keySet)
        .flatMap(Set::stream)
        .distinct()
        .collect(Collectors.toMap(seq -> seq, seq -> seqValMap.stream().mapToLong(m -> m.getOrDefault(seq, 0)).sum()));
    println(result.values().stream().mapToLong(i -> i).max());
}


