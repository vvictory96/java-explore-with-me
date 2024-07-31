package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;

@Mapper
public interface CompilationMapper {

    CompilationMapper COMPILATION_MAPPER = Mappers.getMapper(CompilationMapper.class);

    CompilationDto toCompilationDto(Compilation compilation);
}
