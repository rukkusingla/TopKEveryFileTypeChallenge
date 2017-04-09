import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Rukku Singla on 4/8/17.
 */

public class TopTenEveryLanguage {
    
    private HashMap<String,HashMap<String,Integer>> frequencyMap;
    private HashMap<String,List<String>> resultMap;

    /*
        Constructors
     */
    public TopTenEveryLanguage() {
        this.frequencyMap = new HashMap<>();
        this.resultMap = new HashMap<>();
    }

    /*
        Class Methods
     */

    // Function to get file extension
    public String getExtension(File s)
    {
        String fileName = s.getName();
        String ext = "";
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return ext;
    }

    // Function to get file content
    public String getFileContent(File child)
    {
        String content = null;
        FileReader reader = null;
        try
        {
            reader = new FileReader(child);
            char[] chars = new char[(int) child.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }}
        }
        return content;
    }

    // Function to clean the data
    public String[] cleanData(String content)
    {
        content = content.toLowerCase();
        String[] words = content.split("\\P{Alpha}+");
        return words;
    }

    // Function to build hashmap from the source directory
    public void buildHashFromSource(String myDirectoryPath)
    {
        File dir = new File(myDirectoryPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Ignore system files
                if((".DS_Store").equals(child.getName()))
                    continue;

                String ext = getExtension(child);
                if(!frequencyMap.containsKey(ext))
                {
                    frequencyMap.put(ext, new HashMap<String,Integer>());
                }
                String content = getFileContent(child);

                String[] words = cleanData(content);
                HashMap<String,Integer> temp = frequencyMap.get(ext);

                for(String word:words)
                {
                    if(word.length() <= 1)
                        continue;
                    if(!temp.containsKey(word))
                    {
                        temp.put(word, 1);
                    }
                    else
                    {
                        temp.put(word, temp.get(word) + 1);
                    }
                }
                frequencyMap.put(ext, temp);
            }
        }

        else {
            System.out.print("Directory doesn't contain files");
        }
    }

    // Function to select top K for each type of file with input as buckets and k
    public List<String> selectTopKForEveryLanguage(List<String>[] buckets, int k)
    {
        List<String> anslist = new ArrayList<>();
        int pos = buckets.length-1;
        while (k > 0 && pos >= 0)
        {
            if (buckets[pos]!= null)
            {
                for (String y: buckets[pos])
                {
                    if (k>0)
                    {
                        anslist.add(y);
                        k--;
                    }
                }
            }
            pos--;
        }
        return anslist;
    }

    // MakeBucketsForSorting with input as max frequency and HashMap for each type of file
    public List<String>[] makeBucketsForCountSort(int max,HashMap<String,Integer> map)
    {
        List<String>[] stringList = new List[max+1];
        for(String j: map.keySet())
        {
            int r = map.get(j);
            if(stringList[r] == null)
            {
                stringList[r] = new ArrayList<>();
            }
            stringList[r].add(j);
        }
        return stringList;
    }

    //TopKWords with input arguments-Directory and k
    public void getTopKWords(int k)
    {
        //Iterating through each entry in frequencyMap
        for(Map.Entry<String, HashMap<String, Integer>> entry: frequencyMap.entrySet())
        {
            String key=entry.getKey();
            HashMap<String, Integer> values = entry.getValue();
            
            //Calculating maximum frequency for each type of file
            int max = Integer.MIN_VALUE;
            for(int v: values.values())
            {
                max = Math.max(max, v);
            }
            //Calling MakeBucketsForSorting
            List<String>[] buckets = makeBucketsForCountSort(max, values);

            //Calling SelectTopK foreach TypeOfFile
            List<String> answersList = selectTopKForEveryLanguage(buckets, k);
            resultMap.put(key, answersList);
            
        }
    }

    //Main Function
    public static void main(String[] args) {
        TopTenEveryLanguage topK = new TopTenEveryLanguage();

        topK.buildHashFromSource("/Users/Muthu/Desktop/Source_dir");

        //Calling TopKWords
        topK.getTopKWords(10);

        //Displaying Results
        System.out.println(topK.resultMap.keySet());
        System.out.println(topK.resultMap.values());
    }
}