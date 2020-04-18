package com.sn.cykb.service;

import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.vo.ChaptersVO;
import com.sn.cykb.vo.CommonVO;

/**
 * @author: songning
 * @date: 2020/3/9 22:57
 */
public interface ChaptersService {

    CommonDTO<ChaptersDTO> directory(CommonVO<ChaptersVO> commonVO) throws Exception;

    CommonDTO<ChaptersDTO> firstChapter(String novelsId) throws Exception;

    CommonDTO<ChaptersDTO> readMore(String novelsId, String chaptersId) throws Exception;
}
