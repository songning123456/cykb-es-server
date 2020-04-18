package com.sn.cykb.util;

import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.dto.RelationDTO;
import io.searchbox.core.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/4/18 12:11
 */
public class EsConvertUtil {

    public static List<NovelsDTO> novelsEntityConvert(List<SearchResult.Hit<Object, Void>> src) {
        List<NovelsDTO> target = new ArrayList<>();
        NovelsDTO dto;
        for (SearchResult.Hit<Object, Void> objectVoidHit : src) {
            dto = new NovelsDTO();
            dto.setNovelsId(objectVoidHit.id);
            dto.setTitle(((Map) objectVoidHit.source).get("title"));
            dto.setAuthor(((Map) objectVoidHit.source).get("author"));
            dto.setCategory(((Map) objectVoidHit.source).get("category"));
            dto.setIntroduction(((Map) objectVoidHit.source).get("introduction"));
            dto.setLatestChapter(((Map) objectVoidHit.source).get("latestChapter"));
            dto.setCoverUrl(((Map) objectVoidHit.source).get("coverUrl"));
            dto.setSourceUrl(((Map) objectVoidHit.source).get("sourceUrl"));
            dto.setSourceName(((Map) objectVoidHit.source).get("sourceName"));
            dto.setCreateTime(((Map) objectVoidHit.source).get("createTime"));
            dto.setUpdateTime(((Map) objectVoidHit.source).get("updateTime"));
            target.add(dto);
        }
        return target;
    }

    public static List<RelationDTO> relationEntityConvert(List<SearchResult.Hit<Object, Void>> src) {
        List<RelationDTO> target = new ArrayList<>();
        RelationDTO dto;
        for (SearchResult.Hit<Object, Void> item : src) {
            dto = new RelationDTO();
            dto.setRelationId(item.id);
            dto.setNovelsId(((Map) item.source).get("novelsId"));
            dto.setUniqueId(((Map) item.source).get("uniqueId"));
            dto.setUpdateTime(((Map) item.source).get("updateTime"));
            target.add(dto);
        }
        return target;
    }

    public static List<ChaptersDTO> chaptersEntityConvert(List<SearchResult.Hit<Object, Void>> src, boolean includeContent) {
        List<ChaptersDTO> target = new ArrayList<>();
        ChaptersDTO dto;
        for (SearchResult.Hit<Object, Void> item : src) {
            dto = new ChaptersDTO();
            dto.setChaptersId(item.id);
            dto.setChapter(((Map) item.source).get("chapter"));
            if (includeContent) {
                dto.setContent(((Map) item.source).get("content"));
            }
            dto.setNovelsId(((Map) item.source).get("novelsId"));
            dto.setUpdateTime(((Map) item.source).get("updateTime"));
            target.add(dto);
        }
        return target;
    }

    public static List<Map<String, Object>> chaptersMapConvert(List<SearchResult.Hit<Object, Void>> src, boolean includeContent) {
        List<Map<String, Object>> target = new ArrayList<>();
        for (SearchResult.Hit<Object, Void> item : src) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("chaptersId", item.id);
            map.put("chapter", ((Map) item.source).get("chapter"));
            if (includeContent) {
                map.put("content", ((Map) item.source).get("content"));
            }
            map.put("novelsId", ((Map) item.source).get("novelsId"));
            map.put("updateTime", ((Map) item.source).get("updateTime"));
            target.add(map);
        }
        return target;
    }

    public static ChaptersDTO chaptersMapConvert(Map<String, Object> src) {
        ChaptersDTO chaptersDTO = new ChaptersDTO();
        chaptersDTO.setChaptersId(src.get("es_metadata_id"));
        chaptersDTO.setChapter(src.get("chapter"));
        chaptersDTO.setNovelsId(src.get("novelsId"));
        chaptersDTO.setContent(src.get("content"));
        chaptersDTO.setUpdateTime(src.get("updateTime"));
        return chaptersDTO;
    }
}
