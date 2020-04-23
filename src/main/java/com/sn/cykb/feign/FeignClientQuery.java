package com.sn.cykb.feign;

import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author songning
 * @date 2020/4/23
 * description
 */
@FeignClient(url = "${cykb.timeUrl}", name = "timeFC")
public interface FeignClientQuery {
    @RequestMapping(value = "/chapters/someoneChapter")
    CommonDTO<ChaptersDTO> updateSomeoneChapters(String chaptersId);

    @RequestMapping(value = "/chapters/allChapter")
    CommonDTO<ChaptersDTO> updateAllChapters(String novelsId);
}
