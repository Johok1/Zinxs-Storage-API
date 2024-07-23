package zinxs.wiki.pagesapi;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zinxs.wiki.imagesapi.Image;
import zinxs.wiki.imagesapi.ImageRepository;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class PageService implements PageServiceInterface{

    @Autowired
    private PageRepository pageRepository;



    @Autowired
    private ImageRepository imageRepository;






    /*
          Before, we made a page and automatically associated it with an account, now we will make a page,
          and send the id to the frontend, and it can pass it into the account api to validate it. In the future, for security,
          we can have this endpoint take something like an account token, and use it to create another token that stores the
          page and account together, so then when its set to the page and it can tell that it came from that user and not someone else,
          using this system to mess with peoples page ownership.

     */
    @Override
    public String newPage(String pageName){
        try{
            Page page = new Page();



            page.setPageName(pageName);
            pageName = pageName.replaceAll(" ", "_");


            String basePath = "/classes/static/pages/"+pageName+"/";
            String fileName = pageName+".txt";
            byte[] byteArray = {};
            InputStream input = new ByteArrayInputStream(byteArray);
            String filepath = makeFileAtPathFromInput(basePath, fileName, input);
            page.setFilepath(filepath);


            pageRepository.save(page);

            return String.valueOf(page.getId());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String makeFileAtPathFromInput(String basePath, String fileName, InputStream input){

        try {

            Path directoryPath = Paths.get(basePath);
            Files.createDirectories(directoryPath);
            Path filePath = directoryPath.resolve(fileName);


            File dir = new File(basePath, fileName);

            if (dir.exists()) {
                return "EXIST";
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
            throw new RuntimeException(e);
        }
    }

    /*
           Before we used a method "isPageCreator", in the future we can use some method of encoding the account
           information into a token that we pass, that we then decode to get a string that identifies the account in some way,
           that we then check against something stored inside our page object that indicates the identifier of its owner.

     */
    @Override
    public String setPageName(String pageId, String pageName){
        try{


                Page page = pageRepository.findById(Long.valueOf(pageId)).get();

                page.setPageName(pageName);

                pageRepository.save(page);


                return "true";

        }catch (Exception e){
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPageContent(String pageId){
        try{

            Page page = pageRepository.findById(Long.valueOf(pageId)).get();

            return new String(Files.readAllBytes(Paths.get(page.getFilepath())));

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }





    private ArrayList<Page> replacePageInList(ArrayList<Page> pages, String replaceId, Page replaceWith){
        for(int x = 0; x<pages.size(); x++){
            if(pages.get(x).getId().equals(Long.valueOf(replaceId))){
                pages.set(x,replaceWith);
            }
        }
        return pages;
    }
}
