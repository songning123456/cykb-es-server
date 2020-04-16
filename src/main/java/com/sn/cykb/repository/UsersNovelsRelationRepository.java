package com.sn.cykb.repository;

import com.sn.cykb.entity.UsersNovelsRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface UsersNovelsRelationRepository extends JpaRepository<UsersNovelsRelation, String> {

    @Query(value = "select novels_id from users_novels_relation where unique_id = ?1 order by update_time desc", nativeQuery = true)
    List<String> findByUniqueIdNative(String uniqueId);

    @Modifying
    @Transactional
    @Query(value = "update users_novels_relation set update_time = ?3 where unique_id = ?1 and novels_id = ?2", nativeQuery = true)
    int updateByRecentReadNative(String uniqueId, String novelsId, Date updateTime);

    @Modifying
    @Transactional
    @Query(value = "delete from users_novels_relation where unique_id = ?1 and novels_id in (?2)", nativeQuery = true)
    int deleteInNative(String uniqueId, List<String> novelsIdList);

    UsersNovelsRelation findByUniqueIdAndAndNovelsId(String uniqueId, String novelsId);

    @Query(value = "select novels_id as novelsId, count(1) as total from users_novels_relation group by novels_id order by total desc limit 10", nativeQuery = true)
    List<Map<String, Object>> countByNovelsIdNative();
}
