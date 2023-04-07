String pattern(int n) {

  return Stream.iterate(n , m -> m - 1).limit(n)
    .flatMap(m -> Stream.iterate(m, i -> i - 1).map(x -> {
            int diff = n - m;
            System.out.print("m: " + m);
            System.out.print(" x: " + x);
            System.out.print(" diff " + diff);
            System.out.print(" n-x " + (n - x));
            System.out.print("\n\n");
            return (n - x) >= diff*2 ? (x + diff) : ".";
         }).limit(n))
               .reduce("", (x, y) -> x.toString() + y.toString(), (x, y) -> x + y);
}
