package zinxs.wiki.pagesapi;

import lombok.Getter;
import lombok.Setter;
import zinxs.wiki.accountsapi.Account;
import zinxs.wiki.imagesapi.Image;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class Page implements Serializable {
    @SequenceGenerator(
            name = "page_sequence",
            sequenceName = "page_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "page_sequence"
    )
    private Long id;

    private Account creator;

    private String imgFilepath;

    private String email;

    private String filePath;
    private ArrayList<String> bannedAccounts;
    private ArrayList<String> editAccessAccounts;
    private ArrayList<String> internalTags;

    private ArrayList<String> imageContext;

    private ArrayList<String> videoContext;

    private ArrayList<Image> imageObjs;

    private boolean status;
    private String pageName;

    public Page(){
        this.bannedAccounts = new ArrayList<>();
        this.editAccessAccounts = new ArrayList<>();
        this.internalTags = new ArrayList<>();
        this.status = true;
        this.imageContext = new ArrayList<>();
        this.videoContext = new ArrayList<>();
        this.imageObjs = new ArrayList<>();
    }
}
