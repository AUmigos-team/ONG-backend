package br.com.luzdosbichos.storage;

import java.io.InputStream;

public interface StorageService {
    String upload(String key, InputStream input, String contentType, long size);
    void delete(String key);
    boolean exists(String key);
}
