
import DTW  as dtw
import MFCC_overlap as mfcc_overlap

#This file is deprecated, it does not work anymore 

test = mfcc_overlap.MFCC("toulouse_different_speaker.wav",file_directory ='test')

toulouse=mfcc_overlap.MFCC("toulouse.wav",file_directory ='test')
toulon=mfcc_overlap.MFCC("toulon.wav",file_directory = 'test')

if dtw.DTW(test,toulouse) < dtw.DTW(test,toulon):
    print("Mot reconnu: toulouse")

else:
    print("Mot reconnu: toulon")





