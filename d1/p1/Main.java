void main() throws IOException {
	
	var lines = Files.lines(Path.of("input.txt")).toList();
	int[] left = new int[10_000];
	int[] right = new int[10_000];
	for(int i = 0; i < lines.size(); i++){
		var strs = lines.get(i).split("\\s+");
		println("strs[0]" + strs[0]);
		println("strs[1]" + strs[1]);
		left[i] = Integer.parseInt(strs[0]); 
		right[i] = Integer.parseInt(strs[1]); 
	}

	Arrays.sort(left);
	Arrays.sort(right);

	int total = 0;
	for(int i = 0; i < 10_000; i++){
		total += Math.abs(left[i] - right[i]);
	}
	println(total);
}

