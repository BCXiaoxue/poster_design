package com.govy.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data // Lombok注解：自动生成getter/setter/toString等方法
@Schema(description = "用户实体") // Swagger注解：整个类的API描述
@AllArgsConstructor
@NoArgsConstructor
public class University {
    private Long id;

    @Schema(description = "学校名称", example = "上海交通大学", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
