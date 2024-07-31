package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.ObjectCheckExistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.user.mapper.UserMapper.USER_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectCheckExistence objectCheckExistence;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        if (ids != null) {
            return userRepository.findByIdIn(ids, pageable).stream()
                    .map(USER_MAPPER::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable).stream()
                    .map(USER_MAPPER::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public UserDto createUser(UserShortDto userShortDto) {
        Optional<User> userOpt = userRepository.findByName(userShortDto.getName());
        if (userOpt.isPresent()) {
            throw new ConflictException(String.format("User with name %s already exists",
                    userShortDto.getName()));
        }
        User user = userRepository.save(USER_MAPPER.toUser(userShortDto));
        log.info("User was created [{}]", USER_MAPPER.toUserDto(user));
        return USER_MAPPER.toUserDto(user);
    }


    @Override
    public void deleteUser(Long id) {
        objectCheckExistence.getUser(id);
        userRepository.deleteById(id);
        log.info("User [{}] was deleted", id);
    }

}
