import MFCC_overlap as mfcc
import DTW  as dtw
import numpy as np
import web_crawler as pronunciation
import xlrd
import file_transfer as file_transfer




def main():

    city_name_table = []
    city_mfcc_table = []
    city_dtw_table = []

    data = xlrd.open_workbook("T20F014_1.xls") #city name table
    table = data.sheets()[0]
    t = table.col_values(1)
    del t[0:3]
    del t[-1]
    city_name_table = t

    """
    for city in city_name_table:
        pronunciation.pronunciation_french_get(city)
    """
    """
    for city_name in city_name_table:
        file_transfer.trans_mp3_to_wav("../data/%s.mp3" %city_name)
    """

    for city_name in city_name_table:
        city_mfcc_table.append([mfcc.MFCC("%s.wav" %city_name),city_name])



    test = mfcc.MFCC("toulouse.wav")
    print(test)

    for city_mfcc in city_mfcc_table:
        city_dtw_table.append([dtw.DTW(test,city_mfcc[0]),city_mfcc[1]])

    print (city_dtw_table)

    dtw_min = float('inf')
    for city_dtw in city_dtw_table:
        if(float(city_dtw[0])< dtw_min):
            dtw_min = float(city_dtw[0])
            city_name = city_dtw[1]


    print(city_name)






if __name__ == "__main__":
    main()