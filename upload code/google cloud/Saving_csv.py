import csv
import time
class SavingCsv:
    @classmethod
    def saveAsCsv(cls, dic:dict):
        #statistic_dict[label] = [cnt, weight,round(weight * DataGenerator.co2_dict.get(label, 5), 2)]
        with open(time.strftime("%Y_%m_%d", time.localtime()) + '.csv', 'w') as csvfile:
            writer = csv.writer(csvfile)
            for label, [cnt, weight, co2] in dic.items():
                writer.writerow([label, cnt, str(weight)+"g", str(co2)+"g"])