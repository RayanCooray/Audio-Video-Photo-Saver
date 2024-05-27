package lk.rayan.personal.audiovideophotosaver.service;

import lk.rayan.personal.audiovideophotosaver.model.FileInfo;
import lk.rayan.personal.audiovideophotosaver.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    private FileRepository fileRepository;


    public FileService() {
        this.fileStorageLocation = Paths.get("resources").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FileInfo storeFile(MultipartFile file, String fileType) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String subfolder = getSubfolder(fileType);

        if (subfolder == null) {
            throw new RuntimeException("Invalid file type");
        }

        try {
            // Ensure the directory exists
            Path targetLocation = this.fileStorageLocation.resolve(subfolder).resolve(fileName);
            Files.createDirectories(targetLocation.getParent());
            System.out.println(targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setFileType(fileType);
            fileInfo.setFileUrl("resources/" + subfolder + "/" + fileName);

            return fileRepository.save(fileInfo);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public FileInfo getFile(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new RuntimeException("File not found with id " + fileId));
    }

    private String getSubfolder(String fileType) {
        switch (fileType.toLowerCase()) {
            case "audio":
                return "audio";
            case "photo":
                return "photos";
            case "video":
                return "video";
            default:
                return null;
        }
    }

    public void saveFile(FileInfo fileInfo) {
        fileRepository.save(fileInfo);
    }
}
