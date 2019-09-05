public interface ConstantVars {
    /**
     * 输入文件路径，以|为分隔符，1表示必须，0表示非必须
     */
    String INPUT_FILE_URL = "C:/Users/ZoriChen/Desktop/KeyWord/keyword.txt";

    /**
     * 输出文件路径
     */
    String OUTPUT_FILE_DIR = "C:/Users/ZoriChen/Desktop/KeyWord/";

    /**
     * 是否必须，1表示必须，0表示非必须
     */
    String IS_FLAG = "1";
    String NOT_FLAG = "0";


    /**
     * 生成标题个数
     */
    int LABEL_NUM = 100;

    /**
     * 标题字数限制
     */
    int KEY_WORD_NUM_LIMIT = 30;
}