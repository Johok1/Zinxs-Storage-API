package zinxs.wiki.pagesapi;

public interface PageServiceInterface {






    String newPage(String pageName);
    String getPageName(String pageId);
    String setPageName(String pageId, String pageName);
    String postAccountPageContent(String pageId, String content);
    String getPageContent(String pageId);

}
