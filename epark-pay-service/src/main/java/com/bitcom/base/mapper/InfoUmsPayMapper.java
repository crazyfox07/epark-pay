package com.bitcom.base.mapper;

import com.bitcom.base.domain.InfoUmsPay;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoUmsPayMapper {
    int deleteByPrimaryKey(Integer paramInteger);

    int insert(InfoUmsPay paramInfoUmsPay);

    int insertSelective(InfoUmsPay paramInfoUmsPay);

    InfoUmsPay selectByPrimaryKey(Integer paramInteger);

    int updateByPrimaryKeySelective(InfoUmsPay paramInfoUmsPay);

    int updateByPrimaryKey(InfoUmsPay paramInfoUmsPay);

    InfoUmsPay getUmsPayByMerOrderId(@Param("merOrderId") String paramString);

    int updateCallbackPay(InfoUmsPay paramInfoUmsPay);
}



