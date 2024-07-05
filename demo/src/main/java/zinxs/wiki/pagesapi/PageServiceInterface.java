package zinxs.wiki.pagesapi;

public interface PageServiceInterface {


    String setPageToAccount(String pin, String pageId, String email);

    String registerPage(String pin, String pageId);

    String newAccountPage(String wixId, String pageName);
    String getPageName(String pageId);
    String setPageName(String memberId, String pageId, String pageName);
    String postAccountPageContent(String wixId, String pageId, String content);
    String getPageContent(String wixId, String pageId);

}
