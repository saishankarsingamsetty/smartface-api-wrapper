package com.smartface.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.smartface.dto.UpdateCameraDTO;
import com.smartface.entities.Camera;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CameraMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCameraFromDto(UpdateCameraDTO dto, @MappingTarget Camera camera);

    // ✅ MapStruct will automatically map nested static classes from DTO to entity
    Camera.FaceDetectorConfig toEntity(UpdateCameraDTO.FaceDetectorConfig dto);
    Camera.PedestrianDetectorConfig toEntity(UpdateCameraDTO.PedestrianDetectorConfig dto);
    Camera.PalmDetectorConfig toEntity(UpdateCameraDTO.PalmDetectorConfig dto);

    Camera.SpoofDetectorConfig toEntity(UpdateCameraDTO.SpoofDetectorConfig dto);
    Camera.PalmSpoofDetectorConfig toEntity(UpdateCameraDTO.PalmSpoofDetectorConfig dto);
    Camera.PreviewAttributesConfig toEntity(UpdateCameraDTO.PreviewAttributesConfig dto);

    Camera.ObjectDetectorConfig toEntity(UpdateCameraDTO.ObjectDetectorConfig dto);
    Camera.ObjectTypesConfiguration toEntity(UpdateCameraDTO.ObjectTypesConfiguration dto);
}
