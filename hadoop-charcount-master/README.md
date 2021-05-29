# char Count
The infamous char counting MapReduce example using Hadoop MapReduce.

## Input
Files containing chars to be counted

## Output
A list of chars and their corresponding occurance in the files you provide as the input.

## How to Run
_Instructions require prior Hadoop set up._

1. Clone the repository and open a terminal in the main folder where the `pom.xml` is located.

2. Make a `.jar` in a new `/target` folder as such:
```
mvn clean install
```

3. Create a text file with chars of your choice in it. Name it `input.txt`

4. Copy the file(s) into the HDFS as such:
```
hadoop fs -mkdir charcount/input
hadoop fs -copyFromLocal input.txt charcount/input
```

5. Check that the file is in the file system with `hadoop fs -ls charcount/input`, producing the output of:
```
Found 1 items
-rw-r--r--   1 hduser supergroup         25 2016-12-04 01:52 charcount/input/input.txt
```

6. Run the `jar` using the command below where the `<jar-name>` is the name of your `jar` file and the `<user-name>` is the name of your user group.
```
hadoop jar <jar-name>.jar CharCountJob /user/<user-name>/charcount/input /user/<user-name>/charcount/output
``` 

7. If successful, copy the output files to our local system using:
```
hadoop fs -copyToLocal charcount/output .
```
