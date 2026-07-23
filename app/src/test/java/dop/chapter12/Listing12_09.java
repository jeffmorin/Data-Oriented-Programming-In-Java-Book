package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.lang.String.format;

public class Listing12_09 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 12.9
   * ───────────────────────────────────────────────────────
   * Verifying our transitions
   * ───────────────────────────────────────────────────────
   */
  @Test
  void encodingStateTransitionsViaOrdering() {
    TaskId taskId = new TaskId(String.valueOf(random.nextInt()));
    List<Optional<Job>> states = List.of(
        Optional.empty(),                     //  ┐
        Optional.of(new Started(taskId)),     //  │◄── All of the states a task could be in,
        Optional.of(new Failed(taskId)),      //  │    including the "empty" state before it exists
        Optional.of(new Completed(taskId))    //  ┘
    );

    for (var a : states) {                             //  ┐
      for (var b : states) {                           //  ┘◄── Generating the cartesian product
                                                       //       (all combinations) of the possible states
        System.out.println(format("%-10s can transition to %-10s %s",
            a,
            b,
            order(a) < order(b))
        );
      }
    }
  }

  /*
  [out]
  (Empty)    can transition to (Empty)    false
  (Empty)    can transition to Started    true   // Rule 1
  (Empty)    can transition to Failed     true   // Rule 1
  (Empty)    can transition to Completed  true   // Rule 1
  Started    can transition to (Empty)    false
  Started    can transition to Started    false  // Rule 2
  Started    can transition to Failed     true   // Rule 2
  Started    can transition to Completed  true   // Rule 2
  Failed     can transition to (Empty)    false  // Rule 3
  Failed     can transition to Started    false  // Rule 3
  Failed     can transition to Failed     false  // Rule 2
  Failed     can transition to Completed  false  // Rule 3
  Completed  can transition to (Empty)    false  // Rule 3
  Completed  can transition to Started    false  // Rule 3
  Completed  can transition to Failed     false  // Rule 3
  Completed  can transition to Completed  false  // Rule 2
  */








  static Random random = new Random();

  int order(Job task) {
    return switch (task) {
      case Started __   -> 0;
      case Completed __ -> 1;
      case Failed __    -> 1;
    };
  }

  int order(Optional<Job> task) {
    return task.map(this::order).orElse(-1);
  }

  record TaskId(String value) {}
  sealed interface Job permits Started, Completed, Failed {}
  record Started(TaskId id) implements Job {}
  record Completed(TaskId id) implements Job {}
  record Failed(TaskId id) implements Job {}

}
