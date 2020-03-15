
function optionFunction(rawData,echarts) {
  let data= splitData(rawData)
  return {
    backgroundColor: '#eee',
    animation: false,
    legend: {
      bottom: 10,
      left: 'center',
      data: ['K线', '笔','MA5', 'MA10', 'MA20', 'MA30']
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      },
      backgroundColor: 'rgba(245, 245, 245, 0.8)',
      borderWidth: 1,
      borderColor: '#ccc',
      padding: 10,
      textStyle: {
        color: '#000'
      },
      position: function (pos, params, el, elRect, size) {
        let obj = {top: 10};
        obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 30;
        return obj;
      },
      extraCssText: 'width: 170px'
    },
    axisPointer: {
      link: {xAxisIndex: 'all'},
      label: {
        backgroundColor: '#777'
      }
    },
    toolbox: {
      feature: {
        dataZoom: {
          yAxisIndex: false
        },
        brush: {
          type: ['lineX', 'clear']
        }
      }
    },
    brush: {
      xAxisIndex: 'all',
      brushLink: 'all',
      outOfBrush: {
        colorAlpha: 0.1
      }
    },
    grid: [
      {
        left: '10%',
        right: '8%',
        height: '50%'
      },
      {
        left: '10%',
        right: '8%',
        bottom: '20%',
        height: '15%'
      }
    ],
    xAxis: [
      {
        type: 'category',
        data: data.categoryData,
        scale: true,
        boundaryGap: false,
        axisLine: {onZero: false},
        splitLine: {show: false},
        splitNumber: 20,
        min: 'dataMin',
        max: 'dataMax',
        axisPointer: {
          z: 100
        }
      },
      {
        type: 'category',
        gridIndex: 1,
        data: data.categoryData,
        scale: true,
        boundaryGap: false,
        axisLine: {onZero: false},
        axisTick: {show: false},
        splitLine: {show: false},
        axisLabel: {show: false},
        splitNumber: 20,
        min: 'dataMin',
        max: 'dataMax',
        axisPointer: {
          label: {
            formatter: function (params) {
              let seriesValue = (params.seriesData[0] || {}).value;
              return params.value + (seriesValue != null? '\n' + echarts.format.addCommas(seriesValue): '');
            }
          }
        }
      }
    ],
    yAxis: [
      {
        scale: true,
        splitArea: {
          show: true
        }
      },
      {
        scale: true,
        gridIndex: 1,
        splitNumber: 2,
        axisLabel: {show: false},
        axisLine: {show: false},
        axisTick: {show: false},
        splitLine: {show: false}
      }
    ],
    dataZoom: [
      {
        type: 'inside',
        xAxisIndex: [0, 1],
        start: data.categoryData.length/2,
        end: 100
      },
      {
        show: true,
        xAxisIndex: [0, 1],
        type: 'slider',
        top: '85%',
        start: 98,
        end: 100
      }
    ],
    series: [
      {
        name: 'K线',
        type: 'candlestick',
        data: data.values,
        itemStyle: {
          normal: {
            color: '#06B800',
            color0: '#FA0000',
            borderColor: null,
            borderColor0: null
          }
        },
        tooltip: {
          formatter: function (param) {
            param = param[0];
            return [
              'Date: ' + param.name + '<hr size=1 style="margin: 3px 0">',
              'Open: ' + param.data[0] + '<br/>',
              'Close: ' + param.data[1] + '<br/>',
              'Lowest: ' + param.data[2] + '<br/>',
              'Highest: ' + param.data[3] + '<br/>'
            ].join('');
          }
        }
      },
      {
        name: '笔',
        type: 'line',
        data: calculateBi(rawData.priceType),
        connectNulls: true,
        smooth: false,
        lineStyle: {
          normal: {opacity: 0.5}
        }
      },
      {
        name: 'MA5',
        type: 'line',
        data: calculateMA(5, data),
        smooth: true,
        lineStyle: {
          normal: {opacity: 0.5}
        }
      },
      {
        name: 'MA10',
        type: 'line',
        data: calculateMA(10, data),
        smooth: true,
        lineStyle: {
          normal: {opacity: 0.5}
        }
      },
      {
        name: 'MA20',
        type: 'line',
        data: calculateMA(20, data),
        smooth: true,
        lineStyle: {
          normal: {opacity: 0.5}
        }
      },
      {
        name: 'MA30',
        type: 'line',
        data: calculateMA(30, data),
        smooth: true,
        lineStyle: {
          normal: {opacity: 0.5}
        }
      },
      {
        name: 'Volumn',
        type: 'bar',
        xAxisIndex: 1,
        yAxisIndex: 1,
        data: data.volumes
      }
    ]
  }
}
function calculateBi(data) {
  let result = [];
  let tmp
  for (let i = 0, len = data.length; i < len; i++) {
    tmp= data[i]
    if ( '-' == tmp) {
      result.push('-');
    } else {
      result.push(tmp.split('-')[1]);
    }
  }
  debugger
  return result;
}
function calculateMA(dayCount, data) {
  let result = [];
  for (let i = 0, len = data.values.length; i < len; i++) {
    if (i < dayCount) {
      result.push('-');
      continue;
    }
    let sum = 0;
    for (let j = 0; j < dayCount; j++) {
      sum += data.values[i - j][1];
    }
    result.push(+(sum / dayCount).toFixed(3));
  }
  return result;
}

function splitData(rawData) {
  rawData=rawData.baseData
  let categoryData = [];
  let values = [];
  let volumes = [];
  for (let i = 0; i < rawData.length; i++) {
    categoryData.push(rawData[i].splice(0, 1)[0]);
    values.push(rawData[i]);
    volumes.push(rawData[i][4]);
  }
  return {
    categoryData: categoryData,
    values: values,
    volumes: volumes
  };
}

export var option = optionFunction
