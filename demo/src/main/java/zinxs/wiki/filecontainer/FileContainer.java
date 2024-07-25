package zinxs.wiki.filecontainer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "files", indexes = {
        @Index(name = "idx_filename", columnList = "filename"),
        @Index(name = "idx_basepath", columnList = "basepath"),
        @Index(name = "idx_filepath", columnList = "filepath")
})
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name = "file_type")
public class FileContainer implements Serializable {
    @SequenceGenerator(
            name = "files_sequence",
            sequenceName = "files_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "files_sequence"
    )
    private Long id;

    private String basePath;

    private String filename;

    private String filepath;
}
