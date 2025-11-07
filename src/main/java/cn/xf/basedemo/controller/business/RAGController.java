package cn.xf.basedemo.controller.business;


import cn.xf.basedemo.common.model.RetObj;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.core.io.PathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * RAGController
 *
 * @author 海言
 * @date 2025/10/27
 * @time 14:01
 * @Description RAG 搜索 相关接口
 */
@Slf4j
@RestController()
@RequestMapping("/ai/ollama/rag")
public class RAGController {

    @Resource
    private TokenTextSplitter tokenTextSplitter;
    @Resource(name = "ollamaPgVectorStore")
    private PgVectorStore pgVectorStore;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询 rag 标签列表
     *
     * @return
     */
    @RequestMapping(value = "query_rag_tag_list", method = RequestMethod.GET)
    public RetObj<List<String>> queryRagTagList() {
        List<String> range = redisTemplate.opsForList().range("ai:arg:tags", 0, -1);
        return RetObj.success(range);
    }

    /**
     * 上传知识库文件，并传入标签
     *
     * @param ragTag
     * @param files
     * @return
     */
    @RequestMapping(value = "file/upload", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public RetObj<String> uploadFile(@RequestParam String ragTag, @RequestParam("file") List<MultipartFile> files) {
        log.info("上传知识库开始 {}", ragTag);
        for (MultipartFile file : files) {
            TikaDocumentReader documentReader = new TikaDocumentReader(file.getResource());
            List<Document> documents = documentReader.get();
            List<Document> documentSplitterList = tokenTextSplitter.apply(documents);

            // 添加知识库标签
            documents.forEach(doc -> doc.getMetadata().put("knowledge", ragTag));
            documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", ragTag));

            pgVectorStore.accept(documentSplitterList);

            // 添加知识库记录
            List<String> elements = redisTemplate.opsForList().range("ai:arg:tags", 0, -1);
            assert elements != null;
            if (!elements.contains(ragTag)) {
                redisTemplate.opsForList().leftPush("ai:arg:tags", ragTag);
            }
        }
        log.info("上传知识库完成 {}", ragTag);
        return RetObj.success();
    }

    /**
     * 上传仓库中的代码项目
     *
     * @param repoUrl
     * @param userName
     * @param token
     * @return
     */
    @RequestMapping(value = "analyze_git_repository", method = RequestMethod.POST)
    public RetObj<String> analyzeGitRepository(@RequestParam String repoUrl, @RequestParam String userName, @RequestParam String token) throws Exception {
        String localPath = "./git-cloned-repo";
        String repoProjectName = extractProjectName(repoUrl);
        log.info("克隆路径：{}", new File(localPath).getAbsolutePath());

        FileUtils.deleteDirectory(new File(localPath));

        Git git = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(new File(localPath))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, token))
                .call();

        // 使用Files.walkFileTree遍历目录
        Files.walkFileTree(Paths.get(localPath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log.info("{} 遍历解析路径，上传知识库:{}", repoProjectName, file.getFileName());
                try {
                    TikaDocumentReader reader = new TikaDocumentReader(new PathResource(file));
                    List<Document> documents = reader.get();
                    List<Document> documentSplitterList = tokenTextSplitter.apply(documents);

                    documents.forEach(doc -> doc.getMetadata().put("nowledge", repoProjectName));

                    documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", repoProjectName));

                    pgVectorStore.accept(documentSplitterList);
                } catch (Exception e) {
                    log.error("遍历解析路径，上传知识库失败:{}", file.getFileName());
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                log.info("Failed to access file: {} - {}", file.toString(), exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });

        FileUtils.deleteDirectory(new File(localPath));

        // 添加知识库记录
        List<String> elements = redisTemplate.opsForList().range("ai:arg:tags", 0, -1);
        assert elements != null;
        if (!elements.contains(repoProjectName)) {
            redisTemplate.opsForList().leftPush("ai:arg:tags", repoProjectName);
        }
        git.close();
        log.info("遍历解析路径，上传完成:{}", repoUrl);
        return RetObj.success();
    }

    private String extractProjectName(String repoUrl) {
        String[] parts = repoUrl.split("/");
        String projectNameWithGit = parts[parts.length - 1];
        return projectNameWithGit.replace(".git", "");
    }


}
