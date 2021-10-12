package com.bitcom.base.mapper;

import com.bitcom.base.domain.InfoRefundFlow;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRefundFlowMapper {
    int deleteByPrimaryKey(String paramString);

    int insert(InfoRefundFlow paramInfoRefundFlow);

    int insertSelective(InfoRefundFlow paramInfoRefundFlow);

    InfoRefundFlow selectByPrimaryKey(String paramString);

    int updateByPrimaryKeySelective(InfoRefundFlow paramInfoRefundFlow);

    int updateByPrimaryKey(InfoRefundFlow paramInfoRefundFlow);
}



