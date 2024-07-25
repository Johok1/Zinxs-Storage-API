package zinxs.wiki.filecontainer.images;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageServiceInterface {





    Resource getPageImageUrl(String imageId);

    String getImageName( String imageId);

    String addPageImage(String pageId, String filename, MultipartFile file);

    Resource getPageImg(String imageId);

    String setPageImg(String pageId, String fileName, MultipartFile multipartFile);

    String getPageImageIds(String pageId);
}
