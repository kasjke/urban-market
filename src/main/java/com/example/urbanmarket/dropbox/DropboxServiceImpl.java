package com.example.urbanmarket.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.example.urbanmarket.utils.DropboxUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
@Service
public class DropboxServiceImpl implements DropboxService{
    @Override
    public void createFolder(String path) {
        try {
            DbxClientV2 client = DropboxUtils.getClient();
            client.files().createFolderV2(path);
        } catch (CreateFolderErrorException e) {
            if (e.errorValue.isPath() && e.errorValue.getPathValue().isConflict()) {
                return;
            }
            throw new RuntimeException("Error creating folder in Dropbox", e);
        } catch (DbxException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String uploadImage(String path, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is null or empty");
        }

        try (InputStream inputStream = file.getInputStream()) {
            DbxClientV2 client = DropboxUtils.getClient();
            FileMetadata metadata = client.files().uploadBuilder(path)
                    .withMode(WriteMode.ADD)
                    .uploadAndFinish(inputStream);

            return client.sharing()
                    .createSharedLinkWithSettings(metadata.getPathLower())
                    .getUrl()
                    .replace("www.dropbox.com", "dl.dropboxusercontent.com");
        } catch (IOException | DbxException e) {
            throw new RuntimeException("Error uploading file to Dropbox", e);
        }
    }
}
