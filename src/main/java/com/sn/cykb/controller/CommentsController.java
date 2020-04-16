package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.CommentsDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.service.CommentsService;
import com.sn.cykb.vo.CommentsVO;
import com.sn.cykb.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: songning
 * @date: 2020/3/9 22:55
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @AControllerAspect(description = "发表意见")
    @PostMapping("/publishComment")
    public CommonDTO<CommentsDTO> publishComments(@RequestBody CommonVO<CommentsVO> commonVO) throws Exception {
        CommonDTO<CommentsDTO> commonDTO = commentsService.publishComment(commonVO);
        return commonDTO;
    }
}
