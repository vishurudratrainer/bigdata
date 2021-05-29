import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
    static enum CountersEnum { INPUT_WORDS }

    private boolean caseSensitive;
    private Set<String> patternsToSkip = new HashSet<String>();

    private Configuration configuration;
    private BufferedReader bufferedReader;

    @Override
    public void setup(Context context) throws IOException {
        configuration = context.getConfiguration();
        caseSensitive = configuration.getBoolean("wordcount.case.sensitive", true);

        // Skip certain configured non-word patterns
        if (configuration.getBoolean("wordcount.skip.patterns", false)) {
            URI[] patternsURIs = Job.getInstance(configuration).getCacheFiles();
            for (URI patternsURI : patternsURIs) {
                Path patternsPath = new Path(patternsURI.getPath());
                String patternsFileName = patternsPath.getName().toString();
                parsePatternSkipFile(patternsFileName);
            }
        }
    }

    @Override
    public void map(Object key, Text input, Context context) {
        String line = caseSensitive
                ? input.toString()
                : input.toString().toLowerCase();

        // Remove all the parsed patterns we want to skip.
        for (String pattern : patternsToSkip) {
            line = line.replaceAll(pattern, "");
        }

        try {
            StringTokenizer tokenizer = new StringTokenizer(input.toString());
            while (tokenizer.hasMoreElements()) {
                Text word = new Text();
                word.set(tokenizer.nextToken());
                context.write(word, new IntWritable(1));

                Counter counter = context.getCounter(CountersEnum.class.getName(),
                        CountersEnum.INPUT_WORDS.toString());
                counter.increment(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parsePatternSkipFile(String fileName) {
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            String pattern = null;
            while ((pattern = bufferedReader.readLine()) != null) {
                patternsToSkip.add(pattern);
            }
        } catch (IOException e) {
            System.err.println("Caught exception with parsing the cached file: "
                    + StringUtils.stringifyException(e));
        }
    }
}
