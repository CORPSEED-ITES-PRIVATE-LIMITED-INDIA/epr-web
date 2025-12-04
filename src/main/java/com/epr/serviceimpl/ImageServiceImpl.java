package com.epr.serviceimpl;

import com.epr.entity.Image;
import com.epr.repository.ImageRepository;
import com.epr.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image saveImage(String fileName, String url) {
        Image image = new Image();
        image.setFileName(fileName);
        image.setUrl(url);
        image.setUploadedAt(LocalDateTime.now());

        return imageRepository.save(image);
    }
}