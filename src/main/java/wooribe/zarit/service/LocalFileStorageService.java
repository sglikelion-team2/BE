package wooribe.zarit.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService {
    private final Path root = Paths.get("zarit/project_images"); // 파일을 저장할 기본 경로

    public String saveFile(MultipartFile file, String folder) {
        try {
            // 파일명 중복을 피하기 위해 UUID 사용
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Path path = this.root.resolve(folder).resolve(fileName);

            // 디렉토리가 없으면 생성
            Files.createDirectories(path.getParent());

            // 파일 저장
            Files.copy(file.getInputStream(), path);

            // 저장된 파일의 URL 반환
            return "/zarit/project_images/" + folder + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }
}
