package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.service.ChaptersService;
import com.sn.cykb.vo.ChaptersVO;
import com.sn.cykb.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:55
 */
@RestController
@RequestMapping("/chapters")
public class ChaptersController {

    @Autowired
    private ChaptersService chaptersService;

    @AControllerAspect(description = "获取目录")
    @PostMapping("/directory")
    public CommonDTO<ChaptersDTO> directorys(@RequestBody CommonVO<ChaptersVO> commonVO) {
        CommonDTO<ChaptersDTO> commonDTO = chaptersService.directory(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "从未阅读过/删除浏览器缓存 每次都从第一章开始")
    @GetMapping("/firstChapter")
    public CommonDTO<ChaptersDTO> firstChapters(@RequestParam(value = "novelsId") String novelsId) {
        CommonDTO<ChaptersDTO> commonDTO = chaptersService.firstChapter(novelsId);
        return commonDTO;
    }

    @AControllerAspect(description = "根据浏览器缓存的chaptersId 阅读")
    @GetMapping("/readMore")
    public CommonDTO<ChaptersDTO> readMores(@RequestParam(value = "novelsId") String novelsId, @RequestParam(value = "chaptersId") String chaptersId) {
        CommonDTO<ChaptersDTO> commonDTO = chaptersService.readMore(novelsId, chaptersId);
        return commonDTO;
    }
}
