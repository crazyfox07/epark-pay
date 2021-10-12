package com.bitcom.base.mapper;

import com.bitcom.base.domain.InfoWxPay;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface InfoWxPayMapper {
    int deleteByPrimaryKey(Integer paramInteger);

    int insert(InfoWxPay paramInfoWxPay);

    int insertSelective(InfoWxPay paramInfoWxPay);

    InfoWxPay selectByPrimaryKey(Integer paramInteger);

    int updateByPrimaryKeySelective(InfoWxPay paramInfoWxPay);

    int updateByPrimaryKey(InfoWxPay paramInfoWxPay);

    List<InfoWxPay> queryWxPayByOutTradeNo(String paramString);

    int updateCallBackWxPay(InfoWxPay paramInfoWxPay);
}



