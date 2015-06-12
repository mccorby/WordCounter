package com.mccorby.wordcounter.datasource.file;

import com.mccorby.wordcounter.datasource.ExternalDatasourceImpl;
import com.mccorby.wordcounter.domain.abstractions.Bus;

import java.io.IOException;
import java.io.InputStream;

public class FileDatasourceImpl extends ExternalDatasourceImpl {

    @Override
    protected void postProcessInput(InputStream inputStream) {
    }

    @Override
    protected InputStream obtainInputStream() {

        return null;
    }
}
