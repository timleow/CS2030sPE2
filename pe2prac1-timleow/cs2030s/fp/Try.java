package cs2030s.fp;

import java.lang.Throwable;
/**
 * CS2030S PE2 Question 1
 * AY20/21 Semester 2.
 *
 * @author A0251862A
 */

public abstract class Try<T> {
  /**
   * This method creates a success containing the value produced by the producer
   * passed to it if it produces successfuly, otherwise
   * it returns a failure containing the exception thrown by the producer.
   *
   * @param <T> The type of the value.
   * @param p The producer.
   * @return The monad which is either a failure or a success.
   *
   */
  public static <T> Try<T> of(Producer<? extends T> p) {
    try {
      return success(p.produce());
    } catch (Throwable e) {
      return failure(e);
    }
  }

  /**
   * This factory method creates and returns a new Success containing the value passed to it.
   * 
   * @param <T> The type of the value.
   * @param value The value the success is intended to hold.
   * @return The Success.
   */
  public static <T> Try<T> success(T value) {
    return new Success<>(value);
  }

  /**
   * This factory method creates and returns a new Failure containing the exception thrown.
   *
   * @param <T> The type of value the monad was intended to hold.
   * @param e The throwable exception.
   * @return The Failure.
   *
   */
  public static <T> Try<T> failure(Throwable e) {
    //TODO explain
    @SuppressWarnings("unchecked")
    Try<T> castedFailure = (Try<T>) new Failure(e);
    return castedFailure;
  }

  public abstract T get() throws Throwable;

  public abstract <U> Try<U> map(Transformer<? super T, ? extends U> trans);

  public abstract <U> Try<U> flatMap(Transformer<? super T, ? extends Try<? extends U>> trans);

  public abstract Try<T> onFailure(Consumer<? super T> consumer);

  public abstract Try<T> recover(Transformer<? super Throwable, ? extends T> trans);


  private static final class Success<T> extends Try<T> {
    private T value;

    Success(T value) {
      this.value = value;
    }

    public T get() throws Throwable {
      return this.value;
    }

    @Override 
    public boolean equals(Object obj) {
      if (obj instanceof Success<?>) {
        Success<?> castedObj = (Success<?>) obj;
        if (this.value == null || castedObj.value == null) {
          return this.value == castedObj.value;
        }
        return this.value.equals(castedObj.value);
      }
      return false;
    }

    @Override
    public <U> Try<U> map(Transformer<? super T, ? extends U> trans) {
      try {
        return success(trans.transform(this.get()));
      } catch (Throwable e) {
        return failure(e);
      }
    }

    @Override
    public <U> Try<U> flatMap(Transformer<? super T, ? extends Try<? extends U>> trans) {
      try {
        return success(trans.transform(this.value).get());
      } catch (Throwable e) {
        return failure(e);
      }
    }

    /**
     * This method simply returns the success instance since
     * it is meant to handle failures.
     *
     * @param consumer The consumer.
     * @return The instance from which the method is called.
     *
     */
    @Override
    public Try<T> onFailure(Consumer<? super T> consumer) {
      return this;
    }

    /**
     * This method simply returns the success instance
     * since it is meant to help recover values from
     * failures.
     *
     * @param trans The transformer.
     * @return The instance from which the method is called.
     *
     */
    @Override
    public Try<T> recover(Transformer<? super Throwable, ? extends T> trans) {
      return this;
    }
  }

  private static final class Failure extends Try<Object> {
    private Throwable e;

    Failure(Throwable e) {
      this.e = e;
    }

    public Object get() throws Throwable {
      throw e;
    }

    @Override 
    public boolean equals(Object obj) {
      if (obj instanceof Failure) {
        Failure castedObj = (Failure) obj;
        if (this.e == null || castedObj.e == null) {
          return this.e == castedObj.e;
        }
        return this.e.toString().equals(castedObj.e.toString());
      }
      return false;
    }

    @Override
    public <U> Try<U> map(Transformer<? super Object, ? extends U> trans) {
      //TODO: explain
      @SuppressWarnings("unchecked")
      Try<U> castedFailure = (Try<U>) this;
      return castedFailure;
    }

    @Override
    public <U> Try<U> flatMap(Transformer<? super Object, ? extends Try<? extends U>> trans) {
      return failure(this.e);
    }

    /**
     * This method takes in a consumer, and if the consumer consumes without issue, the original
     * failure instance is returned, otherwise a new failure instance containing the exception 
     * thrown during the consumption process is returned.
     *
     * @param consumer The consumer.
     * @return The failure instance, depending on the success of consumption.
     *
     */
    @Override
    public Try<Object> onFailure(Consumer<? super Object> consumer) {
      try {
        consumer.consume(this.e);
        return this;
      } catch (Throwable ethrown) {
        return failure(ethrown);
      }
    }

    /**
     * This method recovers back by transforming an exception into a value that can be held 
     * in a success if the transformer transforms without throwing an exception, otherwise,
     * it returns a new failure containing the exception thrown from the transformation process.
     *
     * @param trans The transformer which may or may not throw a throwable 
     *              during transformation.
     * @return The success instance if the transformation occured successfully,
     *         otherwise a failure instance containing the exception thrown
     *         by the transformer.
     */
    @Override
    public Try<Object> recover(Transformer<? super Throwable, ? extends Object> trans) {
      try {
        return success(trans.transform(this.e));
      } catch (Throwable ethrown) {
        return failure(ethrown);
      }
    }
  }
}
