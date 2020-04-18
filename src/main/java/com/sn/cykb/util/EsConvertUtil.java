package com.sn.cykb.util;

import com.sn.cykb.dto.NovelsDTO;
import io.searchbox.core.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/4/18 12:11
 */
public class EsConvertUtil {

    public static List<NovelsDTO> novelsAllFieldConvert(List<SearchResult.Hit<Object, Void>> src) {
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
}
