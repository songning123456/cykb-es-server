package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.service.NovelsService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:55
 */
@Slf4j
@RestController
@RequestMapping("/novels")
public class NovelsController {

    @Autowired
    private NovelsService novelsService;

    @AControllerAspect(description = "首页查询小说")
    @PostMapping("/homePage")
    public CommonDTO<NovelsDTO> homePages(@RequestBody CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = novelsService.homePage(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "分类统计小说总数")
    @GetMapping("/classifyCount")
    public CommonDTO<NovelsDTO> classifyCounts() throws Exception {
        CommonDTO<NovelsDTO> commonDTO = novelsService.classifyCount();
        return commonDTO;
    }

    @AControllerAspect(description = "分类查询小说")
    @PostMapping("/classifyResult")
    public CommonDTO<NovelsDTO> classifyResults(@RequestBody CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = novelsService.classifyResult(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "作者也写过")
    @PostMapping("/sameAuthor")
    public CommonDTO<NovelsDTO> sameAuthors(@RequestBody CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = novelsService.sameAuthor(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "快速搜索")
    @GetMapping("/fastSearch")
    public CommonDTO<NovelsDTO> fastSearches(@RequestParam(value = "authorOrTitle") String authorOrTitle) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = novelsService.fastSearch(authorOrTitle);
        return commonDTO;
    }

    @AControllerAspect(description = "搜索结果")
    @PostMapping("/searchResult")
    public CommonDTO<NovelsDTO> searchResults(@RequestBody CommonVO<NovelsVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = novelsService.searchResult(commonVO);
        return commonDTO;
    }
}
