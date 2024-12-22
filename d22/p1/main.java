
long mix(long s, long given){
    return s ^ given;
}

long prune(long s){
    return s % 16777216L;
}

long calcSecret(long s, int steps){
    if(steps == 0){
        return s;
    }
    long nextSecret = prune(mix(s, s << 6));
    nextSecret = prune(mix(nextSecret, nextSecret >> 5));
    nextSecret = prune(mix(nextSecret, nextSecret << 11));;
    return(calcSecret(nextSecret, steps-1));
}

void main() throws Exception {
    var result = Files.lines(Path.of("input.txt"))
        .peek(s -> print(s+ ": "))
        .mapToLong(Long::parseLong)
        .map(s -> calcSecret(s, 2000))
        .peek(System.out::println).sum();
    println(result);
}


