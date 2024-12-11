package cn.xf.basedemo.common.model;

import lombok.Data;

/**
 * packageName cn.xf.basedemo.common.model
 * @author remaindertime
 * @className EsModel
 * @date 2024/12/10
 * @description es基础模型
 */
@Data
public class EsBaseModel<T> {

    public EsBaseModel(String indexName, String documentId, T documentModel, Class<T> clazz) {
        this.indexName = indexName;
        this.documentId = documentId;
        this.documentModel = documentModel;
        this.clazz = clazz;
    }

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
