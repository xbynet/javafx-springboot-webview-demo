package com.github.xbynet.fxboot.functional.model;

import com.github.xbynet.fxboot.annotation.Tokenize;
import org.jsoup.nodes.Element;

/**
 * @author anzhou.tjw
 * @date 2018/6/13 下午7:41
 */
public class CygListGoodInfo {

    private String sellId;

    private String url;

    private String sex;

    @Tokenize
    private String name;

    //等级
    private int grade;

    //重楼
    private boolean chonglong;

    //装备评分
    private int zbScore;

    //修炼评分
    private int xlScore;

    //进阶评分
    private int jjScore;

    //区服
    @Tokenize
    private String server;

    //剩余时间
    private String date;

    private int price;

    //门派
    private String mengpai;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isChonglong() {
        return chonglong;
    }

    public void setChonglong(boolean chonglong) {
        this.chonglong = chonglong;
    }

    public int getZbScore() {
        return zbScore;
    }

    public void setZbScore(int zbScore) {
        this.zbScore = zbScore;
    }

    public int getXlScore() {
        return xlScore;
    }

    public void setXlScore(int xlScore) {
        this.xlScore = xlScore;
    }

    public int getJjScore() {
        return jjScore;
    }

    public void setJjScore(int jjScore) {
        this.jjScore = jjScore;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMengpai() {
        return mengpai;
    }

    public void setMengpai(String mengpai) {
        this.mengpai = mengpai;
    }

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public static CygListGoodInfo parse(Element e){
        CygListGoodInfo info=new CygListGoodInfo();
        String url=e.select("span > a").attr("href");
        info.setUrl(url);
        info.setSellId(url.split("serial_num=")[1].split("&")[0]);
        String[] tmpInfo=e.select("dl > dt > a > span").text()
            .replace("[","").replace("]","").split(" ");
        info.setMengpai(tmpInfo[0]);
        info.setSex(tmpInfo[1]);
        info.setGrade(Integer.valueOf(tmpInfo[2].replace("级","")));
        info.setName(e.select("dl > dt > a ").text());
        Element cl=e.attr("title","该角色拥有重楼装备");
        info.setChonglong(cl!=null);
        org.jsoup.select.Elements scoreEl=e.select("dd.detail > span > b");
        info.setZbScore(Integer.valueOf(scoreEl.get(0).text()));
        info.setXlScore(Integer.valueOf(scoreEl.get(1).text()));
        info.setJjScore(Integer.valueOf(scoreEl.get(2).text()));
        info.setServer(e.select(".server-info").attr("title"));
        String timeStr=e.select(".time").text().split("：")[1];
        info.setDate(timeStr);
        info.setPrice(Integer.valueOf(e.select(".price").text().replace("￥","")));

        return info;
    }

    @Override
    public String toString() {
        return "CygListGoodInfo{" +
            "url='" + url + '\'' +
            ", sex='" + sex + '\'' +
            ", name='" + name + '\'' +
            ", grade=" + grade +
            ", chonglong=" + chonglong +
            ", zbScore=" + zbScore +
            ", xlScore=" + xlScore +
            ", jjScore=" + jjScore +
            ", server='" + server + '\'' +
            ", date=" + date +
            ", price=" + price +
            ", mengpai='" + mengpai + '\'' +
            '}';
    }
}
