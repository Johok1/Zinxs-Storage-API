package zinxs.wiki.pagesapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface PageRepository extends JpaRepository<Page, Long> {

    Optional<Page> findByPageName(String pageName);

    Optional<Page> findById(Long id);

}
