package nl.ealse.ccnl;

import java.util.Optional;
import lombok.Getter;

public class UniqueCheck {

  private boolean nonUnique;

  public boolean uniqueProcess() {
    ProcessData currentData = new ProcessData(ProcessHandle.current());
    long pid = currentData.getPid();
    ProcessHandle.allProcesses().forEach(process -> {
      ProcessData processData = new ProcessData(process, currentData.getCommand());
      long descendentPid = processData.getDescendentPid();
      if (descendentPid != 0L && pid != descendentPid) {
        nonUnique = true;
      }
    });
    return !nonUnique;
  }

  @Getter
  private static class ProcessData {
    private long pid;
    private String command;
    private long descendentPid;

    private ProcessData(ProcessHandle process) {
      this.pid = process.pid();
      this.command = getCommand(process);
    }

    private ProcessData(ProcessHandle process, String command) {
      this.pid = process.pid();
      this.command = command;
      initialize(process);
    }

    private void initialize(ProcessHandle process) {
      process.descendants().forEach(descendent -> {
        if (getCommand(descendent).equals(command)) {
          descendentPid = descendent.pid();
        }
      });
    }

    private String getCommand(ProcessHandle process) {
      Optional<String> commandOptional = process.info().command();
      if (commandOptional.isPresent()) {
        return commandOptional.get();
      }
      return "";
    }
  }

}
