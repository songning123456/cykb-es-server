package com.sn.cykb.repository;

import com.sn.cykb.entity.Novels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface NovelsRepository extends JpaRepository<Novels, String> {

    @Query(value = "select * from novels order by create_time desc limit ?1", nativeQuery = true)
    List<Novels> findFirstHomePageNative(int size);

    @Query(value = "select * from novels where create_time < ?1 order by create_time desc limit ?2", nativeQuery = true)
    List<Novels> findMoreHomePageNative(Long createTime, int size);

    @Query(value = "select * from novels where source_name = ?1 and category = ?2 order by create_time desc limit ?3", nativeQuery = true)
    List<Novels> findFirstClassifyNative(String sourceName, String category, int size);

    @Query(value = "select * from novels where source_name = ?1 and category = ?2 and create_time < ?3 order by create_time desc limit ?4", nativeQuery = true)
    List<Novels> findMoreClassifyNative(String sourceName, String category, Long createTime, int size);

    @Query(value = "select count(1) as total, source_name as sourceName from novels group by source_name", nativeQuery = true)
    List<Map<String, Object>> countSourceNative();

    @Query(value = "select category, count(1) as categoryTotal from novels where source_name = ?1 group by category", nativeQuery = true)
    List<Map<String, Object>> countCategoryBySourceNative(String sourceName);

    List<Novels> findAllByIdInOrderByUpdateTimeDesc(List<String> novelsIds);

    List<Novels> findByAuthorOrderByCreateTimeDesc(String author);

    @Query(value = "select * from novels where author like concat('%', ?1, '%') or title like concat('%', ?1, '%') limit 10", nativeQuery = true)
    List<Novels> findByAuthorOrTitleNative(String authorOrTitle);

    @Query(value = "select * from novels where author like concat('%', ?1, '%') or title like concat('%', ?1, '%') order by create_time desc limit ?2", nativeQuery = true)
    List<Novels> findFirstByAuthorOrTitleNative(String authorOrTitle, int size);

    @Query(value = "select * from novels where (author like concat('%', ?1, '%') or title like concat('%', ?1, '%')) and create_time < ?2  order by create_time desc limit ?3", nativeQuery = true)
    List<Novels> findMoreByAuthorOrTitleNative(String authorOrTitle, Long createTime, int size);
}
