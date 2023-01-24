package rs.raf.demo.services;

import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.repositories.ErrorMessageRepository;

import java.util.List;

@Service
public class ErrorMessageServiceImpl implements IErrorMessageService<ErrorMessage, Long> {

    private ErrorMessageRepository errorMessageRepository;

    public ErrorMessageServiceImpl(ErrorMessageRepository errorMessageRepository) {
        this.errorMessageRepository = errorMessageRepository;
    }

    @Override
    public List<ErrorMessage> findAll(Long userId) {
        return errorMessageRepository.findAllByUserId(userId);
    }
}
