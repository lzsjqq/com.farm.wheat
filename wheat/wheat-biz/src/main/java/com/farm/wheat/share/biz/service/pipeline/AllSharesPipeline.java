package com.farm.wheat.share.biz.service.pipeline;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.dto.SharePriceBaseDTO;
import com.farm.wheat.share.biz.dto.SharePriceDto;
import com.farm.wheat.share.biz.mapper.simple.ShareInfoMapper;
import com.farm.wheat.share.biz.mapper.simple.SharePriceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.List;


/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 21:39
 */
@Service
public class AllSharesPipeline implements Pipeline {

    @Resource
    private ShareInfoMapper shareInfoMapper;


    @Override
    @Transactional
    public void process(ResultItems resultItems, Task task) {
        try {
            String listStr = resultItems.get(ShareConst.SHARE_INFO_BY_TB_FIELD).toString();
            if (StringUtils.isNotBlank(listStr)) {
                List<ShareInfoDto> list = JSONObject.parseArray(listStr, ShareInfoDto.class);
                if (NullCheckUtils.isNotBlank(list)) {
                    for (ShareInfoDto shareInfoDto : list) {
                        shareInfoDto.setCreateBy("sina");
                        shareInfoDto.setUpdateBy("sina");
                        shareInfoMapper.replaceSelective(shareInfoDto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
