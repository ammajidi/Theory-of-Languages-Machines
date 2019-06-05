import java.io.*;
import java.util.*;


public class Main {
    private static Map<String,ArrayList<String>> linesMaker(ArrayList<String> left, ArrayList<String> right) {
        Map<String,ArrayList<String>> lines=new HashMap<>();
        outerloop:
        for (int i = 0; i < left.size(); i++) {

            String[] strings=right.get(i).split("\\|");

            if (strings.length==1){
                for (int index=0;index<strings[0].length();index++){
                    if (String.valueOf(strings[0].charAt(index)).equals(left.get(i))){
                        // System.out.println(String.valueOf(strings[0].charAt(index)));
                        left.remove(i);
                        right.remove(i);
//                        System.out.println("new left is"+left);
//                        System.out.println("new right is"+right);
                        continue outerloop;
                    }

                }

            }
        }

        for (int i = 0; i < left.size(); i++) {
            ArrayList<String> temp = new ArrayList<>();
            String[] strings=right.get(i).split("\\|");
//            System.out.println(left);
            for (String s:strings) {
//                System.out.println(s);
                temp.add(s);
                for (int index=0;index<s.length();index++){
//                    System.out.println(s.charAt(index));
//                    System.out.println(left);
//                    System.out.println(!left.contains(String.valueOf(s.charAt(index))));
//                    System.out.println(!String.valueOf(s.charAt(index)).toLowerCase().equals(String.valueOf(s.charAt(index))));
                    if (!left.contains(String.valueOf(s.charAt(index))) && !String.valueOf(s.charAt(index)).toLowerCase().equals(String.valueOf(s.charAt(index)))){
                        //System.out.println("removed:"+s);
                        temp.remove(s);
                    }
                }
            }
            if (temp.isEmpty()){
//                System.out.println(left.get(i));
                continue;
            }
//            System.out.println(left.get(i));
//            System.out.println(temp);
            lines.put(left.get(i),temp);//String[] az rules mishan ye ozv arraylist.
        }
        return lines;
    }
    private static void getDependency(Map <String,ArrayList<String>> dependancy, String variable,ArrayList result) {
//        System.out.println("result before add "+result);
        result.add(variable) ;
//        System.out.println("result after add "+result);
        int i=0;
//            System.out.println(dependancy.get(variable));
        while (i<dependancy.get(variable).size() ){
//            System.out.println(dependancy.get(variable).get(i));
            if (!result.contains(dependancy.get(variable).get(i)))
                getDependency(dependancy,dependancy.get(variable).get(i),result);
            i++;
        }
    }
    private static ArrayList result(ArrayList<String> depending, ArrayList<String> v1) {
        ArrayList re=new ArrayList();
        for (String s:depending) {
            for (String v:v1) {
                if (!re.contains(v) && s.equals(v))
                    re.add(v);
            }
        }
        return re;
    }
    private static ArrayList<String> makeOutPut(ArrayList<String> result, Map<String, ArrayList<String>> lines) {
        ArrayList<String> newline=new ArrayList<>();
        for (String var:result) {
            String re=var+"->";
            if (lines.get(var).size()>1){
                for (int i=0;i<lines.get(var).size();i++){
                    if (i==lines.get(var).size()-1)
                        re+=lines.get(var).get(i);
                    else                         re+=lines.get(var).get(i)+"|";

                }
            }
            else re+=lines.get(var).get(0);
            newline.add(re);
        }
        return newline;
    }
    public static void main(String[] args) throws IOException {

        //Creating our data structure
        ArrayList<String> line = new ArrayList<>();
        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();
        Map<String, ArrayList<String>> dependencyMap = new HashMap<>();
        ArrayList<String> depending = new ArrayList<>();

        //reading a text file line by line
        //making a file for answer
        File file = new File("C:\\Users\\Markazi.co\\Desktop\\G.txt");
        File output = new File("C:\\Users\\Markazi.co\\Desktop\\output.txt");
        FileWriter fileWriter = new FileWriter(output);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        Scanner scan = new Scanner(file);

        //breaking the text into two parts : before -> and after ->
        while (scan.hasNextLine()) {
            line.add(scan.nextLine());
        }

        for (int i = 0; i < line.size(); i++) {
//            System.out.println(line);
            left.add(line.get(i).substring(0, line.get(i).indexOf("->")));
            right.add(line.get(i).substring(line.get(i).indexOf("->") + 2, line.get(i).length()));
        }
        Map<String,ArrayList<String>> lines1=linesMaker(left,right);
        ArrayList<String> left1 = new ArrayList<>();
        ArrayList<String> right1 = new ArrayList<>();
        for(String string:lines1.keySet()){
            left1.add(string);
        }
        System.out.println(left1);
        for (String string:lines1.keySet()){
            for(String s:lines1.get(string)){
                right1.add(s);
            }
        }
        System.out.println(right1);
        Map<String,ArrayList<String>> lines=linesMaker(left,right);
        System.out.println(lines);
        System.out.println("--------------------------------------------------------------------");



        //Main Algorithm
        //part 1
        ArrayList<String> v1 = new ArrayList<>();
        for (String s:lines.keySet()) {
            v1.add(s);
        }
        System.out.println(" v1 is "+v1);
        for (String var: left1){
            ArrayList temp=new ArrayList();
            for (String s:lines.get(var)){
                for(int index=0;index<s.length();index++){
                    if(!(Character.toLowerCase(s.charAt(index))==s.charAt(index)))
                        temp.add(Character.toString(s.charAt(index)));
                }
            }
            dependencyMap.put(var,temp);
        }
        System.out.println("dependencyMap=" + dependencyMap);
        System.out.println("--------------------------------------------------------------------");
        //part 2

        getDependency(dependencyMap,"S", depending);
        System.out.println(" depending is "+depending);

        ArrayList<String> result= result(depending,v1);
        System.out.println(result);

        ArrayList<String> newline=makeOutPut(result,lines);
        System.out.println(newline);

        for(int i=0;i<newline.size();i++){

            bufferedWriter.write(newline.get(i));
            bufferedWriter.newLine();

        }
        bufferedWriter.close();
    }




}
