package rs.raf.demo.scheduler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.Status;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.services.AsyncOperations;

import java.util.Date;
import java.util.Optional;

public class ScheduledRestart implements Runnable {

    private final Long machineId;
    private final Long userId;
    private final MachineRepository machineRepository;
    private final ErrorMessageRepository errorMessageRepository;
    private final AsyncOperations asyncOperations;

    public ScheduledRestart(Long machineId, MachineRepository machineRepository, ErrorMessageRepository errorMessageRepository, AsyncOperations asyncOperations, Long userId) {
        this.machineId = machineId;
        this.machineRepository = machineRepository;
        this.errorMessageRepository = errorMessageRepository;
        this.asyncOperations = asyncOperations;
        this.userId = userId;
    }

    @Override
    public void run() {
        Optional<Machine> optionalMachine = machineRepository.findById(machineId);

        if(!optionalMachine.isPresent()) {
            errorMessageRepository.save(new ErrorMessage(machineId, "RESTART",
                    "The machine you have attempted to schedule restart does not exist.", new Date(), userId));
            return;
        }

        Machine machine = optionalMachine.get();

        if(machine.getStatus() == Status.STOPPED) {
            errorMessageRepository.save(new ErrorMessage(machineId, "RESTART",
                    "The machine you have attempted to schedule restart is currently stopped.", new Date(), userId));
            return;
        }

        if(!machine.isActive()) {
            errorMessageRepository.save(new ErrorMessage(machineId, "RESTART",
                    "The machine you have attempted to schedule restart is not active.", new Date(), userId));
            return;
        }

        if(machine.isLocked()) {
            errorMessageRepository.save(new ErrorMessage(machineId, "RESTART",
                    "The machine you have attempted to schedule restart is locked.", new Date(), userId));
            return;
        }

        machine.setLocked(true);
        machineRepository.save(machine);
        asyncOperations.asyncRestartMachine(machine);
    }
}
