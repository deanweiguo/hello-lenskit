package com.nlpir.wordseg;

import java.io.UnsupportedEncodingException;  


//import utils.SystemParas;  
import com.sun.jna.Library;  
import com.sun.jna.Native;  
public class test {
	public interface CLibrary extends Library {  
        CLibrary Instance = (CLibrary) Native.loadLibrary(System.getProperty("user.dir")+"\\source\\NLPIR",CLibrary.class);  
        public int NLPIR_Init(String sDataPath,int encoding,String sLicenceCode);  
        public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);  
        // 添加用户词汇  
        public int NLPIR_AddUserWord(String sWord);  
        // 删除用户词汇  
        public int NLPIR_DelUsrWord(String sWord);  
        // 保存用户词汇到用户词典  
        public int NLPIR_SaveTheUsrDic();   
        // 导入用户自定义词典：自定义词典路径，bOverwrite=true表示替代当前的自定义词典，false表示添加到当前自定义词典后    
        public int NLPIR_ImportUserDict(String sFilename, boolean bOverwrite);   
        public String NLPIR_GetLastErrorMsg();  
        public void NLPIR_Exit();  
    }  
      
    public static String transString(String aidString,String ori_encoding,String new_encoding) {  
        try {  
            return new String(aidString.getBytes(ori_encoding),new_encoding);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
    public static void main(String[] args) throws Exception {  
        String argu = "";  
        // String system_charset = "UTF-8";  
        int charset_type = 1;                  
        int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");      
        String nativeBytes;  
  
        // 初始化失败提示  
        if (0 == init_flag) {  
            nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();   
            System.err.println("初始化失败！原因："+nativeBytes);  
            return;  
        }  
          
        String sInput = "这是一本关于信息检索的书，作者是南京大学的。";   
        try {  
            nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);  // 分词函数，是否标注词性  
            System.out.println("原始的分词结果为： " + nativeBytes);  
              
            // 添加两个用户词汇，此处为单个添加方法  
            CLibrary.Instance.NLPIR_AddUserWord("信息检索 n");  // n为词性  
            CLibrary.Instance.NLPIR_AddUserWord("南京大学 n");  
            nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);  
            System.out.println("增加词汇后结果为： " + nativeBytes);  
              
            CLibrary.Instance.NLPIR_DelUsrWord("南京大学");    // 删除其中一个词汇  
            nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);  
            System.out.println("删除词汇后结果为： " + nativeBytes);  
              
            // CLibrary.Instance.NLPIR_SaveTheUsrDic();  // 保存用户自定义词汇，建议不用  
              
            int nCount = CLibrary.Instance.NLPIR_ImportUserDict(System.getProperty("user.dir")+"\\source\\userdic.txt",true);   
            System.out.println(String.format("已导入%d个用户词汇", nCount)); 
            CLibrary.Instance.NLPIR_SaveTheUsrDic();
            nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);  
            System.out.println("导入词典后结果为： " + nativeBytes);  
              
            CLibrary.Instance.NLPIR_Exit();     // 退出  
              
        } catch (Exception ex) {  
            // TODO Auto-generated catch block  
            ex.printStackTrace();  
        }  
    }  

}
