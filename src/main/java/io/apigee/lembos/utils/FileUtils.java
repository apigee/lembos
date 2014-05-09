/*
 * Copyright 2014 Apigee Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apigee.lembos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Various utilities for interacting with files.
 */
public final class FileUtils {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Private constructor.
     */
    private FileUtils() { }

    /**
     * Create an archive containing the path supplied, in a temp file, and all children if the path is a directory.
     *
     * @param pathToArchive the path to archive
     *
     * @return the archive
     *
     * @throws IOException if anything goes wrong
     */
    public static File createZipFile(final File pathToArchive) throws IOException {
        if (pathToArchive == null) {
            throw new RuntimeException("The path to archive cannot be null");
        } else if (!pathToArchive.exists()) {
            // This should never happen
            throw new RuntimeException("The path to archive does not exist");
        }

        final File archive = File.createTempFile(UUID.randomUUID().toString(), ".zip");

        // Create archive
        try (final FileOutputStream fos = new FileOutputStream(archive);
             final ZipOutputStream zos = new ZipOutputStream(fos)) {
            final String entryBase = pathToArchive.getParentFile().getPath() + File.separator;
            Files.walkFileTree(pathToArchive.toPath(), new SimpleFileVisitor<Path>() {

                /**
                 * {@inheritDoc}
                 */
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    try (final FileInputStream fis = new FileInputStream(file.toFile())) {
                        final byte[] buffer = new byte[BUFFER_SIZE];

                        zos.putNextEntry(new ZipEntry(file.toAbsolutePath().toString().replace(entryBase, "")));

                        int length;

                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }

                        zos.closeEntry();
                    }

                    return super.visitFile(file, attrs);
                }

            });
        }

        return archive;
    }

}
