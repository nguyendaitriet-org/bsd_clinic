package com.bsdclinic.resource;

import com.bsdclinic.resource.dto.CreateResourceRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Override
    @Transactional
    public void createResource(List<MultipartFile> files, CreateResourceRequest request) {

    }
}
