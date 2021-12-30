
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataTokens1 {
    public static void segmentations(StringBuilder sb, String fileName){
        //文件的位置信息
        Scanner input = null;
        try {
            input = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //读取文档信息
        while (input.hasNext()) {
            sb.append(input.next());
        }
        sb.setLength(sb.length() - 1);
        sb.deleteCharAt(0);
    }

    /**
     *
     * @param fileName 文件位置
     * @param independentVariableIndex 自变量下标
     * @param startIndex 因变量起始下标
     * @param endIndex 因变量结束下标
     * @param average 需要和自变量比较大小的平均数
     * @param independentVariable 自变量值（String）
     * @param dependentVariable 因变量值（String）
     * @param regex 每行末尾的标识符
     */
    public static void getBasicInfo(String fileName, int independentVariableIndex, int startIndex, int endIndex, double average, String independentVariable, String dependentVariable, String regex){
        double lowerRate = 0, higherRate = 0;
        int lowerChosen = 0, higherChosen = 0, averageChosen = 0;
        int lowerAll = 0, higherAll = 0, averageAll = 0;

        ArrayList<String> highers = new ArrayList<>();
        ArrayList<String> lowers = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);

        segmentations(sb, fileName);

        //拆分文件信息
        String[] lineInfos = sb.toString().split(regex);

        System.out.println("第一级拆分");

        for (String a : lineInfos) {
            String[] eachInfos = a.split(",");
            for (int i = 0; i < eachInfos.length; i++) {
                if (i == independentVariableIndex) {
                    StringBuilder tempStr;
                    try {
                        if (Double.parseDouble(eachInfos[independentVariableIndex]) > average) {
                            tempStr = new StringBuilder(eachInfos[i] + ": ");
                            for (int j = startIndex; j < endIndex; j++) {
                                if (!eachInfos[j].equals("") && eachInfos[j].contains(dependentVariable)) {
                                    tempStr.append(eachInfos[j]);
                                    higherChosen++;
                                }
                            }
                            higherAll++;
                            highers.add(tempStr.toString()+ "\n");
                        }else if (Double.parseDouble(eachInfos[independentVariableIndex]) < average){
                            tempStr = new StringBuilder(eachInfos[i] + ": ");
                            for (int j = startIndex; j < endIndex; j++) {
                                if (!eachInfos[j].equals("") && eachInfos[j].contains(dependentVariable)) {
                                    tempStr.append(eachInfos[j]);
                                    lowerChosen++;
                                }
                            }
                            lowerAll++;
                            lowers.add(tempStr.toString() + "\n");
                        }else{
                            for (int j = startIndex; j < endIndex; j++) {
                                if (!eachInfos[j].equals("") && eachInfos[j].contains(dependentVariable)) {
                                    averageChosen++;
                                }
                            }
                            averageAll++;
                        }
                    } catch (Exception e) {
                        System.out.println("分割线");
                    }
                }
            }
        }

        int p1 = highers.size();
        int p2 = lowers.size();
        for (int i = 0; i < p1; i++) {
            System.out.print(highers.get(i));
        }
        System.out.println("分割线");
        for (int i = 0; i < p2; i++) {
            System.out.print(lowers.get(i));
        }
        if(lowerAll != 0 && higherAll != 0) {
            lowerRate = 1.0 * lowerChosen / lowerAll;
            higherRate = 1.0 * higherChosen / higherAll;
        }
        System.out.printf("higher rate: %.2f\n", higherRate);
        System.out.printf("lower rate: %.2f\n", lowerRate);
        System.out.printf("On average rate: %.2f\n", (1.0*averageChosen)/averageAll);
        System.out.printf("average rate: %.2f\n", 1.0*(lowerChosen + higherChosen + averageChosen)/lineInfos.length);
    }

    public static String format(ArrayList<String> builder){
        return String.format("%s",builder);
    }

    public static void main(String[] args) {
        getBasicInfo("数据2.csv", 13,60,70,3,null,"D", "#&#&#");
    }
}
