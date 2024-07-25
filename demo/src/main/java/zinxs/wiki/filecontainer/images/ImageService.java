package zinxs.wiki.filecontainer.images;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import zinxs.wiki.filecontainer.FileContainer;
import zinxs.wiki.filecontainer.FileContainerService;
import zinxs.wiki.filecontainer.pages.Page;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class ImageService implements ImageServiceInterface {

    //Isn't it interesting how the imageservice class has a
    // dependency on every single repository but the image repository

    private static final Logger logger = Logger.getLogger(ImageService.class.getName());

    @Autowired
    private FileContainerService fileContainerService;

    @Override
    public String getPageImageIds(String pageId){
            Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();
            List<String> imageIds = page.getImageContainerIds();
            String ids = "";
            for(String id : imageIds){
                ids += id + ",";
            }
            return ids;
    }

    @Override
    public Resource getPageImageUrl(String imageId){



            FileContainer container = fileContainerService.getFileContainerRepository().findById(Long.valueOf(imageId)).get();

          try {
              Resource resource = fileContainerService.getFileContentAsResource(container);
              return resource;
          }catch (MalformedURLException e){
              logger.severe(e.getMessage());
              throw new RuntimeException(e);
          }




    }

    @Override
    public String getImageName(String imageId){

            FileContainer container = fileContainerService.getFileContainerRepository().findById(Long.valueOf(imageId)).get();
            return container.getFilename();
    }

    @Override
    public String addPageImage(String pageId, String filename, MultipartFile file){


            Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();
            String basePath = page.getBasePath()+"images/";

            try {
                FileContainer imageContainer = fileContainerService.
                        newFileFromNameAndPathAndExtension(filename, basePath, ".webp");
                fileContainerService.setFileContentAsInputStream(imageContainer,file.getInputStream());
                List<String> imageIds = page.getImageContainerIds();
                imageIds.add(imageContainer.getId()+"");
                fileContainerService.getFileContainerRepository().save(page);
                return "true";
            }catch (IOException e){
                logger.severe(e.getMessage());
                return e.getMessage();
            }

    }

    @Override
    public Resource getPageImg(String pageId){
           try {
               Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();
               FileContainer pageImage = page.getPageImage();
               return fileContainerService.getFileContentAsResource(pageImage);
           }catch (MalformedURLException e){
               logger.severe(e.getMessage());
               throw new RuntimeException(e);
           }
    }



    @Override
    public String setPageImg(String pageId, String fileName, MultipartFile multipartFile) {




              Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();


              String basePath = page.getBasePath()+"logos/";
              try {
                  FileContainer pageImage = fileContainerService.newFileFromNameAndPathAndExtension(
                          fileName, basePath, ".webp"
                  );
                  page.setPageImage(pageImage);
                  fileContainerService.getFileContainerRepository().save(pageImage);
                  return "CREATED";
              }catch (IOException e ){
                  logger.severe(e.getMessage());
                  return e.getMessage();
              }


    }



}
