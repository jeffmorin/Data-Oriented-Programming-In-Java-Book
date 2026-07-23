package dop.chapter12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Listing12_10 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 12.10
   * ───────────────────────────────────────────────────────
   * Verifying the database behaves the way we need it to
   * ───────────────────────────────────────────────────────
   */
  @Test
  void onlyValidStateTransitionsAllowed() {
    TaskId taskId = new TaskId(String.valueOf(random.nextInt()));
    List<Optional<Job>> states = List.of(
        Optional.empty(),
        Optional.of(new Started(taskId)),
        Optional.of(new Failed(taskId)),
        Optional.of(new Completed(taskId))
    );

    MyRepository repo = new MyRepository(db);
    for (var a : states) {
      for (var b : states) {
        if (order(a) < order(b)) {              //  ┐
          Assertions.assertDoesNotThrow(() -> { //  │◄── If state A is "less than"
            a.ifPresent(repo::save);            //  │    (can transition to) state B,
            b.ifPresent(repo::save);            //  │    then the database should allow it
          });                                   //  ┘
        } else {
          Assertions.assertThrows(ConditionFailedException.class, () -> {
          //  ^ All invalid transitions should be caught by our Condition Expression
            a.ifPresent(repo::save);
            b.ifPresent(repo::save);
          });
        }
        repo.delete(taskId);
      }
    }
  }








  static Random random = new Random();
  Object db;
  int order(Optional<Job> job) { return 0; }
  record TaskId(String value) {}
  interface Job {}
  record Started(TaskId id) implements Job {}
  record Failed(TaskId id) implements Job {}
  record Completed(TaskId id) implements Job {}
  static class MyRepository {
    MyRepository(Object db) {}
    void save(Job job) {}
    void delete(TaskId taskId) {}
  }
  static class ConditionFailedException extends RuntimeException {}

}
