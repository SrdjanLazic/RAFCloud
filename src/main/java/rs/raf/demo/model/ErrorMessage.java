package rs.raf.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "errors")
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long machineId;

    @Column
    private String operation;

    @Column
    private String message;

    @Column
    private Date date;

    @Column
    private Long userId;

    public ErrorMessage(Long machineId, String operation, String message, Date date, Long userId) {
        this.machineId = machineId;
        this.operation = operation;
        this.message = message;
        this.date = date;
        this.userId = userId;
    }

    public ErrorMessage() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
