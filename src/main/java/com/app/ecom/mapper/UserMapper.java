package com.app.ecom.mapper;

import com.app.ecom.dto.UserRequestDTO;
import com.app.ecom.dto.UserResponseDTO;
import com.app.ecom.entities.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AddressMapper.class})
public interface UserMapper {

    // Entity to DTO
    UserResponseDTO toDTO(User user);

    // CreateDTO to Entity (for creating new users)
    User toEntity(UserRequestDTO userRequestDTO);

    // List conversions
    List<UserResponseDTO> toDTOList(List<User> users);

    // Update existing entity from DTO (ignoring null values)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UserResponseDTO userResponseDTO, @MappingTarget User user);
}


