package com.bitcom.base.mapper;

import com.bitcom.base.domain.InfoCcbPay;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoCcbPayMapper {
    int deleteByPrimaryKey(Integer paramInteger);

    int insert(InfoCcbPay paramInfoCcbPay);

    int insertSelective(InfoCcbPay infoCcbPay);

    InfoCcbPay selectByPrimaryKey(Integer paramInteger);

    int updateByPrimaryKeySelective(InfoCcbPay paramInfCcbPay);

    int updateByPrimaryKey(InfoCcbPay paramInfoCcbPay);

    List<InfoCcbPay> queryCcbPayByOutTradeNo(String paramString);

    int updateCallBackCcbPay(InfoCcbPay paramInfoCcbPay);
}
