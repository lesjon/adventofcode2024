
List<Set<String>> findLoopsInN(Map<String, Set<String>> map, List<String> path, int stepsLeft){
    String curr = path.get(path.size()-1);
    if (stepsLeft == 0) {
        if(map.get(curr).contains(path.get(0))){
            return List.of(path.stream().collect(Collectors.toSet()));
        }
        return List.of();
    }
    List<Set<String>> result = new ArrayList<>();
    for(var conn : map.get(curr)){
        if(path.size()-2 >= 0 && conn.equals(path.get(path.size()-2))){
            continue;
        }
        List<String> nextPath = new ArrayList<>(path);
        nextPath.add(conn);
        findLoopsInN(map, nextPath, stepsLeft-1).forEach(result::add);
    }
    return result;
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
    Set<Set<String>> loops = new HashSet<>();
    for (String computer : map.keySet()) {
        println(computer);
        if (!computer.startsWith("t")) {
            continue;
        }
        findLoopsInN(map, List.of(computer), 2).forEach(loops::add);
    }
    println("loops: " + loops);
    println(loops.size());
}
