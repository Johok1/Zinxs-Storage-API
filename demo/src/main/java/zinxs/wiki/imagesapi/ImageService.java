package zinxs.wiki.imagesapi;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;
import zinxs.wiki.accountsapi.Account;
import zinxs.wiki.accountsapi.AccountRepository;
import zinxs.wiki.accountsapi.utilities.AuthTokenUtils;
import zinxs.wiki.pagesapi.Page;
import zinxs.wiki.pagesapi.PageRepository;

import java.io.File;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ImageService implements ImageServiceInterface{

    //Isn't it interesting how the imageservice class has a
    // dependency on every single repository but the image repository

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AuthTokenUtils authTokenUtils;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PageRepository pageRepository;

    public String getPageImageIds(String pageId){
        try{
            Page page = pageRepository.findById(Long.valueOf(pageId)).get();
            ArrayList<Image> images = page.getImageObjs();
            String ids = "";
            for(Image image: images){
                ids += image.getId() + ",";
            }
            return ids;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource getPageImageUrl(String pageId, String imageId){
        try{
            Page page = pageRepository.findById(Long.valueOf(pageId)).get();

            ArrayList<Image> images = page.getImageObjs();



            for(Image imgObj : images){
                if(imgObj.getId().equals(Long.valueOf(imageId))){
                    Resource imgResource = new UrlResource(new File(imgObj.getFilepath()).toURI());
                    return imgResource;
                }
            }

            throw new Exception("No associated image found for this page");

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getImageName(String pageId, String imageId){
        try{
            Page page = pageRepository.findById(Long.valueOf(pageId)).get();

            ArrayList<Image> images = page.getImageObjs();



            for(Image imgObj : images){
                if(imgObj.getId().equals(Long.valueOf(imageId))){
                    Resource imgResource = new UrlResource(new File(imgObj.getFilepath()).toURI());
                    return imgResource.getFilename();
                }
            }

            throw new Exception("No associated image found for this page");

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addPageImage(String memberId, String pageId, String filename, MultipartFile file){
        try{
            if(isPageCreator(memberId, pageId)){
                Page page = pageRepository.findById(Long.valueOf(pageId)).get();
                Image image = new Image();


                imageRepository.save(image);
                String pageName = page.getPageName().replaceAll(" ", "_");
                String basePath = "/classes/static/pages/" +pageName  + "/images/";

                Path directoryPath = Paths.get(basePath);
                Files.createDirectories(directoryPath);

                Path filePath = directoryPath.resolve(filename);


                File dir = new File(basePath , filename);

                if (dir.exists()) {
                    return "EXIST";
                }

                try {
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }




                ArrayList<Image> imageObjs = page.getImageObjs();



                image.setFilepath(filePath.toString());
                image.setFilename(filename);
                imageObjs.add(image);
                page.setImageObjs(imageObjs);



                pageRepository.save(page);
                imageRepository.save(image);
                return "true";
            }else{
                throw new Exception("Invalid credentials");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource getPageImg(String pageId){
        try {
            Page page  = pageRepository.findById(Long.valueOf(pageId)).get();
            File image =  new File(page.getImgFilepath());
            Resource imageResource = new UrlResource(image.toURI());
            return imageResource;
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
    public String setPageImg(String token, String pageId, String fileName, MultipartFile multipartFile) {
        try {


            if(isPageCreator(token, pageId)){
                Account account = getAccount(token);
                Page page = pageRepository.findById(Long.valueOf(pageId)).get();


                String basePath = "/classes/static/pages/" +page.getPageName() + "/logos/";

                Path directoryPath = Paths.get(basePath);
                Files.createDirectories(directoryPath);
                Path filePath = directoryPath.resolve(fileName);


                File dir = new File(basePath , fileName);

                if (dir.exists()) {
                    return "EXIST";
                }

                try {
                    Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                page.setImgFilepath(filePath.toString());
                pageRepository.save(page);
                ArrayList<Page> newPageList = replacePageInList(account.getPages(), pageId, page);
                account.setPages(newPageList);
                accountRepository.save(account);
                return "CREATED";
            }else{
                throw new RuntimeException("invalid credentials");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "FAILED " + e.getMessage() ;
        }
    }




    private boolean isPageCreator(String token, String pageId){
        try{
            Account account = getAccount(token);
            Page page = pageRepository.findById(Long.valueOf(pageId)).get();
            if(page.getCreator().getId().equals(account.getId())) {
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Account getAccount(String token){
        try{
            String decodedToken = authTokenUtils.decodeEmail(token);
            Account targetAccount = (Account) accountRepository.findByEmail(decodedToken).get();
            if(targetAccount.isEnabled()){
                return targetAccount;
            }else{
                throw new RuntimeException("Account " + decodedToken + " is disabled!");
            }
        }catch (Exception e){
            throw new RuntimeException("getAccount error " + e);
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
