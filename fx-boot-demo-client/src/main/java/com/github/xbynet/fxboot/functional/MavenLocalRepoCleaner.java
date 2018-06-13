package com.github.xbynet.fxboot.functional;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anzhou.tjw
 * @date 2018/5/21 下午2:09
 */
public class MavenLocalRepoCleaner {

    /**
     * coordinateJson
     * {
     *     "groupId1":"artifactId1:version1,artifactId2:version2...",
     *     "groupId2":"artifactId:version,..."
     * }
     */
    public static void clean(String pkg,String artifactIds) {
        pkg=pkg==null?"":pkg;
        artifactIds=artifactIds==null?"":artifactIds;
        String coordinateJson="{"
            + "\""+pkg+"\":\""+artifactIds+"\""
            + "}";
        Map<String,String> coordinateMap=JSON.parseObject(coordinateJson,HashMap.class);
        Path m2Repo= Paths.get(System.getProperty("user.home"),".m2","repository");
        coordinateMap.entrySet().stream().forEach(v->{
            String groupId=v.getKey();
            groupId = groupId.replace('.', File.separatorChar);
            if(StringUtils.isBlank(v.getValue())){
                Path dir = Paths.get(m2Repo.toString(), groupId);
                try {
                    FileUtils.deleteDirectory(dir.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                String[] artfactIdVers = v.getValue().split(",");

                for (String str : artfactIdVers) {
                    String ver = "";
                    if (str.contains(":")) {
                        ver = str.split(":")[1];
                    }
                    String artfactId = str.split(":")[0];
                    Path dir = Paths.get(m2Repo.toString(), groupId, artfactId, ver);
                    try {
                        FileUtils.deleteDirectory(dir.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
