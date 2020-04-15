@Query(value = "select count(1) as total, source_name as sourceName from novels group by source_name", nativeQuery = true)
List<Map<String, Object>> countSourceNative();

@Query(value = "select category, count(1) as categoryTotal from novels where source_name = ?1 group by category", nativeQuery = true)
List<Map<String, Object>> countCategoryBySourceNative(String sourceName);

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