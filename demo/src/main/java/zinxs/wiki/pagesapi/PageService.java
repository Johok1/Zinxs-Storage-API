package zinxs.wiki.pagesapi;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import zinxs.wiki.utils.FilenameValidator;


import java.io.*;
import java.nio.file.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class PageService implements PageServiceInterface{

    @Autowired
    private PageRepository pageRepository;

    private static final Logger logger = Logger.getLogger(PageService.class.getName());

    @Override
    public String newPage(String pageName){

            Page page = new Page();
            page.setPageName(pageName);
            pageRepository.save(page);
            try {
                if (!FilenameValidator.validateStringFilenameUsingRegex(pageName)) {
                    pageName = FilenameValidator.sanitizeFilename(pageName);
                }
                return createPage(pageName, page);
            }catch (FileAlreadyExistsException e){
                logger.severe("Duplicate file detected. Since filenames have their id's attached to them," +
                        " a duplicate file should never be an issue. " + e.getMessage());
                return e.getMessage();
            }

    }

    private String createPage(String pageName, Page page) throws FileAlreadyExistsException {
        String basePath = "/classes/static/pages/";
        String fileName = pageName + page.getId() + ".txt";
        byte[] byteArray = {};
        InputStream input = new ByteArrayInputStream(byteArray);
        String filepath = makeFileAtPathFromInput(basePath, fileName, input);
        page.setFilepath(filepath);


        pageRepository.save(page);

        return String.valueOf(page.getId());
    }

    private String makeFileAtPathFromInput(String basePath, String fileName, InputStream input) throws FileAlreadyExistsException {

        try {

            Path directoryPath = Paths.get(basePath);
            Files.createDirectories(directoryPath);
            Path filePath = directoryPath.resolve(fileName);


            File dir = new File(basePath, fileName);

            if (dir.exists()) {
                throw new FileAlreadyExistsException(dir.getPath() + " already exists!");
            }


            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPageName(String pageId){
        try{
            Page page = pageRepository.findById(Long.valueOf(pageId)).get();
            return page.getPageName();
        }catch (Exception e){
            logger.severe("A user tried to access a page with an invalid id " + e.getMessage());
            return "Invalid page";
        }
    }


    @Override
    public String setPageName(String pageId, String pageName){
        try{


                Page page = pageRepository.findById(Long.valueOf(pageId)).get();

                page.setPageName(pageName);

                pageRepository.save(page);


                return "true";

        }catch (Exception e){
            logger.severe("A user tried to access a page with an invalid id " + e.getMessage());
            return "Invalid page";
        }
    }

    @Override
    public String postAccountPageContent(String pageId, String content) {
        try{

            Page page = pageRepository.findById(Long.valueOf(pageId)).get();

               // page.setPageContent(content);
                pageRepository.save(page);

                File pageFile = new File(page.getFilepath());

                FileWriter writer = new FileWriter(pageFile);
                writer.write(content);
                writer.close();

                return "true";

        }catch (Exception e){
            logger.severe("A user tried to access a page with an invalid id " + e.getMessage());
            return "Invalid page";
        }
    }

    @Override
    public String getPageContent(String pageId){
        try{

            Page page = pageRepository.findById(Long.valueOf(pageId)).get();

            return new String(Files.readAllBytes(Paths.get(page.getFilepath())));

        }catch (Exception e){
            logger.severe("A user tried to access a page with an invalid id " + e.getMessage());
            return "Invalid page";
        }
    }

}
