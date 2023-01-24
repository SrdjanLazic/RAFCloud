package rs.raf.demo.services;

import org.springframework.http.ResponseEntity;
import rs.raf.demo.dto.MachineSearchDto;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMachineService<T, ID> {
    List<Machine> searchMachines(String email, MachineSearchDto machineSearchDto);
    List<Machine> findAll();
    Machine save(Machine machine);
    ResponseEntity<?> destroyById(Long id);
    ResponseEntity<?> startMachine(Long id);
    ResponseEntity<?> stopMachine(Long id);
    ResponseEntity<?> restartMachine(Long id);
    Optional<Machine> findById(Long id);
    void scheduledStart(Long id, Date date, Long userId);
    void scheduledStop(Long id, Date date, Long userId);
    void scheduledRestart(Long id, Date date, Long userId);

}
