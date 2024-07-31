package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

@Mapper
public interface CategoryMapper {

    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);

    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);
}
