package io.apigee.lembos.mapreduce;

import io.apigee.lembos.utils.FileUtils;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests for {@link LembosMapReduceRunner}.
 */
public class LembosMapReduceRunnerITest {

    private final Path hdfsTestBase = new Path("/tmp/" + getClass().getSimpleName() + System.currentTimeMillis());
    private final java.nio.file.Path localWordcountPath = FileSystems.getDefault().getPath(TestUtils.getExamplesPath(),
                                                                                           "wordcount");
    private Configuration conf;
    private File tmpModuleContainer;
    private FileSystem fs;
    private String fsDefaultName;

    /**
     * Prepare for each test.
     *
     * @throws Exception if anything goes wrong
     */
    @Before
    public void setUp() throws Exception {
        tmpModuleContainer = Files.createTempDirectory("LembosMapReduceModule").toFile();

        conf = TestUtils.getConfiguration();

        fs = FileSystem.get(conf);
        fsDefaultName = conf.get("fs.default.name");

        if (fsDefaultName.endsWith("/")) {
            fsDefaultName = fsDefaultName.substring(0, fsDefaultName.lastIndexOf("/"));
        }
    }

    /**
     * Cleanup after each test.
     *
     * @throws Exception if anything goes wrong
     */
    @After
    public void tearDown() throws Exception {
        FileUtil.fullyDelete(tmpModuleContainer);
        fs.delete(hdfsTestBase, true);
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a local file module.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testLocalFileModule() throws Exception {
        final java.nio.file.Path modulePath = Files.copy(FileSystems.getDefault().getPath(localWordcountPath.toString(),
                                                                                          "index.js"),
                                                         FileSystems.getDefault()
                                                                    .getPath(tmpModuleContainer.getAbsolutePath(),
                                                                             "wordcount.js"));

        assertEquals(0, runJob(modulePath.toFile().getPath()));
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a local directory module.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testLocalDirectoryModule() throws Exception {
        assertEquals(0, runJob(localWordcountPath.toFile().getAbsolutePath()));
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a local file module as an
     * archive.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testLocalFileModuleArchive() throws Exception {
        final java.nio.file.Path modulePath = Files.copy(FileSystems.getDefault().getPath(localWordcountPath.toString(),
                                                                                          "index.js"),
                                                         FileSystems.getDefault()
                                                                    .getPath(tmpModuleContainer.getAbsolutePath(),
                                                                             "wordcount.js"));
        final File zippedFileModule = FileUtils.createZipFile(modulePath.toFile());
        assertEquals(0, runJob(zippedFileModule.getAbsolutePath()));
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a local directory module as an
     * archive.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testLocalDirectoryModuleArchive() throws Exception {
        final File zippedDirModule = FileUtils.createZipFile(new File(localWordcountPath.toFile().getAbsolutePath()));
        assertEquals(0, runJob(zippedDirModule.getAbsolutePath()));
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a remote file module.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testRemoteFileModule() throws Exception {
        final Path remoteFileModule = new Path(hdfsTestBase, "wordcount.js");

        fs.copyFromLocalFile(new Path(FileSystems.getDefault().getPath(localWordcountPath.toString(),
                                                                       "index.js").toUri()),
                             remoteFileModule);

        assertEquals(0, runJob(fsDefaultName + remoteFileModule.toString()));
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a remote directory module.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testRemoteDirectoryModule() throws Exception {
        final Path remoteDirModule = new Path(hdfsTestBase, "wordcount");

        fs.copyFromLocalFile(new Path(FileSystems.getDefault().getPath(localWordcountPath.toString()).toUri()),
                             remoteDirModule);

        assertEquals(0, runJob(fsDefaultName + remoteDirModule.toString()));
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a remote file module as an
     * archive.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testRemoteFileModuleArchive() throws Exception {
        final java.nio.file.Path modulePath = Files.copy(FileSystems.getDefault().getPath(localWordcountPath.toString(),
                                                                                          "index.js"),
                                                         FileSystems.getDefault()
                                                                    .getPath(tmpModuleContainer.getAbsolutePath(),
                                                                             "wordcount.js"));
        final File localZippedFileModule = FileUtils.createZipFile(modulePath.toFile());
        final Path remoteZippedFileModule = new Path(hdfsTestBase, "wordcount.zip");

        fs.copyFromLocalFile(new Path(FileSystems.getDefault().getPath(localZippedFileModule.toString()).toUri()),
                             remoteZippedFileModule);

        assertEquals(0, runJob(fsDefaultName + remoteZippedFileModule.toString()));
    }

    /**
     * Test {@link LembosMapReduceRunner#run(String[])} ()} works as expected against a remote directory module as an
     * archive.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testRemoteDirectoryModuleArchive() throws Exception {
        final File zippedDirModule = FileUtils.createZipFile(new File(localWordcountPath.toFile().getAbsolutePath()));
        final Path remoteZippedDirModule = new Path(hdfsTestBase, "wordcount.zip");

        fs.copyFromLocalFile(new Path(FileSystems.getDefault().getPath(zippedDirModule.toString()).toUri()),
                             remoteZippedDirModule);

        assertEquals(0, runJob(fsDefaultName + remoteZippedDirModule.toString()));
    }

    /**
     * Runs a job and returns its result.
     *
     * @param modulePath the module path
     *
     * @return the Hadoop job
     *
     * @throws Exception if anything goes wrong
     */
    private int runJob(final String modulePath) throws Exception {
        final Path inputPath = new Path(hdfsTestBase, "4300.txt");
        final Path outputPath = new Path(hdfsTestBase, "uniques");
        final java.nio.file.Path txtInputFile = FileSystems.getDefault().getPath(TestUtils.getExamplesPath(),
                                                                                 "wordcount",
                                                                                 "4300.txt");

        // Copy 4300.txt to HDFS
        fs.copyFromLocalFile(new Path(txtInputFile.toUri()), inputPath);

        // Run the job
        int results = 1;

        try {
            results = ToolRunner.run(conf, new LembosMapReduceRunner(), new String[] {
                    "-fs",
                    fsDefaultName,
                    "-jt",
                    conf.get("mapred.job.tracker"),
                    "-D",
                    LembosConstants.MR_MODULE_NAME + "=wordcount",
                    "-D",
                    LembosConstants.MR_MODULE_PATH + "=" + modulePath,
                    "-D",
                    "mapred.input.dir=" + inputPath.makeQualified(fs).toUri().toString(),
                    "-D",
                    "mapred.output.dir=" + outputPath.makeQualified(fs).toUri().toString(),
                    "-D",
                    "mapred.jar=" + System.getProperty("mapred.jar"),
                    "-D",
                    "HADOOP_USER_NAME=" + conf.get("hadoop.user")
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cleanup HDFS
        fs.delete(hdfsTestBase, true);

        // Return result
        return results;
    }

}
