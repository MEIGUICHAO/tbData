/** 注：该JS文件用于存放常用函数，功用相关的函数放在Java文件中注入*/


//##############################################################################################################
//收集热销标题
function getTitleList(){
    next();
    localMethod.loadRenqi();
//    setTimeout(function(){
//        var as = document.getElementsByTagName("a");
//        findForClick(as,"下一页 ");
//    },1000);
//    setTimeout(function(){
//        next();
//    },2000);
//    setTimeout(function(){
//        localMethod.loadRenqi();
//    },4000);


}

function next(){
    var imgs = document.getElementsByTagName("img");
    var salenums = document.getElementsByClassName("deal-cnt");
    var text = "";
    for(var j=0;j<imgs.length;j++){
        try{
            text = text +imgs[j].getAttribute("alt")+ salenums[j].innerText+"\n";
            localMethod.sameResultForSort(imgs[j].getAttribute("alt"),salenums[j].innerText.replace("人收货",""));
        }catch(e){
            localMethod.JI_LOG(e);
        }
    }
    localMethod.JI_LOG(text);
}



//标题组合
function titleCombination(){

    var as = document.getElementsByTagName("a");
    findForClick(as,"关联修饰词");
    for(var j=0;j<5;j++){
        setTimeout(function(){
        getTableTitleData();
            var as = document.getElementsByTagName("a");
            findForClick(as,"下一页 >");
        },500*(j+1));
    }

    setTimeout(function(){
        localMethod.getTitleResult();
    },3000);
//    localMethod.getTitleResult();

}

//标题组合
function relativeTitle(){
    var as = document.getElementsByTagName("a");
    findForSimiliar(as);
}






//跳到市场
function goSearchClick(){

    var as = document.getElementsByTagName("a");
//    for (var i = 0; i < as.length; i++) {
//        localMethod.JI_LOG(as[i].innerHTML+"~~~~"+i);
//    }
    as[18].click();

    setTimeout(function(){
                var as2 = document.getElementsByTagName("a");
                as2[27].click();
                localMethod.JI_LOG("as2~~~~click");
    },3000);

}


function check(url){

    var itemnames = document.getElementsByClassName("info1__itemname");
    var prices = document.getElementsByClassName("info2__price");
    var paids = document.getElementsByClassName("info3__npaid");
    var mUrl = document.getElementsByClassName("info1__itemname");
    localMethod.JI_LOG("itemnames:"+itemnames.length);
    localMethod.JI_LOG("prices:"+prices.length);
    localMethod.JI_LOG("paids:"+paids.length);
    var pay = paids[7].innerText.replace("人付款","");
    var text = url + "\n";
    var maxPrices = 0;
    var minPrices = 100000;
    var averPrices = 0;
    var averNum = 0;
    var minPricesUrl = "";

    if(paids.length>30&&pay>2){
        for(var i=0;i<itemnames.length;i++){
            if(paids[i].innerText.replace("人付款","")>10){
                var price = prices[i].innerText.replace("￥","");
                averPrices = accAdd(averPrices,price);
                averNum = averNum +1;
                if(parseFloat(price)>parseFloat(maxPrices)){
                    maxPrices = price;
                }
                if(parseFloat(price)<parseFloat(minPrices)){
                    minPrices = price;
                    minPricesUrl = mUrl[i].getElementsByTagName("a")[0];
                }
                localMethod.titleSave(itemnames[i].innerText);
                text = text + itemnames[i].innerText +"#####"+paids[i].innerText + "\n";
                localMethod.sameResultForSort(itemnames[i].innerText,paids[i].innerText.replace("人付款",""));

            }
        }
        localMethod.sameResult(text);
        var minSameRecord = "maxPrices:"+maxPrices+",averPrices:"+accDiv(averPrices,averNum)+",minPrices:"+minPrices+"\n"+"sameUrl:"+url+"\n"+"minPricesUrl:"+minPricesUrl;
        localMethod.sameResultRecord(url);
        localMethod.minSameRecord(minSameRecord);
        localMethod.JI_LOG(minSameRecord);


    }


}



function goSearchWord(){
    var as = document.getElementsByTagName("a");
    as[26].click();
}


function setSearchWord(shopword){


    var searchWord = document.getElementsByClassName("search-combobox-input");
    searchWord[0].focus();
    localMethod.showKeyboard();
    searchWord[0].value = shopword;

            var btnSearch = document.getElementsByClassName("submit icon-btn-search");
            btnSearch[0].click();


//    localMethod.afterSearch();
}


