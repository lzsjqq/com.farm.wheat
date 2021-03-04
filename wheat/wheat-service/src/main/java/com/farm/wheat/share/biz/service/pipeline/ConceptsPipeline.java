package com.farm.wheat.share.biz.service.pipeline;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.mapper.simple.ShareConceptInfoMapper;
import com.farm.wheat.share.biz.mapper.simple.ShareConceptPriceMapper;
import com.farm.wheat.share.biz.dto.ShareConceptInfoDTO;
import com.farm.wheat.share.biz.dto.ShareConceptPriceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @create: 2019-10-26 00:41
 */
@Service
public class ConceptsPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(ConceptsPipeline.class);

    @Resource
    private ShareConceptInfoMapper shareConceptInfoMapper;

    @Resource
    private ShareConceptPriceMapper shareConceptPriceMapper;

    @Transactional
    @Override
    public void process(ResultItems resultItems, Task task) {

        String conceptInfos = resultItems.get(ShareConst.CONCEPT_INFO_LIST).toString();
        String conceptPrices = resultItems.get(ShareConst.CONCEPT_PRICE_LIST).toString();

        if (NullCheckUtils.isNotBlank(conceptInfos)) {
            List<ShareConceptInfoDTO> list = JSONObject.parseArray(conceptInfos, ShareConceptInfoDTO.class);
            shareConceptInfoMapper.replaceList(list);
        }
        if (NullCheckUtils.isNotBlank(conceptPrices)) {
            List<ShareConceptPriceDTO> list = JSONObject.parseArray(conceptPrices, ShareConceptPriceDTO.class);
            shareConceptPriceMapper.replaceList(list);
        }

    }
}
