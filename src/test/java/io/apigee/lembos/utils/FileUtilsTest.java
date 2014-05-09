package io.apigee.lembos.utils;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests that {@link FileUtils} works as expected.
 */
public class FileUtilsTest {

    /**
     * Unit test for {@link FileUtils#createZipFile(File)} fails properly when the path to zip
     * is null.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testCreateZipFileWithNullPath() throws Exception {
        try {
            FileUtils.createZipFile(null);

            fail("The line above should had failed");
        } catch (RuntimeException e) {
            assertEquals("The path to archive cannot be null", e.getMessage());
        }
    }

    /**
     * Unit test for {@link FileUtils#createZipFile(File)} fails properly when the path to zip
     * does not exist.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testCreateZipFileWithPathNotExisting() throws Exception {
        try {
            FileUtils.createZipFile(new File(UUID.randomUUID().toString()));

            fail("The line above should had failed");
        } catch (RuntimeException e) {
            assertEquals("The path to archive does not exist", e.getMessage());
        }
    }

    /**
     * Unit test for {@link FileUtils#createZipFile(File)} works properly.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testCreateZipFile() throws Exception {
        final String basePath = TestUtils.class.getResource("/").getPath() + File.separator + "io" + File.separator
                + "apigee" + File.separator + "lembos";
        final File folder = new File(basePath);
        final File file = new File(basePath + File.separator + "utils" + File.separator + getClass().getSimpleName()
                                           + ".class");
        final File folderZip = FileUtils.createZipFile(folder);
        final File fileZip = FileUtils.createZipFile(file);

        assertTrue(folderZip.exists());
        assertTrue(fileZip.exists());

        List<String> expectedZipPaths = getExpectedZipContents(folder);
        List<String> actualZipPaths = getZipContents(folderZip);

        for (final String zippedPath : actualZipPaths) {
            expectedZipPaths.remove(zippedPath);
        }

        assertEquals(0, expectedZipPaths.size());

        expectedZipPaths = getExpectedZipContents(file);
        actualZipPaths = getZipContents(fileZip);

        for (final String zippedPath : actualZipPaths) {
            expectedZipPaths.remove(zippedPath);
        }

        assertEquals(0, expectedZipPaths.size());
    }

    /**
     * Returns a list of paths in the zip file.
     *
     * @param file the zip file
     *
     * @return the zip file paths as a list
     *
     * @throws IOException if anything goes wrong
     */
    private List<String> getZipContents(final File file) throws IOException {
        final List<String> zipPaths = Lists.newArrayList();

        try (final ZipFile zipFile = new ZipFile(file)) {
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                zipPaths.add(entries.nextElement().getName());
            }
        }

        return zipPaths;
    }

    /**
     * Returns a list of paths expected to be in the zip file.
     *
     * @param zippedPath the path being zipped
     *
     * @return the file paths as a list
     *
     * @throws IOException if anything goes wrong
     */
    private static List<String> getExpectedZipContents(final File zippedPath) throws IOException {
        final List<String> zippedPaths = Lists.newArrayList();
        final String entryBase = zippedPath.getParentFile().getPath() + File.separator;
        Files.walkFileTree(zippedPath.toPath(), new SimpleFileVisitor<Path>() {

            /**
             * {@inheritDoc}
             */
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                zippedPaths.add(file.toAbsolutePath().toString().replace(entryBase, ""));

                return super.visitFile(file, attrs);
            }

        });

        return zippedPaths;
    }

}
