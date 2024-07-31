package ru.practicum.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilationByAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationByAdmin(Long compId);

    CompilationDto updateCompilationByAdmin(Long compId, UpdateCompilationRequest request);

    List<CompilationDto> findCompilationByPublic(Boolean pinned, PageRequest pageable);

    CompilationDto findCompilationById(Long compId);
}