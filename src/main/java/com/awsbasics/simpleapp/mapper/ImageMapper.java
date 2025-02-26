package com.awsbasics.simpleapp.mapper;

import com.awsbasics.simpleapp.clientmodel.ImageClientModel;
import com.awsbasics.simpleapp.clientmodel.ImageUploadClientModel;
import com.awsbasics.simpleapp.entity.ImageEntityModel;
import com.awsbasics.simpleapp.service.S3Service;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "bitmap", ignore = true)
    ImageClientModel toClientModel(ImageEntityModel entityModel, @Context S3Service s3Service);

    ImageEntityModel toEntityModel(ImageUploadClientModel clientModel);

    ImageEntityModel toEntityModel(ImageClientModel clientModel);

    @AfterMapping
    default void setBitmapToClient(@MappingTarget ImageClientModel target, ImageEntityModel source, @Context S3Service s3Service) {
        target.setBitmap(s3Service.downloadObject(source.getName()));
    }
}
