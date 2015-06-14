package com.mccorby.wordcounter.datasource.network;

import com.mccorby.wordcounter.datasource.ExternalDatasourceImpl;
import com.mccorby.wordcounter.datasource.entities.WordParser;
import com.mccorby.wordcounter.domain.abstractions.Bus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JAC on 11/06/2015.
 */
public class NetworkDatasourceImpl extends ExternalDatasourceImpl {


    private final URL mUrl;
    private HttpURLConnection connection;

    public NetworkDatasourceImpl(Bus bus, WordParser parser, URL url) {
        super(bus, parser);
        this.mUrl = url;
    }

    @Override
    protected void postProcessInput(InputStream inputStream) {
        try {
            if (inputStream != null)
                inputStream.close();
        } catch (IOException ignored) {
        }

        if (connection != null) {
            connection.disconnect();
        }
    }

    @Override
    protected InputStream obtainInputStream() throws IOException {
        InputStream input;
        connection = null;
        connection = (HttpURLConnection) mUrl.openConnection();
        connection.connect();

        // Expect HTTP 200
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            // Handle server error messages
        }
        // Get the file using the internal app filesystem
        input = connection.getInputStream();
        return input;
    }
}
