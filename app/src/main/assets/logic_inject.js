var isTargetSearched = false;  //判断目的商铺是否浏览完成
var restScanCount = getRandomNum(2, 8);  //得到目的商铺浏览完成后还需浏览商铺数量的随机数

/**
 *  通过商品标题查找目的商铺，若当前页没有找到则随机浏览0-2个商铺，浏览完后翻页再查找目的商铺，依次循环
 *  当找到目的商铺并浏览完成后再随机浏览2-8间商铺
 */
function doTapForScanGoodsByTitle(parentCN, className, titleStr, page, size) {
    var frontSize = 0;
    var backSize = 0;
    var count = 0;

    if (1 == page) {
        frontSize = 0;
        backSize = size;
    } else {
        frontSize = size - 25;
        backSize = size;
    }

    for (var i = frontSize; i < backSize; i++) {
        var titelTv = document.getElementsByClassName(className)[i];
        if (null == titelTv) {
            localMethod.JI_showToast("数组越界！！");
            return;
        }
        titelTxt = titelTv.innerHTML;

        if (!isTargetSearched && titelTxt.indexOf(titleStr) >= 0) {
            isTargetSearched = true;

            // detailWebView执行商品详情页面浏览任务
            var url = getUrlFromHref(parentCN, i);
            localMethod.JI_doEnterShop(url);
            //localMethod.JI_createLog("Page" + page + "####" + titelTxt);

            // listWebView执行翻页刷新商品list表任务
            localMethod.JI_doGetNextPage(20);
            return;
        } else {
            count++;
        }
    }

    // 当找到目的商铺并浏览完成后再随机浏览几间商铺
    if (isTargetSearched) {
        if (0 != restScanCount) {
            restScanCount--;
            localMethod.JI_showToast(restScanCount);
        } else {
            return;
        }
    }

    // 当前页面商铺浏览随机、浏览时长随机逻辑
    if (backSize - frontSize == count) {

        var randomTime = getRandomNum(1, 20);
        var randomIndex1 = getRandomNum(frontSize, backSize);
        var randomIndex2 = getRandomNum(frontSize, backSize);
        var rangeNum = Math.abs(randomIndex2 - randomIndex1);

        //localMethod.JI_showToast(rangeNum);
        var url = getUrlFromHref(parentCN, randomIndex2);
        if (rangeNum < 10) {
            localMethod.JI_doEnterShop(url);
            //localMethod.JI_createLog("Page" + page + "####" + url);
        } else if (rangeNum > 17) {
            localMethod.JI_doEnterShop(url);
            //localMethod.JI_createLog("Page" + page + "####" + url);
            localMethod.JI_doScanCurrentPage(randomTime);
            return;
        }
    }

    localMethod.JI_doGetNextPage(randomTime);

}

///** */
//function doTapForScanOtherGoods(parentCN, randomIndex) {
//    //localMethod.JI_showToast(index);
//    $("." + parentCN).eq(randomIndex).children(".p").children("a").trigger("tap");
//    localMethod.JI_doCloseAlert();
//    localMethod.JI_doScanOtherGoods();
//}

/** 得到指定index的URL*/
function getUrlFromHref(parentCN, index) {
    return $("." + parentCN).eq(index).children(".p").children("a").attr("href");
}

/** 得到指定TAG的数量，用作获取list的数量*/
function getSize(className) {
    var text = document.getElementsByClassName(className).length;
    return text;
}

/** 得到指定范围的随机数*/
function getRandomNum(Min,Max)
{   
   var Range = Max - Min;   
   var Rand = Math.random();   
   return(Min + Math.round(Rand * Range));   
}   


