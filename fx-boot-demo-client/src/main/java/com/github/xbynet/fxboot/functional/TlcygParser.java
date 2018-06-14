package com.github.xbynet.fxboot.functional;

import com.github.xbynet.fxboot.Constants;
import com.github.xbynet.fxboot.functional.model.CygDetailGoodInfo;
import com.github.xbynet.fxboot.functional.model.CygListGoodInfo;
import com.github.xbynet.fxboot.util.BeanCopyUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author anzhou.tjw
 * @date 2018/6/13 下午7:25
 */
//@Component
public class TlcygParser {

    private static final String prefix="http://tl.cyg.changyou.com";

    public static void parser(WebView webView){
        String url="http://tl.cyg.changyou.com/goods/selling?profession=11&level=80-89&have_chosen=profession*11%20level*80-89#goodsTag";
        AtomicInteger cursor=new AtomicInteger(0);
        AtomicInteger detailCursor=new AtomicInteger(0);
        List<String> allListUrl=new ArrayList<>();
        List<CygListGoodInfo> allListInfo=new ArrayList<>();
        List<CygDetailGoodInfo> allDetailInfo=new ArrayList<>();

        final AtomicBoolean isStart=new AtomicBoolean(true);
        webView.getEngine().getLoadWorker().stateProperty().addListener(
            new ChangeListener<Worker.State>() {
                public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        String url=webView.getEngine().getLocation();
                        if(url.startsWith(prefix)){
                            String content= (String)webView.getEngine().executeScript(Constants.JS_OUTER_HTML);
                            Document document=Jsoup.parse(content);
                            if(isStart.compareAndSet(true,false)){
                                Elements tmp=document.select(".ui-pagination > a");
                                Element tmpEl=tmp.get(tmp.size()-2);
                                int allCount=Integer.valueOf(tmpEl.select("span").text());
                                if(allCount>1){
                                    for(int i=2;i<=allCount;i++){
                                        String reg="page_num=\\d+?";
                                        String tmpUrl=tmp.get(2).attr("href");
                                        String newUrl=StringUtils.replaceAll(tmpUrl,reg,"page_num="+i);
                                        System.out.println("添加url:"+newUrl);
                                        allListUrl.add(newUrl);
                                    }
                                }
                            }

                            boolean isList=true;
                            if(url.contains("char_detail")){
                                isList=false;
                            }
                            if(isList){
                                Elements liList=document.select("#J_good_list > li");
                                for(int i=0;i<liList.size();i++){
                                    Element element=liList.get(i);
                                    CygListGoodInfo info=CygListGoodInfo.parse(element);
                                    allListInfo.add(info);
                                    System.out.println("添加ListInfo:"+ToStringBuilder.reflectionToString(info));
                                }
                            }else{
                                CygListGoodInfo cygListGoodInfo=allListInfo.stream().filter(i->i.getUrl().equals(url)|| url.startsWith(i.getUrl())).findFirst().get();
                                CygDetailGoodInfo cygDetailGoodInfo=BeanCopyUtil.copyProperties(cygListGoodInfo,CygDetailGoodInfo.class);
                                CygDetailGoodInfo.parse(document,cygDetailGoodInfo);
                                allDetailInfo.add(cygDetailGoodInfo);
                                System.out.println("添加DetailInfo:"+ToStringBuilder.reflectionToString(cygDetailGoodInfo));
                            }

                           int num=cursor.addAndGet(1);
                            if(num<allListUrl.size()){
                                Platform.runLater(()->{
                                    webView.getEngine().load(allListUrl.get(num));
                                });
                            }else{
                                if(detailCursor.get()<allListInfo.size()){
                                    Platform.runLater(()->{
                                        webView.getEngine().load(allListInfo.get(detailCursor.get()).getUrl());
                                        detailCursor.addAndGet(1);
                                    });
                                }else {
                                    System.out.println(
                                        "======获取完成List count:" + (allListUrl.size() + 1) + ",,,,detailsInfo count:"
                                            + allDetailInfo.size());
                                }
                            }
                        }

                    }
                }
            });

        webView.getEngine().load(url);
    }


}
