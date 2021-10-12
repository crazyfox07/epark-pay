package com.bitcom.base.mapper;

import com.bitcom.base.domain.InfoZfbPay;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoZfbPayMapper {
    int deleteByPrimaryKey(Integer paramInteger);

    int insert(InfoZfbPay paramInfoZfbPay);

    int insertSelective(InfoZfbPay paramInfoZfbPay);

    InfoZfbPay selectByPrimaryKey(Integer paramInteger);

    int updateByPrimaryKeySelective(InfoZfbPay paramInfoZfbPay);

    int updateByPrimaryKey(InfoZfbPay paramInfoZfbPay);

    List<InfoZfbPay> queryZfbPayByOutTradeNo(@Param("outTradeNo") String paramString);

    int updateCallBackZfbPay(InfoZfbPay paramInfoZfbPay);
}



