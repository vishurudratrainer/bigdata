import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CharCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


        String line = value.toString();
        String[] tokenizer = line.split("");
        for (String SingleChar : tokenizer) {
            Text charKey = new Text(SingleChar);
            IntWritable One = new IntWritable(1);
            context.write(charKey, new IntWritable(1));

            Counter counter = context.getCounter(CountersEnum.class.getName(),
                    CountersEnum.INPUT_charS.toString());
            counter.increment(1);
        }
    }


    enum CountersEnum {INPUT_charS}


}
