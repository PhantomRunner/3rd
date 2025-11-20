// Stanislav Kril - 3133810

ss Search implements Callable<Integer[]> {
   private int data[];
   private int freq;
   private int lower, upper;


    public Search(int d[], int a, int b) {
        data = d;
        lower = a;
        upper = b;
    }

    @Override
    public Integer[] call() throws Exception {
        int freq = 0;
        int max = Integer.MIN_VALUE;

        for (int j = lower; j < upper; j++) {
            if (data[j] % 2 == 0) {
                freq++;
            }
            if (data[j] > max) {
                max = data[j];
            }
        }
        return new Integer[]{freq, max};
    }

}
