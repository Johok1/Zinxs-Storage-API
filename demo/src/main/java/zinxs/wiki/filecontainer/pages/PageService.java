package zinxs.wiki.filecontainer.pages;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import zinxs.wiki.filecontainer.FileContainerService;
import java.io.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class PageService implements PageServiceInterface {

   @Autowired
   private FileContainerService fileContainerService;

    private static final Logger logger = Logger.getLogger(PageService.class.getName());

    @Override
    public String newPage(String pageName){

        try {
            Page page = (Page) fileContainerService.newFileFromNameAndPathAndExtension(pageName, "classes/static/pages/", ".txt");
            page.setPageName(pageName);
            fileContainerService.getFileContainerRepository().save(page);
            return page.getId() +"";
        }catch (IOException e){
            logger.severe(e.getMessage());
            return e.getMessage();
        }


    }



    @Override
    public String getPageName(String pageId){
        try{
            Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();


            return page.getPageName();
        }catch (Exception e){
            logger.severe("A user tried to access a page with an invalid id " + e.getMessage());
            return "Invalid page";
        }
    }


    @Override
    public String setPageName(String pageId, String pageName){
        try{


                Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();

                page.setPageName(pageName);

                fileContainerService.getFileContainerRepository().save(page);


                return "true";

        }catch (Exception e){
            logger.severe("A user tried to access a page with an invalid id " + e.getMessage());
            return "Invalid page";
        }
    }

    @Override
    public String postAccountPageContent(String pageId, String content) {
        try{

            Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();
            fileContainerService.setFileContentAsString(page,content);

            return "true";

        }catch (IOException e){
            logger.severe(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String getPageContent(String pageId){
        try{

            Page page = (Page) fileContainerService.getFileContainerRepository().findById(Long.valueOf(pageId)).get();
            return fileContainerService.getFileContentAsString(page);

        }catch (Exception e){
            logger.severe("A user tried to access a page with an invalid id " + e.getMessage());
            return "Invalid page";
        }
    }

}
