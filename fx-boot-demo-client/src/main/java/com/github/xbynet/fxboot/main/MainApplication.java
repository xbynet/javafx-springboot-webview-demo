package com.github.xbynet.fxboot.main;

import com.aquafx_project.AquaFx;
import com.github.xbynet.fxboot.functional.TlcygParser;
import com.github.xbynet.fxboot.ioc.BeanHolder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CompletableFuture;

/**
 * @author anzhou.tjw
 * @date 2018/6/8 下午3:18
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.github.xbynet"})
public class MainApplication extends Application {

    private static String[] savedArgs =new String[0];
    private ConfigurableApplicationContext applicationContext;

    private WebView webView;

    private String port;

    @Override
    public void init() throws Exception {
        super.init();
        CompletableFuture.supplyAsync(() ->
            SpringApplication.run(this.getClass(), savedArgs)
        ).whenComplete((ctx, throwable) -> {
            if (throwable != null) {
                log.error("Failed to load spring application context: ", throwable);
                Platform.runLater(() -> showErrorAlert(throwable));
            } else {
                log.info("springboot start completed!");
                applicationContext=ctx;
                port=applicationContext.getEnvironment().getProperty("server.port");
                Platform.runLater(() -> {
                    //webView.getEngine().load("http://127.0.0.1:"+port);
                    //webView.getEngine().load("http://xbynet.top");
                    //webView.getEngine().load("http://127.0.0.1:"+port+"/home/index");
                    TlcygParser.parser(webView,"http://tl.cyg.changyou.com/");
                });
            }
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    public static void launchApp(String... args){
        savedArgs =args;
        launch(args);
    }

    private static void showErrorAlert(Throwable throwable) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Oops! An unrecoverable error occurred.\n" +
            "Please contact your software vendor.\n\n" +
            "The application will stop now.\n\n" +
            "Error: " + throwable.getMessage());
        alert.showAndWait().ifPresent(response -> Platform.exit());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        webView=new WebView();
        webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/application.css").toExternalForm());


        // show "alert" Javascript messages in stdout (useful to debug)
        webView.getEngine().setOnAlert(new EventHandler<WebEvent<String>>(){
            @Override
            public void handle(WebEvent<String> arg0) {
               log.error("webview error: " + arg0.getData());
            }
        });
        //primaryStage.titleProperty().bind(webView.getEngine().titleProperty());

        webView.getEngine().getLoadWorker().stateProperty().addListener(
            new ChangeListener<Worker.State>() {
                public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        /*Document document=webView.getEngine().getDocument();
                        NodeList headList=document.getElementsByTagName("head");
                        if(headList!=null && headList.getLength()>0) {
                            Node head=headList.item(0);
                            NodeList titleList= ((Element)head).getElementsByTagName("title");
                            if(titleList!=null && titleList.getLength()>0){
                                String title = titleList.item(0).getTextContent();
                                primaryStage.setTitle(title);
                            }
                        }*/
                        primaryStage.setTitle(webView.getEngine().getTitle());
                        //primaryStage.sizeToScene();
                        JSObject win
                            = (JSObject) webView.getEngine().executeScript("window");
                        if(applicationContext!=null) {
                            BeanHolder beanHolder = applicationContext.getBean(BeanHolder.class);
                            beanHolder.getBizBeanMap().entrySet().stream().forEach(
                                e -> win.setMember(e.getKey(), e.getValue()));
                        }
                    }
                }
            });

        webView.getEngine().load(getClass().getResource("/templates/common/loading.html").toExternalForm());


        VBox vBox=new VBox();
        HBox hBox=new HBox(10);
        hBox.setMinWidth(250);
        TextField urlInput=new TextField();
        urlInput.setPrefWidth(150);
        urlInput.setPromptText("请输入网址");

        Button load=new Button("打开");
        //load.getStyleClass().setAll("btn","btn-danger");
        load.setOnAction(e->{
            String url=urlInput.getCharacters().toString();
            System.out.println("input url:"+url);
            if(!url.startsWith("http")){
                url="http://"+url;
            }
            webView.getEngine().load(url);
        });

        Button reload=new Button("reload");
        reload.setOnAction(e->{
            webView.getEngine().reload();
        });

        Button back=new Button("<<");
        back.setOnAction(e->{
            webView.getEngine().executeScript("history.back()");
        });

        Button forward=new Button(">>");
        forward.setOnAction(e->{
            webView.getEngine().executeScript("history.forward()");
        });

        HBox.setHgrow(urlInput, Priority.ALWAYS);
        HBox.setHgrow(load, Priority.ALWAYS);
        HBox.setHgrow(reload, Priority.ALWAYS);
        HBox.setHgrow(back, Priority.ALWAYS);
        HBox.setHgrow(forward, Priority.ALWAYS);
        urlInput.setMaxWidth(Double.MAX_VALUE);
        //load.setMaxWidth(Double.MAX_VALUE);
        hBox.getChildren().addAll(urlInput,load,reload,back,forward);
        VBox.setVgrow(webView,Priority.ALWAYS);
        vBox.getChildren().addAll(hBox,webView);

        Scene scene=new Scene(vBox,getScreenSize().getWidth()*0.85,-1);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("fxbootdemo");
        //mac os x look and feel
        AquaFx.style();

        primaryStage.show();
    }

    public Rectangle2D getScreenSize(){
        //http://java-buddy.blogspot.com/2013/11/get-screen-size-using-javafxstagescreen.html
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        return visualBounds;
    }
    public static void main(String[] args) {
        launchApp(args);
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public WebView getWebView() {
        return webView;
    }
}
