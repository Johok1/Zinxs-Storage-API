package zinxs.wiki.imagesapi;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceInterface {





    Resource getPageImageUrl(String pageId, String imageId);

    String getImageName(String pageId, String imageId);

    String addPageImage(String memberId, String pageId, String filename, MultipartFile file);

    Resource getPageImg(String pageId);

    String setPageImg(String token, String pageId, String fileName, MultipartFile multipartFile);

    String getPageImageIds(String pageId);
}
