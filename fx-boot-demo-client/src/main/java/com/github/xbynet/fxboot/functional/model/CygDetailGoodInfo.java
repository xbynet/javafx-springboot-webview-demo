package com.github.xbynet.fxboot.functional.model;

import com.alibaba.fastjson.JSON;
import com.github.xbynet.fxboot.util.PinYinUtil;
import com.github.xbynet.fxboot.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anzhou.tjw
 * @date 2018/6/13 下午8:10
 */
public class CygDetailGoodInfo extends CygListGoodInfo{

    private static ConcurrentHashMap<String,Field> fieldMap=new ConcurrentHashMap<>();
    //宝石修炼评分
    private int bsxlScore;
    //宝石进阶评分
    private int bsjjScore;
    //心法评分
    private int xfScore;

    //抗性
    private int bk;

    private int hk;

    private int xk;

    private int dk;

    //攻击
    private int bgj;

    private int hgj;

    private int xgj;

    private int dgj;

    //减抗
    private int jbk;

    private int jhk;

    private int jxk;

    private int jdk;

    //减抗下限
    private int jbkxx;
    private int jhkxx;
    private int jxkxx;
    private int jdkxx;

    //穿刺伤害
    private int ccsh;
    //穿刺减免
    private int ccjm;

    //血量
    private int blood;
    //命中
    private int mz;
    //闪避
    private int sb;
    //会心
    private int hx;
    //会防
    private int hf;

    //万宝信息
    private String wbInfo;

    //饺子
    private int jiaozi;

    //金币
    private int jinbi;

    //元宝
    private int yuanbao;

    //称号
    private String chenghao;

    //生活技能信息
    private int caikuang;
    private int zhongzhi;
    private int caiyao;

    private boolean has30;

    private  int yfCount;
    //鲤戏澜芳
    private boolean hasLxnf;
    //仙驹
    private boolean hasXj;
    //沧澜翼羽?
    private boolean hasYy;
    //金鱼
    private boolean hasDajinyu;

    public int getBsxlScore() {
        return bsxlScore;
    }

    public void setBsxlScore(int bsxlScore) {
        this.bsxlScore = bsxlScore;
    }

    public int getBsjjScore() {
        return bsjjScore;
    }

    public void setBsjjScore(int bsjjScore) {
        this.bsjjScore = bsjjScore;
    }

    public int getBk() {
        return bk;
    }

    public void setBk(int bk) {
        this.bk = bk;
    }

    public int getHk() {
        return hk;
    }

    public void setHk(int hk) {
        this.hk = hk;
    }

    public int getXk() {
        return xk;
    }

    public void setXk(int xk) {
        this.xk = xk;
    }

    public int getDk() {
        return dk;
    }

    public void setDk(int dk) {
        this.dk = dk;
    }

    public int getBgj() {
        return bgj;
    }

    public void setBgj(int bgj) {
        this.bgj = bgj;
    }

    public int getHgj() {
        return hgj;
    }

    public void setHgj(int hgj) {
        this.hgj = hgj;
    }

    public int getXgj() {
        return xgj;
    }

    public void setXgj(int xgj) {
        this.xgj = xgj;
    }

    public int getDgj() {
        return dgj;
    }

    public void setDgj(int dgj) {
        this.dgj = dgj;
    }

    public int getJbk() {
        return jbk;
    }

    public void setJbk(int jbk) {
        this.jbk = jbk;
    }

    public int getJhk() {
        return jhk;
    }

    public void setJhk(int jhk) {
        this.jhk = jhk;
    }

    public int getJxk() {
        return jxk;
    }

    public void setJxk(int jxk) {
        this.jxk = jxk;
    }

    public int getJdk() {
        return jdk;
    }

    public void setJdk(int jdk) {
        this.jdk = jdk;
    }

    public int getJbkxx() {
        return jbkxx;
    }

    public void setJbkxx(int jbkxx) {
        this.jbkxx = jbkxx;
    }

    public int getJhkxx() {
        return jhkxx;
    }

    public void setJhkxx(int jhkxx) {
        this.jhkxx = jhkxx;
    }

    public int getJxkxx() {
        return jxkxx;
    }

    public void setJxkxx(int jxkxx) {
        this.jxkxx = jxkxx;
    }

    public int getJdkxx() {
        return jdkxx;
    }

    public void setJdkxx(int jdkxx) {
        this.jdkxx = jdkxx;
    }

    public int getCcsh() {
        return ccsh;
    }

    public void setCcsh(int ccsh) {
        this.ccsh = ccsh;
    }

    public int getCcjm() {
        return ccjm;
    }

