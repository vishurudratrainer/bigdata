# Word Count
The infamous word counting MapReduce example using Hadoop MapReduce.

## Input
Files containing words to be counted

## Output
A list of words and their corresponding occurance in the files you provide as the input.

## How to Run
_Instructions require prior Hadoop set up._

1. Clone the repository and open a terminal in the main folder where the `pom.xml` is located.

2. Make a `.jar` in a new `/target` folder as such:
```
mvn clean install
```

3. Create a text file with words of your choice in it. Name it `input.txt`

4. Copy the file(s) into the HDFS as such:
```
hadoop fs -mkdir wordcount/input
hadoop fs -copyFromLocal input.txt wordcount/input
```

5. Check that the file is in the file system with `hadoop fs -ls wordcount/input`, producing the output of:
```
Found 1 items
-rw-r--r--   1 hduser supergroup         25 2016-12-04 01:52 wordcount/input/input.txt
```

6. Run the `jar` using the command below where the `<jar-name>` is the name of your `jar` file and the `<user-name>` is the name of your user group.
```
hadoop jar <jar-name>.jar WordCountJob /user/<user-name>/wordcount/input /user/<user-name>/wordcount/output
``` 

7. If successful, copy the output files to our local system using:
```
hadoop fs -copyToLocal wordcount/output .
```