function getSameStyleUrlList(){

}

//打印同款链接
function findForSimiliar(as){
    var text = "--------as--------"+ "\n";
    var samestytle=new Array();
    var index = 0;

    for(var i=0;i<as.length;i++){

        if(as[i].innerText=="找同款"&&as[i]!=""){
        text = text + i + "\n"+",innerText:"+as[i].innerText+
//        ",innerHTML:"+as[i].innerHTML+
        ",value:"+as[i].value + ",as:"+as[i]+"\n";
         var link = as[i]+"&sort=sale-desc";
//           localMethod.JI_LOG(link);
           samestytle[index] = link;
           index = index + 1;
        }
    }

    localMethod.linkArray(samestytle);
}

//根据搜索词点击
function findForClick(as,word){
    for(var i=0;i<as.length;i++){
        if(as[i].innerText==word){
           as[i].click();
        }
    }
}

//竞争力、热词获取排序
function goGetChecked(){

    for(var j=0;j<5;j++){
        setTimeout(function(){
            getTableData();
            var as = document.getElementsByTagName("a");
            findForClick(as,"下一页 >");
        },500*(j+1));
    }

    setTimeout(function(){
        localMethod.getHotShopResult();
    },3000);
}


function getTableTitleData(){
    var table = document.getElementsByClassName("table-ng table-ng-basic related-word-table")[0];
    for(var i=0;i<table.rows.length;i++){
        var child = table.getElementsByTagName("tr")[i];
        var text = child.children[0].innerText;
        var text1 = child.children[1].innerText;
        if(i>0){
            text1 = text1.replace("-","0");

            text1 = text1.replace(",","").replace(",","");
//            localMethod.JI_LOG(text+"~~~~~~~~~~");
//            localMethod.JI_LOG(text1+"!!!!!!!!");
            if(text1>700){
                localMethod.titleResult(text,text1);
            }


        }
    }
}

function getTableData(){
    var table = document.getElementsByClassName("table-ng table-ng-basic related-word-table")[0];
    for(var i=0;i<table.rows.length;i++){
        var child = table.getElementsByTagName("tr")[i];
        var text = child.children[0].innerText;
        var text1 = child.children[1].innerText;
        var text2 = child.children[2].innerText;
        var text3 = child.children[3].innerText;
        var text4 = child.children[4].innerText;
        if(i>0){
            var djl = text2.replace("%","");
            djl= djl/100;
            var zhl = text4.replace("%","");
            zhl= zhl/100;
            text1 = text1.replace("-","0");
            text2 = text2.replace("-","0");
            text3 = text3.replace("-","0");
            text4 = text4.replace("-","0");

            text1 = text1.replace(",","").replace(",","");
            text3 = text3.replace(",","").replace(",","");
//            localMethod.JI_LOG(text1+"!!!!!!!!");
//            localMethod.JI_LOG(text3+"!!!!!!!!");
            if(text3!=("0")&&text1>2000){
                var jzl = accDiv(accMul(accMul(text1,djl),zhl),text3);
                var rc = accDiv(text1,text3);
//                localMethod.JI_LOG(jzl+"~~~~~");
//                localMethod.JI_LOG(rc+"~~~~~");
                localMethod.shopResult(text,jzl,rc);
            }


        }
    }
}

//乘法
function accMul(arg1,arg2){
 try{
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{
        m+=s1.split(".")[1].length
        }catch(e){
        }
    try{
        m+=s2.split(".")[1].length
        }catch(e){
        }
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
 }catch(e){
    return 0;
 }
}




/**
 ** 加法函数，用来得到精确的加法结果
 ** 说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
 ** 调用：accAdd(arg1,arg2)
 ** 返回值：arg1加上arg2的精确结果
 **/
function accAdd(arg1, arg2) {
    var r1, r2, m, c;
    try {
        r1 = arg1.toString().split(".")[1].length;
    }
    catch (e) {
        r1 = 0;
    }
    try {
        r2 = arg2.toString().split(".")[1].length;
    }
    catch (e) {
        r2 = 0;
    }
    c = Math.abs(r1 - r2);
    m = Math.pow(10, Math.max(r1, r2));
    if (c > 0) {
        var cm = Math.pow(10, c);
        if (r1 > r2) {
            arg1 = Number(arg1.toString().replace(".", ""));
            arg2 = Number(arg2.toString().replace(".", "")) * cm;
        } else {
            arg1 = Number(arg1.toString().replace(".", "")) * cm;
            arg2 = Number(arg2.toString().replace(".", ""));
        }
    } else {
        arg1 = Number(arg1.toString().replace(".", ""));
        arg2 = Number(arg2.toString().replace(".", ""));
    }
    return (arg1 + arg2) / m;
}

