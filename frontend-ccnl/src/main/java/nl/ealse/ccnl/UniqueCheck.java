package nl.ealse.ccnl;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Check if there is no other instance of this application running. This check will work for the way
 * this application is installed by it's MS installer.
 */
class UniqueCheck  implements Callable<Boolean> {

  private Boolean unique = Boolean.TRUE;

  /**
   * Determine uniqueness of the application.
   *
   * @return true when unique
   */
  @Override
  public Boolean call() {
    ProcessHandle current = ProcessHandle.current();
    String refCommand = getCommand(current);
    long refPid = current.pid();

    ProcessHandle.allProcesses().forEach(process -> {
      // The current process is a child of a process with the same command String.
      // We check all processes with the same command String in the process and its parent.
      // With one instance running it will be the same process as the current process,
      // (both pid's match).
      // When the pid's differ, we have found that there is another instance
      // of this application running.
      if (refCommand.equals(getCommand(process)) && refCommand.equals(getParentCommand(process))
          && refPid != process.pid()) {
        unique = Boolean.FALSE;
      }
    });
    return unique;
  }

  private String getCommand(ProcessHandle process) {
    Optional<String> commandOptional = process.info().command();
    if (commandOptional.isPresent()) {
      return commandOptional.get();
    }
    return "";
  }

  private String getParentCommand(ProcessHandle process) {
    Optional<ProcessHandle> parent = process.parent();
    if (parent.isPresent()) {
      return getCommand(parent.get());
    }
    return null;
  }

}
