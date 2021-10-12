package com.bitcom.base.mapper;

import com.bitcom.base.domain.InfoTradeFlow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoTradeFlowMapper {
    int deleteByPrimaryKey(String paramString);

    int insert(InfoTradeFlow paramInfoTradeFlow);

    int insertSelective(InfoTradeFlow paramInfoTradeFlow);

    InfoTradeFlow selectByPrimaryKey(String paramString);

    int updateByPrimaryKeySelective(InfoTradeFlow paramInfoTradeFlow);

    int updateByPrimaryKey(InfoTradeFlow paramInfoTradeFlow);

    InfoTradeFlow queryTradeFlowByOutTradeNo(@Param("outTradeNo") String paramString);
}



