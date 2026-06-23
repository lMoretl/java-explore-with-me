package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(NewUserRequest request) {
        User user = UserMapper.toEntity(request);
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        checkPageParams(from, size);

        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllById(ids)
                    .stream()
                    .map(UserMapper::toDto)
                    .toList();
        }

        return userRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    private void checkPageParams(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        userRepository.deleteById(userId);
    }
}