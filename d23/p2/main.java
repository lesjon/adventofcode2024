
Map<Set<String>, Set<String>> cache = new HashMap<>();

int largestSetSize = 3;

<T> Set<Set<T>> subsets(Set<T> set){
    // println("subset(" + set.size());
    if(set.size() == 1){
        return Set.of(set);
    }
    Set<Set<T>> result = new HashSet<>();
    for(T t : set){
        var subset = new HashSet<>(set);
        subset.remove(t);
        result.add(subset);
    }
    return result;
}

Set<String> maxConnectedSet(Map<String, Set<String>> map, Set<String> set){
    // println("maxConnectedSet(" + set);
    if(cache.containsKey(set)){
        return cache.get(set);
    }
    boolean isConnectedSet = true;
    for (String computer : set) {
        var setWithoutComputer = new HashSet<>(set);
        setWithoutComputer.remove(computer);
        if(!map.get(computer).containsAll(setWithoutComputer)){
            isConnectedSet = false;
            break;
        }
    }
    if(isConnectedSet){
        // println("isConnectedSet");
        cache.put(set, set);
        return set;
    }
    Set<String> largestSubset = Set.of();
    for(var subset : subsets(set)){
        // println("subset: " + subset);
        if(subset.size() < largestSetSize){
            continue;
        }
        var maxSubset = maxConnectedSet(map, subset);
        if(maxSubset.size() > largestSubset.size()){
            largestSubset = maxSubset;
        }
    }
    // println("largestSubset: " + largestSubset);
    cache.put(set, largestSubset);
    return largestSubset;
}

void main() throws Exception {
    Map<String, Set<String>> map = new HashMap<>();
    Files.lines(Path.of("input.txt"))
        .map(l -> l.split("-"))
        .forEach(strs -> {
            if (map.containsKey(strs[0])) {
                map.get(strs[0]).add(strs[1]);
            } else {
                map.put(strs[0], new HashSet<>(Set.of(strs[1])));
            }
            if (map.containsKey(strs[1])) {
                map.get(strs[1]).add(strs[0]);
            } else {
                map.put(strs[1], new HashSet<>(Set.of(strs[0])));
            }
        });
    println(map);
    Set<Set<String>> sets = new HashSet<>();
    for (String computer : map.keySet()) {
        println(computer);
        for (String conn : map.get(computer)) {
            Set<String> testSet = new HashSet<>(map.get(conn));
            testSet.add(conn);
            sets.add(maxConnectedSet(map, testSet));
            largestSetSize = Math.max(largestSetSize, sets.stream().mapToInt(Set::size).max().getAsInt());
        }
    }
    println("sets: " + sets);
    Set<String> largestSet = sets.stream().reduce((l, r) -> l.size() > r.size() ? l : r).get();
    println(largestSet.stream().sorted().collect(Collectors.joining(",")));
}
