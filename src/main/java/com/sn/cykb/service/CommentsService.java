package com.sn.cykb.service;

import com.sn.cykb.dto.CommentsDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.vo.CommentsVO;
import com.sn.cykb.vo.CommonVO;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
public interface CommentsService {

    CommonDTO<CommentsDTO> publishComment(CommonVO<CommentsVO> commonVO) throws Exception;
}
