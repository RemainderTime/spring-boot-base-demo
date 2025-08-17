package cn.xf.basedemo.common.model;

import lombok.Data;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName cn.xf.basedemo.common.model
 * @author remaindertime
 * @className EsSearchModel
 * @date 2024/12/11
 * @description es 搜索模型
 */
@Data
public class EsSearchModel<T> {

    public EsSearchModel() {
        // 使用 LinkedHashMap 保持插入顺序
        this.sort = new LinkedHashMap<>();
    }

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 文档类型
     */
    private Class<T> clazz;

    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 精准查询字段
     */
    private Map<String, Object> termQuery;

    /**
     * 模糊查询字段(一般是text类型)
     */
    private Map<String, Object> matchQuery;

    /**
     * 排序字段规则 ({"age":"desc"})
     */
    private Map<String, String> sort;

    /**
     * 分组去重字段（支持的字段类型：keyword、numeric、date 和 boolean ）
     */
    private String repeatField;;

    /**
     * 分组嵌套查询别名
     */
    private String innerAlias;

    /**
     * 分组嵌套查询数量
     */
    private Integer innerSize;

    /**
     * 指定需要返回的字段
     */
    private List<String> includes;
    /**
     * 指定需要排除的字段
     */
    private List<String> excludes;

}