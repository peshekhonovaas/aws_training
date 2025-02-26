package com.awsbasics.simpleapp.service;

import com.awsbasics.simpleapp.clientmodel.ImageClientModel;
import com.awsbasics.simpleapp.clientmodel.ImageUploadClientModel;
import com.awsbasics.simpleapp.dao.ImageJpaRepository;
import com.awsbasics.simpleapp.entity.ImageEntityModel;
import com.awsbasics.simpleapp.exception.S3ObjectNotFoundException;
import com.awsbasics.simpleapp.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    private ImageJpaRepository imageJpaRepository;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private FileService fileService;

    @Value("${server.port}")
    private String serverPort;

    @Transactional
    public ResponseEntity<?> findAll() {
        List<ImageEntityModel> entities = imageJpaRepository.findAll();
        List<ImageClientModel> clientModels = entities.stream()
                .map(entityModel -> imageMapper.toClientModel(entityModel, s3Service))
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientModels, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> upload(ImageUploadClientModel downloadClientModel) {
        MultipartFile file = downloadClientModel.getFile();
        InputStream tempInput = fileService.getInputStream(file);
        s3Service.uploadObject(tempInput, file.getOriginalFilename(), downloadClientModel.getName());
        ImageEntityModel entityModel = imageMapper.toEntityModel(downloadClientModel);
        entityModel.setSize(file.getSize());
        entityModel.setFileExtension(fileService.getFileExtension(file.getOriginalFilename()));
        ImageEntityModel save = imageJpaRepository.save(entityModel);
        ImageClientModel imageClientModel = imageMapper.toClientModel(save, s3Service);
        return new ResponseEntity<>(imageClientModel, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> delete(String name) {
        List<ImageEntityModel> byName = imageJpaRepository.findByName(name);
        byName.stream()
                .map(ImageEntityModel::getName)
                .forEach(s3Service::deleteObject);
        byName.forEach(imageJpaRepository::delete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional
    public ResponseEntity<?> getRandom() {
        Optional<ImageEntityModel> entityModel = imageJpaRepository.findRandomEntity();
        ImageClientModel imageClientModel = imageMapper.toClientModel(entityModel.get(), s3Service);
        return new ResponseEntity<>(imageClientModel, HttpStatus.OK);
    }

    @Transactional
    public byte[] getImage(String name) {
        List<ImageEntityModel> byName = imageJpaRepository.findByName(name);
        if (byName == null || byName.size() == 0) {
            throw new S3ObjectNotFoundException();
        }
        return imageMapper.toClientModel(byName.get(0), s3Service).getBitmap();
    }
}
