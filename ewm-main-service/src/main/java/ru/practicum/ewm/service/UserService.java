package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(NewUserRequest request);

    List<UserDto> getAll(int from, int size);

    void delete(Long userId);
}