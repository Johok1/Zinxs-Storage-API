package zinxs.wiki.imagesapi;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "images", indexes = {
        @Index(name = "idx_filename", columnList = "filename"),
        @Index(name = "idx_filepath", columnList = "filepath")
})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Image implements Serializable {
    @SequenceGenerator(
            name = "image_sequence",
            sequenceName = "image_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_sequence"
    )
    private Long id;

    private String filename;

    private String filepath;
}
