package rs.raf.demo.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.Status;
import rs.raf.demo.repositories.MachineRepository;

import java.util.Random;

@Component
public class AsyncOperations {

    private final MachineRepository machineRepository;

    public AsyncOperations(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Async
    public void asyncStartMachine(Machine machine){
        long time = 10000 + new Random().nextInt(2000);
        try {
            Thread.sleep(time);
        } catch(Exception e) {

        }
        machine.setStatus(Status.RUNNING);
        machine.setLocked(false);
        machineRepository.save(machine);
    }

    @Async
    public void asyncStopMachine(Machine machine){
        long time = 10000 + new Random().nextInt(2000);
        try {
            Thread.sleep(time);
        } catch(Exception e) {

        }
        machine.setStatus(Status.STOPPED);
        machine.setLocked(false);
        machineRepository.save(machine);
    }

    @Async
    public void asyncRestartMachine(Machine machine){
        long time = 10000 + new Random().nextInt(2000);
        try {
            Thread.sleep(time/2);
        } catch(Exception e) {

        }
        machine.setStatus(Status.STOPPED);
        machineRepository.save(machine);
        try {
            Thread.sleep(time/2);
        } catch(Exception e) {

        }
        machine.setStatus(Status.RUNNING);
        machine.setLocked(false);
        machineRepository.save(machine);
    }


}
