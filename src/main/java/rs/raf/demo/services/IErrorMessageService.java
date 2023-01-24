package rs.raf.demo.services;

import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Machine;

import java.util.List;

public interface IErrorMessageService<T, ID> {
    List<ErrorMessage> findAll(Long userId);
}
