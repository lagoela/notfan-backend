package com.lagoela.notfan_backend.file;

import com.lagoela.notfan_backend.json.FollowingNotFollowingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping()
    public List<FollowingNotFollowingModel> uploadAndProcessZip(@RequestParam("zipFile")MultipartFile file){
        try {
            fileService.processZip(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

