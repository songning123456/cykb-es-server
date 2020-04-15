List<Chapters> findByChapterAndNovelsId(String chapter, String novelsId);

@Query(value = "select * from chapters where novels_id = ?1 order by update_time asc limit 1", nativeQuery = true)
Chapters findTopByNovelsIdNative(String novelsId);

@Query(value = "select id as chaptersId, chapter from chapters where novels_id = ?1 order by update_time asc", nativeQuery = true)
List<Map<String, Object>> findDirectoryNative(String novelsId);

@Override
Optional<Chapters> findById(String id);

List<Chapters> findByChapterAndNovelsId(String chapter, String novelsId);

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

Users findByUniqueId(String uniqueId);

@Modifying
@Transactional
@Query(value = "update users set avatar=:#{#entity.avatar}, nick_name=:#{#entity.nickName}, gender=:#{#entity.gender},update_time=:#{#entity.updateTime} where unique_id=:#{#entity.uniqueId}", nativeQuery = true)
void updateNative(@Param("entity") Users users);