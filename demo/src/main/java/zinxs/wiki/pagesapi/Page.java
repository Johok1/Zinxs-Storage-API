package zinxs.wiki.pagesapi;

import lombok.Getter;
import lombok.Setter;


import zinxs.wiki.imagesapi.Image;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@Table(name = "pages", indexes = {
        @Index(name = "idx_filepath", columnList = "filepath"),
        @Index(name = "idx_pageName", columnList = "pageName"),
        @Index(name = "idx_imgFilepath", columnList = "imgFilepath")
})
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

    private String imgFilepath;

    private String filepath;

    private String pageName;

    //Unidirectional relation
    @OneToMany(mappedBy = "pages", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArrayList<Image> imageObjs;

    public Page(){
        this.imageObjs = new ArrayList<>();
    }
}
