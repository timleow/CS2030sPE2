import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * CS2030S PE1 Question 2
 * AY21/22 Semester 2.
 *
 * @author A0251862A
 */
class Query {

  public static <T, S> Stream<Map.Entry<T, S>> getFilteredByKey(Map<T, S> table, Predicate<T> p) {
    return table.entrySet().stream().filter(e -> p.test(e.getKey()));
  }

  public static Stream<Integer> getIdsFromName(Map<String, List<Integer>> customerTable,
                                              String name) {
    return customerTable.get(name) == null
      ? Stream.of()
      : customerTable.get(name).stream();
  }

  public static Stream<Double> getCostsFromIDs(Map<Integer, Double> salesTable,
                                               Stream<Integer> purchaseIDStream) {
    return purchaseIDStream.filter(id -> salesTable.get(id) != null).map(id -> salesTable.get(id));
  }

  public static Stream<String> allCustomerCosts(Map<String, List<Integer>> customerTable,
                                                Map<Integer, Double> salesTable) {
    return customerTable.entrySet().stream()
           .flatMap(entry -> getCostsFromIDs(salesTable, 
                                             getIdsFromName(customerTable, entry.getKey()))
                                             .map(cost -> entry.getKey() + ": " + cost));

  }

  public static Stream<String> totaledCustomerCosts(Map<String, List<Integer>> customerTable,
                                                    Map<Integer, Double> salesTable) {
    return customerTable.entrySet().stream()
           .map(entry -> entry.getKey() + ": "
               + getCostsFromIDs(salesTable,
                                 getIdsFromName(customerTable,
                                                entry.getKey()))
                                                .reduce(0.0, (x, y) -> x + y));
    
  }

}

