package com.sn.cykb.repository;

import com.sn.cykb.entity.Chapters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: songning
 * @date: 2020/3/9 23:00
 */
public interface ChaptersRepository extends JpaRepository<Chapters, String> {

    @Query(value = "select * from chapters where novels_id = ?1 order by update_time asc limit 1", nativeQuery = true)
    Chapters findTopByNovelsIdNative(String novelsId);

    @Query(value = "select id as chaptersId, chapter from chapters where novels_id = ?1 order by update_time asc", nativeQuery = true)
    List<Map<String, Object>> findDirectoryNative(String novelsId);

    @Override
    Optional<Chapters> findById(String id);
}
