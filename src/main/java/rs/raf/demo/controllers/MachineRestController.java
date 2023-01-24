package rs.raf.demo.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.dto.MachineCreateDto;
import rs.raf.demo.dto.MachineSearchDto;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;
import rs.raf.demo.services.MachineServiceImpl;
import rs.raf.demo.services.UserServiceImpl;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/machines")
public class MachineRestController {

    private final MachineServiceImpl machineService;
    private final UserServiceImpl userService;

    public MachineRestController(MachineServiceImpl machineService, UserServiceImpl userService) {
        this.machineService = machineService;
        this.userService = userService;
    }

    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Machine> searchMachines(@RequestBody MachineSearchDto machineSearchDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if(machineSearchDto.getStatus() == null){
            List<Status> statuses = new ArrayList<>();
            statuses.add(Status.RUNNING);
            statuses.add(Status.STOPPED);
            machineSearchDto.setStatus(statuses);
        }

        return machineService.searchMachines(email, machineSearchDto);
    }

    @GetMapping(value = "/get")
    public List<Machine> getAllMachines(){
        return machineService.findAll();
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMachine(@RequestBody MachineCreateDto machineCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Machine machine = new Machine();
        User user = userService.findByEmail(authentication.getName()).get();
        machine.setActive(true);
        machine.setDateCreated(LocalDate.now());
        machine.setStatus(Status.STOPPED);
        machine.setName(machineCreateDto.getName());
        machine.setUser(user);

        return new ResponseEntity<>(machineService.save(machine), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMachineById(@PathVariable("id") Long id){
        Optional<Machine> optionalMachine = machineService.findById(id);
        if(optionalMachine.isPresent()) {
            return ResponseEntity.ok(optionalMachine.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(value = "/destroy/{id}")
    public ResponseEntity<?> destroyMachine(@PathVariable Long id){
        machineService.destroyById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startMachine(@PathVariable Long id){
        return machineService.startMachine(id);
    }

    @GetMapping(value = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopMachine(@PathVariable Long id){
        return machineService.stopMachine(id);
    }

    @GetMapping(value = "/restart/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartMachine(@PathVariable Long id){
        return machineService.restartMachine(id);
    }


    @GetMapping(value = "/schedule/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> scheduleStart(@PathVariable Long id, @RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm") Date date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).get();
        this.machineService.scheduledStart(id, date, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/schedule/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> scheduleStop(@PathVariable Long id, @RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm") Date date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).get();
        this.machineService.scheduledStop(id, date, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/schedule/restart/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> scheduleRestart(@PathVariable Long id, @RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm") Date date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName()).get();
        this.machineService.scheduledRestart(id, date, user.getId());
        return ResponseEntity.ok().build();
    }



}
