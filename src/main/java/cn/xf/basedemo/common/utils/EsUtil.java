package cn.xf.basedemo.common.utils;

import cn.xf.basedemo.common.model.EsModel;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.OpType;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName cn.xf.basedemo.common.utils
 * @author remaindertime
 * @className EsUtil
 * @date 2024/12/10
 * @description elasticsearch工具类
 */
//@Component
@Slf4j
public class EsUtil {

//    @Autowired
    private static ElasticsearchClient esClient;

    /**
     * 判断索引是否存在
     * @param indexName
     * @return
     */
    private static boolean existIndex(String indexName) {
        try {
            // 创建 ExistsRequest 请求
            ExistsRequest request = new ExistsRequest.Builder()
                    .index(indexName)
                    .build();
            // 发送请求并获取响应
            BooleanResponse response = esClient.indices().exists(request);
            // 返回索引是否存在
            return response.value();
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除索引
     *
     * @param indexName
     */
    @SneakyThrows
    public static void delIndex(String indexName) {
        if (existIndex(indexName)) {
            return;
        }
        esClient.indices().delete(d -> d.index(indexName));
    }

    /**
     * 创建索引
     *
     * @param indexName
     * @return
     */
    public static void createIndex(String indexName) {
        if (existIndex(indexName)) {
            throw new RuntimeException("索引已经存在");
        }
        try {
            CreateIndexResponse createIndexResponse = esClient.indices().create(c -> c.index(indexName));
            // 处理响应
            if (createIndexResponse.acknowledged()) {
                log.info(" indexed create successfully.");
            } else {
                log.info("Failed to create index.");
            }
        } catch (Exception e) {
            // 捕获异常并打印详细错误信息
            e.printStackTrace();
            throw new RuntimeException("创建索引失败，索引名：" + indexName + "，错误信息：" + e.getMessage(), e);
        }
    }

    /**
     * 新增文档
     * @param esModel
     * @return
     */
    public static boolean addDocument(EsModel esModel) {
        try {
            // 创建 IndexRequest 实例
            IndexRequest request = new IndexRequest.Builder()
                    .index(esModel.getIndexName())
                    .id(esModel.getDocumentId()) //指定文档id,不指定会自动生成
                    .document(esModel.getDocumentModel())
                    .opType(OpType.Create).build(); // 只会在文档 ID 不存在时创建文档
            IndexResponse response = esClient.index(request);
            if ("created".equals(response.result())) {
                log.info("Document created: " + response.id());
                return true;
            } else {
                log.info("Document already exists or failed to create.");
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新文档
     * @param esModel
     * @return
     */
    public boolean updateDocument(EsModel esModel) {
        try {
            UpdateRequest updateRequest = new UpdateRequest.Builder<>()
                    .index(esModel.getIndexName())
                    .id(esModel.getDocumentId())
                    .doc(esModel.getDocumentModel()).build();
            UpdateResponse updateResponse = esClient.update(updateRequest, esModel.getClazz());
            log.info("Document updated: " + updateResponse.id());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新文档指定字段（script 脚本）
     * @param esModel
     * @param script 脚本内容
     * @param params 传递参数内容
     */
    public void updateDocumentWithScript(EsModel esModel, String script, Map<String, JsonData> params) {
        try {
            UpdateRequest updateRequest = new UpdateRequest.Builder<>()
                    .index(esModel.getIndexName())
                    .id(esModel.getDocumentId())
                    .script(s ->
                            s.source(script)// 脚本内容：.source("ctx._source.age += params.increment")
                                    .params(params)) // 传递参数内容：.params("increment",sonData.of(5))
                    .build();
            UpdateResponse updateResponse = esClient.update(updateRequest, esModel.getClazz());
            log.info("Document updated: " + updateResponse.id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建查询条件
     * @param queryMap
     * @return
     */
    public Query createBoolQuery(Map<String, Object> queryMap) {
        BoolQuery.Builder cQuery = new BoolQuery.Builder();
        for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
            if (Objects.isNull(entry.getValue())) {
                continue;
            }
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value.getClass().isArray()) { //数组查询，使用 TermsQuery
                Object[] values = (Object[]) entry.getValue();
                List<FieldValue> objs = Arrays.stream(values)
                        .map(v -> FieldValue.of(v))  // 将每个对象转换为 FieldValue
                        .collect(Collectors.toList());
                cQuery.must(new TermsQuery.Builder()
                        .field(key)
                        .terms(t -> t.value(objs))
                        .build()
                        ._toQuery());
            } else if (value.toString().contains(" ")) {  // 短语查询,使用 MatchPhraseQuery (要严格按照单词顺序字符串中有空格，短信需匹配)
                cQuery.must(new MatchPhraseQuery.Builder()
                        .field(key)
                        .query(value.toString())
                        .build()
                        ._toQuery());
            } else { // 其他情况，使用 TermQuery 精准匹配
                cQuery.must(new TermQuery.Builder()
                        .field(key)
                        .value(value.toString())
                        .build()
                        ._toQuery());
                // 使用 MatchQuery 模糊匹配全文检索分词查询
//                 cQuery.must(new MatchQuery.Builder()
//                 .field(entry.getKey())
//                 .query(value.toString())
//                 .build()
//                 ._toQuery());
            }
        }
        return cQuery.build()._toQuery();
    }
    /**
     * 构建时间区间查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param fieldName 时间字段
     * @return
     */
    public Query createTimeQuery(String startTime, String endTime, String fieldName) {
        DateRangeQuery dataQuery = new DateRangeQuery.Builder()
                .field(fieldName)
                .build();
        // 时间区间查询
        dataQuery.of(o -> o.gte(startTime));
        dataQuery.of(o -> o.lte(endTime));
        return dataQuery._toRangeQuery()._toQuery();
    }


    /**
     * 案例：组合多条件查询(关于 must、mustNot、should 条件的使用)
     */
    public Query combinationQueryTest() {
        //query.must()：and 文档必须满足该条件，如果不满足，文档将不匹配。 and
        //query.should()：or 文档可以不满足该条件，但满足该条件时会得分更高；即使不满足，文档也会出现在查询结果中,只是查询结果靠后。

        //场景1：文档必须符合所有 must 条件和 mustNot 条件，同时至少满足一个 should 条件。如果 should 条件都不满足，文档将被排除不查询出来。
        BoolQuery.Builder query = new BoolQuery.Builder();
        //数字范围查询
        NumberRangeQuery.Builder numberQuery = new NumberRangeQuery.Builder();
        numberQuery.field("age").lte(30.0).build();
        // 构建查询条件
        query.must(o -> o.term(t -> t.field("status").value("active"))) // 必须满足的条件
                .mustNot(o -> o.term(t -> t.field("country").value("China"))) // 不能满足的条件
                .filter(f -> f.bool(bo -> bo
                        .should(so -> so.range(r -> r.number(numberQuery.build()))) // 至少满足一个 should 条件
                        .should(so -> so.term(t -> t.field("gender").value("male"))) // 至少满足一个 should 条件
                        .minimumShouldMatch("1")  // 至少满足一个 should 条件 也可设置百分比 “50%”
                ));
        //场景2：文档必须符合所有 must 条件和 mustNot 条件，同时至少满足一个 should 条件。如果 should 条件都不满足，不用做额外的过滤（按照should原生特性处理）。
        query.must(o -> o.bool(bo -> bo
                        .should(so -> so.range(r -> r.number(numberQuery.build()))) // 至少满足一个 should 条件
                        .should(so -> so.term(t -> t.field("gender").value("male"))) // 至少满足一个 should 条件
                        .minimumShouldMatch("1")  // 至少满足一个 should 条件
                ))
                .must(o -> o.term(t -> t.field("status").value("active"))) // 必须满足的条件
                .mustNot(o -> o.term(t -> t.field("country").value("China"))); // 不能满足的条件

        return query.build()._toQuery();
    }

}
