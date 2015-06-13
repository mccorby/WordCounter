package com.mccorby.wordcounter.datasource;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.repository.datasources.ExternalDatasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A template method pattern that defines the steps to obtain the words from any InputStream
 * Concretions of this class must provide the way of getting such InputStream object and the operations
 * after it's been used.
 *
 * TODO: The process defined in the extractWords could be done by another object (Parser?)
 * This way we could provide different ways of extracting the information from the input stream
 * (Why not extracting just the numbers? Or only the words in capital?)
 * Such object should be provided via injection.
 *
 * Created by JAC on 11/06/2015.
 */
public abstract class ExternalDatasourceImpl implements ExternalDatasource {

    private OnWordAvailableListener mListener;

    /**
     * Retrieve the input stream this external datasource provides.
     * @return an InputStream object to be parsed
     */
    protected abstract InputStream obtainInputStream();

    /**
     * Operations that must be done after processing the input.
     * Mainly clean-up
     * @param inputStream the input stream used in the process
     */
    protected abstract void postProcessInput(InputStream inputStream);

    /**
     * This method extract the words from the input stream.
     * It uses a regex to match just words.
     * @param inputStream
     * @throws IOException
     */
    private void extractWords(InputStream inputStream) throws IOException {
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = in.readLine()) != null) {
            Pattern pattern = Pattern.compile("\\w+");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                WordOccurrence occurrence = new WordOccurrence(matcher.group());
                if (mListener != null)  {
                    mListener.onWordAvailable(occurrence);
                }
            }
        }
    }

    /**
     * Skeleton of the algorithm in the Template Method pattern
     * @throws IOException if there was a problem with the InputStream
     */
    public final void process() throws IOException{
        InputStream is = null;
        try {
            is = obtainInputStream();
            extractWords(is);
        } finally{
            postProcessInput(is);
            mListener.onProcessDone();
        }
    }

    @Override
    public final void startReadingWords(OnWordAvailableListener listener){
        try {
            this.mListener = listener;
            process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
