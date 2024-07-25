package zinxs.wiki.filecontainer.pages;

import org.springframework.web.bind.annotation.*;
import zinxs.wiki.filecontainer.pages.PageServiceInterface;

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
    @GetMapping("getAccountPageContent/{pageId}")
    public String getAccountPageContent(@PathVariable String wixId, @PathVariable String pageId){
        return pageService.getPageContent(pageId);
    }
    @CrossOrigin
    @PostMapping("postPageName/{pageId}/{pageName}")
    public String postPageName(@PathVariable String pageId,
                               @PathVariable String pageName){
        return pageService.setPageName(pageId, pageName);
    }

    @CrossOrigin
    @PostMapping("postNewPage/{pageName}")
    public String postNewPage(@PathVariable String pageName){
        return pageService.newPage(pageName);
    }

    @CrossOrigin
    @PostMapping("postAccountPageContent/{pageId}")
    public String postAccountPageContent( @PathVariable String pageId, @RequestBody String content){
        return pageService.postAccountPageContent(pageId, content);
    }



}
