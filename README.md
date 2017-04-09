# TopKEveryFileTypeChallenge

## How to run
- Instantiate class passing k value to the constructor

    `TopTenEveryLanguage topK = new TopTenEveryLanguage(10);` 
- Now invoke the function build source passing the directory path

    `topK.buildHashFromSource("/Users/rukkusingla/Desktop/Source_dir");`
 - Print results using
 
      `topK.getTopKWords();`
       `topK.prinResults();`
