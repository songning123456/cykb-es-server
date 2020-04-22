package com.sn.cykb.service;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
public interface NovelsService {

    CommonDTO<NovelsDTO> homePage(CommonVO<NovelsVO> commonVO) throws Exception;

    CommonDTO<NovelsDTO> classifyCount() throws Exception;

    CommonDTO<NovelsDTO> classifyResult(CommonVO<NovelsVO> commonVO) throws Exception;

    CommonDTO<NovelsDTO> countByCategory() throws Exception;

    CommonDTO<NovelsDTO> categoryResult(CommonVO<NovelsVO> commonVO) throws Exception;

    CommonDTO<NovelsDTO> sameAuthor(CommonVO<NovelsVO> commonVO) throws Exception;

    CommonDTO<NovelsDTO> fastSearch(String authorOrTitle) throws Exception;

    CommonDTO<NovelsDTO> searchResult(CommonVO<NovelsVO> commonVO) throws Exception;
}
