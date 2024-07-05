package zinxs.wiki.imagesapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findById(Long id);

}

