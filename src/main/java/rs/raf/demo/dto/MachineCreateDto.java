package rs.raf.demo.dto;

import rs.raf.demo.model.Status;
import rs.raf.demo.model.User;

import java.time.LocalDate;

public class MachineCreateDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
