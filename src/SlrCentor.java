import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 *  随机生成商品标题
 */
public class SlrCentor {

    public static List<ArrayList<String>> noRepeatHisTitleList = new ArrayList<ArrayList<String>>();

    public static void main(String[] args) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(ConstantVars.INPUT_FILE_URL)));

        List<String> notFlagedLabeledKeyWordList = new ArrayList<String>();
        List<String> flagedLabeledKeyWordList = new ArrayList<String>();

        String lineStr = null;

        StringBuffer flagedLabelBuf = new StringBuffer("");
        StringBuffer allWKBuf = new StringBuffer("");

        while((lineStr = bufferedReader.readLine()) != null) {
            String[] pair = lineStr.split("\\|");
            String keyWord = pair[0].trim();
            String mustFlag = pair[1].trim();
            allWKBuf.append(keyWord);
            if (ConstantVars.IS_FLAG.equals(mustFlag)) {
                flagedLabelBuf.append(keyWord);
                flagedLabeledKeyWordList.add(keyWord);
            } else {
                notFlagedLabeledKeyWordList.add(keyWord);
            }
        }

        // 标签字数不能太少
        if (allWKBuf.length() <= ConstantVars.KEY_WORD_NUM_LIMIT) {
            System.out.println("标签太少，请检查");
            return;
        }

        // 判断打标标签是否超过30个字符
        if (flagedLabelBuf.length() > ConstantVars.KEY_WORD_NUM_LIMIT) {
            System.out.println("打标标签字符超过30个字符");
            return;
        }


        File outputFile = new File(ConstantVars.OUTPUT_FILE_DIR);
        if (!outputFile.exists()) {
            if (!outputFile.mkdirs()) {
                 System.out.println("创建结果目录失败，请检查权限");
                 return;
            }
        }

        Calendar cal = Calendar.getInstance();
        long timeStamp = cal.getTimeInMillis();
        String outputFileName = ConstantVars.OUTPUT_FILE_DIR + File.separator + "result" + "_" + timeStamp;

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFileName))));

        for (int i = 0; i < ConstantVars.LABEL_NUM; i++) {
            List<String> resultTitleList = new ArrayList<String>();
            String resultTitle = generateRandomKeyWord(flagedLabelBuf, flagedLabeledKeyWordList, notFlagedLabeledKeyWordList, resultTitleList);
            if (!isRepeatTitle(resultTitleList)) {
                bufferedWriter.write(resultTitle);
                bufferedWriter.newLine();
            }

        }

        bufferedWriter.flush();
        bufferedReader.close();
        bufferedWriter.close();
        System.out.println("Hello World!");
    }

    private static boolean isRepeatTitle(List<String> resultLabelList) {

        boolean repeatFlag = false;
        if (noRepeatHisTitleList.size() == 0) {
            noRepeatHisTitleList.add((ArrayList<String>) resultLabelList);
            return repeatFlag;
        }

        for (List<String> eLabelList : noRepeatHisTitleList) {
             if(isContainSameLabels(eLabelList, resultLabelList)) {
                 repeatFlag = true;
                 break;
             }
        }

        if(!repeatFlag) {
            noRepeatHisTitleList.add((ArrayList<String>) resultLabelList);
        }

        return repeatFlag;


    }

    private static boolean isContainSameLabels(List<String> eLabelList, List<String> resultLabelList) {
        boolean isRepeatFlag = true;
        for (String eLabel : eLabelList) {
            boolean isContainFlag = false;
            for(String resultLabel : resultLabelList) {
                if (resultLabel.equals(eLabel)) {
                    isContainFlag = true;
                    break;
                }
            }

            if(!isContainFlag) {
                return false;
            }

            isRepeatFlag = isRepeatFlag && isContainFlag;
        }

        return isRepeatFlag;
    }

    private static String generateRandomKeyWord(StringBuffer flagedLabelBuf, List<String> flagedKeyWordList,
                                                List<String> unFlagedKeyWordList, List<String> resultTitleList) {
        StringBuffer unSortedKwBuf = new StringBuffer();
        unSortedKwBuf.append(flagedLabelBuf);

        List<String> unSortedList = new ArrayList<String>();

        unSortedList.addAll(flagedKeyWordList);

        int unFlagedKWNum = unFlagedKeyWordList.size();

        StringBuffer preUnSortedLabelBuf;

        List<Integer> genRandomPosList = new ArrayList<Integer>();

        List<String> preUnSortedList = null;

        while(unSortedKwBuf.length() < ConstantVars.KEY_WORD_NUM_LIMIT) {
            int randomPos = generateRandomNo(unFlagedKWNum - 1);
            if (genRandomPosList.contains(new Integer(randomPos))) continue;

            genRandomPosList.add(new Integer(randomPos));

            preUnSortedLabelBuf = new StringBuffer();
            preUnSortedLabelBuf.append(unSortedKwBuf);

            preUnSortedList = new ArrayList<String>();
            preUnSortedList.addAll(unSortedList);

            String newKeyWord = unFlagedKeyWordList.get(randomPos);
            unSortedKwBuf.append(newKeyWord);
            unSortedList.add(newKeyWord);
        }

        StringBuffer titleBuf = new StringBuffer("");
        for(String title : preUnSortedList) {
            titleBuf.append(title);
            resultTitleList.add(title);
        }

        return titleBuf.toString();
    }

    private static int generateRandomNo(int upperLimit) {
        Random random = new Random();
        return random.nextInt(upperLimit)%(upperLimit+1);
    }
}