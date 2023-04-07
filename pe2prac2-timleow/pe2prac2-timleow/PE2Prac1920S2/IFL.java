import java.util.function.Supplier; import java.util.function.Predicate; import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

class IFL<T> {
  Supplier<T> head;
  Supplier<IFL<T>> tail;

  IFL(Supplier<T> head, Supplier<IFL<T>> tail) {
    this.head = head;
    this.tail = tail;
  }

  static <T> IFL<T> of(List<? extends T> list) {
    if (list.size() == 0) {
       return new IFL<T>(() -> null, () -> null);
    } else {
      return new IFL<T>(() -> list.get(0),
                     () -> IFL.of(list.subList(1, list.size())));
    }
  }

  Optional<T> findMatch(Predicate<? super T> predicate) { 
    if (this.head.get() == null) {
      return Optional.empty();
    }
    Optional<T> monad = Optional.of(this.head.get());
    return monad.filter(predicate).map(x -> monad).orElseGet(() -> this.tail.get().findMatch(predicate));
  }
}
