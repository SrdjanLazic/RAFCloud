package rs.raf.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import rs.raf.demo.model.Status;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public class MachineSearchDto {

    private String name;
    private List<Status> status;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public MachineSearchDto(String name, List<Status> status, LocalDate dateFrom, LocalDate dateTo) {
        this.name = name;
        this.status = status;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public MachineSearchDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "MachineSearchDto{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}
