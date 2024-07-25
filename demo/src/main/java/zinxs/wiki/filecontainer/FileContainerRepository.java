package zinxs.wiki.filecontainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface FileContainerRepository extends JpaRepository<FileContainer, Long> {

    Optional<FileContainer> findById(Long id);

}