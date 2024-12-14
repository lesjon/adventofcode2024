void main() throws IOException {
	
	var lines = Files.lines(Path.of("input.txt"));
	List<Integer> left = new ArrayList<>();
	List<Integer> right = new ArrayList<>();
	lines.map(line -> line.split("\\s+")).forEach(strs -> {
		left.add(Integer.parseInt(strs[0]));
		right.add(Integer.parseInt(strs[1]));
	});
	Map<Integer, Integer> map = new HashMap<>();
	left.stream().forEach(i -> map.put(i, 0));
	

	int total = 0;
	for (int i : right) {
		if (map.containsKey(i)) {
			total += i;
		}
	}
	println(total);
}

