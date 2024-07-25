package zinxs.wiki.filecontainer.pages;

import lombok.Getter;
import lombok.Setter;


import zinxs.wiki.filecontainer.FileContainer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pages", indexes = {
        @Index(name = "idx_pageName", columnList = "pageName"),
        @Index(name = "idx_pageImage_id", columnList = "pageImage_id")
})
@DiscriminatorValue("page")
public class Page extends FileContainer {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pageImage_id")
    private FileContainer pageImage;

    private String pageName;

    @ElementCollection
    @CollectionTable(name = "page_image_container_ids", joinColumns = @JoinColumn(name = "page_id"))
    @Column(name = "image_container_id")
    private List<String> imageContainerIds;

    public Page(){
        this.imageContainerIds = new ArrayList<>();
    }
}
