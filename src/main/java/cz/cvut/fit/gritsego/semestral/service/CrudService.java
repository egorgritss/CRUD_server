package cz.cvut.fit.gritsego.semestral.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CrudService<T, K> {
    Page<T> readAll(Pageable pageable);
}
