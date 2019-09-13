package com.farm.wheat.share.biz.service.pipeline;

import com.alibaba.fastjson.JSONObject;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.dto.SharePriceBaseDTO;
import com.farm.wheat.share.biz.dto.SharePriceDto;
import com.farm.wheat.share.biz.mapper.simple.ShareInfoMapper;
import com.farm.wheat.share.biz.mapper.simple.SharePriceMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 21:39
 */
@Service
public class SharePricePipeline implements Pipeline {

    @Resource
    private ShareInfoMapper shareInfoMapper;
    @Resource
    private SharePriceMapper sharePriceMapper;

    @Override
    @Transactional
    public void process(ResultItems resultItems, Task task) {
        try {
            if (resultItems == null) {
                return;
            }
            String listStr = resultItems.get(ShareConst.SHARE_NEWEST_BY_TB_FIELD).toString();
            if (StringUtils.isNotBlank(listStr)) {
                List<SharePriceBaseDTO> list = JSONObject.parseArray(listStr, SharePriceBaseDTO.class);
                for (SharePriceBaseDTO sharePriceBaseDTO : list) {
                    String sharePriceBaseDTOStr = JSONObject.toJSONString(sharePriceBaseDTO);
                    ShareInfoDto shareInfoDto = JSONObject.parseObject(sharePriceBaseDTOStr, ShareInfoDto.class);
                    String shareCode = shareInfoDto.getShareCode();
                    Integer integer = map.get(shareCode);
                    ShareInfoDto selectOne;
                    if(integer==null){
                        selectOne= shareInfoMapper.selectByShareCode(shareCode);
                        // 数据不一致插入数据
                        if (selectOne == null) {
                            shareInfoDto.setCreateBy(ShareConst.SOURCE_TX);
                            shareInfoDto.setUpdateBy(ShareConst.SOURCE_TX);
                            shareInfoMapper.insertSelective(shareInfoDto);
                        }
                        map.put(shareCode,selectOne.getIdShareInfo());
                    }else {
                        selectOne=new ShareInfoDto();
                        selectOne.setIdShareInfo(integer);
                    }
                    
                    SharePriceDto sharePriceDto = JSONObject.parseObject(sharePriceBaseDTOStr, SharePriceDto.class);
                    sharePriceDto.setIdShareInfo(selectOne == null ? shareInfoDto.getIdShareInfo() : selectOne.getIdShareInfo());
                    // 检验是否已插入
                    sharePriceMapper.insertSelective(sharePriceDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Map<String, Integer> map = new HashMap<>();
}
