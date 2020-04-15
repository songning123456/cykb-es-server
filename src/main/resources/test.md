@Query(value = "select * from novels where author like concat('%', ?1, '%') or title like concat('%', ?1, '%') limit 10", nativeQuery = true)
List<Novels> findByAuthorOrTitleNative(String authorOrTitle);

@Query(value = "select * from novels where author like concat('%', ?1, '%') or title like concat('%', ?1, '%') order by create_time desc limit ?2", nativeQuery = true)
List<Novels> findFirstByAuthorOrTitleNative(String authorOrTitle, int size);

@Query(value = "select * from novels where (author like concat('%', ?1, '%') or title like concat('%', ?1, '%')) and create_time < ?2  order by create_time desc limit ?3", nativeQuery = true)
List<Novels> findMoreByAuthorOrTitleNative(String authorOrTitle, Long createTime, int size);

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

@Query(value = "select novels_id as novelsId, count(1) as total from users_novels_relation group by novels_id order by total desc limit 10", nativeQuery = true)
List<Map<String, Object>> countByNovelsIdNative();