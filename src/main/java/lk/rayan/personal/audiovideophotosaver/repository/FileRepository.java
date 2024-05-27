package lk.rayan.personal.audiovideophotosaver.repository;

import lk.rayan.personal.audiovideophotosaver.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<FileInfo,Long> {
}
