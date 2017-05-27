# Custom Article Finder

This program searches articles that are related to a given input query, using a tf-df search approach.
Articles are added by selecting a text file in which each line is a link to an RSS feed link, the program will read the content and will parse and add all the articles, computing the term frequencies and inverse document frequencies.

![1](http://i.imgur.com/zbv2RC4.png)
![2](http://i.imgur.com/gDwffCH.png)

## Usage

To run the application just type the following
```
mvn exec:java -D exec.mainClass="cuni.software.ViewRunner"
```

## License
No license, this project was made for educational purposes. The code can be used by anyone for anything.