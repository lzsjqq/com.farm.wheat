package test;

import com.alibaba.fastjson.JSONObject;
import com.farm.wheat.share.chan.util.Linked;
import com.farm.wheat.share.chan.util.LinkedUtil;
import com.farm.wheat.share.chan.util.Price;
import com.farm.wheat.share.chan.util.SharePriceDto;
import org.junit.Test;

import java.util.List;

/**
 * @description:
 * @author: xyc
 * @create: 2020-03-13 22:28
 */
public class Testxx {

    String xx = "[{\"amplitude\":3.67,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68047,\"priceChange\":-0.5,\"priceChangeRatio\":-1.7,\"quarter\":1,\"todayEndPrice\":28.96,\"todayMaxPrice\":29.88,\"todayMinPrice\":28.8,\"todayOpenPrice\":29.5,\"tradingDate\":1546358400000,\"tradingMoney\":9667,\"tradingVolume\":33077,\"turnoverRate\":0.42,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":8.77,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68046,\"priceChange\":-1.85,\"priceChangeRatio\":-6.39,\"quarter\":1,\"todayEndPrice\":27.11,\"todayMaxPrice\":29.15,\"todayMinPrice\":26.61,\"todayOpenPrice\":29.14,\"tradingDate\":1546444800000,\"tradingMoney\":18686,\"tradingVolume\":67816,\"turnoverRate\":0.85,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":5.64,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68045,\"priceChange\":0.79,\"priceChangeRatio\":2.91,\"quarter\":1,\"todayEndPrice\":27.9,\"todayMaxPrice\":30,\"todayMinPrice\":25,\"todayOpenPrice\":27.11,\"tradingDate\":1546531200000,\"tradingMoney\":12943,\"tradingVolume\":46682,\"turnoverRate\":0.59,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.12,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68044,\"priceChange\":1.02,\"priceChangeRatio\":3.66,\"quarter\":1,\"todayEndPrice\":28.92,\"todayMaxPrice\":29,\"todayMinPrice\":28.13,\"todayOpenPrice\":28.18,\"tradingDate\":1546790400000,\"tradingMoney\":10247,\"tradingVolume\":35775,\"turnoverRate\":0.45,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.04,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68043,\"priceChange\":-0.41,\"priceChangeRatio\":-1.42,\"quarter\":1,\"todayEndPrice\":28.51,\"todayMaxPrice\":28.99,\"todayMinPrice\":28.4,\"todayOpenPrice\":28.78,\"tradingDate\":1546876800000,\"tradingMoney\":9695,\"tradingVolume\":33822,\"turnoverRate\":0.42,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":5.16,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68042,\"priceChange\":-0.09,\"priceChangeRatio\":-0.32,\"quarter\":1,\"todayEndPrice\":28.42,\"todayMaxPrice\":29.47,\"todayMinPrice\":28,\"todayOpenPrice\":28.7,\"tradingDate\":1546963200000,\"tradingMoney\":12909,\"tradingVolume\":44747,\"turnoverRate\":0.56,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.92,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68041,\"priceChange\":0.14,\"priceChangeRatio\":0.49,\"quarter\":1,\"todayEndPrice\":28.56,\"todayMaxPrice\":28.91,\"todayMinPrice\":28.08,\"todayOpenPrice\":28.52,\"tradingDate\":1547049600000,\"tradingMoney\":8588,\"tradingVolume\":30126,\"turnoverRate\":0.38,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.27,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68040,\"priceChange\":0.43,\"priceChangeRatio\":1.51,\"quarter\":1,\"todayEndPrice\":28.99,\"todayMaxPrice\":29.3,\"todayMinPrice\":28.08,\"todayOpenPrice\":28.32,\"tradingDate\":1547136000000,\"tradingMoney\":9470,\"tradingVolume\":33049,\"turnoverRate\":0.41,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.93,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68039,\"priceChange\":-0.72,\"priceChangeRatio\":-2.48,\"quarter\":1,\"todayEndPrice\":28.27,\"todayMaxPrice\":29,\"todayMinPrice\":28.15,\"todayOpenPrice\":28.73,\"tradingDate\":1547395200000,\"tradingMoney\":8624,\"tradingVolume\":30433,\"turnoverRate\":0.38,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":5.62,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68038,\"priceChange\":0.58,\"priceChangeRatio\":2.05,\"quarter\":1,\"todayEndPrice\":28.85,\"todayMaxPrice\":29.3,\"todayMinPrice\":27.71,\"todayOpenPrice\":28.12,\"tradingDate\":1547481600000,\"tradingMoney\":19985,\"tradingVolume\":69680,\"turnoverRate\":0.87,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.44,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68037,\"priceChange\":1.21,\"priceChangeRatio\":4.19,\"quarter\":1,\"todayEndPrice\":30.06,\"todayMaxPrice\":30.2,\"todayMinPrice\":28.92,\"todayOpenPrice\":29.18,\"tradingDate\":1547568000000,\"tradingMoney\":24628,\"tradingVolume\":83093,\"turnoverRate\":1.04,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":5.06,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68036,\"priceChange\":0.57,\"priceChangeRatio\":1.9,\"quarter\":1,\"todayEndPrice\":30.63,\"todayMaxPrice\":31.33,\"todayMinPrice\":29.81,\"todayOpenPrice\":29.91,\"tradingDate\":1547654400000,\"tradingMoney\":29916,\"tradingVolume\":97652,\"turnoverRate\":1.23,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.46,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68035,\"priceChange\":0.11,\"priceChangeRatio\":0.36,\"quarter\":1,\"todayEndPrice\":30.74,\"todayMaxPrice\":31.4,\"todayMinPrice\":30.34,\"todayOpenPrice\":30.8,\"tradingDate\":1547740800000,\"tradingMoney\":14541,\"tradingVolume\":47266,\"turnoverRate\":0.59,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.06,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68034,\"priceChange\":0.65,\"priceChangeRatio\":2.11,\"quarter\":1,\"todayEndPrice\":31.39,\"todayMaxPrice\":31.64,\"todayMinPrice\":30.7,\"todayOpenPrice\":30.7,\"tradingDate\":1548000000000,\"tradingMoney\":15537,\"tradingVolume\":49679,\"turnoverRate\":0.62,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.66,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68033,\"priceChange\":-0.74,\"priceChangeRatio\":-2.36,\"quarter\":1,\"todayEndPrice\":30.65,\"todayMaxPrice\":31.65,\"todayMinPrice\":30.5,\"todayOpenPrice\":31.4,\"tradingDate\":1548086400000,\"tradingMoney\":14040,\"tradingVolume\":45572,\"turnoverRate\":0.57,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.23,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68032,\"priceChange\":-0.04,\"priceChangeRatio\":-0.13,\"quarter\":1,\"todayEndPrice\":30.61,\"todayMaxPrice\":31.2,\"todayMinPrice\":30.21,\"todayOpenPrice\":30.93,\"tradingDate\":1548172800000,\"tradingMoney\":9937,\"tradingVolume\":32376,\"turnoverRate\":0.41,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.74,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68031,\"priceChange\":0.54,\"priceChangeRatio\":1.76,\"quarter\":1,\"todayEndPrice\":31.15,\"todayMaxPrice\":31.43,\"todayMinPrice\":30.59,\"todayOpenPrice\":31.09,\"tradingDate\":1548259200000,\"tradingMoney\":12535,\"tradingVolume\":40319,\"turnoverRate\":0.51,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.62,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68030,\"priceChange\":0.5,\"priceChangeRatio\":1.61,\"quarter\":1,\"todayEndPrice\":31.65,\"todayMaxPrice\":32.24,\"todayMinPrice\":30.8,\"todayOpenPrice\":31,\"tradingDate\":1548345600000,\"tradingMoney\":16238,\"tradingVolume\":51234,\"turnoverRate\":0.64,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.38,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68029,\"priceChange\":0.5,\"priceChangeRatio\":1.58,\"quarter\":1,\"todayEndPrice\":32.15,\"todayMaxPrice\":32.55,\"todayMinPrice\":31.48,\"todayOpenPrice\":31.65,\"tradingDate\":1548604800000,\"tradingMoney\":11069,\"tradingVolume\":34459,\"turnoverRate\":0.43,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.89,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68028,\"priceChange\":-0.02,\"priceChangeRatio\":-0.06,\"quarter\":1,\"todayEndPrice\":32.13,\"todayMaxPrice\":32.55,\"todayMinPrice\":31.3,\"todayOpenPrice\":32.23,\"tradingDate\":1548691200000,\"tradingMoney\":11659,\"tradingVolume\":36459,\"turnoverRate\":0.46,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.33,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68027,\"priceChange\":-1.07,\"priceChangeRatio\":-3.33,\"quarter\":1,\"todayEndPrice\":31.06,\"todayMaxPrice\":32.29,\"todayMinPrice\":30.9,\"todayOpenPrice\":31.89,\"tradingDate\":1548777600000,\"tradingMoney\":20707,\"tradingVolume\":66250,\"turnoverRate\":0.83,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":5.15,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68026,\"priceChange\":-0.23,\"priceChangeRatio\":-0.74,\"quarter\":1,\"todayEndPrice\":30.83,\"todayMaxPrice\":31.65,\"todayMinPrice\":30.05,\"todayOpenPrice\":31.27,\"tradingDate\":1548864000000,\"tradingMoney\":24517,\"tradingVolume\":80211,\"turnoverRate\":1.01,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.11,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68025,\"priceChange\":0.52,\"priceChangeRatio\":1.69,\"quarter\":1,\"todayEndPrice\":31.35,\"todayMaxPrice\":31.49,\"todayMinPrice\":30.53,\"todayOpenPrice\":30.8,\"tradingDate\":1548950400000,\"tradingMoney\":14040,\"tradingVolume\":45184,\"turnoverRate\":0.57,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.23,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68024,\"priceChange\":0.35,\"priceChangeRatio\":1.12,\"quarter\":1,\"todayEndPrice\":31.7,\"todayMaxPrice\":31.71,\"todayMinPrice\":31.01,\"todayOpenPrice\":31.3,\"tradingDate\":1549814400000,\"tradingMoney\":28370,\"tradingVolume\":90543,\"turnoverRate\":1.14,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.71,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68023,\"priceChange\":-0.7,\"priceChangeRatio\":-2.21,\"quarter\":1,\"todayEndPrice\":31,\"todayMaxPrice\":31.56,\"todayMinPrice\":30.7,\"todayOpenPrice\":31.56,\"tradingDate\":1549900800000,\"tradingMoney\":27326,\"tradingVolume\":88005,\"turnoverRate\":1.1,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.71,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68022,\"priceChange\":0.56,\"priceChangeRatio\":1.81,\"quarter\":1,\"todayEndPrice\":31.56,\"todayMaxPrice\":31.86,\"todayMinPrice\":31.02,\"todayOpenPrice\":31.06,\"tradingDate\":1549987200000,\"tradingMoney\":23605,\"tradingVolume\":75360,\"turnoverRate\":0.95,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.88,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68021,\"priceChange\":0.94,\"priceChangeRatio\":2.98,\"quarter\":1,\"todayEndPrice\":32.5,\"todayMaxPrice\":32.75,\"todayMinPrice\":31.21,\"todayOpenPrice\":31.45,\"tradingDate\":1550073600000,\"tradingMoney\":24872,\"tradingVolume\":77274,\"turnoverRate\":0.97,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.88,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68020,\"priceChange\":-1.16,\"priceChangeRatio\":-3.57,\"quarter\":1,\"todayEndPrice\":31.34,\"todayMaxPrice\":32.5,\"todayMinPrice\":31.24,\"todayOpenPrice\":32.5,\"tradingDate\":1550160000000,\"tradingMoney\":19240,\"tradingVolume\":60653,\"turnoverRate\":0.76,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.54,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68019,\"priceChange\":1.25,\"priceChangeRatio\":3.99,\"quarter\":1,\"todayEndPrice\":32.59,\"todayMaxPrice\":32.63,\"todayMinPrice\":31.52,\"todayOpenPrice\":31.52,\"tradingDate\":1550419200000,\"tradingMoney\":21375,\"tradingVolume\":66429,\"turnoverRate\":0.83,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":6.6,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68018,\"priceChange\":-0.91,\"priceChangeRatio\":-2.79,\"quarter\":1,\"todayEndPrice\":31.68,\"todayMaxPrice\":33.49,\"todayMinPrice\":31.34,\"todayOpenPrice\":33.48,\"tradingDate\":1550505600000,\"tradingMoney\":30524,\"tradingVolume\":95203,\"turnoverRate\":1.2,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.42,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68017,\"priceChange\":-0.55,\"priceChangeRatio\":-1.74,\"quarter\":1,\"todayEndPrice\":31.13,\"todayMaxPrice\":32.08,\"todayMinPrice\":30.68,\"todayOpenPrice\":31.4,\"tradingDate\":1550592000000,\"tradingMoney\":27888,\"tradingVolume\":89661,\"turnoverRate\":1.13,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.92,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68016,\"priceChange\":-0.63,\"priceChangeRatio\":-2.02,\"quarter\":1,\"todayEndPrice\":30.5,\"todayMaxPrice\":31.42,\"todayMinPrice\":30.2,\"todayOpenPrice\":31.22,\"tradingDate\":1550678400000,\"tradingMoney\":32165,\"tradingVolume\":104261,\"turnoverRate\":1.31,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.36,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68015,\"priceChange\":0.7,\"priceChangeRatio\":2.3,\"quarter\":1,\"todayEndPrice\":31.2,\"todayMaxPrice\":31.33,\"todayMinPrice\":30,\"todayOpenPrice\":30.47,\"tradingDate\":1550764800000,\"tradingMoney\":27164,\"tradingVolume\":88658,\"turnoverRate\":1.11,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":5.26,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68014,\"priceChange\":0.8,\"priceChangeRatio\":2.56,\"quarter\":1,\"todayEndPrice\":32,\"todayMaxPrice\":32.34,\"todayMinPrice\":30.7,\"todayOpenPrice\":31.35,\"tradingDate\":1551024000000,\"tradingMoney\":34564,\"tradingVolume\":109729,\"turnoverRate\":1.38,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.59,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68013,\"priceChange\":-0.52,\"priceChangeRatio\":-1.63,\"quarter\":1,\"todayEndPrice\":31.48,\"todayMaxPrice\":32.7,\"todayMinPrice\":31.23,\"todayOpenPrice\":31.9,\"tradingDate\":1551110400000,\"tradingMoney\":32167,\"tradingVolume\":100767,\"turnoverRate\":1.26,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":5.15,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68012,\"priceChange\":0.52,\"priceChangeRatio\":1.65,\"quarter\":1,\"todayEndPrice\":32,\"todayMaxPrice\":32.43,\"todayMinPrice\":30.81,\"todayOpenPrice\":31.26,\"tradingDate\":1551196800000,\"tradingMoney\":29294,\"tradingVolume\":91910,\"turnoverRate\":1.15,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.66,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68011,\"priceChange\":1.15,\"priceChangeRatio\":3.59,\"quarter\":1,\"todayEndPrice\":33.15,\"todayMaxPrice\":33.24,\"todayMinPrice\":31.75,\"todayOpenPrice\":32.03,\"tradingDate\":1551283200000,\"tradingMoney\":25263,\"tradingVolume\":77285,\"turnoverRate\":0.97,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.68,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68010,\"priceChange\":0.52,\"priceChangeRatio\":1.57,\"quarter\":1,\"todayEndPrice\":33.67,\"todayMaxPrice\":34.36,\"todayMinPrice\":32.81,\"todayOpenPrice\":33.48,\"tradingDate\":1551369600000,\"tradingMoney\":25991,\"tradingVolume\":77405,\"turnoverRate\":0.97,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.37,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68009,\"priceChange\":-0.01,\"priceChangeRatio\":-0.03,\"quarter\":1,\"todayEndPrice\":33.66,\"todayMaxPrice\":34.46,\"todayMinPrice\":32.99,\"todayOpenPrice\":33.71,\"tradingDate\":1551628800000,\"tradingMoney\":43721,\"tradingVolume\":129864,\"turnoverRate\":1.63,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.55,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68008,\"priceChange\":-1.26,\"priceChangeRatio\":-3.74,\"quarter\":1,\"todayEndPrice\":32.4,\"todayMaxPrice\":33.68,\"todayMinPrice\":32.15,\"todayOpenPrice\":33.38,\"tradingDate\":1551715200000,\"tradingMoney\":71622,\"tradingVolume\":220271,\"turnoverRate\":2.77,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.57,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68007,\"priceChange\":-0.19,\"priceChangeRatio\":-0.59,\"quarter\":1,\"todayEndPrice\":32.21,\"todayMaxPrice\":32.33,\"todayMinPrice\":30.85,\"todayOpenPrice\":32.33,\"tradingDate\":1551801600000,\"tradingMoney\":52472,\"tradingVolume\":165878,\"turnoverRate\":2.08,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.95,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68006,\"priceChange\":-0.28,\"priceChangeRatio\":-0.87,\"quarter\":1,\"todayEndPrice\":31.93,\"todayMaxPrice\":32.2,\"todayMinPrice\":31.25,\"todayOpenPrice\":31.8,\"tradingDate\":1551888000000,\"tradingMoney\":26233,\"tradingVolume\":82620,\"turnoverRate\":1.04,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.48,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68005,\"priceChange\":-0.6,\"priceChangeRatio\":-1.88,\"quarter\":1,\"todayEndPrice\":31.33,\"todayMaxPrice\":32.31,\"todayMinPrice\":31.2,\"todayOpenPrice\":31.29,\"tradingDate\":1551974400000,\"tradingMoney\":33033,\"tradingVolume\":104326,\"turnoverRate\":1.31,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":2.87,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68004,\"priceChange\":0.41,\"priceChangeRatio\":1.31,\"quarter\":1,\"todayEndPrice\":31.74,\"todayMaxPrice\":32,\"todayMinPrice\":31.1,\"todayOpenPrice\":31.32,\"tradingDate\":1552233600000,\"tradingMoney\":27851,\"tradingVolume\":88322,\"turnoverRate\":1.11,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.43,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68003,\"priceChange\":-0.62,\"priceChangeRatio\":-1.95,\"quarter\":1,\"todayEndPrice\":31.12,\"todayMaxPrice\":31.97,\"todayMinPrice\":30.88,\"todayOpenPrice\":31.97,\"tradingDate\":1552320000000,\"tradingMoney\":28415,\"tradingVolume\":90429,\"turnoverRate\":1.14,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.02,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68002,\"priceChange\":-0.52,\"priceChangeRatio\":-1.67,\"quarter\":1,\"todayEndPrice\":30.6,\"todayMaxPrice\":31.1,\"todayMinPrice\":29.85,\"todayOpenPrice\":30.8,\"tradingDate\":1552406400000,\"tradingMoney\":42853,\"tradingVolume\":140496,\"turnoverRate\":1.76,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.51,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68001,\"priceChange\":0.43,\"priceChangeRatio\":1.41,\"quarter\":1,\"todayEndPrice\":31.03,\"todayMaxPrice\":31.61,\"todayMinPrice\":30.23,\"todayOpenPrice\":30.3,\"tradingDate\":1552492800000,\"tradingMoney\":26650,\"tradingVolume\":86045,\"turnoverRate\":1.08,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.19,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":68000,\"priceChange\":0.76,\"priceChangeRatio\":2.45,\"quarter\":1,\"todayEndPrice\":31.79,\"todayMaxPrice\":31.9,\"todayMinPrice\":30.6,\"todayOpenPrice\":31.04,\"tradingDate\":1552579200000,\"tradingMoney\":27717,\"tradingVolume\":87943,\"turnoverRate\":1.1,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.5,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67999,\"priceChange\":0.83,\"priceChangeRatio\":2.61,\"quarter\":1,\"todayEndPrice\":32.62,\"todayMaxPrice\":32.99,\"todayMinPrice\":31.56,\"todayOpenPrice\":31.71,\"tradingDate\":1552838400000,\"tradingMoney\":34278,\"tradingVolume\":105471,\"turnoverRate\":1.32,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.74,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67998,\"priceChange\":-0.86,\"priceChangeRatio\":-2.64,\"quarter\":1,\"todayEndPrice\":31.76,\"todayMaxPrice\":32.8,\"todayMinPrice\":31.58,\"todayOpenPrice\":32.5,\"tradingDate\":1552924800000,\"tradingMoney\":22933,\"tradingVolume\":71719,\"turnoverRate\":0.9,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.24,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67997,\"priceChange\":0.09,\"priceChangeRatio\":0.28,\"quarter\":1,\"todayEndPrice\":31.85,\"todayMaxPrice\":32.29,\"todayMinPrice\":31.26,\"todayOpenPrice\":31.7,\"tradingDate\":1553011200000,\"tradingMoney\":13327,\"tradingVolume\":41988,\"turnoverRate\":0.53,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.74,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67996,\"priceChange\":0.95,\"priceChangeRatio\":2.98,\"quarter\":1,\"todayEndPrice\":32.8,\"todayMaxPrice\":33.12,\"todayMinPrice\":31.61,\"todayOpenPrice\":32.1,\"tradingDate\":1553097600000,\"tradingMoney\":33660,\"tradingVolume\":103330,\"turnoverRate\":1.3,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":8.35,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67995,\"priceChange\":1.48,\"priceChangeRatio\":4.51,\"quarter\":1,\"todayEndPrice\":34.28,\"todayMaxPrice\":35.75,\"todayMinPrice\":33.01,\"todayOpenPrice\":35.04,\"tradingDate\":1553184000000,\"tradingMoney\":62175,\"tradingVolume\":181466,\"turnoverRate\":2.28,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":9.71,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67994,\"priceChange\":1.23,\"priceChangeRatio\":3.59,\"quarter\":1,\"todayEndPrice\":35.51,\"todayMaxPrice\":36.94,\"todayMinPrice\":33.61,\"todayOpenPrice\":33.66,\"tradingDate\":1553443200000,\"tradingMoney\":57788,\"tradingVolume\":162391,\"turnoverRate\":2.04,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.63,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67993,\"priceChange\":0.1,\"priceChangeRatio\":0.28,\"quarter\":1,\"todayEndPrice\":35.61,\"todayMaxPrice\":36.31,\"todayMinPrice\":35.02,\"todayOpenPrice\":35.9,\"tradingDate\":1553529600000,\"tradingMoney\":39792,\"tradingVolume\":111128,\"turnoverRate\":1.4,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":7.1,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67992,\"priceChange\":1.21,\"priceChangeRatio\":3.4,\"quarter\":1,\"todayEndPrice\":36.82,\"todayMaxPrice\":37.13,\"todayMinPrice\":34.6,\"todayOpenPrice\":35.61,\"tradingDate\":1553616000000,\"tradingMoney\":41178,\"tradingVolume\":113767,\"turnoverRate\":1.43,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.45,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67991,\"priceChange\":0.29,\"priceChangeRatio\":0.79,\"quarter\":1,\"todayEndPrice\":37.11,\"todayMaxPrice\":37.49,\"todayMinPrice\":35.85,\"todayOpenPrice\":36.31,\"tradingDate\":1553702400000,\"tradingMoney\":49809,\"tradingVolume\":136501,\"turnoverRate\":1.71,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":3.91,\"createBy\":\"WY\",\"createTime\":1568380400000,\"idShareInfo\":39727,\"idSharePrice\":67990,\"priceChange\":-0.53,\"priceChangeRatio\":-1.43,\"quarter\":1,\"todayEndPrice\":36.58,\"todayMaxPrice\":37.91,\"todayMinPrice\":36.46,\"todayOpenPrice\":37.12,\"tradingDate\":1553788800000,\"tradingMoney\":71423,\"tradingVolume\":192919,\"turnoverRate\":2.42,\"updateBy\":\"WY\",\"updateTime\":1568380400000,\"year\":2019},{\"amplitude\":4.76,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68107,\"priceChange\":0.08,\"priceChangeRatio\":0.22,\"quarter\":2,\"todayEndPrice\":36.66,\"todayMaxPrice\":37.74,\"todayMinPrice\":36,\"todayOpenPrice\":36.6,\"tradingDate\":1554048000000,\"tradingMoney\":66761,\"tradingVolume\":181775,\"turnoverRate\":2.28,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.51,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68106,\"priceChange\":-0.66,\"priceChangeRatio\":-1.8,\"quarter\":2,\"todayEndPrice\":36,\"todayMaxPrice\":36.8,\"todayMinPrice\":35.88,\"todayOpenPrice\":36.66,\"tradingDate\":1554134400000,\"tradingMoney\":46284,\"tradingVolume\":127851,\"turnoverRate\":1.6,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.31,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68105,\"priceChange\":0.21,\"priceChangeRatio\":0.58,\"quarter\":2,\"todayEndPrice\":36.21,\"todayMaxPrice\":36.58,\"todayMinPrice\":35.75,\"todayOpenPrice\":35.8,\"tradingDate\":1554220800000,\"tradingMoney\":41853,\"tradingVolume\":115253,\"turnoverRate\":1.45,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.18,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68104,\"priceChange\":-0.07,\"priceChangeRatio\":-0.19,\"quarter\":2,\"todayEndPrice\":36.14,\"todayMaxPrice\":36.65,\"todayMinPrice\":35.86,\"todayOpenPrice\":36.2,\"tradingDate\":1554307200000,\"tradingMoney\":29343,\"tradingVolume\":81213,\"turnoverRate\":1.02,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.96,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68103,\"priceChange\":-0.34,\"priceChangeRatio\":-0.94,\"quarter\":2,\"todayEndPrice\":35.8,\"todayMaxPrice\":36.56,\"todayMinPrice\":35.49,\"todayOpenPrice\":35.49,\"tradingDate\":1554652800000,\"tradingMoney\":31108,\"tradingVolume\":86481,\"turnoverRate\":1.09,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.37,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68102,\"priceChange\":-0.27,\"priceChangeRatio\":-0.75,\"quarter\":2,\"todayEndPrice\":35.53,\"todayMaxPrice\":36.1,\"todayMinPrice\":35.25,\"todayOpenPrice\":35.7,\"tradingDate\":1554739200000,\"tradingMoney\":28131,\"tradingVolume\":78932,\"turnoverRate\":0.99,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":4.81,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68101,\"priceChange\":0.7,\"priceChangeRatio\":1.97,\"quarter\":2,\"todayEndPrice\":36.23,\"todayMaxPrice\":37.27,\"todayMinPrice\":35.56,\"todayOpenPrice\":35.79,\"tradingDate\":1554825600000,\"tradingMoney\":36973,\"tradingVolume\":101346,\"turnoverRate\":1.27,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.59,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68100,\"priceChange\":-0.86,\"priceChangeRatio\":-2.37,\"quarter\":2,\"todayEndPrice\":35.37,\"todayMaxPrice\":36.22,\"todayMinPrice\":35.28,\"todayOpenPrice\":36.22,\"tradingDate\":1554912000000,\"tradingMoney\":24367,\"tradingVolume\":68370,\"turnoverRate\":0.86,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":3.14,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68099,\"priceChange\":-0.64,\"priceChangeRatio\":-1.81,\"quarter\":2,\"todayEndPrice\":34.73,\"todayMaxPrice\":35.84,\"todayMinPrice\":34.73,\"todayOpenPrice\":35.25,\"tradingDate\":1554998400000,\"tradingMoney\":24926,\"tradingVolume\":70842,\"turnoverRate\":0.89,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":3.66,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68098,\"priceChange\":-0.43,\"priceChangeRatio\":-1.24,\"quarter\":2,\"todayEndPrice\":34.3,\"todayMaxPrice\":35.47,\"todayMinPrice\":34.2,\"todayOpenPrice\":35.17,\"tradingDate\":1555257600000,\"tradingMoney\":20022,\"tradingVolume\":57593,\"turnoverRate\":0.72,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":3.44,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68097,\"priceChange\":-0.09,\"priceChangeRatio\":-0.26,\"quarter\":2,\"todayEndPrice\":34.21,\"todayMaxPrice\":34.49,\"todayMinPrice\":33.31,\"todayOpenPrice\":34.3,\"tradingDate\":1555344000000,\"tradingMoney\":40780,\"tradingVolume\":120412,\"turnoverRate\":1.51,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":3.33,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68096,\"priceChange\":-0.11,\"priceChangeRatio\":-0.32,\"quarter\":2,\"todayEndPrice\":34.1,\"todayMaxPrice\":34.94,\"todayMinPrice\":33.8,\"todayOpenPrice\":34.52,\"tradingDate\":1555430400000,\"tradingMoney\":31254,\"tradingVolume\":91333,\"turnoverRate\":1.15,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.43,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68095,\"priceChange\":0.18,\"priceChangeRatio\":0.53,\"quarter\":2,\"todayEndPrice\":34.28,\"todayMaxPrice\":34.78,\"todayMinPrice\":33.95,\"todayOpenPrice\":34.26,\"tradingDate\":1555516800000,\"tradingMoney\":23003,\"tradingVolume\":66824,\"turnoverRate\":0.84,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":3.06,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68094,\"priceChange\":0.02,\"priceChangeRatio\":0.06,\"quarter\":2,\"todayEndPrice\":34.3,\"todayMaxPrice\":34.75,\"todayMinPrice\":33.7,\"todayOpenPrice\":34.39,\"tradingDate\":1555603200000,\"tradingMoney\":15991,\"tradingVolume\":46908,\"turnoverRate\":0.59,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":3.82,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68093,\"priceChange\":0.42,\"priceChangeRatio\":1.22,\"quarter\":2,\"todayEndPrice\":34.72,\"todayMaxPrice\":35.32,\"todayMinPrice\":34.01,\"todayOpenPrice\":34.32,\"tradingDate\":1555862400000,\"tradingMoney\":23942,\"tradingVolume\":68921,\"turnoverRate\":0.87,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019},{\"amplitude\":2.68,\"createBy\":\"WY\",\"createTime\":1568380401000,\"idShareInfo\":39727,\"idSharePrice\":68092,\"priceChange\":-0.15,\"priceChangeRatio\":-0.43,\"quarter\":2,\"todayEndPrice\":34.57,\"todayMaxPrice\":35.14,\"todayMinPrice\":34.21,\"todayOpenPrice\":34.9,\"tradingDate\":1555948800000,\"tradingMoney\":22439,\"tradingVolume\":64814,\"turnoverRate\":0.81,\"updateBy\":\"WY\",\"updateTime\":1568380401000,\"year\":2019}]\n";

    @Test
    public void xx() throws Exception {
        List<SharePriceDto> sharePrices = JSONObject.parseArray(xx, SharePriceDto.class);
        Linked<Price> linked = LinkedUtil.buildLined(sharePrices);
        // 处理包含关系
        int size = linked.getSize();
        for (int i = 0; i < size; i++) {
            System.out.println(linked.get(i));
        }
    }
}