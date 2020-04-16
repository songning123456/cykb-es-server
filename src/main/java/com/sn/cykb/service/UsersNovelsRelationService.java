package com.sn.cykb.service;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.dto.UsersNovelsRelationDTO;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersNovelsRelationVO;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
public interface UsersNovelsRelationService {

    CommonDTO<UsersNovelsRelationDTO> bookcase(CommonVO<UsersNovelsRelationVO> commonVO);

    CommonDTO<UsersNovelsRelationDTO> insertBookcase(CommonVO<UsersNovelsRelationVO> commonVO);

    CommonDTO<UsersNovelsRelationDTO> topBookcase(CommonVO<UsersNovelsRelationVO> commonVO);

    CommonDTO<UsersNovelsRelationDTO> deleteBookcase(CommonVO<UsersNovelsRelationVO> commonVO);

    CommonDTO<UsersNovelsRelationDTO> isExist(CommonVO<UsersNovelsRelationVO> commonVO);

    CommonDTO<NovelsDTO> ourSearch();
}
