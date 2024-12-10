package cn.xf.basedemo.common.model;

import lombok.Data;

/**
 * packageName cn.xf.basedemo.common.model
 * @author remaindertime
 * @className EsModel
 * @date 2024/12/10
 * @description
 */
@Data
public class EsModel<T> {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 文档id
     */
    private String documentId;

    /**
     * 映射对象
     */
    private T documentModel;

    /**
     * 映射对象类对象
     */
    private Class<T> clazz;
}
