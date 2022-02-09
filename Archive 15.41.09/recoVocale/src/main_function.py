import MFCC as mfcc
import DTW  as dtw
import numpy as np


def main():
    city_name_table = ["paris","toulon","toulouse"]
    city_mfcc_table = []
    city_dtw_table = []

    test = mfcc.MFCC("lyon.wav")

    for city in city_name_table:
        city_mfcc_table.append([mfcc.MFCC("%s.wav" %city),city])



    for city_mfcc in city_mfcc_table:
        city_dtw_table.append([dtw.DTW(test,city_mfcc[0]),city_mfcc[1]])


    dtw_min = float('inf')
    for city_dtw in city_dtw_table:
        if(float(city_dtw[0])< dtw_min):
            dtw_min = float(city_dtw[0])
            city_name = city_dtw[1]


    print(city_name)

if __name__ == "__main__":
    main()
