package com.sn.cykb.service;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.dto.RelationDTO;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.RelationVO;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
public interface RelationService {

    CommonDTO<RelationDTO> bookcase(CommonVO<RelationVO> commonVO) throws Exception;

    CommonDTO<RelationDTO> insertBookcase(CommonVO<RelationVO> commonVO) throws Exception;

    CommonDTO<RelationDTO> topBookcase(CommonVO<RelationVO> commonVO) throws Exception;

    CommonDTO<RelationDTO> deleteBookcase(CommonVO<RelationVO> commonVO) throws Exception;

    CommonDTO<RelationDTO> isExist(CommonVO<RelationVO> commonVO) throws Exception;

    CommonDTO<NovelsDTO> ourSearch() throws Exception;
}
