package com.bitcom.base.mapper;

import com.bitcom.base.domain.InfoSwiftcloudPay;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoSwiftcloudPayMapper {
    int deleteByPrimaryKey(Integer paramInteger);

    int insert(InfoSwiftcloudPay paramInfoSwiftcloudPay);

    int insertSelective(InfoSwiftcloudPay paramInfoSwiftcloudPay);

    InfoSwiftcloudPay selectByPrimaryKey(Integer paramInteger);

    int updateByPrimaryKeySelective(InfoSwiftcloudPay paramInfoSwiftcloudPay);

    int updateByPrimaryKey(InfoSwiftcloudPay paramInfoSwiftcloudPay);

    List<InfoSwiftcloudPay> queryPayInfoByOutTradeNo(@Param("outTradeNo") String paramString);

    int updateCallBackPayInfo(InfoSwiftcloudPay paramInfoSwiftcloudPay);
}



