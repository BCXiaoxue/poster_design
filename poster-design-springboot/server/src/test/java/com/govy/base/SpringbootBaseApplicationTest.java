/*
package com.govy.base;

import com.govy.common.properties.CertificateGeneratorProperties;
import com.govy.common.utils.CertificateGeneratorUtil;
import com.govy.common.utils.MinioUtil;
import com.govy.common.utils.ThumbFileUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootBaseApplication.class)
public class SpringbootBaseApplicationTest {

    @Autowired
    private MinioUtil minioUtil;

    @Test
    @SneakyThrows
    public void testAdd() {
        CertificateGeneratorProperties properties = new CertificateGeneratorProperties(
                "绍兴大学",
                "速通小分队",
                new String[]{"小分队", "小分队", "小分队"},
                "大学生海报设计竞赛",
                "一等奖"
        );
        MultipartFile multipartFile = CertificateGeneratorUtil.create(properties);
        MultipartFile file = ThumbFileUtil.startThumbFile(multipartFile);
        String upload = minioUtil.upload(file);
        System.out.println(upload);
    }
}
*/
