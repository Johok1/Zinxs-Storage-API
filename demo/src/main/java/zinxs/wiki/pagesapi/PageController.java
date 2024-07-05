package zinxs.wiki.pagesapi;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "page")
public class PageController {

    private final PageServiceInterface pageService;

    public PageController(PageServiceInterface pageService){
        this.pageService = pageService;
    }

    @CrossOrigin
    @GetMapping("getPageName/{pageId}")
    public String getPageName(@PathVariable String pageId){
        return pageService.getPageName(pageId);
    }

    @CrossOrigin
    @GetMapping("getAccountPageContent/{wixId}/{pageId}")
    public String getAccountPageContent(@PathVariable String wixId, @PathVariable String pageId){
        return pageService.getPageContent(wixId, pageId);
    }
    @CrossOrigin
    @PostMapping("postPageName/{memberId}/{pageId}/{pageName}")
    public String postPageName(@PathVariable String memberId, @PathVariable String pageId,
                               @PathVariable String pageName){
        return pageService.setPageName(memberId, pageId, pageName);
    }

    @CrossOrigin
    @PostMapping("postNewAccountPage/{wixId}/{pageName}")
    public String postNewAccountPage(@PathVariable String wixId, @PathVariable String pageName){
        return pageService.newAccountPage(wixId, pageName);
    }

    @CrossOrigin
    @PostMapping("postAccountPageContent/{wixId}/{pageId}")
    public String postAccountPageContent(@PathVariable String wixId, @PathVariable String pageId, @RequestBody String content){
        return pageService.postAccountPageContent(wixId, pageId, content);
    }

    @CrossOrigin
    @GetMapping("registerPage/{pin}/{pageId}")
    public String registerPage(@PathVariable String pin, @PathVariable String pageId){
        return pageService.registerPage(pin, pageId);
    }

    @CrossOrigin
    @GetMapping("setPageToAccount/{pin}/{pageId}/{email}")
    public String setPageToAccount(@PathVariable String pin, @PathVariable String pageId, @PathVariable String email){
        return pageService.setPageToAccount(pin, pageId, email);
    }
}
