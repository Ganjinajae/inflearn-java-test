package me.youngjin.infleranjavatest.study;

import me.youngjin.infleranjavatest.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
