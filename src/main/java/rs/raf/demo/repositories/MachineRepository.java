package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    @Query("select n from Machine n where" +
            " ((n.name like :machineName or :machineName is null) and" +
            " ((n.dateCreated between :dateFrom and :dateTo) or (:dateFrom is null and :dateTo is null)) and" +
            " (n.status in (:status) or n.status is null)) and n.active = true and n.user.email = :email")
    List<Machine> searchMachines(String email, List<Status> status, String machineName, LocalDate dateFrom, LocalDate dateTo);



}