    public void setCcjm(int ccjm) {
        this.ccjm = ccjm;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getMz() {
        return mz;
    }

    public void setMz(int mz) {
        this.mz = mz;
    }

    public int getSb() {
        return sb;
    }

    public void setSb(int sb) {
        this.sb = sb;
    }

    public int getHx() {
        return hx;
    }

    public void setHx(int hx) {
        this.hx = hx;
    }

    public int getHf() {
        return hf;
    }

    public void setHf(int hf) {
        this.hf = hf;
    }

    public String getWbInfo() {
        return wbInfo;
    }

    public void setWbInfo(String wbInfo) {
        this.wbInfo = wbInfo;
    }

    public int getJiaozi() {
        return jiaozi;
    }

    public void setJiaozi(int jiaozi) {
        this.jiaozi = jiaozi;
    }

    public int getJinbi() {
        return jinbi;
    }

    public void setJinbi(int jinbi) {
        this.jinbi = jinbi;
    }

    public int getYuanbao() {
        return yuanbao;
    }

    public void setYuanbao(int yuanbao) {
        this.yuanbao = yuanbao;
    }

    public int getCaikuang() {
        return caikuang;
    }

    public void setCaikuang(int caikuang) {
        this.caikuang = caikuang;
    }

    public int getZhongzhi() {
        return zhongzhi;
    }

    public void setZhongzhi(int zhongzhi) {
        this.zhongzhi = zhongzhi;
    }

    public int getCaiyao() {
        return caiyao;
    }

    public void setCaiyao(int caiyao) {
        this.caiyao = caiyao;
    }

    public boolean isHas30() {
        return has30;
    }

    public void setHas30(boolean has30) {
        this.has30 = has30;
    }

    public int getYfCount() {
        return yfCount;
    }

    public void setYfCount(int yfCount) {
        this.yfCount = yfCount;
    }

    public boolean isHasLxnf() {
        return hasLxnf;
    }

    public void setHasLxnf(boolean hasLxnf) {
        this.hasLxnf = hasLxnf;
    }

    public boolean isHasXj() {
        return hasXj;
    }

    public void setHasXj(boolean hasXj) {
        this.hasXj = hasXj;
    }

    public boolean isHasYy() {
        return hasYy;
    }

    public void setHasYy(boolean hasYy) {
        this.hasYy = hasYy;
    }

    public boolean isHasDajinyu() {
        return hasDajinyu;
    }

    public void setHasDajinyu(boolean hasDajinyu) {
        this.hasDajinyu = hasDajinyu;
    }

    public String getChenghao() {
        return chenghao;
    }

    public void setChenghao(String chenghao) {
        this.chenghao = chenghao;
    }

    public int getXfScore() {
        return xfScore;
    }

    public void setXfScore(int xfScore) {
        this.xfScore = xfScore;
    }

    public static void parse(Document doc,CygDetailGoodInfo info){
        parseBaseInfoTab0(doc,info);
        parseSkillTab1(doc,info);
        parseZhengShouTab3(doc,info);
        parseRepoTab4(doc,info);
    }

    private static CygDetailGoodInfo parseBaseInfoTab0(Document doc, CygDetailGoodInfo info){
        Element e=doc.select("#goods-detail").first();
        //角色装备信息
        Elements zbInfo=e.select(".role-show > span");
        Arrays.asList("bing","huo","xuan","du","sword","shield").stream().forEach(id->{
            zbInfo.stream().forEach(el->{
                Elements tmpP=el.select("#"+id+" .model .c-o-l >p");
                tmpP.eachText().stream().forEach(v->{
                    String name=v.split(" \\+")[0].split("：")[0];
                    int value= Integer.valueOf(StringUtil.subStr(v,"\\d+"));
                    name=PinYinUtil.getFirstSpell(name);
                    if(!fieldMap.containsKey(name)){
                        ReflectionUtils.getAllFields(CygDetailGoodInfo.class).stream().forEach(f->{
                            fieldMap.putIfAbsent(f.getName(),f);
                        });
                    }
                    Field field=fieldMap.get(name);
                    field.setAccessible(true);
                    try {
                        field.set(info,value);
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                });
            });
        });

        //基本信息
        Elements baseInfo=e.select(".box2.h422 > div");
        baseInfo.stream().forEach(el->{
            String allText=el.text();
            if(allText.contains("血上限")){
                String value=el.select("span > i").text();
                info.setBlood(Integer.valueOf(value));
            }else if(allText.contains("命中")){
                info.setMz(Integer.valueOf(el.select("span").first().ownText()));
            }else if(allText.contains("闪避")){
                info.setSb(Integer.valueOf(el.select("span").first().ownText()));
            }else if(allText.contains("会心攻击")){
                info.setHx(Integer.valueOf(el.select("span").text()));
            }else if(allText.contains("会心防御")){
                info.setHf(Integer.valueOf(el.select("span").text()));
            }
        });

        ConcurrentHashMap<String,Integer> wbMap=new ConcurrentHashMap<>();
        //万宝信息
        Elements wbInfo=e.select(".wanbao-bd > ul > li");
        wbInfo.stream().forEach(el->{
            List<String> list=el.select("span.role-good-item").eachAttr("title");
            list.stream().forEach(v->{
                if(StringUtils.isNotBlank(v)){
                    BaoShiInfo baoShiInfo=BaoShiInfo.from(v);
                    String key=baoShiInfo.getName()+"_"+baoShiInfo.getLevel()+"_"+baoShiInfo.isPure();
                    if(wbMap.putIfAbsent(key,1)!=null){
                        wbMap.computeIfPresent(key,(s,i)->i+1);
                    }
                }
            });
        });
        info.setWbInfo(JSON.toJSONString(wbMap));

        //其余信息
        Elements otherInfo=e.select(".other-info .box2 > div");
        otherInfo.stream().forEach(el->{
            String text=el.ownText();
            String value=el.select("span").first().ownText();

            if(text.contains("当前称号")){
                info.setChenghao(value);
            }else if(text.contains("心法评分")){
                info.setXfScore(Integer.valueOf(value));
            }else if(text.contains("宝石修炼评分")){
                info.setBsxlScore(Integer.valueOf(value));
            }else if(text.contains("宝石进阶评分")){
                info.setBsjjScore(Integer.valueOf(value));
            }else if(text.contains("交子")){
                info.setJiaozi(Integer.valueOf(value));
            }else if(text.contains("金币")){
                info.setJinbi(Integer.valueOf(value));
            }else if(text.contains("所有元宝")){
                info.setYuanbao(Integer.valueOf(value));
            }
        });

        return info;
    }

    private static void parseSkillTab1(Document doc,CygDetailGoodInfo info){
        Element e=doc.select("#tab_1").first();
        String htmlSegment=e.html();
        Document subDoc=Jsoup.parse(htmlSegment);
        subDoc.select("#life-skills .method-info").stream().forEach(el->{
            String name=el.select("p").first().text();
            int level=Integer.valueOf(el.select("p").last().text().split(":")[1]);
            switch (name){
                case "采矿":
                    info.setCaikuang(level);break;
                case "种植":
                    info.setZhongzhi(level);break;
                case "采药":
                    info.setCaiyao(level);break;
            }
        });
    }

    private static void parseZhengShouTab3(Document doc,CygDetailGoodInfo info){
        Element e=doc.select("#tab_3").first();
        String htmlSegment=e.html();
        Document subDoc=Jsoup.parse(htmlSegment);

        info.setHas30(false);
        subDoc.select("#zhenshouBox > .animal-info").stream().forEach(el->{
            final int[] treeNum=new int[3];//ronghe,wuxing,lixing
            el.select(".box2").stream().forEach(ele->{
                if(ele.text().contains("融合度")){
                    Element p=ele.select("p").stream().filter(tmp->tmp.ownText().contains("融合度")).findFirst().get();
                    treeNum[0]=Integer.valueOf(p.select("span").text());
                }else if(ele.text().contains("悟性")){
                    ele.select("span").stream().forEach(tmp->{
                        if(tmp.ownText().contains("悟性")){
                            treeNum[1]=Integer.valueOf(tmp.select("i").text());
                        }else if(tmp.ownText().contains("灵性")){
                            treeNum[2]=Integer.valueOf(tmp.select("i").text());
                        }
                    });
                }
            });
            if(!info.isHas30()) {
                info.setHas30(Arrays.stream(treeNum).allMatch(n -> n == 10));
            }
        });

    }

    private static void parseRepoTab4(Document doc,CygDetailGoodInfo info){
        Element e=doc.select("script").stream().filter(s->{
            String html=s.html();
            return html.contains("var charObj =");
        }).findFirst().get();
        String content=e.html();
        //todo 待优化，需判断时装是否为永久，并排除门派服装
        int yfCount=StringUtils.countMatches(content,"时装");

        info.setYfCount(yfCount);
        info.setHasLxnf(content.contains("鲤戏澜芳"));
        //todo 大金鱼
        //info.setHasDajinyu(content.contains(""));
        info.setHasXj(content.contains("仙驹"));
        info.setHasYy(content.contains("沧澜"));

    }

    public static void main(String[] args) {
        Document document=Jsoup.parse("<div class=\"row2\">体力：\n"
            + "                    <span class=\"span\">13797\n"
            + "                        <b class=\"plus\">(+438)</b>\n"
            + "                    </span>\n"
            + "                </div>");
        System.out.println(document.select("span").first().ownText());
        System.out.println();

        document=Jsoup.parse("<script>var a=b;</script>");
        System.out.println(document.select("script").html());

    }
}