//除法
 function accDiv(arg1,arg2){
 try{
    var t1=0,t2=0,r1,r2;
    try{
        t1=arg1.toString().split(".")[1].length
        }catch(e){}
    try{
        t2=arg2.toString().split(".")[1].length
        }catch(e){}
    with(Math){
        r1=Number(arg1.toString().replace(".",""));
        r2=Number(arg2.toString().replace(".",""));
        return (r1/r2)*pow(10,t2-t1);
    }
 }catch(e){
    return 0;
 }
}

//指标选择
function operaSearch(){

    var checkboxs = document.getElementsByClassName("checkbox undefined undefined");
    var optionsClick = document.getElementsByClassName("option");

    for(var i=0;i<checkboxs.length;i++){
        if(checkboxs[i].innerText=="搜索人数占比"||checkboxs[i].innerText=="搜索热度"||checkboxs[i].innerText=="商城点击占比"
        ||checkboxs[i].innerText=="直通车参考价"||checkboxs[i].innerText=="支付转化率"){
           optionsClick[i].click();
        }
    }
    goGetChecked();
}

function foreachThings(options,i){

        setTimeout(function(){
//           options[i].click();
           localMethod.JI_LOG(options[i].innerHTML+"check_option~~~~"+i);

        },500*(i+1));

}


/** No.1 模拟点击事件############################################################################################*/
//模拟点击事件
function doClickByRI(resId,time) {
 var btn = document.getElementById(resId);
 if(null!=btn){
    setTimeout(function(){
        btn.click();
    },time*1000);
    }
}

function doClickByTag(){
  var itemli = document.getElementsByTagName("li");
  localMethod.JI_showToast("length："+itemli.length);

}
function doComfir(){

    setTimeout(function(){

  var btn = document.getElementsByClassName("layui-layer-btn0");
    localMethod.JI_showToast("btn:"+btn.length);
    btn[0].click();
        },3000);

}


function selectNumRange(position,amount){
  var itema = document.getElementById('framePage').contentWindow.document.getElementsByTagName('input');
  var commitBtn = document.getElementById('framePage').contentWindow.document.getElementById('openBetWinBtn2');


  localMethod.JI_showToast("itema:"+itema.length);
    itema[position].click();
    itema[position].value = amount;
    setTimeout(function(){
        commitBtn.click()
    },2000);

//  localMethod.JI_LOG(btn.className);
    setTimeout(function(){
        btn.click()
    },3000);

}


function doClickByCN(className,time) {
  var itemli = document.getElementsByTagName("li");
  localMethod.JI_showToast("length："+itemli.length);

  var btn = document.getElementsByClassName(className)[0];
  if(null!=btn){
    setTimeout(function(){
        btn.click();
    },time*1000);
    }
}

//模拟触摸事件
function doTapByRI(resId,index) {
   if(null==index){index=0;}
   $("#"+resId).eq(index).trigger("tap");
}

function doTapByCN(className,index) {
  if(null==index){index=0;}
  $("."+className).eq(index).trigger("tap")
}

//根据父控件查找子控件再触摸
function doTapByParentCN(parentCN,className,index) {
  if(null==index){index=0;}
  $("."+parentCN).children("."+className) .eq(index).trigger("tap");
}

function doTapForScanGoods(parentCN,index) {
   if(null==index){index=0;}
   $("."+parentCN).eq(index).children(".p").children("a").trigger("tap");
}


/** No.2 输入文本信息至输入框中############################################################################################*/
function doInputByRI(resId,context,time) {
   var text = document.getElementById(resId);
    setTimeout(function(){
        text.value = context;
    },time*1000);
}

function doInputByCN(className,context,time) {
    var text = document.getElementsByClassName(className)[0];
    setTimeout(function(){
        text.value = context;
    },time*1000);
}


/** No.3 获取控件的文本信息###########################################################################################*/
function doGetTextByRI(resId) {
    var text = document.getElementById(resId);
    return text.value;
}

function doGetTextByCN(className) {
    var text = document.getElementsByClassName(className)[0];
    return text.value;
}

function doGetTextByCNByInner(className) {
    var text = document.getElementsByClassName(className)[0];
    return text.innerHTML;
}



