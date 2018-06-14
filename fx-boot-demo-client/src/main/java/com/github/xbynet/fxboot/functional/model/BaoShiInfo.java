package com.github.xbynet.fxboot.functional.model;

import com.github.xbynet.fxboot.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author anzhou.tjw
 * @date 2018/6/14 上午10:56
 */
public class BaoShiInfo {

    private String name;
    private int level;
    private boolean pure;

    public static final String baoshi="月光石,碧玺,皓石,黄玉,红宝石,红晶石,绿晶石,黄晶石,蓝晶石,绿宝石,祖母绿,变石,紫玉,冥石";
    public BaoShiInfo(){

    }
    public BaoShiInfo(String name, int level, boolean pure){
        this.name=name;
        this.level=level;
        this.pure=pure;
    }

    public static BaoShiInfo from(String desc){
        if(StringUtils.isBlank(desc)){
            return new BaoShiInfo();
        }
        BaoShiInfo baoShiInfo=new BaoShiInfo();
        baoShiInfo.setPure(desc.contains("纯净"));
        Arrays.stream(baoshi.split(",")).forEach(v->{
            if(desc.contains(v)){
                baoShiInfo.setName(v);
            }
        });
        baoShiInfo.setLevel(Integer.valueOf(StringUtil.subStr(desc,"\\d")));
        return baoShiInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isPure() {
        return pure;
    }

    public void setPure(boolean pure) {
        this.pure = pure;
    }
}
