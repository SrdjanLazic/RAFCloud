package rs.raf.demo.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import rs.raf.demo.dto.MachineSearchDto;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.Status;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.scheduler.ScheduledRestart;
import rs.raf.demo.scheduler.ScheduledStart;
import rs.raf.demo.scheduler.ScheduledStop;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MachineServiceImpl implements IMachineService<Machine, Long> {

    private final MachineRepository machineRepository;
    private final AsyncOperations asyncOperations;
    private final TaskScheduler taskScheduler;
    private final ErrorMessageRepository errorMessageRepository;

    public MachineServiceImpl(MachineRepository machineRepository, AsyncOperations asyncOperations, TaskScheduler taskScheduler, ErrorMessageRepository errorMessageRepository) {
        this.machineRepository = machineRepository;
        this.asyncOperations = asyncOperations;
        this.taskScheduler = taskScheduler;
        this.errorMessageRepository = errorMessageRepository;
    }

    @Override
    public List<Machine> searchMachines(String email, MachineSearchDto machineSearchDto) {
        return machineRepository.searchMachines(
                email, machineSearchDto.getStatus(), machineSearchDto.getName(), machineSearchDto.getDateFrom(), machineSearchDto.getDateTo());
    }

    @Override
    public Optional<Machine> findById(Long id) {
        return machineRepository.findById(id);
    }

    @Override
    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    @Override
    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    public ResponseEntity<?> destroyById(Long id) {
        Machine machine = machineRepository.findById(id).get();
        if(machine.isLocked()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot destroy a locked machine.");
        }
        machine.setActive(false);
        return ResponseEntity.ok(machineRepository.save(machine));
    }

    @Override
    public ResponseEntity<?> startMachine(Long id) {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(!optionalMachine.isPresent()) return ResponseEntity.notFound().build();

        Machine machine = optionalMachine.get();
        if(machine.getStatus() == Status.RUNNING) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is already running.");
        }

        if(!machine.isActive()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is not active.");

        if(machine.isLocked()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is locked.");

        machine.setLocked(true);
        machineRepository.save(machine);
        asyncOperations.asyncStartMachine(machine);

        return ResponseEntity.ok(optionalMachine.get());
    }

    @Override
    public ResponseEntity<?> stopMachine(Long id) {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(!optionalMachine.isPresent()) return ResponseEntity.notFound().build();

        Machine machine = optionalMachine.get();
        if(machine.getStatus() == Status.STOPPED) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is already stopped.");

        if(!machine.isActive()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is not active.");

        if(machine.isLocked()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is locked.");

        machine.setLocked(true);
        machineRepository.save(machine);
        asyncOperations.asyncStopMachine(machine);

        return ResponseEntity.ok(optionalMachine.get());
    }

    @Override
    public ResponseEntity<?> restartMachine(Long id) {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(!optionalMachine.isPresent()) return ResponseEntity.notFound().build();

        Machine machine = optionalMachine.get();
        if(machine.getStatus() == Status.STOPPED) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is not running.");

        if(!machine.isActive()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is not active.");

        if(machine.isLocked()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. The requested machine is locked.");

        machine.setLocked(true);
        machineRepository.save(machine);
        asyncOperations.asyncRestartMachine(machine);

        return ResponseEntity.ok(optionalMachine.get());
    }

    @Override
    public void scheduledStart(Long id, Date date, Long userId){
        this.taskScheduler.schedule(new ScheduledStart(id, machineRepository, errorMessageRepository, asyncOperations, userId), date);
    }

    @Override
    public void scheduledStop(Long id, Date date, Long userId) {
        this.taskScheduler.schedule(new ScheduledStop(id, machineRepository, errorMessageRepository, asyncOperations, userId), date);
    }

    @Override
    public void scheduledRestart(Long id, Date date, Long userId) {
        this.taskScheduler.schedule(new ScheduledRestart(id, machineRepository, errorMessageRepository, asyncOperations, userId), date);

    }
}
