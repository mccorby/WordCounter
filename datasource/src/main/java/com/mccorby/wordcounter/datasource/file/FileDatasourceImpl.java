package com.mccorby.wordcounter.datasource.file;

import com.mccorby.wordcounter.datasource.ExternalDatasourceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileDatasourceImpl extends ExternalDatasourceImpl {

    private final File mFile;

    public FileDatasourceImpl(File file) {
        this.mFile = file;
    }

    @Override
    protected void postProcessInput(InputStream inputStream) {
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    protected InputStream obtainInputStream() {
        try {
            return new FileInputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
