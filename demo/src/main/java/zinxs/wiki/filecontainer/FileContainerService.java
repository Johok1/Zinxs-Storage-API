package zinxs.wiki.filecontainer;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import zinxs.wiki.filecontainer.pages.Page;
import zinxs.wiki.filecontainer.utils.FilenameValidator;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class FileContainerService {

    private static final Logger logger = Logger.getLogger(FileContainerService.class.getName());

    @Autowired
    private final FileContainerRepository fileContainerRepository;

    public FileContainerRepository getFileContainerRepository(){
        return this.fileContainerRepository;
    }

    /*
     * filename = filename + id
     * filepath = basepath + filename
     */
    public FileContainer newFileFromNameAndPathAndExtension(String fileName, String basePath, String extension) throws IOException {

        FileContainer file = new FileContainer();
        fileContainerRepository.save(file);
        if (!FilenameValidator.validateStringFilenameUsingRegex(fileName)) {
            fileName = FilenameValidator.sanitizeFilename(fileName);
        }


        file.setFilename(fileName + file.getId() + extension);
        file.setFilepath(basePath+fileName);
        fileContainerRepository.save(file);
        createFileAtPath(file);
        return file;

    }

    public String getFileContentAsString(FileContainer file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getFilepath())));
    }

    public void setFileContentAsInputStream(FileContainer fileContainer, InputStream content) throws IOException {
        Files.copy(content, Paths.get(fileContainer.getFilepath()), StandardCopyOption.REPLACE_EXISTING);
        fileContainerRepository.save(fileContainer);
    }

    public Resource getFileContentAsResource(FileContainer fileContainer) throws MalformedURLException {
        File file =  new File(fileContainer.getFilepath());
        Resource resource = new UrlResource(file.toURI());
        return resource;
    }

    public void setFileContentAsString(FileContainer fileContainer, String content) throws IOException {
        File file = new File(fileContainer.getFilepath());
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
        fileContainerRepository.save(fileContainer);
    }

    private void createFileAtPath(FileContainer file) throws IOException {


        Path directoryPath = Paths.get(file.getFilepath());
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Create the complete file path
        Path filePath = directoryPath.resolve(file.getFilename());

        // Check if the file already exists
        if (Files.exists(filePath)) {
            throw new FileAlreadyExistsException(filePath.toString() + " already exists!");
        }

        // Create the file (this will not actually create the file's content, only the empty file)
        Files.createFile(filePath);

    }


}
