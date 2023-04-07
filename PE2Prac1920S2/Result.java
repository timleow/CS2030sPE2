import java.util.function.*;

abstract class Result<T> {

  public static <T> Success<T> success(T value) {
    return new Success<>(value);
  }

  public static <T> Result<T> error(String errorMessage) {
    return (Result<T>) new Error(errorMessage);
  }


  public abstract Result<T> filter(Predicate<? super T> pred, String errorMsg);

  public abstract <R> Result<R> flatMap(Function<? super T, ? extends Result<? extends R>> f); 

  public abstract T orElseThrow(Exception e) throws IllegalStateException;

  private static final class Success<T> extends Result<T> {
    private T value;

    Success(T value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return "Success: " + this.value;
    }

    @Override
    public Result<T> filter(Predicate<? super T> pred, String errorMsg) {
      if (pred.test(this.value)) {
        return this;
      } else {
        return Result.error(errorMsg);
      }
    }

    @Override
    public <R> Result<R> flatMap(Function<? super T, ? extends Result<? extends R>> f) {
      try {
        return success(f.apply(this.value).value);
      } catch (Exception e) {
        return Result.<R>error(e.getMessage());
      }
    }

    @Override
    public T orElseThrow(Exception e) throws IllegalStateException {
      return this.value;
    }
  }

  private static final class Error extends Result<Object> {
    private String errorMessage;


    @Override
    public Result<Object> filter(Predicate<? super Object> pred, String errorMsg) {
      return this;
    }

    @Override
    public <R> Result<R> flatMap(Function<? super Object,  ? extends Result<? extends R>> f) {
      return error(this.errorMessage);
    }

    @Override
    public Object orElseThrow(Exception e) throws IllegalStateException {
      throw new IllegalStateException();
    }
  }
}
