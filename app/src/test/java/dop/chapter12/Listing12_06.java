package dop.chapter12;

public class Listing12_06 {

  /**
   * ───────────────────────────────────────────────────────
   * The lifecycle of a task
   * ───────────────────────────────────────────────────────
   */
  /*
  Initial state    (transitions to)         Terminal States
       │                   ┌─────────────┐
       ▼               ┌──►│  Completed  │  ◄───┘ │
   ┌────────────┐      │   └─────────────┘        │
   │  Started   ├──────┤                          │
   └────────────┘      │   ┌─────────────┐        │
                       └──►│  Failed     │    ◄───┘
                           └─────────────┘
  */

  /**
   * ───────────────────────────────────────────────────────
   * Listing 12.6
   * ───────────────────────────────────────────────────────
   * The lifecycle modeled as a sealed data type
   * ───────────────────────────────────────────────────────
   */
  record TaskId(String value) {}
  sealed interface Task {
    record Started(TaskId id /* other attrs */) implements Task {}   //  ┐
    record Completed(TaskId id /* other attrs */) implements Task {} //  │◄── The three states
    record Failed(TaskId id /* other attrs */) implements Task {}    //  ┘
  }

}

