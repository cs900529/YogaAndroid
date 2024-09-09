
# 瑜珈關節點角度


## 用途
- 瑜珈教練系統: 提供標準角度，用於規則制定
- 瑜珈評分系統: 將使用者的關節點角度，與標準角度的平均值、標準差進行比較，以此計算平均值
  - 詳細介紹可參考[論文](https://docs.google.com/document/d/1sWPlbKvfi4x-Idih0DI4toHYrIhyDv6y/edit?usp=sharing&ouid=114571548892193624282&rtpof=true&sd=true) : 3.2. 自動瑜珈墊評分系統
  
## 內容

- 瑜珈教練系統用
  - JsonFile/pose/sample.json
- 瑜珈評分系統用
  - JsonFile/pose/sample_score.json: 給予分數計算系統使用，平均關節點角度
  - JsonFile/pose/std_angle.json:  關節點角度標準差
  - JsonFile/pose/weight.json: 關節點的計分權重

sample.json 與 sample_score.json 差異: 兩個都是正確動作的關節點角度，不同系統使用時角度數值會有一些微調，因此分成兩個不同檔案。


