package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.dto.RelationDTO;
import com.sn.cykb.service.RelationService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.RelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:56
 */
@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;

    @AControllerAspect(description = "我的书架")
    @PostMapping("/bookcase")
    public CommonDTO<NovelsDTO> bookcases(@RequestBody CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<NovelsDTO> commonDTO = relationService.bookcase(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "加入书架")
    @PostMapping("/insertBookcase")
    public CommonDTO<RelationDTO> insertBookcases(@RequestBody CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = relationService.insertBookcase(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "将本书置顶")
    @PostMapping("/topBookcase")
    public CommonDTO<RelationDTO> topBookcases(@RequestBody CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = relationService.topBookcase(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "从书架删除本书")
    @PostMapping("/deleteBookcase")
    public CommonDTO<RelationDTO> deleteBookcases(@RequestBody CommonVO<RelationVO> commonVO) throws Exception {
        CommonDTO<RelationDTO> commonDTO = relationService.deleteBookcase(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "大家都在搜")
    @GetMapping("/ourSearch")
    public CommonDTO<NovelsDTO> ourSearches() throws Exception {
        CommonDTO<NovelsDTO> commonDTO = relationService.ourSearch();
        return commonDTO;
    }
}
