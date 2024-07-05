package zinxs.wiki.pagesapi;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinxs.wiki.accountsapi.Account;
import zinxs.wiki.accountsapi.AccountRepository;
import zinxs.wiki.accountsapi.utilities.AuthTokenUtils;
import zinxs.wiki.imagesapi.Image;
import zinxs.wiki.imagesapi.ImageRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
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
    private AccountRepository accountRepository;

    @Autowired
    private AuthTokenUtils authTokenUtils;

    @Autowired
    private ImageRepository imageRepository;


    @Override
    public String setPageToAccount(String pin, String pageName, String email){
        try{
            if(pin.equals("BUST")){
                pageName = pageName.replaceAll(" ", "_");
                Page page = pageRepository.findByPageName(pageName).get();
                Account account = accountRepository.findByEmail(email).get();
                page.setCreator(account);
                pageRepository.save(page);
                ArrayList<Page> pageList = account.getPages();
                pageList.add(page);
                account.setPages(pageList);
                accountRepository.save(account);
                return "true";
            }else{
                return "0-0";
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String registerPage(String pin, String pageName){
        try{
            if(pin.equals("BUST")) {
                Page page = new Page();
                pageName = pageName.replaceAll(" ", "_");
                page.setPageName(pageName);
               // page.setId(Long.valueOf(pageId));
                ArrayList<Image> pageImages = page.getImageObjs();
                //Creating a File object for directory
                File directoryPath = new File("/classes/static/pages/" + pageName + "/images/");
                //List of all files and directories
                File filesList[] = directoryPath.listFiles();

                for (File file : filesList) {
                    if (file.isDirectory()) {

                    } else {
                        Image image = new Image();
                        image.setFilepath(file.getPath());
                        image.setFilename(file.getName());
                        imageRepository.save(image);
                        pageImages.add(image);


                    }


                }
                page.setImageObjs(pageImages);
                pageRepository.save(page);

                File pagePath = new File("/classes/static/pages/" + pageName + "/");
                File pageFileList[] = pagePath.listFiles();
                for (File file : pageFileList) {
                    if (file.isDirectory()) {

                    } else {
                        page.setFilePath(file.getPath());
                    }
                }

                File pageLogoPath = new File("/classes/static/pages/" + pageName + "/logos/");
                File pageLogoFileList[] = pageLogoPath.listFiles();
                for (File file : pageLogoFileList) {
                    if (file.isDirectory()) {

                    } else {
                        page.setImgFilepath(file.getPath());
                    }
                }

                pageRepository.save(page);
                return page.getId() + "";
            }else{
                return "I'm watching you 0-0";
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String newAccountPage(String token, String pageName){
        try{
            Page page = new Page();
            Account account = getAccount(token);
            page.setCreator(account);
            pageName = pageName.replaceAll(" ", "_");
            page.setPageName(pageName);

            pageRepository.save(page);
            ArrayList<Page> pages = account.getPages();
            String basePath = "/classes/static/pages/"+pageName+"/";
            String fileName = pageName+".txt";
            byte[] byteArray = {};
            InputStream input = new ByteArrayInputStream(byteArray);
            String filepath = makeFileAtPathFromInput(basePath, fileName, input);
            page.setFilePath(filepath);
            pages.add(page);
            account.setPages(pages);
            accountRepository.save(account);
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

    @Override
    public String setPageName(String memberId, String pageId, String pageName){
        try{
            if(isPageCreator(memberId, pageId)){
                Account account = getAccount(memberId);
                Page page = pageRepository.findById(Long.valueOf(pageId)).get();
                pageName = pageName.replaceAll(" ", "_");
                page.setPageName(pageName);
                pageRepository.save(page);
                ArrayList<Page> newPageList = replacePageInList(account.getPages(), pageId, page);
                account.setPages(newPageList);
                accountRepository.save(account);
                return "true";
            }else{
                throw new RuntimeException("Invalid credentials for operation setPageName");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String postAccountPageContent(String wixId, String pageId, String content) {
        try{
            Account account = getAccount(wixId);
            Page page = pageRepository.findById(Long.valueOf(pageId)).get();
            if(account.getId().equals(page.getCreator().getId())) {
               // page.setPageContent(content);
                pageRepository.save(page);

                File pageFile = new File(page.getFilePath());

                FileWriter writer = new FileWriter(pageFile);
                writer.write(content);
                writer.close();
                ArrayList<Page> newPageList = replacePageInList(account.getPages(), pageId, page);
                account.setPages(newPageList);
                accountRepository.save(account);
                return "true";
            }else {
                throw new RuntimeException("Invalid Credentials");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPageContent(String wixId, String pageId){
        try{
            Account account = getAccount(wixId);
            Page page = pageRepository.findById(Long.valueOf(pageId)).get();
            if(page.getCreator().getId().equals(account.getId())) {
                return new String(Files.readAllBytes(Paths.get(page.getFilePath())));
            }else {
                throw new RuntimeException("Invalid Credentials");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private boolean isPageCreator(String memberId, String pageId){
        try{
            Account account = getAccount(memberId);
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
