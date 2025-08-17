package cn.xf.basedemo.common.utils;

import cn.xf.basedemo.common.model.EsBaseModel;
import cn.xf.basedemo.common.model.EsSearchModel;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName cn.xf.basedemo.common.utils
 * @author remaindertime
 * @className EsUtil
 * @date 2024/12/10
 * @description elasticsearch工具类
 */
@Slf4j
@Component
public class EsUtil {

    public static ElasticsearchClient esClient;

    {
        esClient = (ElasticsearchClient) ApplicationContextUtils.getBean("elasticsearchClient");
    }

    /**
     * 判断索引是否存在
     * @param indexName
     * @return
     */
    public static boolean existIndex(String indexName) {
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
     * @param esBaseModel
     * @return
     */
    public static boolean addDocument(EsBaseModel esBaseModel) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(esBaseModel.getDocumentModel());
            log.info("es新增文档，文档内容：{}", jsonString);
            // 创建 IndexRequest 实例
            IndexRequest request = new IndexRequest.Builder()
                    .index(esBaseModel.getIndexName())
                    .id(esBaseModel.getDocumentId()) //指定文档id,不指定会自动生成
                    .document(esBaseModel.getDocumentModel())
                    .opType(OpType.Create) // 只会在文档 ID 不存在时创建文档
                    .build();

            IndexResponse response = esClient.index(request);
            if ("created".equals(response.result())) {
                log.info("Document created: " + response.id());
                return true;
            } else {
                log.info("Document already exists or failed to create.");
                return false;
            }
        } catch (Exception e) {
            log.error("es新增文档失败", e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新文档
     * @param esBaseModel
     * @return
     */
    public boolean updateDocument(EsBaseModel esBaseModel) {
        try {
            UpdateRequest updateRequest = new UpdateRequest.Builder<>()
                    .index(esBaseModel.getIndexName())
                    .id(esBaseModel.getDocumentId())
                    .doc(esBaseModel.getDocumentModel()).build();
            UpdateResponse updateResponse = esClient.update(updateRequest, esBaseModel.getClazz());
            log.info("Document updated: " + updateResponse.id());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新文档指定字段（script 脚本）
     * @param esBaseModel
     * @param script 脚本内容
     * @param params 传递参数内容
     */
    public void updateDocumentWithScript(EsBaseModel esBaseModel, String script, Map<String, JsonData> params) {
        try {
            UpdateRequest updateRequest = new UpdateRequest.Builder<>()
                    .index(esBaseModel.getIndexName())
                    .id(esBaseModel.getDocumentId())
                    .script(s ->
                            s.source(script)// 脚本内容：.source("ctx._source.age += params.increment")
                                    .params(params)) // 传递参数内容：.params("increment",sonData.of(5))
                    .build();
            UpdateResponse updateResponse = esClient.update(updateRequest, esBaseModel.getClazz());
            log.info("Document updated: " + updateResponse.id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id查询文档
     * @param esBaseModel
     * @return
     */
    public static <T> T getDocumentById(EsBaseModel esBaseModel) {
        try {
            GetRequest getRequest = new GetRequest.Builder()
                    .index(esBaseModel.getIndexName())
                    .id(esBaseModel.getDocumentId())
                    .build();
            GetResponse<T> getResponse = esClient.get(getRequest, esBaseModel.getClazz());
            if (getResponse.found()) {
                return getResponse.source();
            }
        } catch (Exception e) {
            log.error("es列表查询失败", e);
        }
        return null;
    }

    /**
     * 查询文档列表
     * @param searchModel
     * @return
     */
    public static <T> List<T> getDocumentList(EsSearchModel searchModel) {
        List<T> eslist = new ArrayList<>();
        try {
            SearchResponse<T> search = esClient.search(buildSearchRequest(searchModel), searchModel.getClazz());
            if (Objects.isNull(search)) {
                return eslist;
            }
            HitsMetadata<T> hits = search.hits();
            if (Objects.isNull(hits)) {
                return eslist;
            }
            List<Hit<T>> sourceHitList = hits.hits();
            if (CollectionUtils.isEmpty(sourceHitList)) {
                return eslist;
            }
            sourceHitList.forEach(item -> {
                // 处理每个命中
                eslist.add(item.source());
            });
            return eslist;
        } catch (Exception e) {
            log.error("es列表查询失败", e);
        }
        return eslist;
    }

    /**
     * 查询文档数量
     * @param searchModel
     * @return
     */
    public static long getDocumentCount(EsSearchModel searchModel) {
        try {
            CountRequest.Builder countRequest = new CountRequest.Builder();
            countRequest.index(searchModel.getIndexName());
            countRequest.query(createBoolQuery(searchModel.getTermQuery(), searchModel.getMatchQuery()));
            CountResponse count = esClient.count(countRequest.build());
            if (Objects.isNull(count)) {
                log.info("es列表数量查询异常{}", searchModel);
                return 0;
            }
            return count.count();
        } catch (Exception e) {
            log.error("es列表数量查询失败", e);
        }
        return 0;
    }

    /**
     * 根据id删除文档
     * @param esBaseModel
     * @return
     */
    public static Boolean deleteDocumentById(EsBaseModel esBaseModel) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest.Builder()
                    .index(esBaseModel.getDocumentId())
                    .id(esBaseModel.getDocumentId())
                    .build();
            DeleteResponse deleteResponse = esClient.delete(deleteRequest);
            if ("deleted".equals(deleteResponse.result())) {
                log.info("Document deleted: " + deleteResponse.id());
                return true;
            } else {
                log.info("Document delete failed: " + deleteResponse.id());
                return false;
            }
        } catch (Exception e) {
            log.error("es列表删除失败", e);
        }
        return false;
    }

    /**
     * 根据条件删除文档
     * @param searchModel
     * @return 删除数量
     */
    public static long deleteDocumentByQuery(EsSearchModel searchModel) {
        try {
            DeleteByQueryRequest.Builder deleteRequest = new DeleteByQueryRequest.Builder();
            deleteRequest.index(searchModel.getIndexName());
            deleteRequest.query(createBoolQuery(searchModel.getTermQuery(), searchModel.getMatchQuery()));
            deleteRequest.refresh(true); //设置删除操作后是否立即刷新索引，使删除结果立即可见
            deleteRequest.timeout(new Time.Builder().time("2s").build()); //设置删除操作的超时时间
            deleteRequest.conflicts(Conflicts.Proceed); //Conflicts.Proceed：在版本冲突时继续删除操作;Conflicts.Abort：在版本冲突时中止删除操作
            DeleteByQueryResponse dResponse = esClient.deleteByQuery(deleteRequest.build());
            if (Objects.nonNull(dResponse)) {
                log.info("es条件删除成功，删除数量：{}", dResponse.deleted());
                return dResponse.deleted();
            }
        } catch (Exception e) {
            log.error("es条件删除数据失败", e);
        }
        return 0;
    }

    /**
     * 构建搜索请求对象
     * @param searchModel
     * @return
     */
    private static SearchRequest buildSearchRequest(EsSearchModel searchModel) {
        //定义查询对象
        SearchRequest.Builder searchRequest = new SearchRequest.Builder();
        //设置索引名称
        searchRequest.index(searchModel.getIndexName());
        //分组去重
        if (StringUtils.isNotBlank(searchModel.getRepeatField())) {
            searchRequest.collapse(buildCollapse(searchModel));
        }
        //设置查询条件
        searchRequest.query(createBoolQuery(searchModel.getTermQuery(), searchModel.getMatchQuery()));
        //设置排序规则
        if (searchModel.getSort() != null) {
            searchRequest.sort(buildSort(searchModel.getSort()));
        }
        //设置分页参数
        if (searchModel.getPageSize() != null && searchModel.getPageSize() != null) {
            searchRequest.from(searchModel.getPageSize() * (searchModel.getPageNum() - 1));
            searchRequest.size(searchModel.getPageSize());
        }
        //设置查询字段/排查字段
        SourceConfig sourceConfig = buildSourceConfig(searchModel.getIncludes(), searchModel.getExcludes());
        if (Objects.nonNull(sourceConfig)) {
            searchRequest.source(sourceConfig);
        }
        return searchRequest.build();
    }

    /**
     * 构建查询条件
     * @param termQuery
     * @param matchQuery
     * @return
     */
    private static Query createBoolQuery(Map<String, Object> termQuery, Map<String, Object> matchQuery) {
        BoolQuery.Builder cQuery = new BoolQuery.Builder();
        // TermQuery 精准匹配
        if (termQuery != null) {
            for (Map.Entry<String, Object> entry : termQuery.entrySet()) {
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
                }
            }
        }
        // MatchQuery 模糊匹配全文检索分词查询
        if (matchQuery != null) {
            for (Map.Entry<String, Object> entry : matchQuery.entrySet()) {
                if (Objects.isNull(entry.getValue())) {
                    continue;
                }
                cQuery.must(new MatchQuery.Builder()
                        .field(entry.getKey())
                        .query(entry.getValue().toString())
                        .build()
                        ._toQuery());
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
    public static Query createTimeQuery(String startTime, String endTime, String fieldName) {
        DateRangeQuery dataQuery = new DateRangeQuery.Builder()
                .field(fieldName)
                .build();
        // 时间区间查询
        dataQuery.of(o -> o.gte(startTime));
        dataQuery.of(o -> o.lte(endTime));
        return dataQuery._toRangeQuery()._toQuery();
    }


    /**
     * 设置查询字段/排查字段
     * @param includes 需要字段
     * @param excludes 排除字段
     * @return
     */
    private static SourceConfig buildSourceConfig(List<String> includes, List<String> excludes) {
        boolean isIncludes = CollectionUtils.isEmpty(includes);
        boolean isExcludes = CollectionUtils.isEmpty(excludes);
        //设置查询字段/排查字段
        if (isIncludes || isExcludes) {
            SourceFilter.Builder sourceFilter = new SourceFilter.Builder();
            if (isIncludes)
                sourceFilter.includes(includes);
            if (isExcludes)
                sourceFilter.excludes(excludes);
            return new SourceConfig.Builder().filter(sourceFilter.build()).build();
        }
        return null;
    }


    /**
     * 构建分组去重
     * @param searchModel
     * @return
     */
    private static FieldCollapse buildCollapse(EsSearchModel searchModel) {
        FieldCollapse.Builder fieldCollapse = new FieldCollapse.Builder();
        //设置分组字段
        fieldCollapse.field(searchModel.getRepeatField());
        //设置嵌套配置
        if (StringUtils.isNotBlank(searchModel.getInnerAlias())) {
            InnerHits.Builder innerHits = new InnerHits.Builder();
            //设置别名
            innerHits.name(searchModel.getInnerAlias());
            //设置查询数量
            if (searchModel.getInnerSize() != null) {
                innerHits.size(searchModel.getInnerSize());
            }
            fieldCollapse.innerHits(InnerHits.of(i -> i.name(searchModel.getInnerAlias()).size(10)));
        }
        return fieldCollapse.build();
    }

    /**
     * 构建排序规则
     * @param sortMap
     * @return
     */
    private static List<SortOptions> buildSort(Map<String, String> sortMap) {
        if (sortMap == null) {
            return null;
        }
        List<SortOptions> sortList = new ArrayList<>();
        for (Map.Entry<String, String> sort : sortMap.entrySet()) {
            sortList.add(new SortOptions.Builder().field(f -> f.field(sort.getKey()).order(SortOrder.valueOf(sort.getValue()))).build());
        }
        return sortList;
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
