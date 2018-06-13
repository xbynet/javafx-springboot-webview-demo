package com.github.xbynet.fxboot.functional;

import com.github.xbynet.fxboot.Constants;
import com.github.xbynet.fxboot.functional.model.CygDetailGoodInfo;
import com.github.xbynet.fxboot.functional.model.CygListGoodInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anzhou.tjw
 * @date 2018/6/13 下午7:25
 */
//@Component
public class TlcygParser {

    private static final String prefix="http://tl.cyg.changyou.com";
    public static void parser(WebView webView,String url){
        List<CygListGoodInfo> allListInfo=new ArrayList<>();
        List<CygDetailGoodInfo> allDetailInfo=new ArrayList<>();

        webView.getEngine().getLoadWorker().stateProperty().addListener(
            new ChangeListener<Worker.State>() {
                public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        String url=webView.getEngine().getLocation();
                        if(url.startsWith(prefix)){
                            String content= (String)webView.getEngine().executeScript(Constants.JS_OUTER_HTML);
                            Document document=Jsoup.parse(content);
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
                                }
                            }else{
                                // todo
                            }
                        }
                    }
                }
            });

        webView.getEngine().load(url);
    }


}
