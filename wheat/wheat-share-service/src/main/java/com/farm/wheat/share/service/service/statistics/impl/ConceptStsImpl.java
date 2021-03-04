package com.farm.wheat.share.service.service.statistics.impl;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.service.dto.*;
import com.farm.wheat.share.service.mapper.max.ConceptStsMapper;
import com.farm.wheat.share.service.mapper.simple.ShareConceptDetailMapper;
import com.farm.wheat.share.service.mapper.simple.ShareConceptInfoMapper;
import com.farm.wheat.share.service.mapper.simple.ShareConceptStsCountMapper;
import com.farm.wheat.share.service.mapper.simple.ShareInfoMapper;
import com.farm.wheat.share.service.service.statistics.IConceptSts;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @description:
 * @author: xyc
 * @create: 2019-10-27 21:30
 */
@Service
public class ConceptStsImpl implements IConceptSts {

    @Resource
    private ShareInfoMapper shareInfoMapper;
    @Resource
    private ConceptStsMapper conceptStatisticsMapper;
    @Resource
    private ShareConceptInfoMapper shareConceptInfoMapper;
    @Resource
    private ShareConceptDetailMapper shareConceptDetailMapper;
    @Resource
    private ShareConceptStsCountMapper shareConceptStsCountMapper;


    @Override
    public void stsTopRatio(String date) throws Exception {
        Date target = DateUtils.stringToDate(date, DateUtils.YYYY_MM_DD);
        // 查询所有的概念
        List<ShareConceptInfoDTO> shareConceptInfoDTOS = shareConceptInfoMapper.selectAll();
        if (NullCheckUtils.isNotBlank(shareConceptInfoDTOS)) {
            for (ShareConceptInfoDTO shareConceptInfoDTO : shareConceptInfoDTOS) {
                String simpleName = shareConceptInfoDTO.getSimpleName();
                String conceptName = shareConceptInfoDTO.getConceptName();
                Integer number = shareConceptInfoDTO.getNumber();
                Map<Integer, List<ConceptPriceDTO>> map = new HashMap<>();
                List<ConceptPriceDTO> conceptPriceDTOS = shareConceptDetailMapper.selectBySimpleName(simpleName, target);
                for (ConceptPriceDTO conceptPriceDTO : conceptPriceDTOS) {
                    BigDecimal priceChangeRatio = conceptPriceDTO.getPriceChangeRatio();
                    int key = (int) priceChangeRatio.doubleValue();
                    List<ConceptPriceDTO> value = map.get(key);
                    if (value == null) {
                        value = new ArrayList<>();
                    }
                    value.add(conceptPriceDTO);
                    map.put(key, value);
                }
                ShareConceptStsCountDTO record = new ShareConceptStsCountDTO();
                record.setSimpleName(simpleName);
                record.setConceptName(conceptName);
                record.setTradingDate(target);
                record.setCount(number);
                record.setDetail(JSONObject.toJSONString(mapToList(map)));
                shareConceptStsCountMapper.insert(record);
            }
        }

    }

    private List<ConceptStsDTO> mapToList(Map<Integer, List<ConceptPriceDTO>> map) {
        List<ConceptStsDTO> list = new ArrayList<>();
        ConceptStsDTO dto;
        Set<Map.Entry<Integer, List<ConceptPriceDTO>>> entries = map.entrySet();
        for (Map.Entry<Integer, List<ConceptPriceDTO>> entry : entries) {
            Integer key = entry.getKey();
            List<ConceptPriceDTO> value = entry.getValue();
            dto = new ConceptStsDTO();
            dto.setCount(value.size());
            dto.setType(String.valueOf(key));
            dto.setDetail(value);
            list.add(dto);

        }
        return list;
    }

}
