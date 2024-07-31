package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.util.ObjectCheckExistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.category.mapper.CategoryMapper.CATEGORY_MAPPER;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final ObjectCheckExistence checkExistence;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Start create category: {}", newCategoryDto);
        checkExistence.checkCategoryExistence(newCategoryDto);

        Category category = categoryRepository.save(CATEGORY_MAPPER.toCategory(newCategoryDto));
        log.info("Create category: {}", CATEGORY_MAPPER.toCategoryDto(category));
        return CATEGORY_MAPPER.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        log.info("Start update category [{}]: {}", catId, newCategoryDto);
        Category category = checkExistence.getCategory(catId);
        Optional<Category> cat = categoryRepository.findByName(newCategoryDto.getName());

        if (cat.isPresent() && !category.getName().equals(newCategoryDto.getName())) {
            log.info(String.format("Category name: %s, new category name: %s", category.getName(), newCategoryDto.getName()));
            throw new ConflictException(String.format("Category %s already exists",
                    newCategoryDto.getName()));
        }

        category.setName(newCategoryDto.getName());
        log.info("Update category: {}", checkExistence.getCategory(catId));
        return CATEGORY_MAPPER.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long catId) {
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new ConflictException("You can't delete category with linked events");
        }
        checkExistence.getCategory(catId);
        categoryRepository.deleteById(catId);
        log.info("Delete category [{}]", catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("Get all categories from [{}], size [{}]", from, size);
        PageRequest pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).stream()
                .map(CATEGORY_MAPPER::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.info("Get category: {}", CATEGORY_MAPPER.toCategoryDto(checkExistence.getCategory(catId)));
        return CATEGORY_MAPPER.toCategoryDto(checkExistence.getCategory(catId));
    }
}
