import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class CFG {

    static ArrayList<String> var=new ArrayList<>();
    static ArrayList<String> ter=new ArrayList<>();


    private static boolean ister(char s) {
        boolean result=true;
        if(var.contains(String.valueOf(s))|| s=='=' || s=='>'||  s=='|' || ter.contains(String.valueOf(s)))
            result=false;
        return result;
    }  // returns true if char s is a new terminal
    private static boolean check1(String variable,ArrayList<String> lines,ArrayList<String> v1) {

        boolean result=true;
        String p="";
        for (String line:lines) { //findes p that stars with
            if (line.startsWith(variable))
                p=line;
        }
        String pRight= p.substring(p.indexOf("=>")+2,p.length());
        //System.out.println(pRight);
        String[] parts=pRight.split("\\|");
       // System.out.println(pRight);

        for (String s:parts
             ) {   // System.out.println(s);
                    for (int index=0;index<s.length();index++){
                        //System.out.println(String.valueOf(s.charAt(index)));
                        if (!ter.contains(String.valueOf(s.charAt(index))) && !v1.contains(String.valueOf(s.charAt(index)))) {
                             result= false;
                             break;

                        }else {result=true;}
                    }
        }
        return result;
    }
    private static void check2(Map <String,ArrayList<String>> dependancy, String variable,ArrayList result) {
        result.add(variable) ;
        int i=0;
        while (i<dependancy.get(variable).size() ){
            if (!result.contains(dependancy.get(variable).get(i)))
                check2(dependancy,dependancy.get(variable).get(i),result);
            i++;
        }
    }

    public static void main(String[] args) throws IOException {
        File in = new File("C:\\Users\\Markazi.co\\Desktop\\G.txt");
        File out = new File("C:\\Users\\Markazi.co\\Desktop\\out.txt");
        Scanner sc = new Scanner(in);
        ArrayList<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        for (int i = 0; i < lines.size(); i++) {     // adding variables
            lines.set(i, lines.get(i).replaceAll(" ", ""));
            String line = lines.get(i);
            var.add(line.substring(0, line.indexOf("=>")));
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int t = 0;
            while (t < line.length()) {
                if (ister(line.charAt(t)))
                    ter.add(String.valueOf(line.charAt(t)));
                t++;
            }
        }
        //first check
        ArrayList<String> v1 = new ArrayList<>();
        v1.add(var.get(0));
        for (int line = 0; line < lines.size(); line++) {
            for (String variable : var) { //adding to v1
                if (check1(variable, lines, v1)) {
                    if (!v1.contains(variable) && !(variable == "s"))
                        v1.add(variable);
                }
            }
        }
        // for checking if s should be or not
        ArrayList temp = new ArrayList();
        for (int i = 0, n = lines.get(0).length(); i < n; i++) {
            char c = lines.get(0).charAt(i);
            if (v1.contains(String.valueOf(c)) && c != 's') {
                temp.add(c);
            }
        }
        if (temp.isEmpty())
            v1.remove(0);
        System.out.println("final v1 is :   " + v1.toString());
        ////////////////////////////////////////////////////////////making dependency map

        Map<String, ArrayList<String>> dependancy = new HashMap<>();
        for (String variable : v1) {
            for (String line : lines) { //findes p that stars with
                if (line.startsWith(variable)) {
                    String pRight = line.substring(line.indexOf("=>") + 2, line.length());
                    ArrayList vars = new ArrayList(); // dependence varibales
                    for (int i = 0, n = pRight.length(); i < n; i++) {
                        char c = pRight.charAt(i);
                        if (v1.contains(String.valueOf(c))) {
                            vars.add(String.valueOf(c));
                        }
                    }
                    dependancy.put(variable, vars);        //second check
                }
            }
        }
        System.out.println("dep is :  " + dependancy.toString());
        ArrayList<String> result = new ArrayList();
        check2(dependancy,var.get(0), result);
        ArrayList<String> nvar = new ArrayList<>();
        for (String variable : var) {// finding all unuseful variables
            if (!result.contains(variable))
                nvar.add(variable);
        }
        //////////////////////////////////////////////////// deletion of unuseful rules and variables
        for (int line = 0; line < lines.size(); line++) {
            for (String variable : nvar) {
                if ((lines.get(line).startsWith(variable))) // ghanoo haie ke ba na mofid shoro mishe ro paak mikonim
                    lines.set(line, "");
            }
            if (!lines.get(line).isEmpty()) {
                String pRight = lines.get(line).substring(lines.get(line).indexOf("=>") + 2, lines.get(line).length());
                String[] parts = pRight.split("\\|");
                if (parts.length > 1) {// it has |
                    for (String string : parts) {
                        for (String var : nvar) {
                            if (string.contains(var)) {
                                if (pRight.charAt(pRight.indexOf(string)) == pRight.charAt(pRight.length()-1)) { // agar akharin bashe
                                    System.out.println("is :: " + pRight);
                                    lines.set(line,lines.get(line).replaceAll("\\|"+string,"")); //agar akharin bashe
                                }
                                else
                                    lines.set(line, lines.get(line).replaceAll(string + "\\|", ""));

                            }
                        }
                    }
                }
            }

        }
        ////////////////////////////////////////////////////////
        // write in file

        BufferedWriter writer = new BufferedWriter(new FileWriter(out));
        for (String line : lines) {
            writer.write(line+"\n");
            writer.newLine();
        }
        writer.close();
    }

}