package com.epr.service;

import com.epr.entity.Image;

public interface ImageService {
    Image saveImage(String fileName, String url);
}