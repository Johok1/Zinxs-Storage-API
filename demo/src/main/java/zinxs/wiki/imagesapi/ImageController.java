package zinxs.wiki.imagesapi;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(path = "image")

public class ImageController {

    private final ImageServiceInterface imageService;

    public ImageController(ImageServiceInterface imageService){
        this.imageService = imageService;
    }

    @CrossOrigin
    @GetMapping(value = "getPageImage/{pageId}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody Resource getPageImage(@PathVariable String pageId){
        return imageService.getPageImg(pageId);
    }

    @CrossOrigin
    @PostMapping("postPageImage/{pageId}/{filename}")
    public String postPageImage( @PathVariable String pageId,@PathVariable String filename,
                                @RequestParam("file")MultipartFile file){
        return imageService.setPageImg(pageId,filename, file);
    }
    @CrossOrigin
    @GetMapping("getPageImageUrl/{pageId}/{imageId}")
    public Resource getPageImageUrl(@PathVariable String pageId, @PathVariable String imageId){
        return imageService.getPageImageUrl(pageId, imageId);
    }

    @CrossOrigin
    @GetMapping("getPageImageIds/{pageId}")
    public String getPageImageIds(@PathVariable String pageId){
        return imageService.getPageImageIds(pageId);
    }

    @CrossOrigin
    @GetMapping("getImageName/{pageId}/{imageId}")
    public String getImageName(@PathVariable String pageId, @PathVariable String imageId){
        return imageService.getImageName(pageId, imageId);
    }

    @CrossOrigin
    @PostMapping("addPageImageUrl/{pageId}/{filename}")
    public String addPageImageUrl( @PathVariable String pageId,
                                  @PathVariable String filename, @RequestBody MultipartFile file){
        return imageService.addPageImage( pageId,filename, file);
    }

}
