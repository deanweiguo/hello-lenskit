package com.Process;

import com.method.hownetsim.CharBasedSimilarity;
import com.method.hownetsim.XiaConceptParser;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Similarity {

    private final LinkedHashMap<String, Double> wordSimilarityMap = new LinkedHashMap<String, Double>(2000);
    private static Similarity instance = null;

    public Similarity() {

    }

    public synchronized static Similarity getInstance() {
        if (instance == null) {
            instance = new Similarity();
        }
        return instance;
    }

    /**
     * 句子相似度计算
     *
     * @author zhuwangnan @
     * @param sentence1 用户问句关键词
     * @param sentence2 FAQ库问句关键词
     * @return 
     */
    public double VSMSimilarity(ArrayList<String> sentence1, ArrayList<String> sentence2) {

        //保存每行最大值
        double SimMatrixAB[] = new double[sentence1.size()];
        for (int i = 0; i < sentence1.size(); i++) {
            SimMatrixAB[i] = 0;
            for (int j = 0; j < sentence2.size(); j++) {
                double temp = 0.0;
                if (wordSimilarityMap.containsKey(sentence1.get(i) + "_" + sentence2.get(j))) {
                    temp = wordSimilarityMap.get(sentence1.get(i) + "_" + sentence2.get(j));
                } else if (wordSimilarityMap.containsKey(sentence2.get(j) + "_" + sentence1.get(i))) {
                    temp = wordSimilarityMap.get(sentence2.get(j) + "_" + sentence1.get(i));
                } else {
                    temp = XiaConceptParser.getInstance().getSimilarity(sentence1.get(i), sentence2.get(j));
                    wordSimilarityMap.put(sentence1.get(i) + "_" + sentence2.get(j), temp);
                }
                if (SimMatrixAB[i] < temp) {
                    SimMatrixAB[i] = temp;
                }
            }
        }

        double FinalSimAB = 0;
        for (int k = 0; k < sentence1.size(); k++) {
            FinalSimAB = FinalSimAB + SimMatrixAB[k];
        }

        //Sim(B,A)计算特征向量B和A的相似度矩阵
        double SimMatrixBA[] = new double[sentence2.size()];
        for (int i = 0; i < sentence2.size(); i++) {
            SimMatrixBA[i] = 0;
            for (int j = 0; j < sentence1.size(); j++) {
                double temp=0.0;
                if (wordSimilarityMap.containsKey(sentence2.get(i) + "_" + sentence1.get(j))) {
                    temp = wordSimilarityMap.get(sentence2.get(i) + "_" + sentence1.get(j));
                } else if (wordSimilarityMap.containsKey(sentence1.get(j) + "_" + sentence2.get(i))) {
                    temp = wordSimilarityMap.get(sentence1.get(j) + "_" + sentence2.get(i));
                } else {
                    temp = XiaConceptParser.getInstance().getSimilarity(sentence2.get(i), sentence1.get(j));
                    wordSimilarityMap.put(sentence2.get(i) + "_" + sentence1.get(j), temp);
                }
                if (SimMatrixBA[i] < temp) {
                    SimMatrixBA[i] = temp;
                }
            }
        }
        double FinalSimBA = 0;
        for (int k = 0; k < sentence2.size(); k++) {
            FinalSimBA = FinalSimBA + SimMatrixBA[k];
        }

        double FSim = ((FinalSimBA / sentence2.size()) + (FinalSimAB / sentence1.size())) / 2;

        return FSim;
    }

    /**
     * 计算用户问句和本体推理问句的相似度
     */
    public double SentenceSimilarity(String sentence1, String scentence2) {
        double FSim = new CharBasedSimilarity().getSimilarity(sentence1, scentence2);
        return FSim;
    }

}
