package com.farm.wheat.share.api.vo.request;

import lombok.Data;

/**
 * @description:
 * @author: xyc
 * @create: 2019-09-08 20:45
 */
@Data
public class SharesReq extends BaseReq {
    private String shareCode;
    private String shareName;

}
